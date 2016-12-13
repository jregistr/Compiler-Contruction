package com.jeff.compiler.typechecking.helpers

import com.jeff.compiler.errorhandling.Errors

import scala.collection.mutable.{Map => SymbolMap}

class Method(val name: String, val typee: Klass, private val scope: Scope) extends Scope with Symbole {

  private val symbols: SymbolMap[String, VariableSymbol] = SymbolMap()

  private val initialisedSymbols: SymbolMap[String, VariableSymbol] = SymbolMap()

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
      case s: Some[_] => s
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
      case s: Some[_] => s
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
  override def addSymbol(symbol: Symbole): Unit = {
    symbol match {
      case x: VariableSymbol =>
        findSymbolDeeply(x.name) match {
          case None => symbols.put(x.name, x)
          case Some(found) =>
            throw Errors.duplicateDeclaration(this, found, symbol)
        }
      case _ => throw Errors.invalidSymbolForScope(this, symbol)
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
      case None => throw Errors.variableNotDeclared(this, symbol.name)
      case Some(found: Symbole) =>
        found match {
          case x: VariableSymbol =>
            x.mutable match {
              case true => initialisedSymbols.put(found.name, x)
              case false =>
                isInitialised(x) match {
                  case true => throw Errors.reAssignToImmutable(scope, x)
                  case false => initialisedSymbols.put(found.name, x)
                }
            }
          case _ => throw Errors.invalidOpOnSymbolType(found)
        }
    }
  }

  /**
    * Method to check if a given symbol has been initialised.
    * !!Should make sure the variable exists.
    *
    * @param symbol The symbol to check.
    * @return a boolean.
    */
  override def isInitialised(symbol: Symbole): Boolean = {
    findSymbolDeeply(symbol.name) match {
      case Some(_) => findInitialisedSymbol(symbol.name).isDefined
      case None => throw Errors.variableNotDeclared(this, symbol.name)
    }
  }

}
