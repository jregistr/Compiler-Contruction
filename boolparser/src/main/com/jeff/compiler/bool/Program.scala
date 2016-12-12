package com.jeff.compiler.bool

import scala.io.StdIn._

object Program {

  def main(args: Array[String]): Unit = {
    val lex = new LexAnalyzer
    lex.setText(readLine("Enter Expression:"))
    while (true) {
      val check = readLine("Enter char to check:").charAt(0)
      println(lex.matches(check))
    }
  }

}
