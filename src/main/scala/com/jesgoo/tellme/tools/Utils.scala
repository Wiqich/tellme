package com.jesgoo.tellme.tools

import com.google.protobuf.Message

object Utils {
  val base64decoder = new sun.misc.BASE64Decoder
  def basename(file:String):String = {
    file.substring(file.lastIndexOf("/") + 1)
  }
  
  def base64Decoder(src:String): Seq[Byte] = {
    Base64.decode(src)
    //base64decoder.decodeBuffer(src)
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
    (time/1000%86400%3600/60).toInt
  }
  
  def main(args:Array[String]){
    val bbt = base64Decoder("Cig4YmQwYzUwMTVhYzExMGY3MWVkNzIwOWFjMGYwMjI3OWIwYzE5ZGY4ENXcnakFGioIAxIIZmM3ZjMwNDAaCGFjMWYxYjJhIgIKACoOCgptLmJ4d3gub3JnEgAiHAoIczJhYzIwYTAQARoFCDAQwAIgASi4v8jMySkq-QEIARICCAQaFQgBEg81MDg2NTY5NDMyOTY4NjkYACIAKgAyoAFNb3ppbGxhLzUuMCAoTGludXg7IFU7IEFuZHJvaWQgNC4zOyB6aC1jbjsgSFctSFVBV0VJIEM4ODE2RC9DODgxNkRWMTAwUjAwMUM5MkIxODU7IDk2MCo1NDA7IENUQy8yLjApIEFwcGxlV2ViS2l0LzUzNC4zMCAoS0hUTUwsIGxpa2UgR2Vja28pIE1vYmlsZSBTYWZhcmkvNTM0LjMwOiA3MkRCNzgxNjU0MUJEQkRFNTUyMjdENUM1MEVFNTUwNkAASg8zNTIyNzMwMTczODYzNDAyLAgFEAIYASDWzkIoADAAOABAZEhkUKRXWJCYRIoBCDEwMDQ1OTA3kgEAoAEAQABKCggCEgYIARAnGABSEggAEg4yMjIuMjIxLjE2LjExN1oOY3RyX21vZGVsOmJhc2VaBG51bGxiBghWEAIYK2oOMjIyLjIyMS4xNi4xMTdwAQ==")
    val bbt2 = base64Decoder("CgQIABAmEtMKCAASKGJmY2E0NzVhMWUzYTMzZGE0NjRmZDI0OWQ3NGNmNjJjNzE4ZjIzYjQYqdSOqQUggsz3ugsooNWOqQUwgsz3ugs6IgoIYWI2NmIzYzYSCGFjZjIwMjUxGgAqCHM1MzgxY2M3MAFCBwhWEBYYtwJKEAgAEAAYACjg3EE4AUACSABSBAgBEGRiqwgKpghodHRwOi8vbS5iYWlkdS5jb20vbW9iYWRzLnBocD9hYV9LMDBhRjBTWThqdF9rWFBRMk1LRkxmSXA3cEQxYWsxckhhRkJDUXZvU2o3THRVRC1UQUNYaWdHLW1tTUE4aGNRVWFxbi14cG1xZjRrWkltblYtNTVLdUpsZmxfZTJLUDY0azNzMXVpbzVUNmhyQzF6a0FyU0k3UDRBeEtETkhraTZ1WWYuRFJfaWdSeDJlQzlCODRjQVR4VVowZTZCNllOZ3c0ZXZUeWo1ZVozY2xNU0hyNVZ5RkI4ejFfSG1NXzRtVFR6czFmX3VQaEhJbUMwLm1nSzg1SERzbmp3LW5BUlkwQVBWNUhtWTBBN1Y1SDAwbUxLVzVIYzBVZ2ZxbmZLV1R2M3Fyam1kUGpjWW5qYzNQamJ6UGowNDBaUDg1SDZ2UEhmelBqMHpyamY0bldmc3JpWTBVTG5xbmZLYnVnbXFRZktzcHlmcW5INjBtdi1iNUhuelAwS1dwalkwVWhOWTVIMDBJdi1oNUhEMFRaRmI1Zks4SWpZejBBLWJ1aERxMFpLbzV5UDhRaHFzdUFEOG1pNHNwQXE4VXY3X21NTlZUdjlFSWdQQ1VMUjB1QVB4VExQc3B5ZnFuNktibWRxaFVXWTB1QVB4dWg0ejVIMDB1QVB4SUFZcW4wS2JtZHFZSUhZczBBd1dnTHdiNUgwMHVBUHhJWi1zdUhZczBBd1dnTE56VWpZMHVBUHhtdjZxMEF3V2d2dzFUQS1iNUhtMHVBUHhUQWs5bXZOeHB5ZnFuMEtibWRxYnVndUdtdk54cHlmcW42S2JtZHFCVGg3OHVqWTB1QVB4VFpGR212TnhweWZxcjBLYm1kcWhwZ0YxSTdxWVRoN2J1SFkxbnNLYm1kcTF1eVBFVWh3eElaRjl1QVJxbjFuc242S2JtZHE4dWd3eElaLXN1TnFHdWpZazBBd1dndjdZVXZkeHB5ZnFuMEtibWRxX21NUHhweWZxbjBLYm1kcUdJN3FHdWpZczBBd1dndjdiZ0xucVBIUnpudjc5blctV3JqbXNteWZZbWZLYm1kcUVUZHFHdWpZelFIZjhuNktibWRxVnV5d0dtTnFZWGdLLTVIMDB1QVB4VHZOV2d2d0VVeTdHVVdkYnV5dTlJeWtZMEE3V21MZnFtZ0tHMEFxMWd2M3FteTRiVGhxR3VhWVlRV2MwbXlQWUlqWXowQXdXVU1mcW5IMHMwQVAxVWdmcW4wS0J1THdzNUgwMFRBdVlUV1lzMEFQX3B2N1lJWjBxVEEtc3VmS3pUZ045NUhfVnIxXzBUQTlFNUgwMHVnOXM1Z3dFUUF3SlFIRF9YeW5fSVpuX3VNbl9VQWZfVGc2Vm5Ca2htemtzWEJrTElhczBtTEZXNUhEZHJIUjAQAHIMCAISCDEwMDRlMGU0eg5jdHJfbW9kZWw6YmFzZXoEbnVsbIIBIEY1QTJDNjc3MDJFNjVEQjc1NTIzQUEyNzY2OUM2MEVDigElCAESCAgEEAIYACAAGhUIARIPMDM1MDc0NTU4MDk0MzgzGAA4AJIBDAgCEggIARAnGAAgAJoBDQiQThoICAAQARgAIAGgBgCoBon/kYaC1OqRVQ==")
    val builder :Message.Builder = jesgoo.uilog.Uilog.NoticeLogBody.newBuilder
    //builder.mergeFrom(bbt.toArray)
    //println(builder.build().toString().replace("/r/n", " "))
    
//    val builder2 :Message.Builder = jesgoo.protocol.Protocol.Event.newBuilder
//    builder2.mergeFrom(bbt2.toArray)
//    println(builder2.build().toString().replace("/r/n", " "))
//    val t:Long = 1428577142000L
//    println(formate_time(t))
  }
}