package com.jeff.compiler.typechecking

import com.compiler.generated.antlr.MiniJavaBaseVisitor
import com.compiler.generated.antlr.MiniJavaParser._
import com.jeff.compiler.errorhandling.Errors
import com.jeff.compiler.typechecking.definitions._
import com.jeff.compiler.util.Aliases.ClassMap
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTreeProperty
import com.jeff.compiler.util.Const._


class TypeCheckWalker(classes:ClassMap, scopes:ParseTreeProperty[Scope]) extends MiniJavaBaseVisitor[Klass] {

  private var currentScope: Option[Scope] = None


  override def visitParenExpr(ctx: ParenExprContext): Klass = {
    visit(ctx)
  }

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

  override def visitType(ctx: TypeContext): Klass = {
    val id = Option(ctx.ID())
    id match {
      case Some(i) =>
        classes.get(i.getText) match {
          case Some(klass) => klass
          case None =>null
      }
      case None =>
        val contextName = ctx.getText
        classes.get(contextName) match {
          case Some(klass) => klass
          case None =>throw Errors.unexpectedContext()
        }
    }
  }

  override def visitVarDefinition(ctx: VarDefinitionContext): Klass = {
    currentScope match {
      case Some(rawScope)=>
        rawScope match {
          case method:Method =>
            val varToDefRaw: Symbole = method.findSymbolDeeply(ctx.ID().getText).get
            varToDefRaw match {
              case varToDef:VariableSymbol =>
                val expressionType = Option(visit(ctx.expr()))
                expressionType match {
                  case None => throw Errors.typeNotFound(ctx.expr().getText)
                  case Some(expType:Klass) =>
                    if(varToDef.typee.name == expType.name)
                      expType
                    else
                      throw Errors.typeMismatch(varToDef.typee.name, expType.name)
                }
              case _=> throw Errors.invalidOpOnSymbolType(varToDefRaw)
            }
          case _=> throw Errors.unExpectedScope(rawScope)
        }
      case None=> throw Errors.noScopeFound()
    }
  }

  override def visitConstructorCall(ctx: ConstructorCallContext): Klass = {
    val className = ctx.ID().getText
    classes.get(className) match {
      case Some(x) => x
      case None => throw Errors.typeNotFound(className)
    }
  }

  override def visitMethodDecl(ctx: MethodDeclContext): Klass = super.visitMethodDecl(ctx)

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

  override def visitGreaterThanExpr(ctx: GreaterThanExprContext): Klass = super.visitGreaterThanExpr(ctx)

  override def visitLessThanExpr(ctx: LessThanExprContext): Klass = super.visitLessThanExpr(ctx)

  override def visitNotExpr(ctx: NotExprContext): Klass = super.visitNotExpr(ctx)

  override def visitSubtractExpression(ctx: SubtractExpressionContext): Klass = super.visitSubtractExpression(ctx)

  override def visitPlusExpression(ctx: PlusExpressionContext): Klass = super.visitPlusExpression(ctx)

  override def visitMultiplyExpression(ctx: MultiplyExpressionContext): Klass = super.visitMultiplyExpression(ctx)

  override def visitAndExpr(ctx: AndExprContext): Klass = super.visitAndExpr(ctx)

  override def visitBooleanLit(ctx: BooleanLitContext): Klass = {
    visitChildren(ctx)
    classes(BOOLEAN)
  }


  override def visitMainClass(ctx: MainClassContext): Klass = {
    starClassScope(ctx)
    null
  }

  override def visitBaseClass(ctx: BaseClassContext): Klass = {
    starClassScope(ctx)
    null
  }

  override def visitChildClass(ctx: ChildClassContext): Klass = {
    starClassScope(ctx)
    null
  }

  override def visitArrLenExpression(ctx: ArrLenExpressionContext): Klass = super.visitArrLenExpression(ctx)

  override def visitArrayAccessExpression(ctx: ArrayAccessExpressionContext): Klass = super.visitArrayAccessExpression(ctx)

  private def starClassScope(ctx:ParserRuleContext): Unit = {
    val searchScope = Option(scopes.get(ctx))
    searchScope match {
      case None => throw Errors.noScopeFound()
      case scopeOpt:Option[_] =>
        currentScope = scopeOpt
        visitChildren(ctx)
    }
  }

}
