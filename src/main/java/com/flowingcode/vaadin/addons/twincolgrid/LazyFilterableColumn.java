package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.function.SerializableBiPredicate;
import com.vaadin.flow.function.SerializableConsumer;
import lombok.Getter;

/**
 * Enables lazy filtering support to column.
 *
 * @param <T>
 */
@Getter
public class LazyFilterableColumn<T> extends FilterableColumn<T> {

  /**
   * filter bean field to store the query string.
   */
  private final SerializableConsumer<String> lazyFilterField;

  /**
   * filter condition to apply to column values in selection grid.
   */
  private final SerializableBiPredicate<T, String> eagerFilterCondition;

  public LazyFilterableColumn(TwinColumn<T> column, String filterPlaceholder,
      boolean enableClearButton, SerializableConsumer<String> lazyFilterField,
      SerializableBiPredicate<T, String> eagerFilterCondition) {
    super(column, filterPlaceholder, enableClearButton);
    this.lazyFilterField = lazyFilterField;
    this.eagerFilterCondition = eagerFilterCondition;
  }

  public EagerFilterableColumn<T> asEager() {
    return new EagerFilterableColumn<>(super.getColumn(), super.getFilterPlaceholder(),
        super.isEnableClearButton(), eagerFilterCondition);
  }

}
