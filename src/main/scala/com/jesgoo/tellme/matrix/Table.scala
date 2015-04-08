package com.jesgoo.tellme.matrix

import scala.collection.mutable.ListBuffer

import com.jesgoo.tellme.TMain
class Table(name: String) {
  val tname = name
  val counterlist = new ListBuffer[Counter]

  def addCounter(c: Counter) {
    counterlist.+=(c)
  }

  def toArray(): Array[Counter] = {
    counterlist.toArray
  }

  override def toString: String = {
    val sb = new StringBuilder
    sb ++= (tname) ++= ("#!")
    for (c <- counterlist) {
      sb ++= (c.lines.toString) ++= ("#&") ++= (c.create_time.toString) ++= ("#,")
    }
    val idx = sb.lastIndexOf("#,")
    if (idx < 0 || idx > sb.size) {
      null
    } else {
      sb.toString().substring(0, sb.lastIndexOf("#,"))
    }
  }

  def clearCounter() = {
    val cur_time = System.currentTimeMillis()
    for (c <- counterlist.toArray) {
      if (cur_time - c.create_time > TMain.tcontext.TABLE_TTL_MS) {
        counterlist -= (c)
      }
    }
  }
}

object Table {
  def buildFromString(table_s: String): Table = {
    val t_tmp = table_s.split("#!")
    val t = new Table(t_tmp(0))
    for (line <- t_tmp(1).split("#,")) {
      val tc_tmp = line.split("#&")
      val c = new Counter(tc_tmp(0).toLong, tc_tmp(1).toLong)
      t.addCounter(c)
    }
    t
  }
}