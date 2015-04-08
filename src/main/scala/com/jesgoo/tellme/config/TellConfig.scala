package com.jesgoo.tellme.config

import scala.collection.mutable.HashMap
import scala.io.Source
import scala.collection.JavaConverters._
class TellConfig(loadDefaults: Boolean = true) extends Cloneable {

  val settings = new HashMap[String, String]

  if (loadDefaults) {
    for ((k, v) <- System.getProperties.asScala if k.startsWith("TELLME.")) {
      settings(k) = v
    }
    if (!settings.contains("TELLME.CONFIG")) {
      settings("TELLME.CONFIG") = "conf"
    }
    val conf_dir = settings("TELLME.CONFIG")
    for (line <- Source.fromFile(conf_dir + "/tellme-site.conf").getLines()) {
      if (!line.startsWith("#") && line.trim() != "") {
        val tmp = line.split("=", 2)
        settings(tmp(0)) = tmp(1)
      }
    }
  }
  override def clone: TellConfig = {
    new TellConfig(false).setAll(settings)
  }
  def setAll(settings: Traversable[(String, String)]) = {
    this.settings ++= settings
    this
  }

  def set(k: String, v: String): TellConfig = {
    if (k == null) {
      throw new NullPointerException("null key")
    }
    if (v == null) {
      throw new NullPointerException("null value")
    }
    this
  }

  def get(k: String): String = {
    settings.getOrElse(k, throw new NoSuchElementException(k))
  }

  def get(key: String, defaultValue: String): String = {
    settings.getOrElse(key, defaultValue)
  }

  def getAll: Array[(String, String)] = settings.clone().toArray

  def getOption(key: String): Option[String] = {
    settings.get(key)
  }

  def getInt(key: String, defaultValue: Int): Int = {
    getOption(key).map(_.toInt).getOrElse(defaultValue)
  }

  def getLong(key: String, defaultValue: Long): Long = {
    getOption(key).map(_.toLong).getOrElse(defaultValue)
  }

  def getDouble(key: String, defaultValue: Double): Double = {
    getOption(key).map(_.toDouble).getOrElse(defaultValue)
  }

  def getBoolean(key: String, defaultValue: Boolean): Boolean = {
    getOption(key).map(_.toBoolean).getOrElse(defaultValue)
  }

  override def equals(obj: Any): Boolean = {
    if (obj.isInstanceOf[TellConfig]) {
      val tellconfig = obj.asInstanceOf[TellConfig]
      if (settings.size != tellconfig.settings.size) {
        false
      } else {
        settings.equals(tellconfig.settings)
      }
    } else {
      false
    }
  }
}