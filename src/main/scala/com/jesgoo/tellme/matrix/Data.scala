package com.jesgoo.tellme.matrix

import com.google.protobuf.Message

class Data(name:String , text:String) {
    val time = System.currentTimeMillis()
    val dname = name
    val context = text
    var message:Message = null
}