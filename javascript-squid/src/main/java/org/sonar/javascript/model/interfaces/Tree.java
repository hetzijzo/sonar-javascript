/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011 SonarSource and Eriks Nukis
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.javascript.model.interfaces;

import com.sonar.sslr.api.AstNodeType;
import org.sonar.javascript.model.interfaces.expression.ArrayLiteralTree;
import org.sonar.javascript.model.interfaces.expression.ArrowFunctionTree;
import org.sonar.javascript.model.interfaces.expression.AssignmentExpressionTree;
import org.sonar.javascript.model.interfaces.expression.BinaryExpressionTree;
import org.sonar.javascript.model.interfaces.expression.ExpressionTree;
import org.sonar.javascript.model.interfaces.expression.FunctionExpressionTree;
import org.sonar.javascript.model.interfaces.expression.IdentifierTree;
import org.sonar.javascript.model.interfaces.expression.LiteralTree;
import org.sonar.javascript.model.interfaces.expression.MemberExpressionTree;
import org.sonar.javascript.model.interfaces.expression.NewExpressionTree;
import org.sonar.javascript.model.interfaces.expression.ObjectInitialiserTree;
import org.sonar.javascript.model.interfaces.expression.PairPropertyTree;
import org.sonar.javascript.model.interfaces.expression.ParenthesisedExpressionTree;
import org.sonar.javascript.model.interfaces.expression.RestElementTree;
import org.sonar.javascript.model.interfaces.expression.ThisTree;
import org.sonar.javascript.model.interfaces.expression.UnaryExpressionTree;
import org.sonar.javascript.model.interfaces.expression.YieldExpressionTree;
import org.sonar.javascript.model.interfaces.lexical.SyntaxToken;
import org.sonar.javascript.model.interfaces.lexical.SyntaxTrivia;
import org.sonar.javascript.model.interfaces.statement.BlockTree;
import org.sonar.javascript.model.interfaces.statement.BreakStatementTree;
import org.sonar.javascript.model.interfaces.statement.CaseClauseTree;
import org.sonar.javascript.model.interfaces.statement.CatchBlockTree;
import org.sonar.javascript.model.interfaces.statement.ContinueStatementTree;
import org.sonar.javascript.model.interfaces.statement.DebuggerStatementTree;
import org.sonar.javascript.model.interfaces.statement.DefaultClauseTree;
import org.sonar.javascript.model.interfaces.statement.DoWhileStatementTree;
import org.sonar.javascript.model.interfaces.statement.ElseClauseTree;
import org.sonar.javascript.model.interfaces.statement.EmptyStatementTree;
import org.sonar.javascript.model.interfaces.statement.ExpressionStatementTree;
import org.sonar.javascript.model.interfaces.statement.ForInStatementTree;
import org.sonar.javascript.model.interfaces.statement.ForOfStatementTree;
import org.sonar.javascript.model.interfaces.statement.ForStatementTree;
import org.sonar.javascript.model.interfaces.statement.IfStatementTree;
import org.sonar.javascript.model.interfaces.statement.LabelledStatementTree;
import org.sonar.javascript.model.interfaces.statement.ReturnStatementTree;
import org.sonar.javascript.model.interfaces.statement.SwitchStatementTree;
import org.sonar.javascript.model.interfaces.statement.ThrowStatementTree;
import org.sonar.javascript.model.interfaces.statement.TryStatementTree;
import org.sonar.javascript.model.interfaces.statement.VariableDeclarationTree;
import org.sonar.javascript.model.interfaces.statement.VariableStatementTree;
import org.sonar.javascript.model.interfaces.statement.WhileStatementTree;
import org.sonar.javascript.model.interfaces.statement.WithStatementTree;
import org.sonar.sslr.grammar.GrammarRuleKey;

/**
 * Common interface for all nodes in an abstract syntax tree.
 * <p/>
 * <p>This interface is not intended to be implemented by clients.</p>
 */
public interface Tree {

  boolean is(Kind... kind);

//  void accept(TreeVisitor visitor);

  public enum Kind implements AstNodeType, GrammarRuleKey {

    /**
     * {@link EmptyStatementTree}
     */
    EMPTY_STATEMENT(EmptyStatementTree.class),

    /**
     * {@link DebuggerStatementTree}
     */
    DEBUGGER_STATEMENT(DebuggerStatementTree.class),

    /**
     * {@link VariableStatementTree}
     */
    VARIABLE_STATEMENT(VariableStatementTree.class),

    /**
     * {@link LabelledStatementTree}
     */
    LABELLED_STATEMENT(LabelledStatementTree.class),

