package com.example.messages

case class SetRequest(key: String, value: Object)
case class GetRequest(key: String)
case class KeyNotFound(key: String) extends Exception