package com.jesgoo.tellme.matrix

import scala.annotation.migration
import scala.collection.mutable.HashMap
import com.jesgoo.tellme.TMain
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import akka.event.Logging
import akka.util.Timeout
import com.jesgoo.tellme.tools.Utils

case object START
case class INCREASE(ins: String)

class MatrixContext(tblm_actor: ActorRef) extends Actor {
  val log = Logging(context.system, this)
  implicit val timeout = Timeout(1000)

  var last_time = System.currentTimeMillis()

  val counterNUM = new HashMap[String, Long]

  def follow_feel(cur_time: Long): Boolean = {
    val m = Utils.get_minute(cur_time)
    val slice = TMain.tcontext.TIME_SLICE / 60000
    if (slice == 0) {
      true
    } else {
      m % slice == 0
    }
  }

  override def receive = {
    case START =>
      val cur_time = System.currentTimeMillis()
      if (((cur_time - last_time) >= TMain.tcontext.TIME_SLICE) && follow_feel(cur_time)) {
        //println(follow_feel(cur_time)+" "+cur_time+" ...="+TMain.tcontext.TIME_SLICE / 60000+"  minute="+ Utils.get_minute(cur_time))
        for (key <- counterNUM.keys) {
            val coun = new Counter(counterNUM.get(key).get,cur_time)
            counterNUM += (key -> 0)
            if(coun.lines != 0 || TMain.item_ids.contains(key)){
               tblm_actor ! InsertCountor(key, coun)
            }
        }
        last_time = cur_time
        tblm_actor ! ClearTable
      }
    case INCREASE(ins) =>
      if (counterNUM.contains(ins)) {
        val i = counterNUM.get(ins).get + 1
        counterNUM += (ins -> i)
      } else {
        counterNUM += (ins -> 1)
      }
    case _ =>
      log.warning("unknown message")
  }
}