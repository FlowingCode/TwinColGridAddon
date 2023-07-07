package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.Collection;
import lombok.NonNull;

abstract class TwinColModel<T, F extends FilterableColumn<T>> implements Serializable {

  enum TwinColModelMode {
    EAGER, LAZY;
  }

  protected final Grid<T> grid;
  private final Label columnLabel = new Label();
  private final VerticalLayout layout;
  protected HeaderRow headerRow;
  private boolean droppedInsideGrid = false;
  private boolean allowReordering = false;
  private Registration moveItemsByDoubleClick;

  TwinColModel(@NonNull Grid<T> grid, String className) {
    this.grid = grid;

    layout = new VerticalLayout(columnLabel, grid);
    layout.setClassName(className);
    grid.setClassName("twincol-grid-items");
    columnLabel.setClassName("twincol-grid-label");
  }

  void init() {
    getGrid().setSelectionMode(SelectionMode.MULTI);
    getColumnLabel().setVisible(false);
    getLayout().setSizeFull();
    getLayout().setMargin(false);
    getLayout().setPadding(false);
    getLayout().setSpacing(false);
  }

  boolean isReorderingEnabled() {
    return allowReordering && grid.getSortOrder().isEmpty();
  }

  Grid<T> getGrid() {
    return grid;
  }

  Label getColumnLabel() {
    return columnLabel;
  }

  boolean isDroppedInsideGrid() {
    return droppedInsideGrid;
  }

  void setDroppedInsideGrid(boolean droppedInsideGrid) {
    this.droppedInsideGrid = droppedInsideGrid;
  }

  void setAllowReordering(boolean allowReordering) {
    this.allowReordering = allowReordering;
  }

  boolean isAllowReordering() {
    return allowReordering;
  }

  HeaderRow getHeaderRow() {
    return headerRow;
  }

  void setHeaderRow(HeaderRow headerRow) {
    this.headerRow = headerRow;
  }

  Registration getMoveItemsByDoubleClick() {
    return moveItemsByDoubleClick;
  }

  Registration addItemDoubleClickListener(
      ComponentEventListener<ItemDoubleClickEvent<T>> listener) {
    moveItemsByDoubleClick = grid.addItemDoubleClickListener(listener);
    return moveItemsByDoubleClick;
  }

  void removeItemDoubleClickListener() {
    if (moveItemsByDoubleClick != null) {
      moveItemsByDoubleClick.remove();
      moveItemsByDoubleClick = null;
    }
  }

  VerticalLayout getLayout() {
    return layout;
  }

  abstract <D extends DataProvider<T, ?>> D getDataProvider();

  abstract void addAll(Collection<T> items);

  abstract void removeAll(Collection<T> items);

  abstract void addFilterableColumn(Column<T> column, F filter);

  abstract void addItems(Collection<T> draggedItems, T dropOverItem, GridDropLocation dropLocation);

  abstract TwinColModelMode getMode();

  abstract boolean supportsAddAll();

}
