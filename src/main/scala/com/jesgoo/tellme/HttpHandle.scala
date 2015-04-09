package com.jesgoo.tellme

import scala.collection.mutable.HashMap
import scala.concurrent.Await
import com.jesgoo.tellme.matrix.GETALL
import com.jesgoo.tellme.matrix.TABLES_HASHMAP
import com.jesgoo.tellme.matrix.Table
import akka.actor.Actor
import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http
import spray.http._
import spray.http.HttpMethods._
import spray.json._
import scala.collection.mutable
import spray.can.server.Stats
import com.jesgoo.tellme.tools.Utils
object MyJsonProtocol extends DefaultJsonProtocol {
  implicit object jsonFormat extends RootJsonFormat[HashMap[String, Table]] {
    def write(hm: HashMap[String, Table]) : JsArray = {
      var jas_v = Vector.empty[JsObject]
      for (key <- hm.keysIterator) {
        var tmp_jsa_v = Vector.empty[JsObject]
        for (c <- hm.get(key).get.counterlist) {
          val tmp_jso = JsObject(
            "time" -> JsNumber(Utils.formate_time(c.create_time)),
            "number" -> JsNumber(c.lines))
          tmp_jsa_v = tmp_jso+:tmp_jsa_v
        }
        val table_jso = JsObject(
          "table_name" -> JsString(key),
          "counters" -> JsArray(tmp_jsa_v))
        jas_v = table_jso+:jas_v
      }
      JsArray(jas_v)
    }

    def read(value: JsValue) = value match {
      case _ => deserializationError("Json expected")
    }
  }
}
import MyJsonProtocol._
class HttpHandle(tblm_actor: ActorRef) extends Actor {
  implicit val timeout = Timeout(5000)

  override def receive = {
    case _: Http.Connected => sender ! Http.Register(self)
    case HttpRequest(GET, Uri.Path("/getdata"), _, _, _) =>
      val future = tblm_actor ? GETALL
      val result = Await.result(future.mapTo[TABLES_HASHMAP], timeout.duration).t_hm
      val jsonAst = result.toJson
      sender ! HttpResponse(entity = jsonAst.toString())
    case HttpRequest(GET, Uri.Path("/getdata_last"), _, _, _) =>
      val future = tblm_actor ? GETALL
      val result = Await.result(future.mapTo[TABLES_HASHMAP], timeout.duration).t_hm
      val t_res = new HashMap[String, Table]
      for(key <- result.keysIterator){
        val t = new Table(key)
        t.addCounter(result.get(key).get.lastCounter)
        t_res.+=(key -> t)
      }
      val jsonAst = t_res.toJson
      sender ! HttpResponse(entity = jsonAst.toString())
      
    case _: HttpRequest => sender ! HttpResponse(status = 404, entity = "Unknown resource!")
    case Timedout(HttpRequest(method, uri, _, _, _)) =>
      sender ! HttpResponse(
        status = 500,
        entity = "The " + method + " request to '" + uri + "' has timed out..."
      )
  }
}