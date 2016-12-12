package com.jeff.compiler.bool

import scala.io.StdIn._

object Program {

  def main(args: Array[String]): Unit = {
    val parser = new Parser
    while (true) {
      val expression = readLine("Expression:")
      parser.parse(expression)
    }
  }

}
