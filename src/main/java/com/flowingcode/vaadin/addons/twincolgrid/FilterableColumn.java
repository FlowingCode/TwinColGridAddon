package com.flowingcode.vaadin.addons.twincolgrid;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class FilterableColumn<T> {

  private final TwinColumn<T> column;
  private final String filterPlaceholder;
  private final boolean enableClearButton;

}
