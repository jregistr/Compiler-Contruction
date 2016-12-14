package com.jeff.compiler.errorhandling


import com.compiler.generated.antlr.MiniJavaParser
import com.jeff.compiler.typechecking.helpers.{Klass, Scope, Symbole}
import org.antlr.v4.runtime.{RecognitionException, Recognizer}

import scala.collection.JavaConversions._
import scala.collection.mutable

object Errors {

  def parseError(recognizer: Recognizer[_, _], offendingSymbol: scala.Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException):ParseError = {
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

  def duplicateDeclaration(attemptedAddToScope:Scope, foundSymbol:Symbole, attemptedSymbol:Symbole):DuplicateDeclarationError = {
    val symFound = s"Duplicate symbol declaration found. A symbol with name = ${foundSymbol.name} and Type with class name = ${foundSymbol.typee}"
    val inScope = s"in scope with name = ${attemptedAddToScope.name}"
    DuplicateDeclarationError(s"$symFound\n$inScope")
  }

  def reAssignToImmutable(scope:Scope, symbol: Symbole):ReAssignToImmutableVariableError = {
    val attempted = s"Attempted to assign value to variable ${symbol.name}:${symbol.typee}"
    val rest = s"in scope ${scope.name} that has already been assigned."
    ReAssignToImmutableVariableError(s"$attempted $rest")
  }

  def variableNotDeclared(scope: Scope, name:String):VariableNotDeclaredError = {
    VariableNotDeclaredError(s"Variable with name = $name has not been declared in scope with name ${scope.name}")
  }

  def invalidOpOnSymbolType(symbole: Symbole):InvalidOptOnSymbolType = {
    InvalidOptOnSymbolType(s"Attempted to assign to symbol {$symbole} that is not a variable symbol.")
  }

  def invalidSymbolForScope(scope: Scope, symbole: Symbole):InvalidOptOnSymbolType = {
    InvalidOptOnSymbolType(s"Symbole with name $symbole and type ${symbole.typee} does not belong in scope ${scope.name}")
  }

  def cannotAssignClassSymbols(scope: Klass):InvalidOptOnSymbolType = {
    InvalidOptOnSymbolType(s"Cannot assign class variables in class scope directly. Class:${scope.name}")
  }

  def cyclicDependencyError(inheritanceLine:List[Klass]):CyclicDependencyError = {
    if(inheritanceLine.length < 2)
      throw new IllegalArgumentException("Inheritance line should be of at least length = 2")

    val stringBuilder = new StringBuilder()
    stringBuilder.append("Cyclic dependency found.\n")
    stringBuilder.append("Below is the inheritance line:")
    stringBuilder.append(inheritanceLine.mkString("\n"))
    CyclicDependencyError(stringBuilder.mkString)
  }

}
