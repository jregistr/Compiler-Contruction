package com.jeff.compiler.bool


import scala.collection.mutable.{Map => IMap}
import scala.util.{Failure, Success}

class Parser {

  private val store = IMap() ++ (for(e <- 'a' to 'z') yield e -> false).toMap
  private val lex = new LexAnalyzer

  def parse(expression:String):Unit = {
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
          case '?' =>
            matchEnd()
            println(store(char))
          case '=' =>
            matchEnd()
            store(char) = e()
        }
    }
  }

  private def matchEnd():Unit = {
    val value = lex.matches(Const.END)
    value match {
      case Failure(er) => throw er
    }
  }

  private def e():Boolean = {
    var value = f()
    while (lex.matches('^').isSuccess) {
      value = value ^ f()
    }
    value
  }

  private def f():Boolean = {
    var value = g()
    while (lex.matches('|').isSuccess) {
      value = value | g()
    }
    value
  }

  private def g():Boolean = {
    var value = h()
    while (lex.matches('&').isSuccess) {
      value = value & h()
    }
    value
  }

  private def h():Boolean = {
    lex.matches('!') match {
      case Failure(_) => i()
      case Success(_) => !h()
    }
  }

  private def i():Boolean = {
    lex.matches('(') match {
      case Failure(_) => j()
      case Success(_) =>
        val value = e()
        lex.matches(')') match {
          case Success(_) => value
          case Failure(er) => throw er
        }
    }
  }

  private def j():Boolean = {
    val check = lex.matches(Const.LIT)
    check match {
      case Success(inner) => if(inner == '0') false else true
      case Failure(_) =>
        val id = lex.matches(Const.ID)
        id match {
          case Failure(err) => throw err
          case Success(idValue) =>
            store(idValue)
        }
    }
  }


}
