package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.SortOrderProvider;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.ValueProvider;
import java.util.Comparator;
import java.util.function.Supplier;

/** Fluent helper object that delegates setters on both columns. */
public class FilterableTwinColumn<T> extends TwinColumn<T> {

  public FilterableTwinColumn(Column<T> availableColumn, Column<T> selectionColumn) {
    super(availableColumn, selectionColumn);
  }

  /**
   * Set the placeholder of the filter text field on both columns.
   *
   * @see TextField#setPlaceholder(String)
   */
  public FilterableTwinColumn<T> setFilterPlaceholder(String filterPlaceholder) {
    TwinColGrid.getFilterTextField(getAvailableColumn()).setPlaceholder(filterPlaceholder);
    TwinColGrid.getFilterTextField(getSelectionColumn()).setPlaceholder(filterPlaceholder);
    return this;
  }

  /**
   * Set to {@code false} to hide the clear button which clears the filter text field.
   *
   * @see TextField#setClearButtonVisible(boolean)
   */
  public FilterableTwinColumn<T> setClearButtonVisible(boolean clearButtonVisible) {
    TwinColGrid.getFilterTextField(getAvailableColumn()).setClearButtonVisible(clearButtonVisible);
    TwinColGrid.getFilterTextField(getSelectionColumn()).setClearButtonVisible(clearButtonVisible);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setWidth(String width) {
    super.setWidth(width);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setFlexGrow(int flexGrow) {
    super.setFlexGrow(flexGrow);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setAutoWidth(boolean autoWidth) {
    super.setAutoWidth(autoWidth);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setKey(String key) {
    super.setKey(key);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setComparator(Comparator<T> comparator) {
    super.setComparator(comparator);
    return this;
  }

  @Override
  public <V extends Comparable<? super V>> TwinColumn<T> setComparator(
      ValueProvider<T, V> keyExtractor) {
    super.setComparator(keyExtractor);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setSortProperty(String... properties) {
    super.setSortProperty(properties);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setSortOrderProvider(SortOrderProvider provider) {
    super.setSortOrderProvider(provider);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setSortable(boolean sortable) {
    super.setSortable(sortable);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setHeader(String labelText) {
    super.setHeader(labelText);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setFooter(String labelText) {
    super.setFooter(labelText);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setHeader(Supplier<Component> footerComponentSupplier) {
    super.setHeader(footerComponentSupplier);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setFooter(Supplier<Component> footerComponentSupplier) {
    super.setFooter(footerComponentSupplier);
    return this;
  }

  @Override
  public FilterableTwinColumn<T> setClassNameGenerator(
      SerializableFunction<T, String> classNameGenerator) {
    super.setClassNameGenerator(classNameGenerator);
    return this;
  }

}
