package com.jesgoo.tellme

import scala.collection.mutable.HashMap

import com.jesgoo.tellme.config.TellConfig
import com.jesgoo.tellme.config.TellContext
import com.jesgoo.tellme.driver.EventExcuteProtoBufCounter
import com.jesgoo.tellme.driver.NormalCounter
import com.jesgoo.tellme.driver.ParserMessage
import com.jesgoo.tellme.driver.TailSource
import com.jesgoo.tellme.driver.UPDATE_CUT
import com.jesgoo.tellme.driver.UiExcuteProtoBufCounter
import com.jesgoo.tellme.matrix.LOAD_DATA
import com.jesgoo.tellme.matrix.STORE_DATA
import com.jesgoo.tellme.matrix.MatrixContext
import com.jesgoo.tellme.matrix.START
import com.jesgoo.tellme.matrix.SnapshotDriver
import com.jesgoo.tellme.matrix.TableManager
import com.jesgoo.tellme.tools.Utils
import scala.concurrent.duration._

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.io.IO
import spray.can.Http
object TMain {

  val tcontext = new TellContext
  val map_ids = new HashMap[String, String]
  val item_ids = new HashMap[String, String]

  val protobuf_actor = new HashMap[String, ActorRef]

  implicit val system = ActorSystem("Main")
  import system.dispatcher
  def init = {
    val w_map = tcontext.PARSER_MAP.trim()
    if (w_map != "") {
      for (p <- w_map.split(";")) {
        val tmp_p = p.split(":")
        if (tmp_p.length >= 2) {
          map_ids.+=(tmp_p(0) -> tmp_p(1).toUpperCase())
        }
      }
    }
    val m_items = tcontext.EXCUTE_ITEM.trim()
    if (m_items != "") {
      for (skv <- m_items.split(";")) {
        val tmpsdk_arr = skv.split(":")
        item_ids += (tmpsdk_arr(0) -> tmpsdk_arr(1))
      }
    }
  }

  def initActor(matrix_actor: ActorRef) = {
    val tail_files = tcontext.TAIL_FILES
    for (f <- tail_files.split(",")) {
      val name = Utils.basename(f)
      if (item_ids.size > 0 && map_ids.contains(name) && item_ids.contains(name)) {
        var protobuf: ActorRef = null
        if (map_ids.get(name).get.toUpperCase() == "EVENT") {
          protobuf = system.actorOf(Props(new EventExcuteProtoBufCounter(matrix_actor, null)), name = name + "_event_protobuf")
        } else if (map_ids.get(name).get.toUpperCase() == "UI") {
          protobuf = system.actorOf(Props(new UiExcuteProtoBufCounter(matrix_actor, null)), name = name + "_ui_protobuf")
        }
        protobuf ! UPDATE_CUT(item_ids.get(name).get)
        protobuf_actor += (name -> protobuf)
        if (protobuf != null) {
          val parser = system.actorOf(Props(new ParserMessage(matrix_actor, protobuf)), name = name + "_parser_message")
          val normal_actor = system.actorOf(Props(new NormalCounter(matrix_actor, null)),
            name = name + "_normalCounter")
          val tail_actor = system.actorOf(Props(new TailSource(f, Array(parser, normal_actor))),
            name = "tail_" + name)
          tail_actor ! START
        }
      } else {
        val normal_actor = system.actorOf(Props(new NormalCounter(matrix_actor, null)),name = name+"normal_actor")
        val tail_actor = system.actorOf(Props(new TailSource(f, Array(normal_actor))),
          name = "tail_" + name)
        tail_actor ! START
      }
    }
  }

  def main(args: Array[String]) {
    init
    val tblm_actor = system.actorOf(Props[TableManager], name = "table_manager")
    val matrix_actor = system.actorOf(Props(new MatrixContext(tblm_actor)), name = "matrixContext")
    //load之前的tables
    val snap_actor = system.actorOf(Props(new SnapshotDriver(tblm_actor)), name = "snapshot_driver")
    snap_actor ! LOAD_DATA
    //初始化 各个counter
    initActor(matrix_actor)
    //start http server
    println("start http server")
    val handler = system.actorOf(Props(new HttpHandle(tblm_actor)), name = "handler")
    IO(Http) ! Http.Bind(handler, interface = "0.0.0.0", port = tcontext.HTTP_PORT)
    
    val cancellable_matrix = system.scheduler.schedule(0 milliseconds,
        tcontext.MATRIX_PERIOD milliseconds, matrix_actor, START)
    
    val cancellable_snapshot = system.scheduler.schedule((tcontext.TIME_SLICE*0.9).toLong milliseconds,
       (tcontext.TIME_SLICE*0.9).toLong milliseconds, snap_actor, STORE_DATA)
        
    while(true){
      if(tcontext.newConfig){
          init
          for(acf_name <- protobuf_actor.keysIterator){
            protobuf_actor.get(acf_name).get ! UPDATE_CUT(item_ids.get(acf_name).get)
          }
      }
      Thread.sleep(tcontext.CONFIG_UPDATE_PERIOD)
    }
  }
}
