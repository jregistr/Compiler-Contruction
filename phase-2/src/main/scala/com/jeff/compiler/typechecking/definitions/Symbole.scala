package com.jeff.compiler.typechecking.definitions

/**
  * Basic symbol
  */
trait Symbole {
  val name:String
  val typee:Klass
}

/**
  * Symbols with ids
  */
trait IdentifiableSymbol extends Symbole {
  private var _id:Int = _

  def id_=(id:Int) = _id = id
  def id = _id
}

/**
  * Trait for symbols that are variables.
  */
trait VariableSymbol extends IdentifiableSymbol {
  val mutable:Boolean
}

/**
  * Method parameter.
  * @param name The name of the parameter.
  * @param typee the type of the parameter.
  */
case class Parameter(name:String, typee:Klass) extends IdentifiableSymbol

/**
  * Class field.
  * @param name The name of the field.
  * @param typee The type of the field.
  */
case class Field(name:String, typee:Klass, mutable: Boolean) extends VariableSymbol

/**
  * Local method variable.
  * @param name The name of the local variable.
  * @param typee The type of the local variable.
  */
case class LocalVariable(name:String, typee:Klass, mutable: Boolean) extends VariableSymbol
