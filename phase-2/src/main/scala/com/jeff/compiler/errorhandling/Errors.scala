package com.jeff.compiler.errorhandling


import com.compiler.generated.antlr.MiniJavaParser
import com.jeff.compiler.typechecking.helpers.{Scope, Symbole}
import org.antlr.v4.runtime.{RecognitionException, Recognizer}

import scala.collection.JavaConversions._

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

}
