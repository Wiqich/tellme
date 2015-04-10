package com.jesgoo.tellme.driver

import scala.sys.process._
import akka.actor.ActorRef
import com.jesgoo.tellme.matrix.Data
import akka.actor.Actor
import com.jesgoo.tellme.matrix.START
import scala.concurrent.Await
import com.jesgoo.tellme.tools.Utils
import akka.actor.Props

class TailSource(file: String, nexts: Array[ActorRef]) extends Actor {
  val cmd = Seq("tail", "-n 0", "-F", file)
  val filename = Utils.basename(file)
  var it: Iterator[String] = null
  def init() {
    try {
      val strp = cmd.lineStream
      it = strp.iterator
    } catch {
      case e: Exception =>
        it = null
    }
  }
  var tmpstr = ""
  override def receive = {
    case START =>
      if (it == null) {
        init()
      } else {
        try {
          tmpstr = it.next
          val data = new Data(filename, tmpstr)
          for(next<-nexts){
            next ! data
          }
        } catch {
          case e: Exception =>
            e.printStackTrace()
            it = null
            Thread.sleep(2000)
        }
      }
      self ! START
  }
}