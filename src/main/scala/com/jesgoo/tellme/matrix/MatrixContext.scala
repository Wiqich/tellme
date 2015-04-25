package com.jesgoo.tellme.matrix

import scala.annotation.migration
import scala.collection.mutable.HashMap

import com.jesgoo.tellme.TMain
import com.jesgoo.tellme.tools.Utils

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import akka.event.Logging
import akka.util.Timeout

case object START
case class INCREASE(ins: String)

class MatrixContext(tblm_actor: ActorRef,post : ActorRef = null) extends Actor {
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
  var time_slace = TMain.tcontext.TIME_SLICE
  
  def follow_you_heart(cur_time:Long){
    val tt = cur_time/60000*60000
    val _slace = TMain.tcontext.TIME_SLICE
    time_slace = _slace - (cur_time -tt)
    if(time_slace <0 || time_slace>_slace){
      time_slace = _slace
    }
  }
  
  override def receive = {
    case START =>
      val cur_time = System.currentTimeMillis()
      if (((cur_time - last_time) >= time_slace) && follow_feel(cur_time)) {
        //println(follow_feel(cur_time)+" "+cur_time+" ...="+TMain.tcontext.TIME_SLICE / 60000+"  minute="+ Utils.get_minute(cur_time))
        for (key <- counterNUM.keys) {
            val coun = new Counter(counterNUM.get(key).get,cur_time)
            counterNUM += (key -> 0)
            if(coun.lines != 0 || TMain.item_ids.contains(key)){
               tblm_actor ! InsertCountor(key, coun)
               if(post != null){
                 post ! POST(key,coun.lines.toString,coun.create_time)
               }
            }
        }
        follow_you_heart(cur_time)
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