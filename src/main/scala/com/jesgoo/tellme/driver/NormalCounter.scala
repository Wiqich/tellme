package com.jesgoo.tellme.driver

import com.jesgoo.tellme.matrix.Data
import com.jesgoo.tellme.matrix.MatrixContext
import akka.actor.Actor
import akka.actor.ActorRef
import akka.event.Logging
import com.jesgoo.tellme.matrix.INCREASE

class NormalCounter(mc:ActorRef,actor:ActorRef) extends Actor{
  val log = Logging(context.system, this)
  def receive = {
    case d:Data => 
      if(actor != null){
          actor ! d
      }
      mc ! INCREASE(d.dname)
    case _ => log.info("received unknown message")
  }
}