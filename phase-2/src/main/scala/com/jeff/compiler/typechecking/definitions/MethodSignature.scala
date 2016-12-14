package com.jeff.compiler.typechecking.definitions


case class MethodSignature(returnType:Klass, name:String, parameters:List[Klass]) {


  def isIdentical(signature: MethodSignature):Boolean = {
    returnType.name == signature.returnType.name &&
    name.equals(signature.name) &&
    parameters.length == signature.parameters.length &&
    parameters.zip(signature.parameters).forall((x:(Klass, Klass)) => x._1.name == x._2.name)
  }

}
