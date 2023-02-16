package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.function.SerializableBiPredicate;
import lombok.Getter;

/**
 * Enables in memory filtering support to column.
 *
 * @param <T>
 */
@Getter
public class EagerFilterableColumn<T> extends FilterableColumn<T> {

  /**
   * filter condition to apply to column values.
   */
  private final SerializableBiPredicate<T, String> filterCondition;

  public EagerFilterableColumn(TwinColumn<T> column, String filterPlaceholder,
      boolean enableClearButton, SerializableBiPredicate<T, String> filterCondition) {
    super(column, filterPlaceholder, enableClearButton);
    this.filterCondition = filterCondition;
  }

}
