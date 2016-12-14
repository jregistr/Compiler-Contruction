package com.jeff.compiler.typechecking.helpers

import com.jeff.compiler.errorhandling.Errors

import scala.collection.mutable.ListBuffer

import scala.collection.mutable.{Map => MutableMap}

/**
  * Class to represent a class scope.
  *
  * @param name       The name of the class.
  * @param superClass An optional Klass that is the parent of this class
  */
class Klass(val name: String, private var superClass: Option[Klass]) extends Scope {

  private val fields: MutableMap[String, Field] = MutableMap()
  private val initialisedFields: MutableMap[String, Field] = MutableMap()
  private val methods: MutableMap[String, Method] = MutableMap()

  /**
    * Method to get the optional enclosing scope.
    *
    * @return The optional scope enclosing this scope.
    */
  override def enclosingScope: Option[Klass] = superClass

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
    * Method to initialise a symbol.
    *
    * @param symbol The symbol to initialise.
    * @return A try.
    */
  override def initialiseSymbol(symbol: Symbole): Unit = {
    symbol match {
      case field: Field =>
        fields.get(name) match {
          case Some(_) =>
            field.mutable match {
              case true => initialisedFields.put(field.name, field)
              case false => isInitialised(symbol) match {
                  case false => initialisedFields.put(field.name, field)
                  case true => throw Errors.reAssignToImmutable(this, symbol)
              }
            }
          case None => enclosingScope match {
            case Some(enclosing) => enclosing.initialiseSymbol(symbol)
            case None => throw Errors.variableNotDeclared(this, symbol.name)
          }
        }
      case _ => throw Errors.invalidOpOnSymbolType(symbol)
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
      case field:Field =>
        findFieldLocally(field.name) match {
          case Some(_) =>initialisedFields.get(name).isDefined
          case None => enclosingScope match {
            case Some(enclosing) => enclosing.isInitialised(symbol)
            case None => throw Errors.variableNotDeclared(this, field.name)
          }
        }
      case _=> throw Errors.invalidOpOnSymbolType(symbol)
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
    initialisedFields.get(name) match {
      case s:Some[_] => s
      case None => enclosingScope match {
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
      case field:Field => addField(field)
      case method:Method => addMethod(method)
      case _=> throw Errors.invalidSymbolForScope(this, symbol)
    }
  }

  /**
    * Method to add a field. Checks to see if a field with such name does not exist locally
    * or up the tree.
    * @param field The field to add to this class.
    */
  def addField(field: Field): Unit = {
    findSymbolDeeply(field.name) match {
      case None=> fields.put(field.name, field)
      case Some(found) => throw Errors.duplicateDeclaration(this, found, field)
    }
  }

  /**
    * Method to add a method to this class.
    * @param method The method to add.
    */
  def addMethod(method: Method): Unit = {
    findFieldDeeply(method.name) match {
      case None =>
        findMethod(method.name) match {
          case None => methods.put(method.name, method)
          case Some(pair)=>checkForProperOverride(pair._2, method) match {
            case true => methods.put(method.name, method)
            case false => throw Errors.duplicateDeclaration(this, pair._2, method)
          }
        }
      case Some(found) => throw Errors.duplicateDeclaration(this, found, method)
    }
  }

  def findFieldLocally(name: String): Option[Field] = fields.get(name)

  def findFieldDeeply(name: String): Option[Field] = {
    findFieldLocally(name) match {
      case s:Some[_] => s
      case None => enclosingScope match {
        case Some(enclosing) => enclosing.findFieldDeeply(name)
        case None => None
      }
    }
  }

  def findMethod(name: String): Option[(Klass, Method)] = {
    methods.get(name) match {
      case Some(method) => Some(this, method)
      case None => enclosingScope match {
        case Some(enclosing) => enclosing.findMethod(name)
        case None => None
      }
    }
  }

  def checkForProperOverride(belowMethod: Method, currentMethod: Method):Boolean = {
    belowMethod.signature.isIdentical(currentMethod.signature)
  }

    /**
      * Method to get inheritance line.
      * @return A list of the classes making the hierarchy.
      */
    def getInheritanceLine:List[Klass] = {
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
    def checkForCycles():Unit = {
      val builder = ListBuffer[Klass]()
      var currentSuper: Option[Klass] = superClass
      while (currentSuper.isDefined) {
        builder += currentSuper.get
        if(currentSuper.get.name == this.name) {
          throw Errors.cyclicDependencyError(builder.toList)
        }
        currentSuper = currentSuper.get.superClass
      }
    }

    def setSuperClass(klass: Klass):Unit = {
      superClass match {
        case None => superClass = Some(klass)
        case Some(found) => throw Errors.superAlreadyDefined(this, found, klass)
      }
    }

}
