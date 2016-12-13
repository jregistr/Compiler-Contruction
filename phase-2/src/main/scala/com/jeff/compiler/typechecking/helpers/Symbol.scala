package com.jeff.compiler.typechecking.helpers


class Symbol(val name:String, typee:Klass, isField:Boolean) {

  var paramId:Option[Int] = None
  var localId:Option[Int] = None
  var init:Boolean = false

  def initialized:Boolean = init

  def isParameter:Boolean = paramId.isDefined

  def isLocal:Boolean = localId.isDefined

}
