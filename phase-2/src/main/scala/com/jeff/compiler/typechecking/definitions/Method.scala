package com.jeff.compiler.typechecking.definitions

import com.jeff.compiler.errorhandling.Errors
import com.jeff.compiler.util.Aliases.{LocalVarMap, ParamMap}
import org.antlr.v4.runtime.Token

import scala.collection.mutable.{Map => MutableMap}

class Method(val name: String, val typee: Klass, private val parentScope: Scope, val parameters:ParamMap, val token: Token) extends Scope with Symbole {

  private val vars: LocalVarMap = MutableMap()

  private val initialisedVars: LocalVarMap = MutableMap()

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
      case s:Some[_] => s
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
    findSymbolLocally(name) match  {
      case s:Some[_] => s
      case None => enclosingScope match {
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
    initialisedVars.get(name) match {
      case s: Some[_] => s
      case None =>
        parameters.get(name) match {
          case s:Some[_] => s
          case None =>
            enclosingScope match {
              case Some(enclosing) => enclosing.findInitialisedSymbol(name)
              case None => None
            }
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
      case x: LocalVariable =>
        findSymbolDeeply(x.name) match {
          case None => vars.put(x.name, x)
          case Some(found) =>
            throw Errors.duplicateDeclaration(this, found, symbol, symbol.token)
        }
      case _ => throw Errors.invalidSymbolForScope(this, symbol, symbol.token)
    }
  }

  /**
    * Method to initialise a symbol.
    *
    * @param symbol The symbol to initialise.
    * @return A try.
    */
  override def initialiseSymbol(symbol: Symbole): Unit = {
    findSymbolLocally(symbol.name) match {
      case Some(found:Symbole) =>
        found match {
          case x:LocalVariable =>
            x.mutable match {
              case true => initialisedVars.put(x.name, x)
              case false => initialisedVars.get(x.name).isDefined match {
                case true => throw Errors.reAssignToImmutable(this, x, symbol.token)
                case false => initialisedVars.put(x.name, x)
              }
            }
          case _=> throw Errors.invalidOpOnSymbolType(found, symbol.token)
        }
      case None =>
        enclosingScope match {
          case Some(enclosing) => enclosing.initialiseSymbol(symbol)
          case None => throw Errors.variableNotDeclared(this, symbol.name, symbol.token)
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
    symbol match {
      case v: VariableSymbol =>
        findSymbolDeeply(symbol.name) match {
          case Some(_) => findInitialisedSymbol(symbol.name).isDefined
          case None => throw Errors.variableNotDeclared(this, symbol.name, symbol.token)
        }
      case _ => throw Errors.invalidOpOnSymbolType(symbol, symbol.token)
    }
  }

}



object Method {
  def isProperOverride(first:Method, second:Method):Boolean = {
    first.typee.name == second.typee.name &&
    first.name.equals(second.name) &&
    first.parameters.values.map(_.typee) == second.parameters.values.map(_.typee)
  }
}

