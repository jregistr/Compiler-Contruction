package com.jeff.compiler.typechecking.listeners

import com.compiler.generated.antlr.MiniJavaBaseListener
import com.compiler.generated.antlr.MiniJavaParser._
import com.jeff.compiler.errorhandling.Errors
import com.jeff.compiler.typechecking.definitions._
import com.jeff.compiler.util.Aliases.{ClassMap, ParamMap}
import org.antlr.v4.runtime.{ParserRuleContext, Token}
import org.antlr.v4.runtime.tree.ParseTreeProperty

import scala.collection.JavaConversions._
import scala.collection.mutable


class SymbolListener(classes: ClassMap, scopes: ParseTreeProperty[Scope]) extends MiniJavaBaseListener {

  private var currentScope: Option[Scope] = None

  override def enterMainClass(ctx: MainClassContext): Unit = setCurrentScope(ctx)

  override def exitMainClass(ctx: MainClassContext): Unit = leaveScope(ctx)

  override def enterBaseClass(ctx: BaseClassContext): Unit = setCurrentScope(ctx)

  override def exitBaseClass(ctx: BaseClassContext): Unit = leaveScope(ctx)

  override def enterChildClass(ctx: ChildClassContext): Unit = setCurrentScope(ctx)

  override def exitChildClass(ctx: ChildClassContext): Unit = leaveScope(ctx)


  override def enterFieldDeclaration(ctx: FieldDeclarationContext): Unit = {
    val info = doVariableDecFoundCheck(ctx, ctx.ID().getSymbol)
    currentScope.get.addSymbol(Field(info._1, info._2, info._3, ctx.ID().getSymbol))
  }

  override def enterVariableDeclaration(ctx: VariableDeclarationContext): Unit = {
    val info = doVariableDecFoundCheck(ctx, ctx.ID().getSymbol)
    currentScope.get.addSymbol(LocalVariable(info._1, info._2, info._3, ctx.ID().getSymbol))
  }

  override def enterMethodDecl(ctx: MethodDeclContext): Unit = {
    currentScope match {
      case None => throw Errors.noScopeFound(ctx.ID().getSymbol)
      case Some(scope) =>
        val name = ctx.ID().getText
        val typeName = ctx.`type`().getText
        classes.get(typeName) match {
          case Some(foundType) =>
            val parameters: ParamMap = mutable.LinkedHashMap() ++= getMethodParameters(ctx.methodParam()).map(p => p.name -> p)
            val method = new Method(name, foundType, scope, parameters, ctx.ID().getSymbol)
            scope.addSymbol(method)
            setCurrentScope(ctx, method)
          case None => throw Errors.typeNotFound(typeName, ctx.ID().getSymbol)
        }
    }
  }

  override def exitMethodDecl(ctx: MethodDeclContext): Unit = leaveScope(ctx)


  override def enterVarDefinition(ctx: VarDefinitionContext): Unit = {
    currentScope match {
      case None => Errors.noScopeFound(ctx.ID().getSymbol)
      case Some(scope) =>
        val name = ctx.ID().getText
        val symbol = scope.findSymbolDeeply(name)
        symbol match {
          case None => throw Errors.variableNotDeclared(scope, name, ctx.ID().getSymbol)
          case Some(x) => scope.initialiseSymbol(x)
        }
    }
  }

  private def getMethodParameters(params: java.util.List[MethodParamContext]): List[Parameter] = {
    params.map(paramContext => makeParameter(paramContext)).toList
  }

  private def makeParameter(ctx: MethodParamContext): Parameter = {
    val name = ctx.ID().getText
    val typeName = ctx.`type`().getText
    classes.get(typeName) match {
      case None => throw Errors.typeNotFound(typeName, ctx.ID().getSymbol)
      case Some(foundType) => Parameter(name, foundType,ctx.ID().getSymbol)
    }
  }

  private def doVariableDecFoundCheck(rawContext: ParserRuleContext, token: Token): (String, Klass, Boolean) = {
    val values: (String, String, Boolean) = rawContext match {
      case ctx: FieldDeclarationContext =>
        (ctx.ID().getText, ctx.`type`().getText, Option(ctx.mutable).isDefined)
      case ctx: VariableDeclarationContext =>
        (ctx.ID().getText, ctx.`type`().getText, Option(ctx.mutable).isDefined)
      case _ => throw Errors.unexpectedContext(token)
    }

    currentScope match {
      case None => throw Errors.noScopeFound(token)
      case Some(scope) =>
        //check if type exists
        classes.get(values._2) match {
          case None => throw Errors.typeNotFound(values._2, token)
          case Some(foundType: Klass) =>
            scope.findSymbolDeeply(values._1) match {
              case Some(foundSymbol) => throw Errors.duplicateDeclaration(scope, values._1, token)
              case None => (values._1, foundType, values._3)
            }
        }

    }
  }

  private def setCurrentScope(context: ParserRuleContext, scope: Scope): Unit = {
    currentScope = Some(scope)
    scopes.put(context, scope)
  }

  private def setCurrentScope(context: ParserRuleContext): Unit = {
    val name = context match {
      case main: MainClassContext =>
        main.className().getText
      case base: BaseClassContext =>
        base.className().getText
      case child: ChildClassContext =>
        child.className().getText
      case _ => throw Errors.unexpectedContext(context.start)
    }


    classes.get(name) match {
      case None => throw Errors.typeNotFound(context.getText, context.start)
      case found: Some[Klass] =>
        currentScope = found
        scopes.put(context, found.get)
    }
  }

  private def leaveScope(ctx:ParserRuleContext): Unit = {
    currentScope match {
      case Some(current) =>
        currentScope = current.enclosingScope
      case None => throw Errors.noScopeFound(ctx.stop)
    }
  }

}
