package com.jesgoo.tellme.matrix

import scala.collection.mutable.HashMap
import com.jesgoo.tellme.TMain
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.Await
import akka.actor.ActorRef

case object START
case class INCREASE(ins: String)

class MatrixContext(tblm_actor:ActorRef) extends Actor {
  val log = Logging(context.system, this)
  val period = TMain.tcontext.MATRIX_PERIOD
  val slice = TMain.tcontext.TIME_SLICE
  implicit val timeout = Timeout(1000)

  var last_time = System.currentTimeMillis()

  val counterNUM = new HashMap[String, Long]

  override def receive = {
    case START =>
      val cur_time = System.currentTimeMillis()
      if (cur_time - last_time >= slice) {
        for (key <- counterNUM.keys) {
          val coun = new Counter(counterNUM.get(key).get)
          counterNUM += (key -> 0)
          tblm_actor ! InsertCountor(key, coun)
        }
        last_time = cur_time
        tblm_actor!ClearTable
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