/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2021 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridNoneSelectionModel;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@CssImport(value = "./styles/multiselect-cb-hide.css", themeFor = "vaadin-grid")
public class TwinColGrid<T> extends VerticalLayout
    implements HasValue<ValueChangeEvent<Set<T>>, Set<T>>, HasComponents, HasSize {

  private static final class TwinColModel<T> implements Serializable {
    final Grid<T> grid = new Grid<>();
    final Label columnLabel = new Label();
    final VerticalLayout layout = new VerticalLayout(columnLabel, grid);
    HeaderRow headerRow;
    boolean droppedInsideGrid = false;

    @SuppressWarnings("unchecked")
    ListDataProvider<T> getDataProvider() {
      return (ListDataProvider<T>) grid.getDataProvider();
    }

    Collection<T> getItems() {
      return getDataProvider().getItems();
    }
  }

  private final TwinColModel<T> left;

  private final TwinColModel<T> right;

  /** @deprecated Use getLeftGrid() */
  @Deprecated protected final Grid<T> leftGrid;

  /** @deprecated Use getRightGrid() */
  @Deprecated protected final Grid<T> rightGrid;

  /** @deprecated Use getLeftGrid().getDataProvider() */
  @Deprecated protected ListDataProvider<T> leftGridDataProvider;

  /** @deprecated Use getRightGrid().getDataProvider() */
  @Deprecated protected ListDataProvider<T> rightGridDataProvider;

  private final Button addAllButton = new Button();

  private final Button addButton = new Button();

  private final Button removeButton = new Button();

  private final Button removeAllButton = new Button();

  private final VerticalLayout buttonContainer;

  private Grid<T> draggedGrid;

  private Label fakeButtonContainerLabel = new Label();

  /** Constructs a new TwinColGrid with an empty {@link ListDataProvider}. */
  public TwinColGrid() {
    this(DataProvider.ofCollection(new LinkedHashSet<>()), null);
  }

  /**
   * Constructs a new TwinColGrid with data provider for options.
   *
   * @param dataProvider the data provider, not {@code null}
   * @param caption the component caption
   */
  public TwinColGrid(final ListDataProvider<T> dataProvider, String caption) {
    left = new TwinColModel<>();
    right = new TwinColModel<>();
    leftGrid = left.grid;
    rightGrid = right.grid;

    setMargin(false);
    setPadding(false);
    if (caption != null) {
      add(new Label(caption));
    }

    setDataProvider(dataProvider);

    rightGridDataProvider = DataProvider.ofCollection(new LinkedHashSet<>());
    getRightGrid().setDataProvider(rightGridDataProvider);

    addButton.setIcon(VaadinIcon.ANGLE_RIGHT.create());
    addButton.setWidth("3em");
    addAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_RIGHT.create());
    addAllButton.setWidth("3em");
    removeButton.setIcon(VaadinIcon.ANGLE_LEFT.create());
    removeButton.setWidth("3em");
    removeAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_LEFT.create());
    removeAllButton.setWidth("3em");

    fakeButtonContainerLabel.getElement().setProperty("innerHTML", "&nbsp;");
    fakeButtonContainerLabel.setVisible(false);
    buttonContainer =
        new VerticalLayout(
            fakeButtonContainerLabel, addAllButton, addButton, removeButton, removeAllButton);
    buttonContainer.setPadding(false);
    buttonContainer.setSpacing(false);
    buttonContainer.setSizeUndefined();

    getLeftGrid().setWidth("100%");
    getRightGrid().setWidth("100%");

    addAllButton.addClickListener(
        e -> {
          left.getItems().stream().forEach(getLeftGrid().getSelectionModel()::select);
          updateSelection(new LinkedHashSet<>(getLeftGrid().getSelectedItems()), new HashSet<>());
        });

    addButton.addClickListener(
        e ->
            updateSelection(
                new LinkedHashSet<>(getLeftGrid().getSelectedItems()), new HashSet<>()));

    removeButton.addClickListener(
        e -> updateSelection(new HashSet<>(), getRightGrid().getSelectedItems()));

    removeAllButton.addClickListener(
        e -> {
          right.getItems().stream().forEach(getRightGrid().getSelectionModel()::select);
          updateSelection(new HashSet<>(), getRightGrid().getSelectedItems());
        });

    getElement().getStyle().set("display", "flex");

    forEachSide(
        side -> {
          side.grid.setSelectionMode(SelectionMode.MULTI);
          side.columnLabel.setVisible(false);
          side.layout.setSizeFull();
          side.layout.setMargin(false);
          side.layout.setPadding(false);
          side.layout.setSpacing(false);
        });

    HorizontalLayout hl = new HorizontalLayout(left.layout, buttonContainer, right.layout);
    hl.getElement().getStyle().set("min-height", "0px");
    hl.getElement().getStyle().set("flex", "1 1 0px");
    hl.setMargin(false);
    hl.setWidthFull();
    add(hl);
    setSizeUndefined();
  }

  /** Return the left grid component */
  public Grid<T> getLeftGrid() {
    return leftGrid;
  }

  /** Return the right grid component */
  public Grid<T> getRightGrid() {
    return rightGrid;
  }

  private void forEachSide(Consumer<TwinColModel<T>> consumer) {
    consumer.accept(left);
    consumer.accept(right);
  }

  public void setItems(Collection<T> items) {
    setDataProvider(DataProvider.ofCollection(items));
  }

  public void setItems(Stream<T> items) {
    setDataProvider(DataProvider.fromStream(items));
  }

  /** @deprecated Use {@code getLeftGrid.getDataProvider()} */
  @Deprecated
  public void setLeftGridClassName(String classname) {
    getLeftGrid().setClassName(classname);
  }

  /** @deprecated Use {@code getLeftGrid().addClassName(classname)} */
  @Deprecated
  public void addLeftGridClassName(String classname) {
    getLeftGrid().addClassName(classname);
  }

  /** @deprecated Use {@code getLeftGrid().removeClassName(classname)} */
  @Deprecated
  public void removeLeftGridClassName(String classname) {
    getLeftGrid().removeClassName(classname);
  }

  /** @deprecated Use {@code getRightGrid().setClassName(classname)} */
  @Deprecated
  public void setRightGridClassName(String classname) {
    getRightGrid().setClassName(classname);
  }

  /** @deprecated Use {@code getRightGrid().addClassName(classname)} */
  @Deprecated
  public void addRightGridClassName(String classname) {
    getRightGrid().addClassName(classname);
  }

  /** @deprecated Use {@code getRightGrid().removeClassName(classname)} */
  @Deprecated
  public void removeRightGridClassName(String classname) {
    getRightGrid().removeClassName(classname);
  }

  private void setDataProvider(ListDataProvider<T> dataProvider) {
    leftGridDataProvider = dataProvider;
    getLeftGrid().setDataProvider(dataProvider);
    if (right.getDataProvider() != null) {
      right.getItems().clear();
      right.getDataProvider().refreshAll();
    }
  }

  /**
   * Constructs a new TwinColGrid with the given options.
   *
   * @param options the options, cannot be {@code null}
   */
  public TwinColGrid(final Collection<T> options) {
    this(DataProvider.ofCollection(new LinkedHashSet<>(options)), null);
  }

  /**
   * Constructs a new TwinColGrid with caption and the given options.
   *
   * @param caption the caption to set, can be {@code null}
   * @param options the options, cannot be {@code null}
   */
  public TwinColGrid(final Collection<T> options, final String caption) {
    this(DataProvider.ofCollection(new LinkedHashSet<>(options)), caption);
  }

  /**
   * Sets the text shown above the right column. {@code null} clears the caption.
   *
   * @param rightColumnCaption The text to show, {@code null} to clear
   * @return this instance
   */
  public TwinColGrid<T> withRightColumnCaption(final String rightColumnCaption) {
    right.columnLabel.setText(rightColumnCaption);
    right.columnLabel.setVisible(true);
    fakeButtonContainerLabel.setVisible(true);
    return this;
  }

  /**
   * Sets the text shown above the left column. {@code null} clears the caption.
   *
   * @param leftColumnCaption The text to show, {@code null} to clear
   * @return this instance
   */
  public TwinColGrid<T> withLeftColumnCaption(final String leftColumnCaption) {
    left.columnLabel.setText(leftColumnCaption);
    left.columnLabel.setVisible(true);
    fakeButtonContainerLabel.setVisible(true);
    return this;
  }

  /**
   * Adds a new text column to this {@link Grid} with a value provider. The column will use a {@link
   * TextRenderer}. The value is converted to a String using the provided {@code
   * itemLabelGenerator}.
   *
   * @param itemLabelGenerator the value provider
   * @param header the column header
   * @return this instance
   */
  public TwinColGrid<T> addColumn(
      final ItemLabelGenerator<T> itemLabelGenerator, final String header) {
    getLeftGrid().addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header);
    getRightGrid().addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header);
    return this;
  }

  /**
   * Adds a new sortable text column to this {@link Grid} with a value provider. The column will use
   * a {@link TextRenderer}. The value is converted to a String using the provided {@code
   * itemLabelGenerator}.
   *
   * @param itemLabelGenerator the value provider
   * @param comparator the in-memory comparator
   * @param header the column header
   * @return this instance
   */
  public TwinColGrid<T> addSortableColumn(
      final ItemLabelGenerator<T> itemLabelGenerator,
      Comparator<T> comparator,
      final String header) {
    forEachSide(
        side ->
            side.grid
                .addColumn(new TextRenderer<>(itemLabelGenerator))
                .setHeader(header)
                .setComparator(comparator)
                .setSortable(true));
    return this;
  }

  /**
   * Adds a new sortable text column to this {@link Grid} with a value provider. The column will use
   * a {@link TextRenderer}. The value is converted to a String using the provided {@code
   * itemLabelGenerator}.
   *
   * @param itemLabelGenerator the value provider
   * @param comparator the in-memory comparator
   * @param header the column header
   * @param header the column key
   * @return this instance
   */
  public TwinColGrid<T> addSortableColumn(
      final ItemLabelGenerator<T> itemLabelGenerator,
      Comparator<T> comparator,
      final String header,
      final String key) {
    forEachSide(
        side ->
            side.grid
                .addColumn(new TextRenderer<>(itemLabelGenerator))
                .setHeader(header)
                .setComparator(comparator)
                .setSortable(true)
                .setKey(key));
    return this;
  }

  public TwinColGrid<T> withoutAddAllButton() {
    addAllButton.setVisible(false);
    checkContainerVisibility();
    return this;
  }

  public TwinColGrid<T> withoutRemoveAllButton() {
    removeAllButton.setVisible(false);
    checkContainerVisibility();
    return this;
  }

  public TwinColGrid<T> withoutAddButton() {
    addButton.setVisible(false);
    checkContainerVisibility();
    return this;
  }

  public TwinColGrid<T> withoutRemoveButton() {
    removeButton.setVisible(false);
    checkContainerVisibility();
    return this;
  }

  private void checkContainerVisibility() {
    boolean atLeastOneIsVisible =
        removeButton.isVisible()
            || addButton.isVisible()
            || removeAllButton.isVisible()
            || addAllButton.isVisible();
    buttonContainer.setVisible(atLeastOneIsVisible);
  }

  public TwinColGrid<T> withSizeFull() {
    setSizeFull();
    return this;
  }

  /**
   * Adds drag n drop support between grids.
   *
   * @return this instance
   */
  public TwinColGrid<T> withDragAndDropSupport() {
    configDragAndDrop(left, right);
    configDragAndDrop(right, left);
    return this;
  }

  /**
   * Returns the text shown above the right column.
   *
   * @return The text shown or {@code null} if not set.
   */
  public String getRightColumnCaption() {
    return right.columnLabel.getText();
  }

  /**
   * Returns the text shown above the left column.
   *
   * @return The text shown or {@code null} if not set.
   */
  public String getLeftColumnCaption() {
    return left.columnLabel.getText();
  }

  /**
   * Set {@code value} to grid
   *
   * @param value the value, cannot be {@code null}
   */
  @Override
  public void setValue(final Set<T> value) {
    Objects.requireNonNull(value);
    final Set<T> newValues =
        value.stream()
            .map(Objects::requireNonNull)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    updateSelection(newValues, new LinkedHashSet<>(getLeftGrid().getSelectedItems()));
  }

  /**
   * Returns the current value of this object which is an immutable set of the currently selected
   * items.
   *
   * @return the current selection
   */
  @Override
  public Set<T> getValue() {
    return Collections.unmodifiableSet(new LinkedHashSet<>(right.getItems()));
  }

  @Override
  public Registration addValueChangeListener(
      ValueChangeListener<? super ValueChangeEvent<Set<T>>> listener) {
    return right
        .getDataProvider()
        .addDataProviderListener(
            e -> {
              ComponentValueChangeEvent<TwinColGrid<T>, Set<T>> e2 =
                  new ComponentValueChangeEvent<>(TwinColGrid.this, TwinColGrid.this, null, true);
              listener.valueChanged(e2);
            });
  }

  @Override
  public boolean isReadOnly() {
    return isReadOnly();
  }

  @Override
  public boolean isRequiredIndicatorVisible() {
    return isRequiredIndicatorVisible();
  }

  @Override
  public void setReadOnly(final boolean readOnly) {
    getLeftGrid().setSelectionMode(readOnly ? SelectionMode.NONE : SelectionMode.MULTI);
    getRightGrid().setSelectionMode(readOnly ? SelectionMode.NONE : SelectionMode.MULTI);
    addButton.setEnabled(!readOnly);
    removeButton.setEnabled(!readOnly);
    addAllButton.setEnabled(!readOnly);
    removeAllButton.setEnabled(!readOnly);
  }

  @Override
  public void setRequiredIndicatorVisible(final boolean visible) {
    setRequiredIndicatorVisible(visible);
  }

  private void updateSelection(final Set<T> addedItems, final Set<T> removedItems) {
    left.getItems().addAll(removedItems);
    left.getItems().removeAll(addedItems);

    right.getItems().addAll(addedItems);
    right.getItems().removeAll(removedItems);

    forEachSide(
        side -> {
          side.getDataProvider().refreshAll();
          side.grid.getSelectionModel().deselectAll();
        });
  }

  private void configDragAndDrop(
      final TwinColModel<T> sourceModel, final TwinColModel<T> targetModel) {

    final Set<T> draggedItems = new LinkedHashSet<>();

    sourceModel.grid.setRowsDraggable(true);
    sourceModel.grid.addDragStartListener(
        event -> {
          draggedGrid = null;

          if (!(sourceModel.grid.getSelectionModel() instanceof GridNoneSelectionModel)) {
            draggedItems.addAll(event.getDraggedItems());
          }
          targetModel.grid.setDropMode(GridDropMode.ON_GRID);
        });

    sourceModel.grid.addDragEndListener(
        event -> {
          if (targetModel.droppedInsideGrid && sourceModel.grid == draggedGrid) {

            if (draggedGrid == null) {
              draggedItems.clear();
              return;
            }

            final ListDataProvider<T> dragGridSourceDataProvider = sourceModel.getDataProvider();

            dragGridSourceDataProvider.getItems().removeAll(draggedItems);
            dragGridSourceDataProvider.refreshAll();

            targetModel.droppedInsideGrid = false;
            draggedGrid = null;

            draggedItems.clear();
            sourceModel.grid.deselectAll();
          } else {
            draggedItems.clear();
          }
        });

    targetModel.grid.addDropListener(
        event -> {
          draggedGrid = sourceModel.grid;

          targetModel.droppedInsideGrid = true;
          final ListDataProvider<T> dragGridTargetDataProvider = targetModel.getDataProvider();
          dragGridTargetDataProvider.getItems().addAll(draggedItems);
          dragGridTargetDataProvider.refreshAll();
        });
  }

  /** @deprecated Use {@code getLeftGrid().addSelectionListener(listener);} */
  @Deprecated
  public void addLeftGridSelectionListener(SelectionListener<Grid<T>, T> listener) {
    getLeftGrid().addSelectionListener(listener);
  }

  /** @deprecated Use {@code getRightGrid().addSelectionListener(listener);} */
  @Deprecated
  public void addRightGridSelectionListener(SelectionListener<Grid<T>, T> listener) {
    getRightGrid().addSelectionListener(listener);
  }

  public TwinColGrid<T> addFilterableColumn(
      final ItemLabelGenerator<T> itemLabelGenerator,
      SerializableFunction<T, String> filterableValue,
      final String header,
      String filterPlaceholder,
      boolean enableClearButton) {
    forEachSide(
        side -> {
          Column<T> column =
              side.grid.addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header);
          TextField filterTF = new TextField();
          filterTF.setClearButtonVisible(enableClearButton);

          filterTF.addValueChangeListener(
              event ->
                  side.getDataProvider()
                      .addFilter(
                          filterableEntity ->
                              StringUtils.containsIgnoreCase(
                                  filterableValue.apply(filterableEntity), filterTF.getValue())));

          if (side.headerRow == null) {
            side.headerRow = side.grid.appendHeaderRow();
          }

          side.headerRow.getCell(column).setComponent(filterTF);
          filterTF.setValueChangeMode(ValueChangeMode.EAGER);
          filterTF.setSizeFull();
          filterTF.setPlaceholder(filterPlaceholder);
        });

    return this;
  }

  public TwinColGrid<T> addFilterableColumn(
      final ItemLabelGenerator<T> itemLabelGenerator,
      final String header,
      String filterPlaceholder,
      boolean enableClearButton) {
    return addFilterableColumn(
        itemLabelGenerator, itemLabelGenerator, header, filterPlaceholder, enableClearButton);
  }

  public TwinColGrid<T> selectRowOnClick() {
    forEachSide(
        side -> {
          side.grid.addClassName("hide-selector-col");

          side.grid.addItemClickListener(
              c -> {
                if (side.grid.getSelectedItems().contains(c.getItem())) {
                  side.grid.deselect(c.getItem());
                } else {
                  side.grid.select(c.getItem());
                }
              });
        });
    return this;
  }
}
