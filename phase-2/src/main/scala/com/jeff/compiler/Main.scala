package com.jeff.compiler

import java.io.{File, InputStream}
import java.nio.file.Paths

import com.compiler.generated.antlr.{MiniJavaLexer, MiniJavaParser}
import com.jeff.compiler.codegeneration.CodeGenerator
import com.jeff.compiler.errorhandling.ParseErrorListener
import com.jeff.compiler.typechecking.definitions.{Klass, Scope}
import com.jeff.compiler.typechecking.listeners.{ClassListener, SymbolListener}
import com.jeff.compiler.typechecking.visitors.TypeCheckWalker
import com.jeff.compiler.util.Aliases.ClassMap
import com.jeff.compiler.util.Const
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.tree.{ParseTree, ParseTreeProperty, ParseTreeWalker}
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream, DiagnosticErrorListener, Token}
import com.jeff.compiler.util.Const._

import scala.collection.mutable.{Map => MutableMap}


object Main {

  def main(args: Array[String]): Unit = {

    val inputStream: InputStream = getClass.getClassLoader.getResourceAsStream("Simple.minijava")
    val antlrStream = new ANTLRInputStream(inputStream)

    val lexer = new MiniJavaLexer(antlrStream)
    val tokenStream = new CommonTokenStream(lexer)
    val parser = new MiniJavaParser(tokenStream)

    parser.removeErrorListeners()

    parser.addErrorListener(new DiagnosticErrorListener())
    parser.getInterpreter.setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION)

    parser.addErrorListener(new ParseErrorListener)

    val tree: ParseTree = parser.goal()

    val classes: ClassMap = MutableMap()
    val scopes: ParseTreeProperty[Scope] = new ParseTreeProperty[Scope]()
    val calls = new ParseTreeProperty[Klass]()

    classes ++= List(INT, INTARR, BOOLEAN).map(name => (name, new Klass(name, null, None)))

    declareClasses(classes, tree)
    declareAndPreCheckSymbols(classes, tree, scopes)

    runTypeCheck(classes, tree, scopes, calls)

    codeGenWalk(classes, tree, scopes, calls)
  }

  private def declareClasses(classes: ClassMap, tree: ParseTree): Unit = {
    val classListener = new ClassListener(classes)
    ParseTreeWalker.DEFAULT.walk(classListener, tree)
    classListener.errorForMissingParents()
    println("Collected classes")
  }

  private def declareAndPreCheckSymbols(classes: ClassMap, tree: ParseTree, scopes: ParseTreeProperty[Scope]): Unit = {
    val symbolListener = new SymbolListener(classes, scopes)
    ParseTreeWalker.DEFAULT.walk(symbolListener, tree)
    println("Collected symbols")
  }

  private def runTypeCheck(classes: ClassMap, tree: ParseTree, scopes: ParseTreeProperty[Scope], calls: ParseTreeProperty[Klass]): Unit = {
    val typeCheckWalker = new TypeCheckWalker(classes, scopes, calls)
    typeCheckWalker.visit(tree)
    println("Type check finished")
  }

  private def codeGenWalk(classes: ClassMap, tree: ParseTree, scopes: ParseTreeProperty[Scope], calls: ParseTreeProperty[Klass]): Unit = {
    mkDir()
    val codeGenerator = new CodeGenerator(classes, scopes, calls)
    ParseTreeWalker.DEFAULT.walk(codeGenerator, tree)
    println("Generated code!")
  }

  private def mkDir(): Unit ={
    new File(Paths.get(Const.OUTPUT_DIR).toUri).mkdir()
  }

  //  private def prettyPrint(classes:ClassMap): Unit = {
  //    classes.values.foreach(klass => {
  //      print(klass.name)
  //      if(klass.superClass.isDefined){
  //        print(s" extends ${klass.superClass.get.name}")
  //      }
  //      println()
  //
  //      println("\tvariables")
  //      klass.fields.values.foreach(field => {
  //        println(s"\t\t${if(field.mutable) "mutable" else "immutable"} ${field.name}:${field.typee.name}")
  //      })
  //
  //      println("\tMethods")
  //      klass.methods.values.foreach(method => {
  //        println(s"\t\t${method.name}:${method.typee.name} -> [${method.parameters.values.map(p => s"${p.name}:${p.typee.name}").mkString(",")}]")
  //
  //        println("\t\t\tLocals")
  //        method.vars.values.foreach(local => {
  //          println(s"\t\t\t\t${if(local.mutable) "mutable" else "immutable"} ${local.name}:${local.typee.name}")
  //        })
  //      })
  //      println("------------------------------------------------------------------")
  //    })
  //  }

}
