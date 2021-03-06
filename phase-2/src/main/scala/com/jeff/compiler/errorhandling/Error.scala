package com.jeff.compiler.errorhandling


abstract class CompilerError(msg:String) extends RuntimeException(msg)

case class ParseError(msg:String) extends CompilerError(msg)

case class DuplicateDeclarationError(msg:String) extends CompilerError(msg)

case class ReAssignToImmutableVariableError(msg:String) extends CompilerError(msg)

case class VariableNotDeclaredError(msg:String) extends CompilerError(msg)

case class InvalidOptOnSymbolType(msg:String) extends CompilerError(msg)

case class CyclicDependencyError(msg:String) extends CompilerError(msg)

case class SuperClassAlreadyDefined(msg:String) extends CompilerError(msg)

case class NoClassDefFoundError(msg:String) extends CompilerError(msg)

case class TypeNotFoundError(msg:String) extends CompilerError(msg)

case class IllegalStateError(msg:String) extends CompilerError(msg)

case class TypeMismatchError(msg:String) extends CompilerError(msg)
