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
import org.sonar.javascript.se.sv.SymbolicValue;
import org.sonar.javascript.tree.symbols.Scope;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.Tree.Kind;
import org.sonar.plugins.javascript.api.tree.expression.BinaryExpressionTree;

@Rule(key = "S3785")
public class InOperatorTypeErrorCheck extends SeCheck {

  private static final String MESSAGE = "TypeError can be thrown as this operand might have primitive type.";

  private Set<Tree> hasIssue = new HashSet<>();

  @Override
  public void startOfExecution(Scope functionScope) {
    hasIssue.clear();
  }

  @Override
  public void beforeBlockElement(ProgramState currentState, Tree element) {
    if (element.is(Kind.RELATIONAL_IN)) {
      SymbolicValue rightOperandValue = currentState.peekStack();
      Constraint rightOperandConstraint = currentState.getConstraint(rightOperandValue);
      if (rightOperandConstraint.isIncompatibleWith(Constraint.OBJECT) && !hasIssue.contains(element)) {
        BinaryExpressionTree inOperation = (BinaryExpressionTree) element;
        addIssue(inOperation.rightOperand(), MESSAGE)
          .secondary(inOperation.operator());

        hasIssue.add(element);
      }
    }
  }
}
