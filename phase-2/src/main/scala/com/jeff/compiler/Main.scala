package com.jeff.compiler

import java.io.InputStream

import com.compiler.generated.antlr.{MiniJavaLexer, MiniJavaParser}
import com.jeff.compiler.errorhandling.ParseErrorListener
import com.jeff.compiler.typechecking.definitions.{Klass, Scope}
import com.jeff.compiler.typechecking.listeners.{ClassListener, SymbolListener}
import com.jeff.compiler.util.Aliases.ClassMap
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.{ParseTree, ParseTreeProperty, ParseTreeWalker}
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream, DiagnosticErrorListener, Token}
import com.jeff.compiler.util.Const._

import scala.collection.mutable.{Map => MutableMap}


object Main {

  def main(args: Array[String]): Unit = {

    val inputStream: InputStream = getClass.getClassLoader.getResourceAsStream("HelloWorld.minijava")
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
    classes ++= List(INT, INTARR, BOOLEAN).map(name => (name, new Klass(name, null, None)))

    classWalk(classes, tree)
    symbolsWalk(classes, tree, scopes)

    classes.remove(INT)
    classes.remove(INTARR)
    classes.remove(BOOLEAN)

//    prettyPrint(classes)

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

//  private def prettyPrint(classes:ClassMap): Unit ={
//    classes.values.foreach(klass => {
//      print(klass.name)
//      if(klass.superClass.isDefined){
//        print(s" extends ${klass.superClass.get.name}")
//      }
//      println()
//
//      println("\tvariables")
//      klass.fields.values.foreach(field => {
//        println(s"\t\t${field.name}:${field.typee.name}")
//      })
//
//      println("\tMethods")
//      klass.methods.values.foreach(method => {
//        println(s"\t\t${method.name}:${method.typee.name} -> [${method.parameters.values.map(p => s"${p.name}:${p.typee.name}").mkString(",")}]")
//
//        println("\t\t\tLocals")
//        method.vars.values.foreach(local => {
//          println(s"\t\t\t\t${local.mutable} ${local.name}:${local.typee.name}")
//        })
//      })
//
//      println("------------------------------------------------------------------")
//    })
//  }

}
