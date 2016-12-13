package com.jeff.compiler.error

import com.compiler.generated.antlr.MiniJavaParser
import org.antlr.v4.runtime._
import scala.collection.JavaConversions._

class ParseErrorListener extends BaseErrorListener {

  override def syntaxError(recognizer: Recognizer[_, _], offendingSymbol: scala.Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): Unit = {
    Option(e) match {
      case None =>
        System.err.println(s"Error while Parsing at ($line, $charPositionInLine): $msg")
      case Some(_) =>
        val rulesAsString = e.getCtx.toString(recognizer)
        val brackets = (s:Char) => s == '[' || s == ']'
        val rulesList = rulesAsString.split(' ').map((elem) => elem.filterNot(brackets)).toList
        val head = rulesList.head
        val ex = e.getExpectedTokens.toList.map((s) => MiniJavaParser.VOCABULARY.getDisplayName(s)).toList

        throw new RuntimeException(s"Invalid $head. Expected ${ex.mkString(" ")}, but ${offendingSymbol.toString} was found")
    }
  }

}
