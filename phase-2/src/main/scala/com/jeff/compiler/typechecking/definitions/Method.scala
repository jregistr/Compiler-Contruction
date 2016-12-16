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

  /*  /**
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
    }*/


  /**
    * Method to add a symbol to a scope.
    *
    * @param symbol The symbol to add.
    * @return A try signaling success or failure.
    */
  override def addSymbol(symbol: Symbole): Unit = vars.put(symbol.name, symbol.asInstanceOf[LocalVariable])

  //  {
  //    symbol match {
  //      case x:LocalVariable =>
  //      case _=> None
  //    }
  //  }

  /**
    * Method to initialise a symbol.
    *
    * @param symbol The symbol to initialise.
    * @return A try.
    */
  override def initialiseSymbol(symbol: Symbole): Unit = initialisedVars.put(symbol.name, symbol.asInstanceOf[VariableSymbol])

  //{

  //    symbol match {
  //      case variable: VariableSymbol =>
  //        if (variable.mutable) {
  //          val findLocalRes = findSymbolLocally(variable.name).asInstanceOf[Option[VariableSymbol]]
  //
  //          val findAbove: Option[VariableSymbol] = enclosingScope match {
  //            case None => None
  //            case Some(outerScope) =>
  //              outerScope.findSymbolLocally(variable.name) match {
  //                case Some(found) =>
  //                  found match {
  //                    case asVar: VariableSymbol => Some(asVar)
  //                    case _ => throw Errors.invalidOpOnSymbolType(symbol, symbol.token)
  //                  }
  //                case None => None
  //              }
  //          }
  //
  //          if (findLocalRes.isEmpty && findAbove.isEmpty) {
  //            throw Errors.variableNotDeclared(this, symbol.name, symbol.token)
  //          } else {
  //            initialisedVars.put(variable.name, variable)
  //            if (findAbove.isDefined) {
  //              enclosingScope.get.initialiseSymbol(variable)
  //            } else {
  //              Some(variable)
  //            }
  //          }
  //        } else {
  //          throw Errors.reAssignToImmutable(this, symbol, symbol.token)
  //        }
  //      case _ => throw Errors.invalidOpOnSymbolType(symbol, symbol.token)
  //    }
  // }

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


