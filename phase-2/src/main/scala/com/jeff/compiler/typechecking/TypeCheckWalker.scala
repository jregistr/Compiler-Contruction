package com.jeff.compiler.typechecking

import com.compiler.generated.antlr.MiniJavaBaseVisitor
import com.compiler.generated.antlr.MiniJavaParser._
import com.jeff.compiler.errorhandling.Errors
import com.jeff.compiler.typechecking.definitions.{Klass, Scope}
import com.jeff.compiler.util.Aliases.ClassMap
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTreeProperty
import com.jeff.compiler.util.Const._


class TypeCheckWalker(classes:ClassMap, scopes:ParseTreeProperty[Scope]) extends MiniJavaBaseVisitor[Klass] {

  private var currentScope: Option[Scope] = None


  override def visitIntLiteral(ctx: IntLiteralContext): Klass = {
    visitChildren(ctx)
    classes(INT)
  }

  override def visitIdLiteral(ctx: IdLiteralContext): Klass = {
    val id = ctx.ID().getText
    currentScope match {
      case None => throw Errors.noScopeFound()
      case Some(scope) =>
        scope.findSymbolDeeply(id) match {
          case None => throw Errors.variableNotDeclared(scope, id)
          case Some(symbole) => symbole.typee
        }
    }
  }

  def findOuterScopeClass():Klass = {
    currentScope match {
      case None => throw Errors.noScopeFound()
      case Some(scope) => scope.enclosingScope match {
        case None => throw Errors.noScopeFound()
        case Some(outer) => outer.isInstanceOf[Klass] match {
          case true => outer.asInstanceOf[Klass]
          case false => throw Errors.unExpectedScope(outer)
        }
      }
    }
  }

  override def visitConstructorCall(ctx: ConstructorCallContext): Klass = {
    val className = ctx.ID().getText
    classes.get(className) match {
      case Some(x) => x
      case None => throw Errors.typeNotFound(className)
    }
  }

  override def visitThisCall(ctx: ThisCallContext): Klass = {
    visitChildren(ctx)
    findOuterScopeClass()
  }

  override def visitIntegerArr(ctx: IntegerArrContext): Klass = {
    val sizeTypeName = visit(ctx.expr()).name
    sizeTypeName match {
      case INT => classes(INTARR)
      case _=> throw Errors.typeMismatch(INT, sizeTypeName)
    }
  }

  override def visitBooleanLit(ctx: BooleanLitContext): Klass = {
    visitChildren(ctx)
    classes(BOOLEAN)
  }

  private def stepInScope(ctx:ParserRuleContext): Unit = {
    val searchScope = Option(scopes.get(ctx))
    searchScope match {
      case scopeOpt:Option[_] =>
        currentScope = scopeOpt
        visitChildren(ctx)
      case None => throw Errors.noScopeFound()
    }
  }

}
