package com.jeff.compiler.bool

import scala.util.{Failure, Success, Try}

class  LexAnalyzer {

  private var line:String = "$"
  private var yy:Char = ' '
  private var token:Char = ' '

  def setText(text:String): Unit = {
    line = text.replaceAll("\\s", "")
  }

  def matches(tok:Char):Try[Char] = {
    if(tok == token) {
      val lexVal = yy
      consume()
      Success(lexVal)
    }else {
      Failure(new ParsingException(s"$tok", "$yy"))
    }
  }

  private def consume(): Unit = {

  }

}