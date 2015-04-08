package com.jesgoo.tellme.matrix

import akka.actor.Actor
import scala.collection.mutable.HashMap
import akka.event.Logging

case class InsertCountor(name: String, counter: Counter)
case object ClearTable
case object GETALL
case class TABLES_HASHMAP(t_hm: HashMap[String, Table])
case class ADD_TABLE(line: String)

class TableManager extends Actor {
  val log = Logging(context.system, this)
  val tables = new HashMap[String, Table]

  def receive = {
    case InsertCountor(name, counter) =>
      if (!tables.contains(name)) {
        tables.+=(name -> new Table(name))
      }
      tables.get(name).get.addCounter(counter)
    case ClearTable =>
      for (t <- tables.values) {
        t.clearCounter()
      }
    case GETALL =>
      sender ! TABLES_HASHMAP(tables)
    //sender ! tables.clone()
    case ADD_TABLE(line) =>
      try {
        val t = Table.buildFromString(line)
        tables += (t.tname -> t)
      } catch {
        case e: Exception =>
          e.printStackTrace()
          System.exit(1)
      }
    case _ => log.warning("unknown message")
  }
}