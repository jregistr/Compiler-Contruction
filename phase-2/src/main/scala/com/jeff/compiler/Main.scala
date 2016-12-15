package com.jeff.compiler

import com.compiler.generated.antlr.{MiniJavaLexer, MiniJavaParser}
import com.jeff.compiler.errorhandling.ParseErrorListener
import com.jeff.compiler.typechecking.definitions.{Klass, Scope}
import com.jeff.compiler.typechecking.listeners.{ClassListener, SymbolListener}
import com.jeff.compiler.util.Aliases.ClassMap
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.{ParseTree, ParseTreeProperty, ParseTreeWalker}
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream, DiagnosticErrorListener}

import scala.collection.mutable.{Map => MutableMap}


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

    val classes:ClassMap = MutableMap()
    val scopes:ParseTreeProperty[Scope] = new ParseTreeProperty[Scope]()
    classes ++= List("int", "int[]", "boolean").map(name => (name, new Klass(name, None)))

    classWalk(classes, tree)
    symbolsWalk(classes, tree, scopes)

//    println(classes("Car").findFieldLocally("gas"))

  }

  private def classWalk(classes:ClassMap, tree:ParseTree): Unit = {
    val classListener = new ClassListener(classes)
    ParseTreeWalker.DEFAULT.walk(classListener, tree)
    classListener.errorForMissingParents()
  }

  private def symbolsWalk(classes:ClassMap, tree:ParseTree, scopes:ParseTreeProperty[Scope]):Unit = {
    val symbolListener = new SymbolListener(classes, scopes)
    ParseTreeWalker.DEFAULT.walk(symbolListener, tree)
  }

}
