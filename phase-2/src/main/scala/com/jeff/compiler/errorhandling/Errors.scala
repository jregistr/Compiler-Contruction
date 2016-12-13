package com.jeff.compiler.errorhandling

import com.compiler.generated.antlr.MiniJavaParser
import org.antlr.v4.runtime.{RecognitionException, Recognizer}

import scala.collection.JavaConversions._

object Errors {

  private def printAndExist(msg: String): Unit = {
    System.err.println(msg)
    System.exit(1)
  }

  def parseError(recognizer: Recognizer[_, _], offendingSymbol: scala.Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): Unit = {
    Option(e) match {
      case None =>
        printAndExist(s"Error while Parsing at ($line, $charPositionInLine): $msg")
      case Some(_) =>
        val rulesAsString = e.getCtx.toString(recognizer)
        val brackets = (s: Char) => s == '[' || s == ']'
        val rulesList = rulesAsString.split(' ').map((elem) => elem.filterNot(brackets)).toList
        val head = rulesList.head
        val ex: List[String] = e.getExpectedTokens.toList.map((s) => MiniJavaParser.VOCABULARY.getDisplayName(s)).toList

        printAndExist(s"Invalid $head. Expected ${ex.mkString(" ")}, but ${e.getOffendingToken.getText} was found")
    }
  }

}
