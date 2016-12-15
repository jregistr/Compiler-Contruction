package com.jeff.compiler.typechecking.definitions

import com.jeff.compiler.util.Aliases.ParamMap


case class MethodSignature(returnType:Klass, name:String, parameters:ParamMap) {

  def isIdentical(signature: MethodSignature):Boolean = {
    returnType.name == signature.returnType.name &&
    name.equals(signature.name) &&
    signature.parameters.values.map(_.typee) == parameters.values.map(_.typee)
  }

}
