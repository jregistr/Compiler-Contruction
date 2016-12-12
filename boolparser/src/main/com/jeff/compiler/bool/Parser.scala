package com.jeff.compiler.bool


import scala.collection.mutable.{Map => IMap}
import scala.util.{Failure, Success}

class Parser {

  private val store = IMap() ++ (for(e <- 'a' to 'z') yield e -> false).toMap
  private val lex = new LexAnalyzer

  def parse(expression:String):Unit = {
    val a = 10 -> "value"
    lex.setText(expression)
    s()
  }

  private def s():Unit = {
    val next = lex.matches(Const.ID)
    next match {
      case Failure(e) => println(e)
      case Success(char) => q(char)
    }
  }

  private def q(char:Char):Unit = {
    val next = lex.matches('?', '=')
    next match {
      case Failure(e) => println(e)
      case Success(value) =>
        value match {
          case '?' => println(store(char))
          case '=' => store(char) = e()
        }
    }
  }

  private def e():Boolean = {
    val
  }

  private def f():Boolean = {
    false
  }

  private def g():Boolean = {
    false
  }

  private def h():Boolean = {
    false
  }

  private def i():Boolean = {
    false
  }

  private def j():Boolean = {
    false
  }

  private def k():Boolean = {
    false
  }


}
