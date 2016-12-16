package com.jeff.compiler.typechecking.definitions

import com.jeff.compiler.util.Aliases.{LocalVarMap, ParamMap, VariableMap}

import scala.collection.mutable.{Map => MutableMap}

class Method(val name: String, val typee: Klass, private val parentScope: Scope, val parameters: ParamMap) extends Scope with Symbole {

  private val vars: LocalVarMap = MutableMap()

  private val initialisedVars: VariableMap = MutableMap()

  /**
    * Method to get the optional enclosing scope.
    *
    * @return The optional scope enclosing this scope.
    */
  override def enclosingScope: Option[Scope] = Some(parentScope)

  /**
    * Method to search for a symbol locally in a given scope.
    *
    * @param name The name of the symbol.
    * @return An option that may enclose the symbol.
    */
  override def findSymbolLocally(name: String): Option[Symbole] = {
    parameters.get(name) match {
      case s: Some[_] => s
      case None => vars.get(name)
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
  override def addSymbol(symbol: Symbole): Unit = vars.put(symbol.name, symbol.asInstanceOf[LocalVariable])

  /**
    * Method to initialise a symbol.
    *
    * @param symbol The symbol to initialise.
    * @return A try.
    */
  override def initialiseSymbol(symbol: Symbole): Unit = initialisedVars.put(symbol.name, symbol.asInstanceOf[VariableSymbol])

  override def isInitialised(name: String): Boolean = {
    initialisedVars.get(name) match {
      case None => parameters.get(name) match {
        case Some(_) => true
        case None => enclosingScope match {
          case None => false
          case Some(en) => en.isInitialised(name)
        }
      }
      case Some(_) => true
    }
  }

  /**
    * Method to get the initialised symbols for a scope.
    *
    * @return A list containing all initialised variables.
    */
  override def initialisedSymbols(): List[Symbole] = initialisedVars.values.toList ++ (enclosingScope match {
    case Some(scope: Scope) => scope.initialisedSymbols()
    case None => List.empty
  })

}


