package com.jesgoo.tellme.driver

import scala.collection.mutable.ListBuffer
import com.jesgoo.tellme.matrix.Data
import com.jesgoo.tellme.matrix.DespKeyValue
import akka.actor.Actor
import akka.actor.ActorRef
import akka.event.Logging
import com.jesgoo.tellme.matrix.KV
import scala.collection.mutable.HashMap
import com.jesgoo.tellme.matrix.INCREASE
import com.jesgoo.tellme.tools.Utils

case class UPDATE_CUT(cut: String)

class EventExcuteProtoBufCounter(mc: ActorRef, actor: ActorRef) extends ProtoBufCounter {

  val log = Logging(context.system, this)

  val NAME = "EVENT"

  val list_excut = new HashMap[String, DespKeyValue]

  def update_list_excut(excut: String) = {
    list_excut.clear()
    for (cut <- excut.split(",")) {
      val tmp_cut = cut.split("=")
      if (tmp_cut.length == 2) {
        val tmp_despkv = new DespKeyValue(cut)
        val tmp_cut_key = tmp_cut(0).split("#")
        val tmp_cut_value = tmp_cut(1).split("#")
        if (tmp_cut_key.length == tmp_cut_value.length) {
          for (i <- 0 to tmp_cut_key.length-1) {
            val tmp_kv = new KV(tmp_cut_key(i), tmp_cut_value(i))
            tmp_despkv.addKV(tmp_kv)
          }
        }
        list_excut += (tmp_cut(0) -> tmp_despkv)
      }
    }
  }

  def receive = {
    case d: Data =>
      val mesg = d.message.asInstanceOf[jesgoo.protocol.Protocol.Event].getBody
      if (actor != null) {
        actor ! d
      }
      for (cut_dkv_name <- list_excut.keysIterator) {
        var jadge = true
        for (kv <- list_excut.get(cut_dkv_name).get.keys) {
          val m_v = match_id(kv.key, mesg)
          if (m_v == null) {
            mc ! INCREASE(d.dname + ":" + kv.key  + ":ERROR")
          } else {
            if (!m_v.contains(kv.value)) {
              jadge = false
            }
          }
        }
        if (jadge) {
          mc ! INCREASE(d.dname + ":" + list_excut.get(cut_dkv_name).get.dname)
        }
      }
    case UPDATE_CUT(cut) => 
      update_list_excut(cut)

    case _               => log.info("received unknown message")
  }

  def match_id(id: String, mesg: jesgoo.protocol.Protocol.Event.Body): String = id match {
    case "media_id" =>
      mesg.getMedia.getMediaId
    case "channel_id" =>
      mesg.getMedia.getChannelId
    case "os_id" =>
      mesg.getDevice.getOs.getNumber.toString
    case "adslot_id" =>
      mesg.getMedia.getAdslotId
    case "dsp_id" =>
      mesg.getDspInfo.getDsp.getNumber.toString
    case "dspmedia_id" =>
      mesg.getDspInfo.getMediaId
    case "exp_tags" =>
      Utils.listToString(mesg.getExptagsList.toArray)    
    case _ =>
      null
  }

}