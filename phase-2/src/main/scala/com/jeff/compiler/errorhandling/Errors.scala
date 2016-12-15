package com.jeff.compiler.errorhandling


import com.compiler.generated.antlr.MiniJavaParser
import com.jeff.compiler.typechecking.definitions.{Klass, Scope, Symbole}
import org.antlr.v4.runtime.{RecognitionException, Recognizer, Token}

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

  def duplicateDeclaration(attemptedAddToScope: Scope, foundSymbol: Symbole, attemptedSymbol: Symbole, token:Token): DuplicateDeclarationError = {
    val symFound = s"Duplicate symbol declaration found. A symbol with name = ${foundSymbol.name} and Type with class name = ${foundSymbol.typee}"
    val inScope = s"in scope with name = ${attemptedAddToScope.name}"
    DuplicateDeclarationError(s"$symFound\n$inScope at line ${token.getLine}")
  }

  def reAssignToImmutable(scope: Scope, symbol: Symbole, token: Token): ReAssignToImmutableVariableError = {
    val attempted = s"Attempted to assign value to variable ${symbol.name}:${symbol.typee.name}"
    val rest = s"in scope ${scope.name} that has already been assigned."
    ReAssignToImmutableVariableError(s"$attempted $rest at line ${token.getLine}")
  }

  def variableNotDeclared(scope: Scope, name: String, token: Token): VariableNotDeclaredError = {
    VariableNotDeclaredError(s"Variable with name = $name has not been declared in scope with name ${scope.name} at line ${token.getLine}")
  }

  def invalidOpOnSymbolType(symbole: Symbole, token: Token): InvalidOptOnSymbolType = {
    InvalidOptOnSymbolType(s"Attempted to assign to symbol {$symbole} that is not a variable symbol. at line ${token.getLine}")
  }

  def invalidSymbolForScope(scope: Scope, symbole: Symbole, token: Token): InvalidOptOnSymbolType = {
    InvalidOptOnSymbolType(s"Symbole with name ${symbole.name} and type ${symbole.typee.name} does not belong in scope ${scope.name} at line ${token.getLine}")
  }

  def cannotAssignClassSymbols(scope: Klass, token: Token): InvalidOptOnSymbolType = {
    InvalidOptOnSymbolType(s"Cannot assign class variables in class scope directly. Class:${scope.name} at line ${token.getLine}")
  }

  def cyclicDependencyError(inheritanceLine: List[Klass], token: Token): CyclicDependencyError = {
    if (inheritanceLine.length < 2)
      throw new IllegalArgumentException("Inheritance line should be of at least length = 2 at line ${token.getLine}")

    val stringBuilder = new StringBuilder()
    stringBuilder.append("Cyclic dependency found.\n")
    stringBuilder.append("Below is the inheritance line:\n")
    stringBuilder.append(inheritanceLine.map(_.name).mkString("\n"))
    stringBuilder.append("at line ${token.getLine}")
    CyclicDependencyError(stringBuilder.mkString)
  }

  def superAlreadyDefined(current: Klass, originalSuper: Klass, attempted: Klass, token: Token): SuperClassAlreadyDefined = {
    SuperClassAlreadyDefined(s"Class:{${current.name} already has {${originalSuper.name} defined as super class. Attempted Class:${attempted.name} at line ${token.getLine}")
  }

  def duplicateClassDeclaration(name: String, token: Token): DuplicateDeclarationError = {
    DuplicateDeclarationError(s"Duplicate class declaration found. Classname:$name at line ${token.getLine}")
  }

  def noClassDefFound(name:String, token: Token): NoClassDefFoundError = {
     NoClassDefFoundError(s"No class with name = $name has been declared at line ${token.getLine}")
  }

  def noClassDefFound(names:List[String], token: Token): NoClassDefFoundError = {
    NoClassDefFoundError(s"No class with name = ${names.mkString(",")} has been declared at line ${token.getLine}")
  }

  def illegalState(msg:String, token: Token):IllegalStateError = IllegalStateError(msg + " at line ${token.getLine}")

  def noScopeFound(token: Token):IllegalStateError = IllegalStateError("No scope found")

  def noScopeFoundFor(name:String, token: Token):IllegalStateError = IllegalStateError(s"No scope found for:$name  at line ${token.getLine}")

  def unexpectedContext(token: Token):IllegalStateError = IllegalStateError("Unexpected context found  at line ${token.getLine}")

  def typeNotFound(typeName:String, token: Token):TypeNotFoundError = {
    TypeNotFoundError(s"Type $typeName not found  at line ${token.getLine}")
  }

  def duplicateDeclaration(scope: Scope, name:String, token: Token):DuplicateDeclarationError = {
    DuplicateDeclarationError(s"Duplicate declaration in scope:${scope.name}. Var name:$name at line ${token.getLine}")
  }

  def typeMismatch(msg:String, token: Token):TypeMismatchError = TypeMismatchError(s"$msg at line ${token.getLine}")

  def typeMismatch(expected:String, found:String, token: Token):TypeMismatchError = TypeMismatchError(s"Type mismatch. Expected type:{$expected}, but found:{$found}.  At line ${token.getLine}")

  def unExpectedScope(token: Token):IllegalStateError = IllegalStateError("Unexpected scope encountered. At line ${token.getLine}")

  def unExpectedScope(scope:Scope, token: Token):IllegalStateError = IllegalStateError(s"Unexpected scope encountered. Scope Name:${scope.name}.  At line ${token.getLine}")

}
