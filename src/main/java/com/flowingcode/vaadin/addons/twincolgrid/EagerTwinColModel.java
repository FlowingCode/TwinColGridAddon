package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.InMemoryDataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;

/**
 * Model that supports {@link InMemoryDataProvider} for {@link TwinColGrid} available and selection
 * grids.
 *
 * @param <T>
 */
class EagerTwinColModel<T> extends TwinColModel<T, EagerFilterableColumn<T>> {

  EagerTwinColModel(@NonNull Grid<T> grid, String className) {
    super(grid, className);
    this.grid.setDataProvider(DataProvider.ofCollection(new ArrayList<>()));
  }

  @Override
  @SuppressWarnings("unchecked")
  ListDataProvider<T> getDataProvider() {
    return (ListDataProvider<T>) grid.getDataProvider();
  }

  @Override
  void addAll(Collection<T> items) {
    getDataProvider().getItems().addAll(items);
  }

  @Override
  void removeAll(Collection<T> items) {
    getDataProvider().getItems().removeAll(items);
  }

  @Override
  void addFilterableColumn(Column<T> column, EagerFilterableColumn<T> filter) {
    TextField filterField = new TextField();
    filterField.setClearButtonVisible(filter.isEnableClearButton());
    filterField.setValueChangeMode(ValueChangeMode.EAGER);
    filterField.setSizeFull();
    filterField.setPlaceholder(filter.getFilterPlaceholder());
    filterField.addValueChangeListener(
        event -> getDataProvider()
            .addFilter(item -> filter.getFilterCondition().test(item, filterField.getValue())));

    if (headerRow == null) {
      setHeaderRow(grid.appendHeaderRow());
    }

    headerRow.getCell(column).setComponent(filterField);
  }

  void clear() {
    getDataProvider().getItems().clear();
  }

  void addItems(Collection<T> draggedItems, T dropOverItem, GridDropLocation dropLocation) {
    if (dropOverItem != null) {
      Collection<T> collection = getDataProvider().getItems();
      List<T> list = new ArrayList<>(collection);
      int dropIndex = list.indexOf(dropOverItem) + (dropLocation == GridDropLocation.BELOW ? 1 : 0);
      list.addAll(dropIndex, draggedItems);
      getDataProvider().getItems().clear();
      addAll(list);
    } else {
      addAll(draggedItems);
    }
    getDataProvider().refreshAll();
  }

  Collection<T> getItems() {
    return getDataProvider().getItems();
  }

  @Override
  TwinColModelMode getMode() {
    return TwinColModelMode.EAGER;
  }

  @Override
  boolean supportsAddAll() {
    return true;
  }

}
