package com.jesgoo.tellme.matrix

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat

import com.jesgoo.tellme.TMain

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.event.Logging
import spray.http.HttpEntity.apply


case class POST(name:String,data:String,time:Long)
class PostToThird extends Actor {
  val log = Logging(context.system, this)
  val host = "http://127.0.0.1"
  val minuteFormat = new SimpleDateFormat("yyyyMMddHHmm")
  override def receive = {
    case POST(name,data,time) =>
      val url = host+":"+TMain.tcontext.POST_COUNTER_PORT+"/index/"+name+"/"+minuteFormat.format(time)+"00"
      //println(url)
      submitPOST(url,data)
    case _ => 
      log.warning("received unknown message")
  }
  var reqOut : OutputStreamWriter= null
  var in : InputStream = null
  def submitPOST(url:String,data:String) = {
    
    try{
      val reqUrl = new java.net.URL(url)
      val connection = reqUrl.openConnection 
      connection.setDoOutput(true);  
      reqOut = new OutputStreamWriter(connection.getOutputStream());  
      reqOut.write(data)
      reqOut.flush
      
      var charCount = -1
      in = connection.getInputStream()
      val responseMessage = new StringBuilder
      val br = new BufferedReader(new InputStreamReader(in, "GBK")) 
      charCount = br.read()
      while (charCount != -1) {  
        responseMessage.append(charCount.toChar)
        charCount = br.read()
      }
      if(!responseMessage.toString().contains("true")){
        log.warning("send url "+url+" data is "+data+" result = "+responseMessage.toString())
      } 
    }catch{
      case e:Exception =>
        e.printStackTrace()
    }finally{
      if(reqOut != null){
        reqOut.close()
      }
      if(in !=null){
        in.close()
      }
    }
  }
}

object test {
  def main(args:Array[String]){
    val system = ActorSystem("name")
    val postTo = system.actorOf(Props[PostToThird], name = "postToThird")
    postTo ! POST("test","564330",1429952597000L)
  }
}