    /**
     * {@link ContinueStatementTree}
     */
    CONTINUE_STATEMENT(ContinueStatementTree.class),

    /**
     * {@link BreakStatementTree}
     */
    BREAK_STATEMENT(BreakStatementTree.class),

    /**
     * {@link ReturnStatementTree}
     */
    RETURN_STATEMENT(ReturnStatementTree.class),

    /**
     * {@link ThrowStatementTree}
     */
    THROW_STATEMENT(ThrowStatementTree.class),

    /**
     * {@link WithStatementTree}
     */
    WITH_STATEMENT(WithStatementTree.class),

    /**
     * {@link BlockTree}
     */
    BLOCK(BlockTree.class),

    /**
     * {@link TryStatementTree}
     */
    TRY_STATEMENT(TryStatementTree.class),

    /**
     * {@link CatchBlockTree}
     */
    CATCH_BLOCK(CatchBlockTree.class),

    /**
     * {@link VariableDeclarationTree}
     */
    VARIABLE_DECLARATION(VariableDeclarationTree.class),

    /**
     * {@link SwitchStatementTree}
     */
    SWITCH_STATEMENT(SwitchStatementTree.class),

    /**
     * {@link CaseClauseTree}
     */
    CASE_CLAUSE(CaseClauseTree.class),

    /**
     * {@link DefaultClauseTree}
     */
    DEFAULT_CLAUSE(DefaultClauseTree.class),

    /**
     * {@link IfStatementTree}
     */
    IF_STATEMENT(IfStatementTree.class),

    /**
     * {@link ElseClauseTree}
     */
    ELSE_CLAUSE(ElseClauseTree.class),

    /**
     * {@link WhileStatementTree}
     */
    WHILE_STATEMENT(WhileStatementTree.class),

    /**
     * {@link DoWhileStatementTree}
     */
    DO_WHILE_STATEMENT(DoWhileStatementTree.class),

    /**
     * {@link ExpressionStatementTree}
     */
    EXPRESSION_STATEMENT(ExpressionStatementTree.class),

    /**
     * {@link ForOfStatementTree}
     */
    FOR_OF_STATEMENT(ForOfStatementTree.class),

    /**
     * {@link ForStatementTree}
     */
    FOR_STATEMENT(ForStatementTree.class),

    /**
     * {@link ForInStatementTree}
     */
    FOR_IN_STATEMENT(ForInStatementTree.class),

    /**
     * {@link IdentifierTree}
     */
    IDENTIFIER(IdentifierTree.class),

    /**
     * {@link LiteralTree}
     * {@code null}
     */
    NULL_LITERAL(LiteralTree.class),

    /**
     * {@link LiteralTree}
     * {@code boolean}
     */
    BOOLEAN_LITERAL(LiteralTree.class),

    /**
     * {@link LiteralTree}
     * {@code numeric}
     */
    NUMERIC_LITERAL(LiteralTree.class),

    /**
     * {@link LiteralTree}
     * {@code string}
     */
    STRING_LITERAL(LiteralTree.class),

    /**
     * {@link LiteralTree}
     * {@code regexp}
     */
    REGULAR_EXPRESSION_LITERAL(LiteralTree.class),

    /**
     * {@link ArrayLiteralTree}
     */
    ARRAY_LITERAL(ArrayLiteralTree.class),

    /**
     * {@link ExpressionTree}
     * {@code undefined}
     */
    UNDEFINED(ExpressionTree.class),

    /**
     * {@link ObjectInitialiserTree}
     */
    OBJECT_INITIALISER(ObjectInitialiserTree.class),

    /**
     * {@link PairPropertyTree}
     */
    PAIR_PROPERTY(PairPropertyTree.class),

    /**
     * {@link FunctionExpressionTree}
     * {@code * name () {}}
     */
    METHOD_GENERATOR(FunctionExpressionTree.class),

    /**
     * {@link FunctionExpressionTree}
     * {@code name() {}}
     */
    METHOD(FunctionExpressionTree.class),

    /**
     * {@link FunctionExpressionTree}
     * {@code set name (val) {}}
     */
    METHOD_SETTER(FunctionExpressionTree.class),

    /**
     * {@link FunctionExpressionTree}
     * {@code get name () {}}
     */
    METHOD_GETTER(FunctionExpressionTree.class),

    /**
     * {@link FunctionExpressionTree}
     * {@code function * () {}}
     */
    GENERATOR_FUNCTION_EXPRESSION(FunctionExpressionTree.class),

