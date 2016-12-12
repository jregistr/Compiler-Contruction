package com.jeff.compiler.bool

import scala.collection.mutable.{Queue => Line}
import scala.util.{Failure, Success, Try}

class LexAnalyzer {

  private val line: Line[Char] = new Line[Char]()

  def setText(text: String): Unit = {
    line.clear()
    text.foreach(char => {
      if (char != ' ') {
        line.enqueue(char)
      }
    })
  }

  def matches(tok: Char): Try[Char] = {
    val res = consume()
    res match {
      case Failure(err) => Failure(err)
      case Success(value) =>
        if (tok == value._1)
          Success(value._2)
        else
          Failure(new ParsingException(
        tok match {
          case Const.ID => "an id"
          case Const.LIT => "literal value"
          case _ => tok.toString
        }
      ))
    }
  }

  def matches(tok1:Char, tok2:Char):Try[Char] = {
    val res = matches(tok1)
    res match {
      case Success(v) => Success(v)
      case Failure(_) => matches(tok2)
    }
  }

  private def consume(): Try[(Char, Char)] = {
    val top = if (line.nonEmpty) Some(line.dequeue()) else None
    top match {
      case None | Some('\n') =>
        Success(Const.END, Const.END)
      case Some(inner) =>
        inner match {
          case x if x.isLetter && x.isLower && x <= 'z' => Success(Const.ID, inner)
          case y if y == '0' || y == '1' => Success(Const.LIT, inner)
          case '&' | '|' | '^' | '!' | '=' | '?' | '(' | ')' => Success(inner, inner)
          case _ => Failure(new ParsingException(inner))
        }
    }
  }

}