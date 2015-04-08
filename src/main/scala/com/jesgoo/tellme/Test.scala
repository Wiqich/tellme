package com.jesgoo.tellme

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorSystem

case object Vcor
case object Mvcc

class actor1 extends Actor{
  override def receive = {
    case Vcor =>
      println("Start sleep")
      Thread.sleep(10000)
      println("End sleep")
    case Mvcc =>
      println("v587")
    
  }
}

object Test {
  implicit val system = ActorSystem("Main")
  def main(args:Array[String]){
    val tblm_actor = system.actorOf(Props[actor1])
    tblm_actor ! Vcor
     val tblm_actor2 = system.actorOf(Props[actor1])
    tblm_actor2 ! Mvcc
  }
}