package com.jeff.compiler.typechecking.helpers

import com.jeff.compiler.errorhandling.Errors

import scala.collection.mutable.{Map => MutableMap}


class Klass(val name:String, superClass:Option[Klass]) extends Scope{

  private val symbols: MutableMap[String, Symbole] = MutableMap()

  private val initialisedSymbols: MutableMap[String, Symbole] = MutableMap()

  /**
    * Method to get the optional enclosing scope.
    *
    * @return The optional scope enclosing this scope.
    */
  override def enclosingScope: Option[Scope] = None

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
  override def findSymbolDeeply(name: String): Option[Symbole] = findSymbolLocally(name)

  /**
    * Method to search for an initialised symbol. Checks the current and any parent scope available.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  override def findInitialisedSymbol(name: String): Option[Symbole] = initialisedSymbols.get(name)

  /**
    * Method to add a symbol to a scope.
    *
    * @param symbol The symbol to add.
    * @return A try signaling success or failure.
    */
  override def addSymbol(symbol: Symbole): Unit = {
    findSymbolDeeply(symbol.name) match {
      case None => symbols.put(symbol.name, symbol)
      case Some(found) => throw Errors.duplicateDeclaration(this, found, symbol)
    }
  }

  /**
    * Method to initialise a symbol.
    *
    * @param symbol The symbol to initialise.
    * @return A try.
    */
  override def initialiseSymbol(symbol: Symbole): Unit = {
    findSymbolDeeply(symbol.name) match {
        case Some(_) =>
          symbol match {
            case x:VariableSymbol =>
              x.mutable match {
                case false =>
                  isInitialised(x) match {
                    case false =>initialisedSymbols.put(x.name, x)
                    case true => throw Errors.reAssignToImmutable(this, x)
                  }
                case true => initialisedSymbols.put(x.name, x)
              }
            case _=> throw Errors.invalidOpOnSymbolType(symbol)
          }
        case None => throw Errors.variableNotDeclared(this, symbol.name)
    }
  }

  /**
    * Method to check if a given symbol has been initialised.
    *
    * @param symbol The symbol to check.
    * @return a boolean.
    */
  override def isInitialised(symbol: Symbole): Boolean = {
    symbol match {
      case x:VariableSymbol =>
        findSymbolDeeply(symbol.name) match {
          case None => throw Errors.variableNotDeclared(this, symbol.name)
          case Some(_) => initialisedSymbols.get(symbol.name).isDefined
        }
      case _=> throw Errors.invalidOpOnSymbolType(symbol)
    }
  }
}
