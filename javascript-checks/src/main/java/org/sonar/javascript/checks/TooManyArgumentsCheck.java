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

import com.google.common.collect.Lists;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.javascript.checks.utils.CheckUtils;
import org.sonar.javascript.se.Constraint;
import org.sonar.javascript.se.ProgramState;
import org.sonar.javascript.se.SeCheck;
import org.sonar.javascript.se.sv.BuiltInFunctionSymbolicValue;
import org.sonar.javascript.se.sv.SymbolicValue;
import org.sonar.javascript.tree.impl.SeparatedList;
import org.sonar.javascript.tree.symbols.Scope;
import org.sonar.javascript.tree.symbols.type.FunctionType;
import org.sonar.plugins.javascript.api.symbols.Symbol;
import org.sonar.plugins.javascript.api.symbols.SymbolModel;
import org.sonar.plugins.javascript.api.symbols.Type;
import org.sonar.plugins.javascript.api.tree.ScriptTree;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.Tree.Kind;
import org.sonar.plugins.javascript.api.tree.declaration.FunctionTree;
import org.sonar.plugins.javascript.api.tree.expression.CallExpressionTree;
import org.sonar.plugins.javascript.api.tree.expression.DotMemberExpressionTree;
import org.sonar.plugins.javascript.api.tree.expression.ExpressionTree;
import org.sonar.plugins.javascript.api.visitors.Issue;
import org.sonar.plugins.javascript.api.visitors.SubscriptionVisitor;
import org.sonar.plugins.javascript.api.visitors.TreeVisitorContext;

@Rule(key = "S930")
public class TooManyArgumentsCheck extends SeCheck {

  private static final String MESSAGE = "%s expects %s argument%s, but %s %s provided.";
  
  private static final List<String> ignoredBuiltInFunctions = Lists.newArrayList("toString", "toLocaleString");

  /**
   * Expressions for which an issue has already be raised. Used to avoid multiple identical issues due to different branches.
   */
  private Set<CallExpressionTree> withIssue = new HashSet<>();

  @Override
  public void startOfFile(ScriptTree scriptTree) {
    issues.reset();
    withIssue.clear();
  }

  /**
   * Gets the issues for built-in functions.
   */
  @Override
  public void beforeBlockElement(ProgramState currentState, Tree element) {
    if (element.is(Kind.CALL_EXPRESSION)) {
      CallExpressionTree callExpression = (CallExpressionTree) element;
      SeparatedList<Tree> actualArguments = callExpression.arguments().parameters();
      int nbActualArguments = actualArguments.size();

      SymbolicValue calleeValue = currentState.peekStack(actualArguments.size());
      if (nbActualArguments > 0 && calleeValue instanceof BuiltInFunctionSymbolicValue && mustProcess(callExpression)) {
        IntFunction<Constraint> signature = ((BuiltInFunctionSymbolicValue) calleeValue).signature();
        // the following ugly algorithm is specifically tuned for functions with rest arguments (that is, with "..." in the argument list)
        int index = nbActualArguments - 1;
        while (index >= 0) {
          // for a non-existing argument, method "apply" returns null
          if (signature.apply(index) != null) {
            break;
          }
          index--;
        }
        int nbAllowedArguments = index + 1;

        if (nbActualArguments > nbAllowedArguments) {
          String message = getMessage(callExpression, nbAllowedArguments, nbActualArguments);
          addIssue(callExpression.arguments(), message);
          withIssue.add(callExpression);
        }
      }
    }
  }

  @Override
  public List<Issue> scanFile(TreeVisitorContext context) {
    // issues from built-in functions (obtained by symbolic execution)
    List<Issue> allIssues = Lists.newArrayList(issues.getList());

    // issues from user-defined functions (obtained by the visitor)
    CallExpressionGatherer visitor = new CallExpressionGatherer();
    visitor.scanTree(context.getTopTree());
    visitor.callExpressions
      .stream()
      .map(callExpression -> checkCallExpression(callExpression, context.getSymbolModel()))
      .filter(Objects::nonNull)
      .forEach(allIssues::add);

    return allIssues;
  }

