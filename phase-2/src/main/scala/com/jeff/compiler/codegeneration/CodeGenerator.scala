package com.jeff.compiler.codegeneration

import java.io.{FileOutputStream, PrintStream}

import com.compiler.generated.antlr.MiniJavaBaseListener
import com.compiler.generated.antlr.MiniJavaParser._
import com.jeff.compiler.errorhandling.Errors
import com.jeff.compiler.typechecking.definitions._
import com.jeff.compiler.util.Aliases.{AsmMethod, ClassMap}
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTreeProperty
import org.objectweb.asm.Opcodes._
import org.objectweb.asm.commons.{GeneratorAdapter, Method => AMethod}
import org.objectweb.asm.{ClassWriter, Label, Type}

import scala.annotation.tailrec
import scala.collection.mutable

class CodeGenerator(classes: ClassMap, scopes: ParseTreeProperty[Scope], methodCallers: ParseTreeProperty[Klass]) extends MiniJavaBaseListener {

  var currentScope: Option[Scope] = None
  var classWriter: ClassWriter = _
  var currentMethod: AsmMethod = _
  var methodGen: GeneratorAdapter = _
  var labelStack: mutable.Stack[Label] = mutable.Stack()

  var lastSeenImmutable: Option[VariableSymbol] = None

  override def enterMainClass(ctx: MainClassContext): Unit = {
    val className = ctx.className().getText
    enterClass(className)
    methodGen = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, AMethod.getMethod("void main (String[])", true), null, null, classWriter)
  }

  override def exitMainClass(ctx: MainClassContext): Unit = leaveClass(ctx.className().getText)

  override def enterBaseClass(ctx: BaseClassContext): Unit = {
    setCurrentScope(ctx)
    enterClass(ctx.className().getText)
  }

  override def exitBaseClass(ctx: BaseClassContext): Unit = {
    leaveClass(ctx.className().getText)
    exitScope(ctx)
  }

  override def enterChildClass(ctx: ChildClassContext): Unit = {
    setCurrentScope(ctx)
    enterClass(ctx.className().getText)
  }

  override def exitChildClass(ctx: ChildClassContext): Unit = {
    leaveClass(ctx.className().getText)
    exitScope(ctx)
  }

  override def enterMethodDecl(ctx: MethodDeclContext): Unit = {
    checkCurrentScopeClass(ctx)
    setCurrentScope(ctx)
    val methodName = ctx.ID().getText
    val scope = currentScope.get
    val method = findMethodOrError(ctx, scope, methodName)
    val parameters = method.parameters.toIndexedSeq
    parameters.indices.foreach(id => {
      val currentParam = parameters(id)._2
      currentParam.id = id
    })

    currentMethod = AsmConverter.methodToAsmMethod(method)
    methodGen = new GeneratorAdapter(ACC_PUBLIC, currentMethod, null, null, classWriter)
  }

  override def exitMethodDecl(ctx: MethodDeclContext): Unit = {
    methodGen.returnValue()
    methodGen.endMethod()
    exitScope(ctx)
  }


  override def enterImmutableFieldDeclaration(ctx: ImmutableFieldDeclarationContext): Unit = {
    checkCurrentScopeClass(ctx)
    val scope = currentScope.get
    val field: Field = scope.findSymbolDeeply(ctx.ID().getText).get.asInstanceOf[Field]
    classWriter.visitField(ACC_PUBLIC,
      field.name,
      AsmConverter.classToAsmType(field.typee).getDescriptor,
      null, null
    )
    lastSeenImmutable = Some(field)
    classWriter.visitEnd()
  }

  override def enterImmutableFieldAssign(ctx: ImmutableFieldAssignContext): Unit = {
    println(currentScope.get.asInstanceOf[Klass].name)
    val lastField: Field = lastSeenImmutable match {
      case Some(field: Field) => field
      case _ => throw Errors.typeNotFound("found immutable assign without last immutable seen", ctx.start)
    }

    lastSeenImmutable = None

    val enclosing: Klass = enclosingClassScope(currentScope)
    val fieldType: Klass = lastField.typee

    val enclosingAsm = AsmConverter.classToAsmType(enclosing)
    val fieldAsm = AsmConverter.classToAsmType(fieldType)

//    methodGen.loadThis()
  methodGen.putField(enclosingAsm, lastField.name, fieldAsm)

}

