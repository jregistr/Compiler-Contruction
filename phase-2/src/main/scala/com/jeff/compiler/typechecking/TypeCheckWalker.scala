package com.jeff.compiler.typechecking

import com.compiler.generated.antlr.MiniJavaBaseVisitor
import com.compiler.generated.antlr.MiniJavaParser._
import com.jeff.compiler.errorhandling.Errors
import com.jeff.compiler.typechecking.definitions._
import com.jeff.compiler.util.Aliases.ClassMap
import com.jeff.compiler.util.Const._
import org.antlr.v4.runtime.{ParserRuleContext, Token}
import org.antlr.v4.runtime.tree.ParseTreeProperty

import scala.collection.JavaConversions._


class TypeCheckWalker(classes: ClassMap, scopes: ParseTreeProperty[Scope]) extends MiniJavaBaseVisitor[Klass] {

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
      case None => throw Errors.noScopeFound(ctx.ID().getSymbol)
      case Some(scope) =>
        scope.findSymbolDeeply(id) match {
          case None => throw Errors.variableNotDeclared(scope, id, ctx.ID().getSymbol)
          case Some(symbole) => symbole.typee
        }
    }
  }

  def findOuterScopeClass(ctx: ParserRuleContext): Klass = {
    currentScope match {
      case None => throw Errors.noScopeFound(ctx.getStart)
      case Some(scope) => scope.enclosingScope match {
        case None => throw Errors.noScopeFound(ctx.getStart)
        case Some(outer) => outer.isInstanceOf[Klass] match {
          case true => outer.asInstanceOf[Klass]
          case false => throw Errors.unExpectedScope(outer, ctx.getStart)
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
          case None => null
        }
      case None =>
        val contextName = ctx.getText
        classes.get(contextName) match {
          case Some(klass) => klass
          case None => throw Errors.unexpectedContext(ctx.ID().getSymbol)
        }
    }
  }

  override def visitVarDefinition(ctx: VarDefinitionContext): Klass = {
    currentScope match {
      case Some(rawScope) =>
        rawScope match {
          case method: Method =>
            val varToDefRaw: Symbole = method.findSymbolDeeply(ctx.ID().getText).get
            varToDefRaw match {
              case varToDef: VariableSymbol =>
                val expressionType = Option(visit(ctx.expr()))
                expressionType match {
                  case None => throw Errors.typeNotFound(ctx.expr().getText, ctx.ID().getSymbol)
                  case Some(expType: Klass) =>
                    if (varToDef.typee.name == expType.name)
                      expType
                    else
                      throw Errors.typeMismatch(varToDef.typee.name, expType.name, ctx.ID().getSymbol)
                }
              case _ => throw Errors.invalidOpOnSymbolType(varToDefRaw, ctx.ID().getSymbol)
            }
          case _ => throw Errors.unExpectedScope(rawScope, ctx.ID().getSymbol)
        }
      case None => throw Errors.noScopeFound(ctx.ID().getSymbol)
    }
  }

  override def visitConstructorCall(ctx: ConstructorCallContext): Klass = {
    val className = ctx.ID().getText
    classes.get(className) match {
      case Some(x) => x
      case None => throw Errors.typeNotFound(className, ctx.stop)
    }
  }

  override def visitMethodDecl(ctx: MethodDeclContext): Klass = {
    startMethodScope(ctx)
    val methodReturnName = ctx.`type`().getText
    visitChildren(ctx)
    classes.get(methodReturnName) match {
      case None => throw Errors.typeNotFound(methodReturnName, ctx.ID().getSymbol)
      case Some(methodReturnType: Klass) =>
        val returnExpressionType = visit(ctx.expr())
        if (returnExpressionType.name == methodReturnType.name)
          methodReturnType
        else throw Errors.typeMismatch(methodReturnType.name, returnExpressionType.name, ctx.ID().getSymbol)
    }
  }


  override def visitMethodParam(ctx: MethodParamContext): Klass = {
    checkInMethodScope(ctx)
    val typeName = ctx.`type`().getText
    classes.get(typeName) match {
      case None => throw Errors.typeNotFound(typeName, ctx.ID().getSymbol)
      case Some(klass) => klass
    }
  }


  override def visitMethodCallExpression(ctx: MethodCallExpressionContext): Klass = {
    val methodOwner: Klass = visit(ctx.expr(0))
    val methodName = ctx.ID().getText
    methodOwner.findSymbolDeeply(methodName) match {
      case None => throw Errors.typeNotFound(methodName, ctx.ID().getSymbol)
      case Some(foundSymbole) =>
        foundSymbole match {
          case method: Method =>
            val passedParamsTypes = ctx.expr().tail.map(x => visit(x)).toList
            val methodParamTypes = method.parameters.map(_._2.typee).toList
            val allMatch = passedParamsTypes.length == methodParamTypes.length && passedParamsTypes.zip(methodParamTypes).forall(pair => pair._1.name == pair._2.name)
            if (allMatch)
              method.typee
            else
              throw Errors.typeMismatch("Input parameters do not match method parameters", ctx.ID().getSymbol)
          case _ => throw Errors.typeMismatch("Method", foundSymbole.name, ctx.ID().getSymbol)
        }
    }
  }

  override def visitThisCall(ctx: ThisCallContext): Klass = {
    visitChildren(ctx)
    findOuterScopeClass(ctx)
  }

  override def visitIntegerArr(ctx: IntegerArrContext): Klass = {
    val sizeTypeName = visit(ctx.expr()).name
    sizeTypeName match {
      case INT => classes(INTARR)
      case _ => throw Errors.typeMismatch(INT, sizeTypeName, ctx.expr().start)
    }
  }

  override def visitGreaterThanExpr(ctx: GreaterThanExprContext): Klass = twoMatch(visit(ctx.expr(0)), visit(ctx.expr(1)), INT, ctx)

  override def visitLessThanExpr(ctx: LessThanExprContext): Klass = twoMatch(visit(ctx.expr(0)), visit(ctx.expr(1)), INT, ctx)

  override def visitNotExpr(ctx: NotExprContext): Klass = {
    val expType = visit(ctx.expr())
    expType.name match {
      case BOOLEAN => classes(BOOLEAN)
      case _=> throw Errors.typeMismatch(BOOLEAN, expType.name, ctx.start)
    }
  }

  override def visitArrayDefinition(ctx: ArrayDefinitionContext): Klass = {
    checkInMethodScope(ctx)
    val id = ctx.ID().getText
    val indexType = visit(ctx.expr(0))
    val valueType = visit(ctx.expr(1))
    
  }

  override def visitWhileLoopHead(ctx: WhileLoopHeadContext): Klass = ???

  override def visitIfStatement(ctx: IfStatementContext): Klass = ???

  override def visitSubtractExpression(ctx: SubtractExpressionContext): Klass = twoMatch(visit(ctx.expr(0)), visit(ctx.expr(1)), INT, ctx)

  override def visitPlusExpression(ctx: PlusExpressionContext): Klass = twoMatch(visit(ctx.expr(0)), visit(ctx.expr(1)), INT, ctx)

  override def visitMultiplyExpression(ctx: MultiplyExpressionContext): Klass = twoMatch(visit(ctx.expr(0)), visit(ctx.expr(1)), INT, ctx)

  override def visitAndExpr(ctx: AndExprContext): Klass = twoMatch(visit(ctx.expr(0)), visit(ctx.expr(1)), BOOLEAN, ctx.expr(0))

  private def twoMatch(left:Klass, right:Klass, expected:String, ctx:ParserRuleContext):Klass = {
    checkInMethodScope(ctx)
    if(left.name == expected && right.name == expected)
      classes(expected)
    else
      throw Errors.typeMismatch(expected, s"LEFT:${left.name}, RIGHT:${right.name}", ctx.start)
  }

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

  override def visitArrLenExpression(ctx: ArrLenExpressionContext): Klass = {
    val arrayType = visit(ctx.expr())
    arrayType.name match {
      case INTARR => classes(INT)
      case _ => throw Errors.typeMismatch(INTARR, arrayType.name, ctx.start)
    }
  }

  override def visitArrayAccessExpression(ctx: ArrayAccessExpressionContext): Klass = {
    checkInMethodScope(ctx)
    val arrayType = visit(ctx.atom())
    val indexType = visit(ctx.expr())
    arrayType.name match {
      case INTARR =>
        indexType.name match {
          case INT => classes(INT)
          case _ => throw Errors.typeMismatch(INT, indexType.name, ctx.expr().start)
        }
      case _ => throw Errors.typeMismatch(INTARR, arrayType.name, ctx.atom().start)
    }
  }

  private def checkInMethodScope(ctx: ParserRuleContext): Unit = {
    currentScope match {
      case None => throw Errors.noScopeFound(ctx.start)
      case Some(scope) =>
        scope match {
          case m: Method =>
          case _ => throw Errors.unExpectedScope(scope, ctx.start)
        }
    }
  }

  private def startMethodScope(ctx: MethodDeclContext): Unit = {
    currentScope match {
      case None => throw Errors.noScopeFound(ctx.`type`().start)
      case Some(scope) =>
        scope match {
          case klass: Klass =>
            val searchScope = Option(scopes.get(ctx))
            searchScope match {
              case None => throw Errors.noScopeFoundFor(ctx.getText, ctx.`type`().start)
              case s: Some[_] => currentScope = s
            }
          case _ => throw Errors.unExpectedScope(scope, ctx.`type`().start)
        }
    }
  }

  private def starClassScope(ctx: ParserRuleContext): Unit = {
    val searchScope = Option(scopes.get(ctx))
    searchScope match {
      case None => throw Errors.noScopeFound(ctx.start)
      case scopeOpt: Option[_] =>
        currentScope = scopeOpt
        visitChildren(ctx)
    }
  }

}
