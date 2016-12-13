package com.jeff.compiler.errorhandling

import org.antlr.v4.runtime._

class ParseErrorListener extends BaseErrorListener {

  override def syntaxError(recognizer: Recognizer[_, _], offendingSymbol: scala.Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): Unit = {
    throw Errors.parseError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
  }

}
