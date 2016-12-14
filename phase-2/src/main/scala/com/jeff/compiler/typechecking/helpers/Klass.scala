package com.jeff.compiler.typechecking.helpers

import com.jeff.compiler.errorhandling.Errors

//rememeber to write get parameters method in method

import scala.collection.mutable.{ListBuffer, Map => MutableMap}

/**
  * Class to represent a class scope.
  * @param name The name of the class.
  * @param superClass An optional Klass that is the parent of this class
  */
class Klass(val name:String, val superClass:Option[Klass]) extends Scope {

  private val fields:MutableMap[String, Field] = MutableMap()
  private val methods:MutableMap[String, Method] = MutableMap()

  /**
    * Method to get the optional enclosing scope.
    *
    * @return The optional scope enclosing this scope.
    */
  override def enclosingScope: Option[Scope] = superClass

  /**
    * Method to search for a symbol locally in a given scope.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  override def findSymbolLocally(name: String): Option[Symbole] = ???

  /**
    * Method to initialise a symbol.
    *
    * @param symbol The symbol to initialise.
    * @return A try.
    */
  override def initialiseSymbol(symbol: Symbole): Unit = throw Errors.cannotAssignClassSymbols(this)

  /**
    * Method to check if a given symbol has been initialised.
    *
    * @param symbol The symbol to check.
    * @return a boolean.
    */
  override def isInitialised(symbol: Symbole): Boolean = ???

  def findFieldLocally(name:String):Option[Field] =  ???

  def findInitialisedField(name:String):Option[Field] = ???

  def findFieldDeeply(name:String):Option[Field] = ???

  def findMethodsWithName(name:String):List[Method] = ???

  def findMethodWithNameAndParams(name:String, parameters:List[Parameter]) = ???



  //  /**
  //    * Method to check if a given symbol has been initialised.
  //    *
  //    * @param symbol The symbol to check.
  //    * @return a boolean.
  //    */
  //  override def isInitialised(symbol: Symbole): Boolean = {
  //    symbol match {
  //      case x:VariableSymbol =>
  //        findSymbolDeeply(symbol.name) match {
  //          case None => throw Errors.variableNotDeclared(this, symbol.name)
  //          case Some(_) => initialisedSymbols.get(symbol.name).isDefined
  //        }
  //      case _=> throw Errors.invalidOpOnSymbolType(symbol)
  //    }
  //  }

//  private val symbols: MutableMap[String, Symbole] = MutableMap()
//
//  private val initialisedSymbols: MutableMap[String, Symbole] = MutableMap()
//
//  /**
//    * Method to get the optional enclosing scope.
//    *
//    * @return The optional scope enclosing this scope.
//    */
//  override def enclosingScope: Option[Scope] = superClass
//
//  /**
//    * Method to search for a symbol locally in a given scope.
//    *
//    * @param name The name of the symbol.
//    * @return An option that may enclose the symbol.
//    */
//  override def findSymbolLocally(name: String): Option[Symbole] = {
//    symbols.get(name)
//  }
//
//  /**
//    * Method to search for a symbol globally in relation to the scope.
//    *
//    * @param name The name of the symbol.
//    * @return An option that may enclose the symbol.
//    */
//  override def findSymbolDeeply(name: String): Option[Symbole] = {
//    findSymbolLocally(name) match {
//      case s:Some[_] => s
//      case None =>
//        superClass match {
//          case Some(x) => x.findSymbolDeeply(name)
//          case None => None
//        }
//    }
//  }
//
//  /**
//    * Method to search for an initialised symbol. Checks the current and any parent scope available.
//    *
//    * @param name The name of the symbol.
//    * @return An option that may enclose the symbol.
//    */
//  override def findInitialisedSymbol(name: String): Option[Symbole] = initialisedSymbols.get(name)
//
//  /**
//    * Method to add a symbol to a scope.
//    *
//    * @param symbol The symbol to add.
//    * @return A try signaling success or failure.
//    */
//  override def addSymbol(symbol: Symbole): Unit = {
//
//    symbol match {
//      case method:Method => addMethod(method)
//      case field:Field => addField(field)
//      case _=> throw Errors.invalidOpOnSymbolType(symbol)
//    }
//
////    findSymbolDeeply(symbol.name) match {
////      case None => symbols.put(symbol.name, symbol)
////      case Some(found) => throw Errors.duplicateDeclaration(this, found, symbol)
////    }
//  }
//
//  /**
//    * Method to initialise a symbol.
//    *
//    * @param symbol The symbol to initialise.
//    * @return A try.
//    */
//  override def initialiseSymbol(symbol: Symbole): Unit = throw Errors.cannotAssignClassSymbols(this)
//

//
//  /**
//    * Method to get inheritance line.
//    * @return A list of the classes making the hierarchy.
//    */
//  def getInheritanceLine:List[Klass] = {
//    val builder = ListBuffer[Klass]()
//    var cur = superClass
//    while (cur.isDefined) {
//      builder += cur.get
//      cur = cur.get.superClass
//    }
//    builder.toList
//  }
//
//  /**
//    * Method to check for inheritance cycles.
//    */
//  def checkForCycles():Unit = {
//    val builder = ListBuffer[Klass]()
//    var currentSuper: Option[Klass] = superClass
//    while (currentSuper.isDefined) {
//      builder += currentSuper.get
//      if(currentSuper.get.name == this.name) {
//        throw Errors.cyclicDependencyError(builder.toList)
//      }
//      currentSuper = currentSuper.get.superClass
//    }
//  }
//
//  def getOwnMethods:List[Method] = {
//    symbols.values.filter((v) => v match {
//      case x:Method => true
//      case _=> false
//    }).map(_=>asInstanceOf[Method])
//      .toList
//  }
//
//  def getAllMethods:List[Method] = {
//    val builder = ListBuffer[Method]()
//    var cur = Option(this)
//    while (cur.isDefined) {
//      builder ++= cur.get.getOwnMethods
//      cur = cur.get.superClass
//    }
//    builder.toList
//  }
//
//  def getMethod(name:String):Method = ???
//
//  def getVariable(name:String):Method = ???
//
//  def addMethod(method:Method):Unit = ???
//
//  def addField(field: Field):Unit = ???
//
//

}
