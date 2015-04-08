package com.jesgoo.tellme.driver

import scala.collection.mutable.HashMap
import com.google.protobuf.Message
import com.jesgoo.tellme.TMain
import com.jesgoo.tellme.matrix.Data
import akka.actor.Actor
import akka.actor.ActorRef
import akka.event.Logging
import com.jesgoo.tellme.tools.Utils
import com.jesgoo.tellme.matrix.INCREASE

class ParserMessage(mc:ActorRef,actor:ActorRef) extends Actor{
  val log = Logging(context.system, this)
 
  var builder : Message.Builder = null
  
  def initMessage(d:Data) = {
    if(builder == null){
      if(TMain.map_ids.contains(d.dname)){
        if(TMain.map_ids.get(d.dname).get.toUpperCase() == "EVENT"){
          builder = jesgoo.protocol.Protocol.Event.newBuilder
        }else if(TMain.map_ids.get(d.dname).get.toUpperCase() == "UI"){
          builder = jesgoo.uilog.Uilog.NoticeLogBody.newBuilder
        }
      }
    }
  }
  def receive = {
    case d:Data => 
      try{
      if(actor != null){
          initMessage(d)
          val tmp_str = d.context.split(" ")
          builder.mergeFrom(Utils.base64Decoder(tmp_str(tmp_str.length-1)).toArray)
          d.message = builder.build()
          actor ! d
      }
      }catch{
        case e:Exception =>
          e.printStackTrace()
            mc ! INCREASE("PARSER_MESSAGE:ERRROR")
      }
    case _ => log.info("received unknown message") 
  }
}