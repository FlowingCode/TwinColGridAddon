package com.flowingcode.vaadin.addons.twincolgrid;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColModel.TwinColModelMode;

/**
 * Eager filter configuration for {@link TwinColGrid}.
 *
 * @param <T>
 */
public class EagerFilterConfiguration<T> extends FilterConfiguration<T, EagerFilterableColumn<T>> {

  @SafeVarargs
  public EagerFilterConfiguration(EagerFilterableColumn<T>... columns) {
    super(columns);
  }

  void apply(TwinColGrid<T> grid) {
    filteredColumns.forEach(grid::addFilterableColumn);
  }

  @Override
  boolean supports(TwinColModelMode mode) {
    return TwinColModelMode.EAGER.equals(mode);
  }

}
