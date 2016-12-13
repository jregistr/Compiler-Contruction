// Generated from C:/Users/JeffRegistre/class/Compiler-Contruction/phase-2/src/main/java\MiniJava.g4 by ANTLR 4.5.3
package com.compiler.generated.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MiniJavaParser}.
 */
public interface MiniJavaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#goal}.
	 * @param ctx the parse tree
	 */
	void enterGoal(MiniJavaParser.GoalContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#goal}.
	 * @param ctx the parse tree
	 */
	void exitGoal(MiniJavaParser.GoalContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#mainClass}.
	 * @param ctx the parse tree
	 */
	void enterMainClass(MiniJavaParser.MainClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#mainClass}.
	 * @param ctx the parse tree
	 */
	void exitMainClass(MiniJavaParser.MainClassContext ctx);
	/**
	 * Enter a parse tree produced by the {@code baseClass}
	 * labeled alternative in {@link MiniJavaParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void enterBaseClass(MiniJavaParser.BaseClassContext ctx);
	/**
	 * Exit a parse tree produced by the {@code baseClass}
	 * labeled alternative in {@link MiniJavaParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void exitBaseClass(MiniJavaParser.BaseClassContext ctx);
	/**
	 * Enter a parse tree produced by the {@code childClass}
	 * labeled alternative in {@link MiniJavaParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void enterChildClass(MiniJavaParser.ChildClassContext ctx);
	/**
	 * Exit a parse tree produced by the {@code childClass}
	 * labeled alternative in {@link MiniJavaParser#classDecl}.
	 * @param ctx the parse tree
	 */
	void exitChildClass(MiniJavaParser.ChildClassContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImmutableVariable}
	 * labeled alternative in {@link MiniJavaParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImmutableVariable(MiniJavaParser.ImmutableVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImmutableVariable}
	 * labeled alternative in {@link MiniJavaParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImmutableVariable(MiniJavaParser.ImmutableVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MutableVariable}
	 * labeled alternative in {@link MiniJavaParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMutableVariable(MiniJavaParser.MutableVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MutableVariable}
	 * labeled alternative in {@link MiniJavaParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMutableVariable(MiniJavaParser.MutableVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImmutableField}
	 * labeled alternative in {@link MiniJavaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImmutableField(MiniJavaParser.ImmutableFieldContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImmutableField}
	 * labeled alternative in {@link MiniJavaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImmutableField(MiniJavaParser.ImmutableFieldContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MutableField}
	 * labeled alternative in {@link MiniJavaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMutableField(MiniJavaParser.MutableFieldContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MutableField}
	 * labeled alternative in {@link MiniJavaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMutableField(MiniJavaParser.MutableFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#methodDecl}.
	 * @param ctx the parse tree
	 */
	void enterMethodDecl(MiniJavaParser.MethodDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#methodDecl}.
	 * @param ctx the parse tree
	 */
	void exitMethodDecl(MiniJavaParser.MethodDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#methodParam}.
	 * @param ctx the parse tree
	 */
	void enterMethodParam(MiniJavaParser.MethodParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#methodParam}.
	 * @param ctx the parse tree
	 */
	void exitMethodParam(MiniJavaParser.MethodParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MiniJavaParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MiniJavaParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code basicBlock}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBasicBlock(MiniJavaParser.BasicBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code basicBlock}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBasicBlock(MiniJavaParser.BasicBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code printToConsole}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterPrintToConsole(MiniJavaParser.PrintToConsoleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printToConsole}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitPrintToConsole(MiniJavaParser.PrintToConsoleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varDefinition}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterVarDefinition(MiniJavaParser.VarDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varDefinition}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitVarDefinition(MiniJavaParser.VarDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayDefinition}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterArrayDefinition(MiniJavaParser.ArrayDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayDefinition}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitArrayDefinition(MiniJavaParser.ArrayDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileLoopHead}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileLoopHead(MiniJavaParser.WhileLoopHeadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileLoopHead}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileLoopHead(MiniJavaParser.WhileLoopHeadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStatement}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(MiniJavaParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStatement}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(MiniJavaParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code greaterThanExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanExpr(MiniJavaParser.GreaterThanExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code greaterThanExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanExpr(MiniJavaParser.GreaterThanExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrLenExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArrLenExpression(MiniJavaParser.ArrLenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrLenExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArrLenExpression(MiniJavaParser.ArrLenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayAccessExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccessExpression(MiniJavaParser.ArrayAccessExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayAccessExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccessExpression(MiniJavaParser.ArrayAccessExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(MiniJavaParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(MiniJavaParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subtractExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSubtractExpression(MiniJavaParser.SubtractExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subtractExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSubtractExpression(MiniJavaParser.SubtractExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lessThanExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLessThanExpr(MiniJavaParser.LessThanExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lessThanExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLessThanExpr(MiniJavaParser.LessThanExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpr(MiniJavaParser.AtomExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpr(MiniJavaParser.AtomExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code methodCallExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallExpression(MiniJavaParser.MethodCallExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code methodCallExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallExpression(MiniJavaParser.MethodCallExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plusExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPlusExpression(MiniJavaParser.PlusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plusExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPlusExpression(MiniJavaParser.PlusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multiplyExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMultiplyExpression(MiniJavaParser.MultiplyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multiplyExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMultiplyExpression(MiniJavaParser.MultiplyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(MiniJavaParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(MiniJavaParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(MiniJavaParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(MiniJavaParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(MiniJavaParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(MiniJavaParser.IntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code idLiteral}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterIdLiteral(MiniJavaParser.IdLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code idLiteral}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitIdLiteral(MiniJavaParser.IdLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constructorCall}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterConstructorCall(MiniJavaParser.ConstructorCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constructorCall}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitConstructorCall(MiniJavaParser.ConstructorCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code thisCall}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterThisCall(MiniJavaParser.ThisCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code thisCall}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitThisCall(MiniJavaParser.ThisCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integerArr}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterIntegerArr(MiniJavaParser.IntegerArrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integerArr}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitIntegerArr(MiniJavaParser.IntegerArrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanLit}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLit(MiniJavaParser.BooleanLitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanLit}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLit(MiniJavaParser.BooleanLitContext ctx);
}