  private Issue checkCallExpression(CallExpressionTree tree, SymbolModel symbolModel) {
    FunctionTree functionTree = getFunction(tree);

    if (functionTree != null) {
      int parametersNumber = functionTree.parameterList().size();
      int argumentsNumber = tree.arguments().parameters().size();

      if (!hasRestParameter(functionTree) && !builtInArgumentsUsed(functionTree, symbolModel) && argumentsNumber > parametersNumber) {
        String message = getMessage(tree, parametersNumber, argumentsNumber);
        return addIssue(tree.arguments(), message)
          .secondary(functionTree.parameterClause(), "Formal parameters");
      }
    }
    return null;
  }

  private static String getMessage(CallExpressionTree tree, int parametersNumber, int argumentsNumber) {
    ExpressionTree callee = getCallee(tree); 
    String calleeName;
    if (callee.is(Kind.FUNCTION_EXPRESSION)) {
      calleeName = "This function";
    } else if (callee.is(Kind.DOT_MEMBER_EXPRESSION)) {
      calleeName = "\"" + ((DotMemberExpressionTree)callee).property().name() + "\"";
    } else {
      calleeName = "\"" + CheckUtils.asString(callee) + "\"";
    }
    return String.format(MESSAGE, calleeName, parametersNumber, parametersNumber == 1 ? "" : "s", argumentsNumber, argumentsNumber > 1 ? "were" : "was");
  }

  private static ExpressionTree getCallee(CallExpressionTree callExpression) {
    return CheckUtils.removeParenthesis(callExpression.callee()); 
  }

  private boolean mustProcess(CallExpressionTree callExpression) {
    if (withIssue.contains(callExpression)) {
      return false;
    } else {
      ExpressionTree callee = getCallee(callExpression);
      if (callee.is(Kind.DOT_MEMBER_EXPRESSION)) {
        String functionName = ((DotMemberExpressionTree)callee).property().name();
        return !ignoredBuiltInFunctions.contains(functionName);
      }
      return true;
    }
  }

  /*
   * @return true if function's last parameter has "... p" format and stands for all rest parameters
   */
  private static boolean hasRestParameter(FunctionTree functionTree) {
    List<Tree> parameters = functionTree.parameterList();
    return !parameters.isEmpty() && (parameters.get(parameters.size() - 1).is(Tree.Kind.REST_ELEMENT));
  }

  @Nullable
  private static FunctionTree getFunction(CallExpressionTree tree) {
    Set<Type> types = tree.callee().types();

    if (types.size() == 1 && types.iterator().next().kind().equals(Type.Kind.FUNCTION)) {
      return ((FunctionType) types.iterator().next()).functionTree();
    }

    return null;
  }

  private static boolean builtInArgumentsUsed(FunctionTree tree, SymbolModel symbolModel) {
    Scope scope = symbolModel.getScope(tree);
    if (scope == null) {
      throw new IllegalStateException("No scope found for FunctionTree");
    }

    Symbol argumentsBuiltInVariable = scope.lookupSymbol("arguments");
    if (argumentsBuiltInVariable == null) {
      if (!tree.is(Kind.ARROW_FUNCTION)) {
        throw new IllegalStateException("No 'arguments' symbol found for function scope");
      } else {
        return false;
      }
    }

    boolean isUsed = !argumentsBuiltInVariable.usages().isEmpty();
    return argumentsBuiltInVariable.builtIn() && isUsed;
  }

  /**
   * An object to collect all call expressions.
   * In particular it collects the call expressions located out of any function (that is,
   * in the global scope), which cannot be performed by the symbolic execution engine.
   */
  private static class CallExpressionGatherer extends SubscriptionVisitor {

    Set<CallExpressionTree> callExpressions = new HashSet<>(); 

    @Override
    public List<Kind> nodesToVisit() {
      return Lists.newArrayList(Kind.CALL_EXPRESSION);
    }

    @Override
    public void visitNode(Tree tree) {
      callExpressions.add((CallExpressionTree) tree);
    }

  }

}
