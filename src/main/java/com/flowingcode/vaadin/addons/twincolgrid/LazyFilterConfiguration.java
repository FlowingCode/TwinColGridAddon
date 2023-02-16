package com.flowingcode.vaadin.addons.twincolgrid;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColModel.TwinColModelMode;
import lombok.RequiredArgsConstructor;

/**
 * Lazy filter configuration for {@link TwinColGrid}.
 *
 * @param <T>
 */
@RequiredArgsConstructor
public class LazyFilterConfiguration<T> extends FilterConfiguration<T, LazyFilterableColumn<T>> {

  private final LazyFilter<T> lazyFilter;

  @SafeVarargs
  public LazyFilterConfiguration(LazyFilter<T> lazyFilter, LazyFilterableColumn<T>... columns) {
    super(columns);
    this.lazyFilter = lazyFilter;
  }

  @Override
  void apply(TwinColGrid<T> grid) {
    filteredColumns.forEach(grid::addFilterableColumn);
    grid.setLazyFilter(lazyFilter);
  }

  @Override
  boolean supports(TwinColModelMode mode) {
    return TwinColModelMode.LAZY.equals(mode);
  }

}
