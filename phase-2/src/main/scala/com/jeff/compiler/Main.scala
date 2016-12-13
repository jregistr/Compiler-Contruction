package com.jeff.compiler

import com.compiler.generated.antlr.{MiniJavaLexer, MiniJavaParser}
import com.jeff.compiler.errorhandling.ParseErrorListener
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream, DiagnosticErrorListener}


object Main {

  def main(args: Array[String]): Unit = {

    val inputStream = getClass.getClassLoader.getResourceAsStream("HelloWorld.minijava")
    val antlrStream = new ANTLRInputStream(inputStream)

    val lexer = new MiniJavaLexer(antlrStream)
    val tokenStream = new CommonTokenStream(lexer)
    val parser = new MiniJavaParser(tokenStream)

    parser.removeErrorListeners()

    parser.addErrorListener(new DiagnosticErrorListener())
    parser.getInterpreter.setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION)

    parser.addErrorListener(new ParseErrorListener)

    val tree:ParseTree = parser.goal()

  }

}
