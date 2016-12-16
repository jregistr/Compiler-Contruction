package com.jeff.compiler.codegeneration

import com.jeff.compiler.typechecking.definitions.{Klass, Method}
import com.jeff.compiler.util.Aliases.AsmMethod
import com.jeff.compiler.util.Const
import com.jeff.compiler.util.Const._
import org.objectweb.asm.Type
import org.objectweb.asm.commons.{Method => AMethod}

object AsmConverter {

  val OBJECT: String = "java/lang/Object"

  def methodToAsmMethod(method: Method): AsmMethod = {
    val mSig = s"${method.typee.name} ${method.name} ${method.parameters.map(_._2.typee.name).toList.mkString("(", ",", ")")}"
    AMethod.getMethod(mSig, true)
  }

  def classToAsmType(klass: Klass): Type = {
    klass.name match {
      case INT => Type.INT_TYPE
      case INTARR => Type.getType(classOf[Array[Int]])
      case BOOLEAN => Type.BOOLEAN_TYPE
      case _ => Type.getType(s"L${klass.name};")
    }
  }

  def initMethod = AMethod.getMethod("void <init> ()")

  def classFileName(klassName: String): String = Const.OUTPUT_DIR.concat(s"$klassName.class")

  def asmPrintMethod = org.objectweb.asm.commons.Method.getMethod("void println (int)")

}
