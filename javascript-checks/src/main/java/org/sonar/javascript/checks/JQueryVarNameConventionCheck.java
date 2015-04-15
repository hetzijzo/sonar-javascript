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
package org.sonar.javascript.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.javascript.ast.resolve.Symbol;
import org.sonar.javascript.ast.resolve.SymbolModel;
import org.sonar.javascript.ast.resolve.Usage;
import org.sonar.javascript.model.interfaces.Tree;
import org.sonar.javascript.model.interfaces.declaration.InitializedBindingElementTree;
import org.sonar.javascript.model.interfaces.declaration.ScriptTree;
import org.sonar.javascript.model.interfaces.expression.AssignmentExpressionTree;
import org.sonar.javascript.model.interfaces.expression.ExpressionTree;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Rule(
    key = "S2713",
    name = "JQuery cache variables should comply with a convention name",
    priority = Priority.MINOR,
    tags = {Tags.JQUERY, Tags.CONVENTION})
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("5min")
public class JQueryVarNameConventionCheck extends AbstractJQueryCheck {

  private static final String MESSAGE = "Rename variable \"%s\" to match the regular expression %s.";

  private static final String DEFAULT_FORMAT = "^\\$[a-z][a-zA-Z0-9]*$";

  @RuleProperty(
      key = "format",
      description = "Regular expression used to check the variable names against",
      defaultValue = "" + DEFAULT_FORMAT)
  private String format = DEFAULT_FORMAT;

  public void setFormat(String format){
    this.format = format;
  }

  @Override
  public void visitScript(ScriptTree tree) {
    Pattern pattern = Pattern.compile(format);
    SymbolModel symbolModel = getContext().getSymbolModel();
    List<Symbol> symbols = symbolModel.getSymbols(Symbol.Kind.VARIABLE);
    for (Symbol symbol : symbols){
      Tree firstJQueryStorage = getJQueryStorage(symbol, symbolModel);
      if (firstJQueryStorage != null && !pattern.matcher(symbol.name()).matches()){
        getContext().addIssue(this, firstJQueryStorage, String.format(MESSAGE, symbol.name(), format));
      }
    }
  }

  @Nullable
  private Tree getJQueryStorage(Symbol symbol, SymbolModel symbolModel) {
    Collection<Usage> usages = symbolModel.getUsageFor(symbol);
    for (Usage usage : usages){
      if (usage.kind().equals(Usage.Kind.WRITE)){
        ExpressionTree expressionTree = null;
        Tree usageTree = usage.usageTree();
        if (usageTree.is(Tree.Kind.ASSIGNMENT)) {
          expressionTree = ((AssignmentExpressionTree) usageTree).expression();
        } else if (usageTree.is(Tree.Kind.INITIALIZED_BINDING_ELEMENT)){
          expressionTree = ((InitializedBindingElementTree)usageTree).right();
        }
        if (expressionTree != null && isSelector(expressionTree)){
          return usageTree;
        }
      }
    }
    return null;
  }

}