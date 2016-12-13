package com.jeff.compiler.bool

import scala.io.StdIn._

object Program {

  def main(args: Array[String]): Unit = {
    val parser = new Parser
    while (true) {
      val expression = readLine("Expression:")
      parser.parse(expression)
    }

//    val stuff = List(
//      List(1,2,3),
//      List(4,5,6),
//      List(7,8,9)
//    )

//    for {
//      firstLevel <- stuff
//      secondLevel <- firstLevel
//    }
  }

}
