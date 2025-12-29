/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2025 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.select.Select;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Binary compatibility utils for Vaadin 14-24 */
public class CompatibilityExtension {

  private static final Method setItems = lookupSetItems();

  private static Method lookupSetItems() {
    try {
      return Select.class.getMethod("setItems", Object[].class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> void setItems(Select<T> select, T[] items) {    
    try {
      setItems.invoke(select, (Object) items);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

}
