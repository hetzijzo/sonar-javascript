/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.javascript.checks;

import java.util.HashSet;
import java.util.Set;
import org.sonar.check.Rule;
import org.sonar.javascript.se.Constraint;
import org.sonar.javascript.se.ProgramState;
import org.sonar.javascript.se.SeCheck;
import org.sonar.javascript.tree.symbols.Scope;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.expression.BinaryExpressionTree;
import org.sonar.plugins.javascript.api.tree.expression.ExpressionTree;

@Rule(key = "S3758")
public class ValuesNotConvertibleToNumbersCheck extends SeCheck {

  private static final String MESSAGE = "Re-evaluate the data flow; this operand could be \"%s\" here.";

  private static final Constraint CONVERTIBLE_TO_NUMBER = Constraint.ANY_NUMBER.or(Constraint.ANY_BOOLEAN).or(Constraint.DATE).or(Constraint.NULL);

  /**
   * The operands for which an issue has already been raised.
   */
  private Set<ExpressionTree> operandsWithIssues = new HashSet<>();

  @Override
  public void startOfExecution(Scope functionScope) {
    operandsWithIssues.clear();
  }

  @Override
  public void beforeBlockElement(ProgramState currentState, Tree element) {
    if (element.is(
      Tree.Kind.LESS_THAN,
      Tree.Kind.LESS_THAN_OR_EQUAL_TO,
      Tree.Kind.GREATER_THAN,
      Tree.Kind.GREATER_THAN_OR_EQUAL_TO
    )) {
      check(currentState, (BinaryExpressionTree) element);
    }
  }

  private void check(ProgramState state, BinaryExpressionTree element) {
    Constraint leftConstraint = state.getConstraint(state.peekStack(1));
    Constraint rightConstraint = state.getConstraint(state.peekStack(0));

    boolean leftIsUndefined = leftConstraint.isStricterOrEqualTo(Constraint.UNDEFINED); 
    boolean rightIsUndefined = rightConstraint.isStricterOrEqualTo(Constraint.UNDEFINED); 

    if (checkObjectIsComparedNumerically(leftConstraint, rightConstraint)) {
      raiseIssue(element, true, false, "Object");
    } else if (checkObjectIsComparedNumerically(rightConstraint, leftConstraint)) {
      raiseIssue(element, false, true, "Object");
    } else if (leftIsUndefined || rightIsUndefined) {
      raiseIssue(element, leftIsUndefined, rightIsUndefined, "undefined");
    }
  }
  
  private static boolean checkObjectIsComparedNumerically(Constraint constraint1, Constraint constraint2) {
    return isObjectNotConvertibleToNumber(constraint1)
      && isConvertibleToNumber(constraint2);
  }
  
  private static boolean isObjectNotConvertibleToNumber(Constraint c) {
    return c.isStricterOrEqualTo(Constraint.OBJECT) 
       && !c.isStricterOrEqualTo(Constraint.BOOLEAN_OBJECT)
       && !c.isStricterOrEqualTo(Constraint.NUMBER_OBJECT)
       && !c.isStricterOrEqualTo(Constraint.DATE);
  }
  
  private static boolean isConvertibleToNumber(Constraint c) {
    return c.isStricterOrEqualTo(CONVERTIBLE_TO_NUMBER);
  }
  
  private void raiseIssue(
    BinaryExpressionTree comparison,
    boolean raiseIssueForLeft,
    boolean raiseIssueForRight,
    String messageParam
  ) {
    if (raiseIssueForLeft) {
      raiseIssue(comparison.leftOperand(), messageParam);
    }
    if (raiseIssueForRight) {
      raiseIssue(comparison.rightOperand(), messageParam);
    }
  }

  /**
   * Raises an issue (but not twice for the same operand).
   */
  private void raiseIssue(ExpressionTree operand, String messageParam) {
    if (!operandsWithIssues.contains(operand)) {
      operandsWithIssues.add(operand);
      addIssue(operand, String.format(MESSAGE, messageParam));
    }
  }

}
