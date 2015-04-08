package com.jesgoo.tellme.matrix

import scala.collection.mutable.ListBuffer

case class KV(key:String,value:String)

class DespKeyValue(name :String) {
  val keys = new ListBuffer[KV]
  val dname = name
  def addKV(kv:KV) = { keys+=kv}
}