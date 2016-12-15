package com.jeff.compiler.typechecking.listeners

import com.compiler.generated.antlr.MiniJavaBaseListener
import com.compiler.generated.antlr.MiniJavaParser.{BaseClassContext, ChildClassContext, MainClassContext}
import com.jeff.compiler.errorhandling.Errors
import com.jeff.compiler.typechecking.definitions.Klass
import com.jeff.compiler.util.Aliases.ClassMap

import scala.collection.mutable.ListBuffer


class ClassListener(classes: ClassMap) extends MiniJavaBaseListener {

  val awaitingParent:ListBuffer[(Klass, String)] = ListBuffer()

  override def enterMainClass(ctx: MainClassContext): Unit = {
    addClass(ctx.className().getText, None)
  }

  override def enterBaseClass(ctx: BaseClassContext): Unit = {
    val name = ctx.className().getText
    addClass(name, None)
  }

  override def enterChildClass(ctx: ChildClassContext): Unit = {
    val name = ctx.className().getText
    val parentName = ctx.parentName().getText

    val klass = addClass(name, Some(parentName))
    klass.checkForCycles()
  }

  private def addClass(name: String, parentName:Option[String]): Klass = {
    val klass:Klass = classes.get(name) match {
      case None =>
        parentName match {
          case Some(lookingFor:String) =>
            classes.get(lookingFor) match {
              case None =>
                val klass = new Klass(name, None)
                awaitingParent +=(klass -> lookingFor)
                klass
              case foundParent:Some[_] =>
                val klass = new Klass(name, foundParent)
                klass
            }
          case None =>
            new Klass(name, None)
        }
      case Some(_) => throw Errors.duplicateClassDeclaration(name)
    }
    classes.put(name, klass)
    checkWaiters()
    klass
  }

  private def checkWaiters(): Unit = {
    for(i <- awaitingParent.indices) {
      val current = awaitingParent(i)
      classes.get(current._2) match {
        case Some(foundParent) =>
          awaitingParent.remove(i)
          current._1.setSuperClass(foundParent)
        case None =>
      }
    }
  }

  def errorForMissingParents():Unit = {
    if(awaitingParent.nonEmpty) {
      throw Errors.noClassDefFound(awaitingParent.map(_._2).toList)
    }
  }

}
