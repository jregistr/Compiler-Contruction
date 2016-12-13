// Generated from C:/Users/JeffRegistre/class/Compiler-Contruction/phase-2/src/main/java\MiniJava.g4 by ANTLR 4.5.3
package com.compiler.generated.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MiniJavaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MiniJavaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#goal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGoal(MiniJavaParser.GoalContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#mainClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainClass(MiniJavaParser.MainClassContext ctx);
	/**
	 * Visit a parse tree produced by the {@code baseClass}
	 * labeled alternative in {@link MiniJavaParser#classDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseClass(MiniJavaParser.BaseClassContext ctx);
	/**
	 * Visit a parse tree produced by the {@code childClass}
	 * labeled alternative in {@link MiniJavaParser#classDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChildClass(MiniJavaParser.ChildClassContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImmutableVariable}
	 * labeled alternative in {@link MiniJavaParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmutableVariable(MiniJavaParser.ImmutableVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MutableVariable}
	 * labeled alternative in {@link MiniJavaParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMutableVariable(MiniJavaParser.MutableVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImmutableField}
	 * labeled alternative in {@link MiniJavaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImmutableField(MiniJavaParser.ImmutableFieldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MutableField}
	 * labeled alternative in {@link MiniJavaParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMutableField(MiniJavaParser.MutableFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#methodDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDecl(MiniJavaParser.MethodDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#methodParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodParam(MiniJavaParser.MethodParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniJavaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(MiniJavaParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code basicBlock}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicBlock(MiniJavaParser.BasicBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code printToConsole}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintToConsole(MiniJavaParser.PrintToConsoleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varDefinition}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDefinition(MiniJavaParser.VarDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayDefinition}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayDefinition(MiniJavaParser.ArrayDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileLoopHead}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileLoopHead(MiniJavaParser.WhileLoopHeadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStatement}
	 * labeled alternative in {@link MiniJavaParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(MiniJavaParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code greaterThanExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThanExpr(MiniJavaParser.GreaterThanExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrLenExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrLenExpression(MiniJavaParser.ArrLenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayAccessExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayAccessExpression(MiniJavaParser.ArrayAccessExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(MiniJavaParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subtractExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubtractExpression(MiniJavaParser.SubtractExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lessThanExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThanExpr(MiniJavaParser.LessThanExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomExpr(MiniJavaParser.AtomExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code methodCallExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCallExpression(MiniJavaParser.MethodCallExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code plusExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusExpression(MiniJavaParser.PlusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multiplyExpression}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplyExpression(MiniJavaParser.MultiplyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(MiniJavaParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link MiniJavaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(MiniJavaParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intLiteral}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(MiniJavaParser.IntLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code idLiteral}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdLiteral(MiniJavaParser.IdLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constructorCall}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorCall(MiniJavaParser.ConstructorCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code thisCall}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisCall(MiniJavaParser.ThisCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code integerArr}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerArr(MiniJavaParser.IntegerArrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanLit}
	 * labeled alternative in {@link MiniJavaParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLit(MiniJavaParser.BooleanLitContext ctx);
}