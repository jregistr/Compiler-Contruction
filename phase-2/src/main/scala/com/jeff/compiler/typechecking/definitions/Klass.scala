package com.jeff.compiler.typechecking.definitions

import com.jeff.compiler.errorhandling.Errors
import com.jeff.compiler.util.Aliases.{FieldMap, MethodMap}
import org.antlr.v4.runtime.{ParserRuleContext, Token}

import scala.collection.mutable.{ListBuffer, Map => MutableMap}

/**
  * Class to represent a class scope.
  *
  * @param name       The name of the class.
  * @param _superClass An optional Klass that is the parent of this class
  */
class Klass(val name: String, val token: Token, private var _superClass: Option[Klass]) extends Scope {

  val fields: FieldMap = MutableMap()
  private val initialisedFields: FieldMap = MutableMap()
  val methods: MethodMap = MutableMap()

  /**
    * Method to get the optional enclosing scope.
    *
    * @return The optional scope enclosing this scope.
    */
  override def enclosingScope: Option[Klass] = _superClass

  /**
    * Method to search for a symbol locally in a given scope.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  override def findSymbolLocally(name: String): Option[Symbole] = {
    fields.get(name) match {
      case s: Some[_] => s
      case None => methods.get(name)
    }
  }

  /**
    * Method to get the initialised symbols for a scope.
    *
    * @return A list containing all initialised variables.
    */
  override def initialisedSymbols(): List[Symbole] = initialisedFields.values.toList ++ (enclosingScope match {
    case Some(scope: Scope) => scope.initialisedSymbols()
    case None => List.empty
  })

  override def isInitialised(name: String): Boolean = {
    initialisedFields.get(name) match {
      case Some(_) => true
      case None => enclosingScope match {
        case None => false
        case Some(en) => en.isInitialised(name)
      }
    }
  }

  /**
    * Method to search for a symbol globally in relation to the scope.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  override def findSymbolDeeply(name: String): Option[Symbole] = {
    findSymbolLocally(name) match {
      case s: Some[_] => s
      case None => enclosingScope match {
        case Some(enclosing) => enclosing.findSymbolDeeply(name)
        case None => None
      }
    }
  }

  /**
    * Method to add a symbol to a scope.
    *
    * @param symbol The symbol to add.
    * @return A try signaling success or failure.
    */
  override def addSymbol(symbol: Symbole): Unit = {
    symbol match {
      case field:Field => fields.put(field.name, field)
      case _=> methods.put(symbol.name, symbol.asInstanceOf[Method])
    }
  }

  /**
    * Method to initialise a symbol.
    *
    * @param symbol The symbol to initialise.
    * @return A try.
    */
  override def initialiseSymbol(symbol: Symbole): Unit = initialisedFields.put(symbol.name, symbol.asInstanceOf[Field])

  /**
    * Method to get inheritance line.
    *
    * @return A list of the classes making the hierarchy.
    */
  def getInheritanceLine: List[Klass] = {
    val builder = ListBuffer[Klass]()
    var cur = superClass
    while (cur.isDefined) {
      builder += cur.get
      cur = cur.get.superClass
    }
    builder.toList
  }

  /**
    * Method to check for inheritance cycles.
    */
  def checkForCycles(ctx: ParserRuleContext): Unit = {
    val builder = ListBuffer[Klass]()
    var currentSuper: Option[Klass] = superClass
    while (currentSuper.isDefined) {
      builder += currentSuper.get
      if (currentSuper.get.name == this.name) {
        throw Errors.cyclicDependencyError(builder.toList, ctx.start)
      }
      currentSuper = currentSuper.get.superClass
    }
  }

  def superClass_=(klass: Klass): Unit = {
    _superClass match {
      case None => _superClass = Some(klass)
      case Some(found) => throw new RuntimeException(s"Super has been defined for class:$name already.")
    }
  }

  def superClass = _superClass

}
