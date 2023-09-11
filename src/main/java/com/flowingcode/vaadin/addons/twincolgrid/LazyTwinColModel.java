package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;

/**
 * Model that supports {@link BackEndDataProvider} for {@link TwinColGrid} available grid.
 *
 * @param <T>
 */
class LazyTwinColModel<T> extends TwinColModel<T, LazyFilterableColumn<T>> {

  private final ConfigurableFilterDataProvider<T, Void, LazyFilter<T>> dataProviderWrapper;
  private LazyFilter<T> lazyFilter;

  LazyTwinColModel(@NonNull Grid<T> grid, String className) {
    super(grid, className);

    lazyFilter = new BaseLazyFilter<>();
    dataProviderWrapper = getDataProvider().withConfigurableFilter();
    dataProviderWrapper.setFilter(lazyFilter);
    grid.setDataProvider(dataProviderWrapper);
  }

  @Override
  @SuppressWarnings("unchecked")
  DataProvider<T, LazyFilter<T>> getDataProvider() {
    return (DataProvider<T, LazyFilter<T>>) grid.getDataProvider();
  }

  @Override
  void addAll(Collection<T> items) {
    lazyFilter.getSelectedItems().removeAll(items);
  }

  @Override
  void removeAll(Collection<T> items) {
    lazyFilter.getSelectedItems().addAll(items);
  }

  @Override
  void addFilterableColumn(Column<T> column, LazyFilterableColumn<T> filter) {
    TextField filterField = new TextField();
    filterField.setClearButtonVisible(filter.isEnableClearButton());
    filterField.setValueChangeMode(ValueChangeMode.EAGER);
    filterField.setSizeFull();
    filterField.setPlaceholder(filter.getFilterPlaceholder());
    filterField.addValueChangeListener(
        event -> {
          filter.getLazyFilterField().accept(filterField.getValue());
          getDataProvider().refreshAll();
        });

    if (headerRow == null) {
      setHeaderRow(grid.appendHeaderRow());
    }

    headerRow.getCell(column).setComponent(filterField);
  }

  void addItems(Collection<T> draggedItems,
      T dropOverItem, GridDropLocation dropLocation) {
    if (dropOverItem != null) {
      Collection<T> collection = lazyFilter.getSelectedItems();
      List<T> list = new ArrayList<>(collection);
      int dropIndex = list.indexOf(dropOverItem) + (dropLocation == GridDropLocation.BELOW ? 1 : 0);
      list.addAll(dropIndex, draggedItems);
      lazyFilter.getSelectedItems().clear();
      addAll(list);
    } else {
      addAll(draggedItems);
    }
    grid.getDataProvider().refreshAll();
  }

  <C extends LazyFilter<T>> void setFilter(C filter) {
    lazyFilter = filter;
    dataProviderWrapper.setFilter(filter);
  }

  @Override
  TwinColModelMode getMode() {
    return TwinColModelMode.LAZY;
  }

  @Override
  boolean supportsAddAll() {
    return false;
  }

}
