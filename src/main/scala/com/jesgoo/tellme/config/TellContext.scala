package com.jesgoo.tellme.config

class TellContext {
  
  var config = new TellConfig
  
  var matrix_period_default=(config.getLong("time.slice", 1000/10)).toLong
  def TABLE_TTL_MS = config.getLong("table.ttl.ms",8000)
  
  def TAIL_FILES = config.get("tail.files","")
  
  def MATRIX_PERIOD = config.getLong("matrix.period", matrix_period_default)
  
  def TIME_SLICE = config.getLong("time.slice", 1000)
  
  def HTTP_PORT= config.getInt("http.port",8080)
  
  def PARSER_MAP = config.get("parser.map","")
  
  def EXCUTE_ITEM = config.get("excute.item","")
  
  def DUMP_DB_FILE = config.get("dump.db.file","/tmp/tellme/dump.db")
  
  def CONFIG_UPDATE_PERIOD = config.getInt("config.update.period",60000)
  
  def newConfig : Boolean = {
    val configv = new TellConfig
    if(!config.equals(configv)){
        println("update new config")
        config = configv
        matrix_period_default=(config.getLong("time.slice", 1000/10)).toLong
        true
    }else{
      false
    }
  }
}