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
