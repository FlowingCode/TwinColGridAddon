package com.flowingcode.vaadin.addons.twincolgrid;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColModel.TwinColModelMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class FilterConfiguration<T, C extends FilterableColumn<T>> {

  protected final Collection<C> filteredColumns = new ArrayList<>();

  @SafeVarargs
  protected FilterConfiguration(C... columns) {
    filteredColumns.addAll(Arrays.asList(columns));
  }

  public void addFilteredColumn(C column) {
    filteredColumns.add(column);
  }

  /**
   * Applies this {@link FilterConfiguration} to grid.
   * 
   * @param grid
   */
  abstract void apply(TwinColGrid<T> grid);

  /**
   * Checks if {@link FilterConfiguration} supports the given {@link TwinColModelMode}
   * 
   * @param mode mode to check.
   * @return true mode is supported.
   */
  abstract boolean supports(TwinColModelMode mode);

}
