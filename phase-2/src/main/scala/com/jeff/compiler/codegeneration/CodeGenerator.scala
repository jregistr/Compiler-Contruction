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

import scala.collection.mutable

class CodeGenerator(classes: ClassMap, scopes: ParseTreeProperty[Scope], methodCallers: ParseTreeProperty[Klass]) extends MiniJavaBaseListener {

  var currentScope: Option[Scope] = None
  var classWriter: ClassWriter = _
  var currentMethod: AsmMethod = _
  var methodGen: GeneratorAdapter = _
  var labelStack: mutable.Stack[Label] = mutable.Stack()

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

  override def enterMethodDecleration(ctx: MethodDeclerationContext): Unit = {
    checkCurrentScopeClass(ctx)
    setCurrentScope(ctx)
    val methodName = ctx.ID().getText
    val scope = currentScope.get
    val method = scope.findSymbolDeeply(methodName).get.asInstanceOf[Method]
    val parameters = method.parameters.toIndexedSeq
    parameters.indices.foreach(id => {
      val currentParam = parameters(id)._2
      currentParam.id = id
    })

    currentMethod = AsmConverter.methodToAsmMethod(method)
    methodGen = new GeneratorAdapter(ACC_PUBLIC, currentMethod, null, null, classWriter)
  }

  override def exitMethodDecleration(ctx: MethodDeclerationContext): Unit = {
    methodGen.returnValue()
    methodGen.endMethod()
    exitScope(ctx)
  }

  override def exitFieldDeclaration(ctx: FieldDeclarationContext): Unit = {
    checkCurrentScopeClass(ctx)
    val scope = currentScope.get
    val field = scope.findSymbolDeeply(ctx.ID().getText).get.asInstanceOf[Field]
    classWriter.visitField(ACC_PUBLIC, field.name, AsmConverter.classToAsmType(field.typee).getDescriptor, null, null)
    classWriter.visitEnd()
  }

  override def exitVariableDeclaration(ctx: VariableDeclarationContext): Unit = {
    checkCurrentScopeMethod(ctx)
    val scope = currentScope.get

    val variable: LocalVariable = scope.findSymbolDeeply(ctx.ID().getText).get.asInstanceOf[LocalVariable]
    val asmType = AsmConverter.classToAsmType(variable.typee)
    val localId = methodGen.newLocal(asmType)
    variable.id = localId
  }

  override def enterArrayDefinition(ctx: ArrayDefinitionContext): Unit = {
    checkCurrentScopeMethod(ctx)
    val scope = currentScope.get
    val symbole = scope.findSymbolDeeply(ctx.ID().getText).get
    symbole match {
      case property: Field =>
        val enclosing = enclosingClassScope(Some(scope))
        methodGen.loadThis()
        methodGen.getField(AsmConverter.classToAsmType(enclosing), property.name, AsmConverter.classToAsmType(property.typee))
      case localSymbol: LocalVariable => methodGen.loadLocal(localSymbol.id, AsmConverter.classToAsmType(localSymbol.typee))
      case paramSymbol: Parameter => methodGen.loadArg(paramSymbol.id)
      case _ => throw Errors.invalidOpOnSymbolType(symbole, ctx.start)
    }
  }

  override def enterVarDefinition(ctx: VarDefinitionContext): Unit = {
    checkCurrentScopeMethod(ctx)
    val scope = currentScope.get
    val variable = scope.findSymbolDeeply(ctx.ID().getText).get
    if (variable.isInstanceOf[Field]) {
      methodGen.loadThis()
    }
  }

  override def exitVarDefinition(ctx: VarDefinitionContext): Unit = {
    checkCurrentScopeMethod(ctx)
    val scope = currentScope.get
    val symbole = scope.findSymbolDeeply(ctx.ID().getText).get
    symbole match {
      case field: Field =>
        val enclosing = enclosingClassScope(Some(scope))
        methodGen.putField(AsmConverter.classToAsmType(enclosing), field.name, AsmConverter.classToAsmType(field.typee))
      case localVariable: LocalVariable =>
        methodGen.storeLocal(localVariable.id, AsmConverter.classToAsmType(localVariable.typee))
      case parameter: Parameter =>
        methodGen.storeArg(parameter.id)
    }
  }

  override def exitArrayDefinition(ctx: ArrayDefinitionContext): Unit = {
    checkCurrentScopeMethod(ctx)
    methodGen.arrayStore(Type.INT_TYPE)
  }

  override def exitLessThanExpr(ctx: LessThanExprContext): Unit = {
    checkCurrentScopeMethod(ctx)
    val trueLabel = methodGen.newLabel()
    val endLabel = methodGen.newLabel()

    methodGen.ifCmp(Type.INT_TYPE, GeneratorAdapter.LT, trueLabel)
    methodGen.push(false)
    methodGen.goTo(endLabel)
    methodGen.mark(trueLabel)
    methodGen.push(true)
    methodGen.mark(endLabel)
  }

