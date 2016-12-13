package com.jeff.compiler.typechecking.helpers

import com.jeff.compiler.errorhandling.Errors

import scala.collection.mutable.{Map => SymbolMap}
import scala.util.Try

class Method(val name: String, val typee: Klass, private val scope: Scope) extends Scope with Symbole {

    private val symbols: SymbolMap[String, Symbole] = SymbolMap()

    private val initialisedSymbols: SymbolMap[String, Symbole] = SymbolMap()

  /**
    * Method to get the optional enclosing scope.
    *
    * @return The optional scope enclosing this scope.
    */
  override def enclosingScope: Option[Scope] = Some(scope)

  /**
    * Method to search for a symbol locally in a given scope.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  override def findSymbolLocally(name: String): Option[Symbole] = {
    symbols.get(name)
  }

  /**
    * Method to search for a symbol globally in relation to the scope.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  override def findSymbolDeeply(name: String): Option[Symbole] = {
    symbols.get(name) match {
      case s:Some[_] => s
      case None =>
        enclosingScope match {
          case Some(enclosing) => enclosing.findSymbolDeeply(name)
          case None => None
        }
    }
  }

  /**
    * Method to search for an initialised symbol. Checks the current and any parent scope available.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  override def findInitialisedSymbol(name: String): Option[Symbole] = {
    initialisedSymbols.get(name) match {
      case s:Some[_] => s
      case None =>
        enclosingScope match {
          case Some(enclosing) => enclosing.findInitialisedSymbol(name)
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
  override def addSymbol(symbol: Symbole): Try[Symbole] = ???

  /**
    * Method to initialise a symbol.
    *
    * @param symbol The symbol to initialise.
    * @return A try.
    */
  override def initialiseSymbol(symbol: Symbole): Try[Symbole] = {
    
  }

  /**
    * Method to check if a given symbol has been initialised.
    *
    * @param symbol The symbol to check.
    * @return a boolean.
    */
  override def isInitialised(symbol: Symbole): Boolean = {
    findInitialisedSymbol(symbol.name).isDefined
  }

//  private val symbols: SymbolMap[String, Symbole] = SymbolMap()
//  private val initialisedSymbols: SymbolMap[String, Symbole] = SymbolMap()
//
//  /**
//    * Method to get the optional enclosing scope.
//    *
//    * @return The optional scope enclosing this scope.
//    */
//  override def enclosingScope: Option[Scope] = {
//    Some(scope)
//  }
//
//  /**
//    * Method to search for a symbol locally in a given scope.
//    *
//    * @param name The name of the symbol.
//    * @return An option that may enclose the symbol.
//    */
//  override def findSymbolLocally(name: String): Option[Symbole] = {
//    symbols.get(name) match {
//      case s: Some[_] => s
//      case None => initialisedSymbols.get(name)
//    }
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
//      case s: Some[_] => s
//      case None =>
//        enclosingScope match {
//          case Some(enclosing) => enclosing.findSymbolDeeply(name)
//          case None => None
//        }
//    }
//  }
//
//  /**
//    * Method to add a symbol to a scope.
//    *
//    * @param symbol The symbol to add.
//    * @return A try signaling success or failure.
//    */
//  override def addSymbol(symbol: Symbole): Unit = {
//    findSymbolDeeply(symbol.name) match {
//      case None =>
//        symbols += symbol.name -> symbol
//      case Some(it) =>
//        throw Errors.duplicateDeclaration(this, it, symbol)
//    }
//  }
//
//  /**
//    * Method to initialise a symbol.
//    *
//    * @param symbol The symbol to initialise.
//    * @return A try.
//    */
//  override def initialiseSymbol(symbol: Symbole): Unit = {
//    //    findSymbolDeeply(symbol.name) match {
//    //      case None => throw Errors.variableNotDeclared(this, symbol.name)
//    //      case Some(it) =>
//    //
//    //    }
//  }
//
//  /**
//    * Method to check if a given symbol has been initialised.
//    *
//    * @param symbol The symbol to check.
//    * @return a boolean.
//    */
//  override def isInitialised(symbol: Symbole): Boolean = {
//    initialisedSymbols.get(symbol.name) match {
//      case Some(_) => true
//      case None =>
//        enclosingScope match {
//          case Some(enclosing) => enclosing.isInitialised(symbol)
//          case None => false
//        }
//    }
//  }

}
