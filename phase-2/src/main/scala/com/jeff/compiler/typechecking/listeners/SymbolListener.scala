package com.jeff.compiler.typechecking.listeners

import com.compiler.generated.antlr.MiniJavaBaseListener
import com.compiler.generated.antlr.MiniJavaParser._
import com.jeff.compiler.errorhandling.Errors
import com.jeff.compiler.typechecking.definitions._
import com.jeff.compiler.util.Aliases.{ClassMap, ParamMap}
import org.antlr.v4.runtime.tree.ParseTreeProperty
import org.antlr.v4.runtime.{ParserRuleContext, Token}

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

  override def enterMethodDecleration(ctx: MethodDeclerationContext): Unit = {
    checkInClassScope(ctx.ID().getSymbol)
    val scope = currentScope.get
    val name = ctx.ID().getText
    val typeName = ctx.`type`().getText
    classes.get(typeName) match {
      case Some(foundType) =>
        val parameters: ParamMap = mutable.LinkedHashMap() ++= getMethodParameters(ctx.methodParam()).map(p => p.name -> p)
        val method = new Method(name, foundType, scope, parameters)

        scope.findSymbolDeeply(name) match {
          case None =>
            scope.addSymbol(method)
            setCurrentScope(ctx, method)
          case Some(foundSymbol) =>
            foundSymbol match {
              case declaredMethod: Method =>
                if (checkForProperOverride(declaredMethod, method)) {
                  scope.addSymbol(method)
                  setCurrentScope(ctx, method)
                } else {
                  throw Errors.duplicateDeclaration(scope, foundSymbol, method, ctx.ID().getSymbol)
                }
              case _ => throw Errors.duplicateDeclaration(scope, foundSymbol, method, ctx.ID().getSymbol)
            }
        }
      case None => throw Errors.typeNotFound(typeName, ctx.`type`().start)
    }
  }

  override def exitMethodDecleration(ctx: MethodDeclerationContext): Unit = leaveScope(ctx)

  override def enterFieldDeclaration(ctx: FieldDeclarationContext): Unit = {
    checkInClassScope(ctx.ID().getSymbol)
    addVariableToCurrentScope(Field(ctx.ID().getText, findClassOrError(ctx.`type`().getText, ctx.ID().getSymbol), mutable = true), ctx)
  }


  override def enterVariableDeclaration(ctx: VariableDeclarationContext): Unit = {
    checkInMethodScope(ctx.start)
    addVariableToCurrentScope(LocalVariable(ctx.ID().getText, findClassOrError(ctx.`type`().getText, ctx.ID().getSymbol), mutable = true), ctx)
  }

  private def addVariableToCurrentScope(variable: VariableSymbol, ctx: ParserRuleContext): VariableSymbol = {
    val scope = currentScope.get
    scope.findSymbolDeeply(variable.name) match {
      case Some(foundSymbol) => throw Errors.duplicateDeclaration(scope, foundSymbol, variable, ctx.start)
      case None =>
        scope.addSymbol(variable)
        variable
    }
  }

  private def findClassOrError(typeName: String, token: Token): Klass = {
    classes.get(typeName) match {
      case Some(klass) => klass
      case None => throw Errors.typeNotFound(typeName, token)
    }
  }

  private def checkForProperOverride(belowMethod: Method, currentMethod: Method): Boolean = {
    isProperOverride(belowMethod, currentMethod)
  }

  private def isProperOverride(first: Method, second: Method): Boolean = {
    first.typee.name == second.typee.name &&
      first.name.equals(second.name) &&
      first.parameters.values.map(_.typee) == second.parameters.values.map(_.typee)
  }

  private def checkInClassScope(currentToken: Token): Unit = {
    currentScope match {
      case None => throw Errors.noScopeFound(currentToken)
      case Some(scope) =>
        if (!scope.isInstanceOf[Klass]) {
          throw Errors.unExpectedScope(scope, currentToken)
        }
    }
  }

  private def checkInMethodScope(currentToken:Token):Unit = {
    currentScope match {
      case None => throw Errors.noScopeFound(currentToken)
      case Some(scope) =>
        if (!scope.isInstanceOf[Method]) {
          throw Errors.unExpectedScope(scope, currentToken)
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
      case Some(foundType) => Parameter(name, foundType)
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

  private def leaveScope(ctx: ParserRuleContext): Unit = {
    currentScope match {
      case Some(current) =>
        currentScope = current.enclosingScope
      case None => throw Errors.noScopeFound(ctx.stop)
    }
  }

}