    /**
     * {@link FunctionExpressionTree}
     * {@code function * () {}}
     */
    FUNCTION_EXPRESSION(FunctionExpressionTree.class),

    /**
     * {@link ArrowFunctionTree}
     */
    ARROW_FUNCTION(ArrowFunctionTree.class),


    /**
     * {@link ParenthesisedExpressionTree}
     */
    PARENTHESISED_EXPRESSION(ParenthesisedExpressionTree.class),

    /**
     * {@link ThisTree}
     * {@code this}
     */
    THIS(ThisTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code ++}
     */
    POSTFIX_INCREMENT(UnaryExpressionTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code --}
     */
    POSTFIX_DECREMENT(UnaryExpressionTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code ++}
     */
    PREFIX_INCREMENT(UnaryExpressionTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code --}
     */
    PREFIX_DECREMENT(UnaryExpressionTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code +}
     */
    UNARY_PLUS(UnaryExpressionTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code -}
     */
    UNARY_MINUS(UnaryExpressionTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code ~}
     */
    BITWISE_COMPLEMENT(UnaryExpressionTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code !}
     */
    LOGICAL_COMPLEMENT(UnaryExpressionTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code delete!}
     */
    DELETE(UnaryExpressionTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code void}
     */
    VOID(UnaryExpressionTree.class),

    /**
     * {@link UnaryExpressionTree}
     * {@code typeof}
     */

    /**
     * {@link BinaryExpressionTree}
     * {@code *}
     */
    MULTIPLY(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code /}
     */
    DIVIDE(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code %}
     */
    REMAINDER(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code +}
     */
    PLUS(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code -}
     */
    MINUS(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code <<}
     */
    LEFT_SHIFT(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code >>}
     */
    RIGHT_SHIFT(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code >>>}
     */
    UNSIGNED_RIGHT_SHIFT(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code <}
     */
    LESS_THAN(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code >}
     */
    GREATER_THAN(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code <=}
     */
    LESS_THAN_OR_EQUAL_TO(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code >=}
     */
    GREATER_THAN_OR_EQUAL_TO(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code ==}
     */
    EQUAL_TO(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code ==}
     */
    STRICT_EQUAL_TO(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code !=}
     */
    NOT_EQUAL_TO(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code !==}
     */
    STRICT_NOT_EQUAL_TO(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code &}
     */
    AND(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code ^}
     */
    XOR(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code |}
     */
    OR(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code &&}
     */
    CONDITIONAL_AND(BinaryExpressionTree.class),

    /**
     * {@link BinaryExpressionTree}
     * {@code ||}
     */
    CONDITIONAL_OR(BinaryExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code =}
     */
    ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code *=}
     */
    MULTIPLY_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code /=}
     */
    DIVIDE_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code %=}
     */
    REMAINDER_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code +=}
     */
    PLUS_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code -=}
     */
    MINUS_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code <<=}
     */
    LEFT_SHIFT_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code >>=}
     */
    RIGHT_SHIFT_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code >>>=}
     */
    UNSIGNED_RIGHT_SHIFT_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code &=}
     */
    AND_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code ^=}
     */
    XOR_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link AssignmentExpressionTree}
     * {@code |=}
     */
    OR_ASSIGNMENT(AssignmentExpressionTree.class),

    /**
     * {@link NewExpressionTree}
     * {@code new expression}
     */
    NEW_EXPRESSION(NewExpressionTree.class),

    /**
     * {@link NewExpressionTree}
     * {@code new super}
     */
    NEW_SUPER(NewExpressionTree.class),

    /**
     * {@link MemberExpressionTree}
     */
    DOT_MEMBER_EXPRESSION(MemberExpressionTree.class),

    /**
     * {@link MemberExpressionTree}
     */
    BRACKET_MEMBER_EXPRESSION(MemberExpressionTree.class),

    /**
     * {@link YieldExpressionTree}
     */
    YIELD_EXPRESSION(YieldExpressionTree.class),

    /**
     * {@link RestElementTree}
     */
    REST_ELEMENT(RestElementTree.class),

    /**
     * {@link ExpressionTree}
     */
    TEMPORARY_EXPRESSION(ExpressionTree.class),

    TYPEOF(UnaryExpressionTree.class),

    TOKEN(SyntaxToken.class),

    TRIVIA(SyntaxTrivia.class);

    final Class<? extends Tree> associatedInterface;

    private Kind(Class<? extends Tree> associatedInterface) {
      this.associatedInterface = associatedInterface;
    }

    public Class<? extends Tree> getAssociatedInterface() {
      return associatedInterface;
    }
  }

}