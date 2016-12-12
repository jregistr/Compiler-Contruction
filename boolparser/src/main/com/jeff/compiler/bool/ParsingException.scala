package com.jeff.compiler.bool


class ParsingException (message:String) extends Exception(message) {

  def this(unExpected:Char) {
    this(s"Error While parsing. Unexpected character found:$unExpected")
  }

  def this(expected:String, found:String) {
    this(s"Error While parsing. Expected $expected but found $found")
  }

}