  override def exitIdLiteral(ctx: IdLiteralContext): Unit = {
    checkCurrentScopeMethod(ctx)
    val scope = currentScope.get
    val symbole = scope.findSymbolDeeply(ctx.ID().getText).get
    val outerClass = enclosingClassScope(Some(scope))
    val outerClassAsm = AsmConverter.classToAsmType(outerClass)
    symbole match {
      case field: Field =>
        val fieldTypeAsm = AsmConverter.classToAsmType(field.typee)
        methodGen.loadThis()
        methodGen.getField(outerClassAsm, field.name, fieldTypeAsm)
      case parameter: Parameter =>
        methodGen.loadArg(parameter.id)
      case variable: LocalVariable =>
        val symbolTypeAsm = AsmConverter.classToAsmType(variable.typee)
        methodGen.loadLocal(variable.id, symbolTypeAsm)
      case _ => throw Errors.invalidOpOnSymbolType(symbole, ctx.start)
    }
  }

  override def enterIfBlock(ctx: IfBlockContext): Unit = {
    methodGen.ifZCmp(GeneratorAdapter.EQ, labelStack.pop)
  }

  override def exitIfBlock(ctx: IfBlockContext): Unit = {
    methodGen.goTo(labelStack.pop())
  }

  override def enterElseBlock(ctx: ElseBlockContext): Unit = {
    methodGen.mark(labelStack.pop())
  }

  override def exitElseBlock(ctx: ElseBlockContext): Unit = {
    methodGen.mark(labelStack.pop())
  }

  override def exitMethodCallExpression(ctx: MethodCallExpressionContext): Unit = {
    val klass = methodCallers.get(ctx)
    val method: Method = klass.findSymbolLocally(ctx.ID().getText).get.asInstanceOf[Method]
    val typeAsAsm = AsmConverter.classToAsmType(klass)
    val methodAsm = AsmConverter.methodToAsmMethod(method)
    methodGen.invokeVirtual(typeAsAsm, methodAsm)
  }

  override def enterIntLiteral(ctx: IntLiteralContext): Unit = {
    methodGen.push(Integer.parseInt(ctx.INT_LIT().getText))
  }


  override def enterWhileLoopHead(ctx: WhileLoopHeadContext): Unit = {
    val enterWhile = methodGen.mark()
    val exitWhile = methodGen.newLabel()
    labelStack push exitWhile
    labelStack push enterWhile
    labelStack push exitWhile
  }

  override def enterWhileBlock(ctx: WhileBlockContext): Unit = {
    methodGen.ifZCmp(GeneratorAdapter.EQ, labelStack.pop)
  }

  override def exitWhileLoopHead(ctx: WhileLoopHeadContext): Unit = {
    methodGen.goTo(labelStack.pop)
    methodGen.mark(labelStack.pop)
  }

  override def exitSubtractExpression(ctx: SubtractExpressionContext): Unit = methodGen.math(GeneratorAdapter.SUB, Type.INT_TYPE)

  override def exitPlusExpression(ctx: PlusExpressionContext): Unit = methodGen.math(GeneratorAdapter.ADD, Type.INT_TYPE)

  override def exitMultiplyExpression(ctx: MultiplyExpressionContext): Unit = methodGen.math(GeneratorAdapter.MUL, Type.INT_TYPE)

  override def exitAndExpr(ctx: AndExprContext): Unit = methodGen.math(GeneratorAdapter.AND, Type.BOOLEAN_TYPE)

  override def enterBooleanLit(ctx: BooleanLitContext): Unit = {
    val predicate = ctx.BOOLEAN_LIT().getText.toBoolean
    methodGen.push(predicate)
  }

  override def exitIntLiteral(ctx: IntLiteralContext): Unit = methodGen.push(Integer.parseInt(ctx.INT_LIT().getText))


  override def exitArrayAccessExpression(ctx: ArrayAccessExpressionContext): Unit = {
    methodGen.arrayLoad(Type.INT_TYPE)
  }

  override def exitArrLenExpression(ctx: ArrLenExpressionContext): Unit = {
    methodGen.arrayLength()
  }

  override def exitThisCall(ctx: ThisCallContext): Unit = methodGen.loadThis()

  override def exitIntegerArr(ctx: IntegerArrContext): Unit = methodGen.newArray(Type.INT_TYPE)

  override def exitNotExpr(ctx: NotExprContext): Unit = {
    methodGen.not()
  }

  override def enterConstructorCall(ctx: ConstructorCallContext): Unit = {
    val objectType = Type.getObjectType(ctx.ID().getText)
    methodGen.newInstance(objectType)
    methodGen.dup()
    methodGen.invokeConstructor(objectType, AsmConverter.initMethod)
  }

  override def enterPrintToConsole(ctx: PrintToConsoleContext): Unit = {
    methodGen.getStatic(Type.getType(classOf[System]), "out", Type.getType(classOf[PrintStream]))
  }

  override def exitPrintToConsole(ctx: PrintToConsoleContext): Unit = {
    methodGen.invokeVirtual(Type.getType(classOf[PrintStream]), AsmConverter.asmPrintMethod)
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

  private def enclosingClassScope(scope: Option[Scope]): Klass = {
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
