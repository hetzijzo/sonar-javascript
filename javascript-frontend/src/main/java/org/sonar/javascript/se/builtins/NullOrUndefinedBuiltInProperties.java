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
package org.sonar.javascript.se.builtins;

import java.util.Map;
import org.sonar.javascript.se.Constraint;
import org.sonar.javascript.se.sv.SymbolicValue;

public class NullOrUndefinedBuiltInProperties extends BuiltInProperties {

  private static final String ERROR_MESSAGE = "We should not try to execute properties on 'null' or 'undefined' as it leads to TypeError";

  @Override
  Map<String, Constraint> getPropertiesConstraints() {
    throw new IllegalStateException(ERROR_MESSAGE);
  }

  @Override
  Map<String, SymbolicValue> getMethods() {
    throw new IllegalStateException(ERROR_MESSAGE);
  }

  @Override
  Map<String, Constraint> getOwnPropertiesConstraints() {
    throw new IllegalStateException(ERROR_MESSAGE);
  }

  @Override
  Map<String, SymbolicValue> getOwnMethods() {
    throw new IllegalStateException(ERROR_MESSAGE);
  }

}
