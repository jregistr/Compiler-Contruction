package com.jeff.compiler.util

import com.jeff.compiler.typechecking.definitions._

import scala.collection.mutable


object Aliases {
  type ParamMap = mutable.LinkedHashMap[String, Parameter]
  type SymbolMap = mutable.Map[String, Symbole]
  type VariableMap = mutable.Map[String, VariableSymbol]
  type LocalVarMap = mutable.Map[String, LocalVariable]
  type ClassMap = mutable.Map[String, Klass]
  type FieldMap = mutable.Map[String, Field]
  type MethodMap = mutable.Map[String, Method]
}
