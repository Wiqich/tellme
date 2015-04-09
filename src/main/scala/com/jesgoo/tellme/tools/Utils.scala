package com.jesgoo.tellme.tools

import com.google.protobuf.Message

object Utils {
  def basename(file:String):String = {
    file.substring(file.lastIndexOf("/") + 1)
  }
  
  def base64Decoder(src:String): Seq[Byte] = {
    Base64.decode(src)
  }
  
  def listToString(lis:Array[_]):String = {
    lis.toString
  }
  def formate_time(time_o:Long) : Long = {
    val time = time_o.toString
    if(time.length() == 10){
      time.toLong
    }else{
      time.substring(0, 10).toLong
    }
  }
  def get_minute(time:Long) :Int = {
    (time%86400%3600/60).toInt
  }
  
  def main(args:Array[String]){
    val bbt = base64Decoder("CigyZGYyODdmMjk5MTllY2UzMzQ3YjNiMjc5Y2U5NTRlOTg3OWEzZmRhEKDVjqkFGi0IAxIIZmM5Y2MwMDEaCGFjZmYwMjUwIgIKACoRCg1nLmZhc3RhcGkubmV0EgAiHQoIczM3MzI3ZjQQCBoGCMACEMACIAEomPvJmskpKrQCCAESAggEGhQIARIOQTAwMDAwNDk3NTQ2MEUYASIAKgAy6wFNb3ppbGxhLzUuMCAoTGludXg7IFU7IEFuZHJvaWQgNC4xLjI7IHpoLWNuOyBIVUFXRUkgQzg4MTUgQnVpbGQvSHVhd2VpQzg4MTUpIEFwcGxlV2ViS2l0LzUzMy4xIChLSFRNTCwgbGlrZSBHZWNrbylWZXJzaW9uLzQuMCBNUVFCcm93c2VyLzUuNCBUQlMvMDI1NDEzIE1vYmlsZSBTYWZhcmkvNTMzLjEgVjFfQU5EX1NRXzUuNS4wXzIyOF9ZWUJfRCBRUS81LjUuMC4yNDE1IE5ldFR5cGUvV0lGSSBXZWJQLzAuMy4wOiBEREZCN0RDMEVCMTFDRERBNTUyM0FBQTAwMjcxMTBCRkABSgAyLQgCEAIYAiDsgTEoADAAOABAZEhkUOqSAVjo2nKKAQgxMDA0NDkzNJIBAKABAEAASgoIAhIGCAEQJxgAUhIIABIOMjE4LjIwNS4xNy4yMzVaDmN0cl9tb2RlbDpiYXNlWgRudWxsWg90ZXh0X2ljb25fdHBsOjVaCnRleHRfdHBsOjViBwhWEBAY4wFqDjIxOC4yMDUuMTcuMjM1cAE=")
    val bbt2 = base64Decoder("CgQIABAmEtMKCAASKGJmY2E0NzVhMWUzYTMzZGE0NjRmZDI0OWQ3NGNmNjJjNzE4ZjIzYjQYqdSOqQUggsz3ugsooNWOqQUwgsz3ugs6IgoIYWI2NmIzYzYSCGFjZjIwMjUxGgAqCHM1MzgxY2M3MAFCBwhWEBYYtwJKEAgAEAAYACjg3EE4AUACSABSBAgBEGRiqwgKpghodHRwOi8vbS5iYWlkdS5jb20vbW9iYWRzLnBocD9hYV9LMDBhRjBTWThqdF9rWFBRMk1LRkxmSXA3cEQxYWsxckhhRkJDUXZvU2o3THRVRC1UQUNYaWdHLW1tTUE4aGNRVWFxbi14cG1xZjRrWkltblYtNTVLdUpsZmxfZTJLUDY0azNzMXVpbzVUNmhyQzF6a0FyU0k3UDRBeEtETkhraTZ1WWYuRFJfaWdSeDJlQzlCODRjQVR4VVowZTZCNllOZ3c0ZXZUeWo1ZVozY2xNU0hyNVZ5RkI4ejFfSG1NXzRtVFR6czFmX3VQaEhJbUMwLm1nSzg1SERzbmp3LW5BUlkwQVBWNUhtWTBBN1Y1SDAwbUxLVzVIYzBVZ2ZxbmZLV1R2M3Fyam1kUGpjWW5qYzNQamJ6UGowNDBaUDg1SDZ2UEhmelBqMHpyamY0bldmc3JpWTBVTG5xbmZLYnVnbXFRZktzcHlmcW5INjBtdi1iNUhuelAwS1dwalkwVWhOWTVIMDBJdi1oNUhEMFRaRmI1Zks4SWpZejBBLWJ1aERxMFpLbzV5UDhRaHFzdUFEOG1pNHNwQXE4VXY3X21NTlZUdjlFSWdQQ1VMUjB1QVB4VExQc3B5ZnFuNktibWRxaFVXWTB1QVB4dWg0ejVIMDB1QVB4SUFZcW4wS2JtZHFZSUhZczBBd1dnTHdiNUgwMHVBUHhJWi1zdUhZczBBd1dnTE56VWpZMHVBUHhtdjZxMEF3V2d2dzFUQS1iNUhtMHVBUHhUQWs5bXZOeHB5ZnFuMEtibWRxYnVndUdtdk54cHlmcW42S2JtZHFCVGg3OHVqWTB1QVB4VFpGR212TnhweWZxcjBLYm1kcWhwZ0YxSTdxWVRoN2J1SFkxbnNLYm1kcTF1eVBFVWh3eElaRjl1QVJxbjFuc242S2JtZHE4dWd3eElaLXN1TnFHdWpZazBBd1dndjdZVXZkeHB5ZnFuMEtibWRxX21NUHhweWZxbjBLYm1kcUdJN3FHdWpZczBBd1dndjdiZ0xucVBIUnpudjc5blctV3JqbXNteWZZbWZLYm1kcUVUZHFHdWpZelFIZjhuNktibWRxVnV5d0dtTnFZWGdLLTVIMDB1QVB4VHZOV2d2d0VVeTdHVVdkYnV5dTlJeWtZMEE3V21MZnFtZ0tHMEFxMWd2M3FteTRiVGhxR3VhWVlRV2MwbXlQWUlqWXowQXdXVU1mcW5IMHMwQVAxVWdmcW4wS0J1THdzNUgwMFRBdVlUV1lzMEFQX3B2N1lJWjBxVEEtc3VmS3pUZ045NUhfVnIxXzBUQTlFNUgwMHVnOXM1Z3dFUUF3SlFIRF9YeW5fSVpuX3VNbl9VQWZfVGc2Vm5Ca2htemtzWEJrTElhczBtTEZXNUhEZHJIUjAQAHIMCAISCDEwMDRlMGU0eg5jdHJfbW9kZWw6YmFzZXoEbnVsbIIBIEY1QTJDNjc3MDJFNjVEQjc1NTIzQUEyNzY2OUM2MEVDigElCAESCAgEEAIYACAAGhUIARIPMDM1MDc0NTU4MDk0MzgzGAA4AJIBDAgCEggIARAnGAAgAJoBDQiQThoICAAQARgAIAGgBgCoBon/kYaC1OqRVQ==")
    val builder :Message.Builder = jesgoo.uilog.Uilog.NoticeLogBody.newBuilder
    builder.mergeFrom(bbt.toArray)
    println(builder.build().toString().replace("/r/n", " "))
    
    val builder2 :Message.Builder = jesgoo.protocol.Protocol.Event.newBuilder
    builder2.mergeFrom(bbt2.toArray)
    println(builder2.build().toString().replace("/r/n", " "))
    val t:Long = 1428577142000L
    println(formate_time(t))
  }
}