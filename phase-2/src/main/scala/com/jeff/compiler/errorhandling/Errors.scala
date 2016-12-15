package com.jeff.compiler.errorhandling


import com.compiler.generated.antlr.MiniJavaParser
import com.jeff.compiler.typechecking.definitions.{Klass, Scope, Symbole}
import org.antlr.v4.runtime.{RecognitionException, Recognizer}

import scala.collection.JavaConversions._

object Errors {

  def parseError(recognizer: Recognizer[_, _], offendingSymbol: scala.Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): ParseError = {
    val message = Option(e) match {
      case None =>
        s"Error while Parsing at ($line, $charPositionInLine): $msg"
      case Some(_) =>
        val rulesAsString = e.getCtx.toString(recognizer)
        val brackets = (s: Char) => s == '[' || s == ']'
        val rulesList = rulesAsString.split(' ').map((elem) => elem.filterNot(brackets)).toList
        val head = rulesList.head
        val ex: List[String] = e.getExpectedTokens.toList.map((s) => MiniJavaParser.VOCABULARY.getDisplayName(s)).toList

        s"Invalid $head. Expected ${ex.mkString(" ")}, but ${e.getOffendingToken.getText} was found"
    }
    ParseError(message)
  }

  def duplicateDeclaration(attemptedAddToScope: Scope, foundSymbol: Symbole, attemptedSymbol: Symbole): DuplicateDeclarationError = {
    val symFound = s"Duplicate symbol declaration found. A symbol with name = ${foundSymbol.name} and Type with class name = ${foundSymbol.typee}"
    val inScope = s"in scope with name = ${attemptedAddToScope.name}"
    DuplicateDeclarationError(s"$symFound\n$inScope")
  }

  def reAssignToImmutable(scope: Scope, symbol: Symbole): ReAssignToImmutableVariableError = {
    val attempted = s"Attempted to assign value to variable ${symbol.name}:${symbol.typee.name}"
    val rest = s"in scope ${scope.name} that has already been assigned."
    ReAssignToImmutableVariableError(s"$attempted $rest")
  }

  def variableNotDeclared(scope: Scope, name: String): VariableNotDeclaredError = {
    VariableNotDeclaredError(s"Variable with name = $name has not been declared in scope with name ${scope.name}")
  }

  def invalidOpOnSymbolType(symbole: Symbole): InvalidOptOnSymbolType = {
    InvalidOptOnSymbolType(s"Attempted to assign to symbol {$symbole} that is not a variable symbol.")
  }

  def invalidSymbolForScope(scope: Scope, symbole: Symbole): InvalidOptOnSymbolType = {
    InvalidOptOnSymbolType(s"Symbole with name ${symbole.name} and type ${symbole.typee.name} does not belong in scope ${scope.name}")
  }

  def cannotAssignClassSymbols(scope: Klass): InvalidOptOnSymbolType = {
    InvalidOptOnSymbolType(s"Cannot assign class variables in class scope directly. Class:${scope.name}")
  }

  def cyclicDependencyError(inheritanceLine: List[Klass]): CyclicDependencyError = {
    if (inheritanceLine.length < 2)
      throw new IllegalArgumentException("Inheritance line should be of at least length = 2")

    val stringBuilder = new StringBuilder()
    stringBuilder.append("Cyclic dependency found.\n")
    stringBuilder.append("Below is the inheritance line:\n")
    stringBuilder.append(inheritanceLine.map(_.name).mkString("\n"))
    CyclicDependencyError(stringBuilder.mkString)
  }

  def superAlreadyDefined(current: Klass, originalSuper: Klass, attempted: Klass): SuperClassAlreadyDefined = {
    SuperClassAlreadyDefined(s"Class:{${current.name} already has {${originalSuper.name} defined as super class. Attempted Class:${attempted.name}")
  }

  def duplicateClassDeclaration(name: String): DuplicateDeclarationError = {
    DuplicateDeclarationError(s"Duplicate class declaration found. Classname:$name")
  }

  def noClassDefFound(name:String): NoClassDefFoundError = {
     NoClassDefFoundError(s"No class with name = $name has been declared")
  }

  def noClassDefFound(names:List[String]): NoClassDefFoundError = {
    NoClassDefFoundError(s"No class with name = ${names.mkString(",")} has been declared")
  }

  def illegalState(msg:String):IllegalStateError = IllegalStateError(msg)

  def noScopeFound():IllegalStateError = IllegalStateError("No scope found")

  def unexpectedContext():IllegalStateError = IllegalStateError("Unexpected context found")

  def typeNotFound(typeName:String):TypeNotFoundError = {
    TypeNotFoundError(s"Type $typeName not found")
  }

  def duplicateDeclaration(scope: Scope, name:String):DuplicateDeclarationError = {
    DuplicateDeclarationError(s"Duplicate declaration in scope:${scope.name}. Var name:$name")
  }

}