//  override def exitImmutableFieldAssign(ctx: ImmutableFieldAssignContext): Unit = {
//
//  }

  override def enterArrayDefinition(ctx: ArrayDefinitionContext): Unit = {
    checkCurrentScopeMethod(ctx)
//    currentScope match {
//      case None => throw new AssertionError("Scope not defined at variable declaration exit")
//      case Some(currScope) =>
//
//        currScope.deepFind(ctx.ID().getText) match {
//          case Some(property: PropertySymbol) =>
//            val enclosingKlass = TypeChecker.getEnclosingKlass(Some(currScope))
//            methodGenerator.loadThis()
//            methodGenerator.getField(enclosingKlass.asAsmType, property.name, property.kType.asAsmType)
//          case Some(localSymbol: VarSymbol) => methodGenerator.loadLocal(localSymbol.id, localSymbol.kType.asAsmType)
//          case Some(paramSymbol: ParamSymbol) => methodGenerator.loadArg(paramSymbol.id)
//        }
//    }
  }

  override def exitArrayDefinition(ctx: ArrayDefinitionContext): Unit = {
    methodGen.arrayStore(Type.INT_TYPE)
  }

  override def exitLessThanExpr(ctx: LessThanExprContext): Unit = {
    val trueLabel = methodGen.newLabel()
    val endLabel = methodGen.newLabel()

    methodGen.ifCmp(Type.INT_TYPE, GeneratorAdapter.LT, trueLabel)
    methodGen.push(false)
    methodGen.goTo(endLabel)
    methodGen.mark(trueLabel)
    methodGen.push(true)
    methodGen.mark(endLabel)
  }

  override def exitSubtractExpression(ctx: SubtractExpressionContext): Unit = methodGen.math(GeneratorAdapter.SUB, Type.INT_TYPE)

  override def exitPlusExpression(ctx: PlusExpressionContext): Unit = methodGen.math(GeneratorAdapter.ADD, Type.INT_TYPE)

  override def exitMultiplyExpression(ctx: MultiplyExpressionContext): Unit = methodGen.math(GeneratorAdapter.MUL, Type.INT_TYPE)

  override def exitAndExpr(ctx: AndExprContext): Unit = methodGen.math(GeneratorAdapter.AND, Type.BOOLEAN_TYPE)

  override def enterBooleanLit(ctx: BooleanLitContext): Unit = {
    val predicate = ctx.BOOLEAN_LIT().getText.toBoolean
    methodGen.push(predicate)
  }

  override def enterPrintToConsole(ctx: PrintToConsoleContext): Unit = {
    methodGen.getStatic(Type.getType(classOf[System]), "out", Type.getType(classOf[PrintStream]))
  }

  override def exitIntLiteral(ctx: IntLiteralContext): Unit = methodGen.push(Integer.parseInt(ctx.INT_LIT().getText))

  override def exitPrintToConsole(ctx: PrintToConsoleContext): Unit = {
    methodGen.invokeVirtual(Type.getType(classOf[PrintStream]), org.objectweb.asm.commons.Method.getMethod("void println (int)"))
  }

  override def exitIdLiteral(ctx: IdLiteralContext): Unit = {
    val scope = currentScope.get
    scope.findSymbolDeeply(ctx.getText) match {
      case None => throw Errors.illegalState("Not found", ctx.getStart)
      case Some(param: Parameter) => methodGen.loadArg(param.id)
      case Some(field: Field) =>
        val enclosingKlass = AsmConverter.classToAsmType(enclosingClassScope(currentScope))
        val symbolType = AsmConverter.classToAsmType(field.typee)
        methodGen.loadThis()
        methodGen.getField(enclosingKlass, field.name, symbolType)
      case Some(local: LocalVariable) =>
        local.name match {
          case "true" =>
            val predicate = ctx.ID().getText.toBoolean
            methodGen.push(predicate)
          case "false" =>
            val predicate = ctx.ID().getText.toBoolean
            methodGen.push(predicate)
          case _ =>
            methodGen.loadLocal(local.id, AsmConverter.classToAsmType(local.typee))
        }
      case _ => throw new IllegalStateException("")
    }
  }

  private def setCurrentScope(ctx: ParserRuleContext): Unit = {
    Option(scopes.get(ctx)) match {
      case s: Some[_] => currentScope = s
      case None => throw Errors.noScopeFound(ctx.start)
    }
  }

  private def exitScope(ctx: ParserRuleContext): Unit = {
    currentScope match {
      case Some(scope) => currentScope = scope.enclosingScope
      case None => throw Errors.noScopeFound(ctx.start)
    }
  }

  private def findMethodOrError(ctx: ParserRuleContext, scope: Scope, methodName: String): Method = {
    scope.findSymbolDeeply(scope.name) match {
      case Some(symbole) => symbole match {
        case method: Method => method
        case _ => throw Errors.typeMismatch("Method", symbole.name, ctx.start)
      }
      case None =>
        throw Errors.variableNotDeclared(scope, methodName, ctx.start)
    }
  }

  private def checkCurrentScopeClass(ctx: ParserRuleContext): Unit = {
    currentScope match {
      case Some(scope) =>
        if (!scope.isInstanceOf[Klass]) {
          throw Errors.unExpectedScope(ctx.start)
        }
      case None => throw Errors.noScopeFound(ctx.start)
    }
  }

  private def checkCurrentScopeMethod(ctx: ParserRuleContext): Unit = {
    currentScope match {
      case Some(scope) =>
        if (!scope.isInstanceOf[Method]) {
          throw Errors.unExpectedScope(ctx.start)
        }
      case None => throw Errors.noScopeFound(ctx.start)
    }
  }

  @tailrec private def enclosingClassScope(scope: Option[Scope]): Klass = {
    scope match {
      case Some(method: Method) => enclosingClassScope(method.enclosingScope)
      case Some(klass: Klass) => klass
      case _ => throw new IllegalStateException("Invalid state")
    }
  }

  private def enterClass(className: String): Unit = {
    val klass: Klass = classes(className)
    val superClassName: String = klass.superClass match {
      case Some(s) => s.name
      case None => AsmConverter.OBJECT
    }

    classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES)
    classWriter.visit(V1_1, ACC_PUBLIC, klass.name, null, superClassName, null)
    methodGen = new GeneratorAdapter(ACC_PUBLIC, AsmConverter.initMethod, null, null, classWriter)
    methodGen.loadThis()

    methodGen.invokeConstructor(
      if (klass.superClass.isDefined)
        Type.getObjectType(superClassName)
      else Type.getType(classOf[Object]),
      AsmConverter.initMethod)

    methodGen.returnValue()
    methodGen.endMethod()
  }

  private def leaveClass(className: String): Unit = {
    classWriter.visitEnd()
    methodGen.returnValue()
    methodGen.endMethod()
    val stream = new FileOutputStream(AsmConverter.classFileName(className))
    stream.write(classWriter.toByteArray)
    stream.close()
  }

}
