package com.jeff.compiler.bool


class ParsingException (message:String) extends Exception(message) {

  private val E = "Error While parsing."

  def this(unExpected:Char) {
    this(s"$E Unexpected character found:$unExpected")
  }

  def this(expected:String, found:String) {
    this(s"$E Expected $expected but found $found")
  }

}
