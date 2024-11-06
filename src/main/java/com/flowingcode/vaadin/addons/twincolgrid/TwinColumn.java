package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.SortOrderProvider;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.ValueProvider;
import java.util.Comparator;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Fluent helper object that delegates setters on both columns. */
@RequiredArgsConstructor
public class TwinColumn<T> {

  /**
   * Returns the column in the grid with the available items.
   *
   * @return The column in the grid with the available items.
   */
  @Getter
  private final Column<T> availableColumn;

  /**
   * Returns the column in the grid with the selected items.
   *
   * @return The column in the grid with the selected items.
   */
  @Getter
  private final Column<T> selectionColumn;

  /**
   * Sets the width of the columns as a CSS-string.
   *
   * @see Column#setWidth(String)
   *
   * @param width the width to set both columns to, as a CSS-string, not {@code null}
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setWidth(String width) {
    availableColumn.setWidth(width);
    selectionColumn.setWidth(width);
    return this;
  }

  /**
   * Sets the flex grow ratio for the columns. When set to 0, column width is fixed.
   *
   * @see Column#setFlexGrow(int)
   *
   * @param flexGrow the flex grow ratio
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setFlexGrow(int flexGrow) {
    availableColumn.setFlexGrow(flexGrow);
    selectionColumn.setFlexGrow(flexGrow);
    return this;
  }

  /**
   * Enables or disables automatic width for the columns.
   *
   * @see Column#setAutoWidth(boolean)
   *
   * @param autoWidth whether to enable or disable automatic width on both columns
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setAutoWidth(boolean autoWidth) {
    availableColumn.setAutoWidth(autoWidth);
    selectionColumn.setAutoWidth(autoWidth);
    return this;
  }

  /**
   * Sets the user-defined identifier to map the columns.
   *
   * @see Column#setKey(String)
   *
   * @param key the identifier key, can't be {@code null}
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setKey(String key) {
    availableColumn.setKey(key);
    selectionColumn.setKey(key);
    return this;
  }

  /**
   * Sets a comparator to use with in-memory sorting with both columns.
   *
   * @see Column#setComparator(Comparator)
   *
   * @param comparator the comparator to use when sorting data in both columns
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setComparator(Comparator<T> comparator) {
    availableColumn.setComparator(comparator);
    selectionColumn.setComparator(comparator);
    return this;
  }

  /**
   * Sets a comparator to use with in-memory sorting with both columns based on the return type of
   * the given {@link ValueProvider}.
   *
   * @see Column#setComparator(ValueProvider)
   *
   * @param <V> the value of the column
   * @param keyExtractor the value provider used to extract the {@link Comparable} sort key
   * @return this instance, for method chaining
   * @see Comparator#comparing(java.util.function.Function)
   */
  public <V extends Comparable<? super V>> TwinColumn<T> setComparator(
      ValueProvider<T, V> keyExtractor) {
    availableColumn.setComparator(keyExtractor);
    selectionColumn.setComparator(keyExtractor);
    return this;
  }

  /**
   * Sets strings describing back end properties to be used when sorting the columns.
   *
   * @see Column#setSortProperty(String...)
   *
   * @param properties the array of strings describing backend properties
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setSortProperty(String... properties) {
    availableColumn.setSortProperty(properties);
    selectionColumn.setSortProperty(properties);
    return this;
  }

  /**
   * Sets the sort orders when sorting the columns. The sort order provider is a function which
   * provides {@link QuerySortOrder} objects to describe how to sort by the columns.
   *
   * @see Column#setSortOrderProvider(SortOrderProvider)
   *
   * @param provider the function to use when generating sort orders with the given direction
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setSortOrderProvider(SortOrderProvider provider) {
    availableColumn.setSortOrderProvider(provider);
    selectionColumn.setSortOrderProvider(provider);
    return this;
  }

  /**
   * Sets whether the user can sort the columns or not.
   *
   * @see Column#setSortable(boolean)
   *
   * @param sortable {@code true} if the columns can be sorted by the user; {@code false} if not
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setSortable(boolean sortable) {
    availableColumn.setSortable(sortable);
    selectionColumn.setSortable(sortable);
    return this;
  }

  /**
   * Sets a header text to both columns.
   *
   * @see Column#setHeader(String)
   *
   * @param labelText the text to be shown at the columns headers
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setHeader(String labelText) {
    availableColumn.setHeader(labelText);
    selectionColumn.setHeader(labelText);
    return this;
  }

  /**
   * Sets a footer text to both columns.
   *
   * @see Column#setFooter(String)
   *
   * @param labelText the text to be shown at the columns footers
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setFooter(String labelText) {
    availableColumn.setFooter(labelText);
    selectionColumn.setFooter(labelText);
    return this;
  }

  /**
   * Sets a header component to both columns.
   *
   * @see Column#setHeader(String)
   *
   * @param headerComponentSupplier a supplier that instantiates the component to be used in the
   *        header of each column
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setHeader(Supplier<Component> headerComponentSupplier) {
    availableColumn.setHeader(headerComponentSupplier.get());
    selectionColumn.setHeader(headerComponentSupplier.get());
    return this;
  }

  /**
   * Sets a footer component to both columns.
   *
   * @see Column#setFooter(String)
   *
   * @param footerComponentSupplier a supplier that instantiates the component to be used in the
   *        footer of each column
   * @return this instance, for method chaining
   */
  public TwinColumn<T> setFooter(Supplier<Component> footerComponentSupplier) {
    availableColumn.setFooter(footerComponentSupplier.get());
    selectionColumn.setFooter(footerComponentSupplier.get());
    return this;
  }

  /**
   * Sets the function that is used for generating CSS class names for cells in both columns.
   *
   * @see Column#setClassNameGenerator(SerializableFunction)
   *
   * @param classNameGenerator the class name generator to set, not {@code null}
   * @return this instance, for method chaining
   * @throws NullPointerException if {@code classNameGenerator} is {@code null}
   */
  public TwinColumn<T> setClassNameGenerator(SerializableFunction<T, String> classNameGenerator) {
    availableColumn.setClassNameGenerator(classNameGenerator);
    selectionColumn.setClassNameGenerator(classNameGenerator);
    return this;
  }

}
