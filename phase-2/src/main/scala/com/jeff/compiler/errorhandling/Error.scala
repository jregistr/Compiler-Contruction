package com.jeff.compiler.errorhandling


abstract class CompilerError(msg:String) extends RuntimeException(msg)

case class ParseError(msg:String) extends CompilerError(msg)

case class DuplicateDeclarationError(msg:String) extends CompilerError(msg)

case class ReAssignToImmutableVariableError(msg:String) extends CompilerError(msg)

case class VariableNotDeclaredError(msg:String) extends CompilerError(msg)

case class InvalidOptOnSymbolType(msg:String) extends CompilerError(msg)

case class CyclicDependencyError(msg:String) extends CompilerError(msg)

