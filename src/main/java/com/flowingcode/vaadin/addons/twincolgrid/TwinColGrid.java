/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2022 Flowing Code
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
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridNoneSelectionModel;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableComparator;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@JsModule(value = "./src/fc-twin-col-grid-auto-resize.js")
@CssImport(value = "./styles/multiselect-cb-hide.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/twin-col-grid-button.css")
@CssImport(value = "./styles/twincol-grid.css")
public class TwinColGrid<T> extends VerticalLayout
    implements HasValue<ValueChangeEvent<Set<T>>, Set<T>>, HasComponents, HasSize {

  private static final class TwinColModel<T> implements Serializable {
    final Grid<T> grid;
    final Label columnLabel = new Label();
    final VerticalLayout layout;
    HeaderRow headerRow;
    boolean droppedInsideGrid = false;
    boolean allowReordering = false;
    Registration moveItemsByDoubleClick;

    TwinColModel(@NonNull Grid<T> grid, String className) {
      this.grid = grid;
      layout = new VerticalLayout(columnLabel, grid);

      layout.setClassName(className);
      grid.setClassName("twincol-grid-items");
      columnLabel.setClassName("twincol-grid-label");
    }

    @SuppressWarnings("unchecked")
    ListDataProvider<T> getDataProvider() {
      return (ListDataProvider<T>) grid.getDataProvider();
    }

    Collection<T> getItems() {
      return getDataProvider().getItems();
    }

    boolean isReorderingEnabled() {
      return allowReordering && grid.getSortOrder().isEmpty();
    }
  }

  /** enumeration of all available orientation for TwinGolGrid component */
  public enum Orientation {
    HORIZONTAL,
    VERTICAL,
    HORIZONTAL_REVERSE,
    VERTICAL_REVERSE;
  }

  private final TwinColModel<T> available;

  private final TwinColModel<T> selection;

  private Label captionLabel;

  private final Button addAllButton = createActionButton();

  private final Button addButton = createActionButton();

  private final Button removeButton = createActionButton();

  private final Button removeAllButton = createActionButton();

  private Component buttonContainer;

  private Grid<T> draggedGrid;

  private Label fakeButtonContainerLabel = new Label();

  private Orientation orientation = Orientation.HORIZONTAL;

  private boolean autoResize = false;

  private boolean isFromClient = false;

  private static <T> ListDataProvider<T> emptyDataProvider() {
    return DataProvider.ofCollection(new LinkedHashSet<>());
  }

  /** Constructs a new TwinColGrid with an empty {@link ListDataProvider}. */
  public TwinColGrid() {
    this(Grid::new);
  }

  /**
   * Constructs a new empty TwinColGrid, using the specified supplier for instantiating both grids.
   *
   * @param gridSupplier a supplier for instantiating both grids
   */
  public TwinColGrid(Supplier<Grid<T>> gridSupplier) {
    this(gridSupplier.get(), gridSupplier.get());
  }

  /**
   * Constructs a new empty TwinColGrid, using the specified grids for each side.
   *
   * @param availableGrid the grid that contains the available items
   * @param selectionGrid the grid that contains the selected items
   */
  public TwinColGrid(@NonNull Grid<T> availableGrid, @NonNull Grid<T> selectionGrid) {

    if (availableGrid == selectionGrid) {
      throw new IllegalArgumentException("Grids must be different");
    }

    available = new TwinColModel<>(availableGrid, "twincol-grid-available");
    selection = new TwinColModel<>(selectionGrid, "twincol-grid-selection");

    setClassName("twincol-grid");

    setMargin(false);
    setPadding(false);

    setDataProvider(emptyDataProvider());
    ListDataProvider<T> rightGridDataProvider = DataProvider.ofCollection(new LinkedHashSet<>());
    getSelectionGrid().setDataProvider(rightGridDataProvider);

    getAvailableGrid().setWidth("100%");
    getSelectionGrid().setWidth("100%");

    addAllButton.addClickListener(
        e -> {
          List<T> filteredItems = available.getDataProvider().withConfigurableFilter()
              .fetch(new Query<>()).collect(Collectors.toList());
          updateSelection(new LinkedHashSet<>(filteredItems), new HashSet<>(), true);
        });

    addButton.addClickListener(
        e ->
            updateSelection(
            new LinkedHashSet<>(getAvailableGrid().getSelectedItems()), new HashSet<>(), true));

    removeButton.addClickListener(
        e -> updateSelection(new HashSet<>(), getSelectionGrid().getSelectedItems(), true));

    removeAllButton.addClickListener(
        e -> {
          List<T> filteredItems= selection.getDataProvider().withConfigurableFilter().fetch(new Query<>()).collect(Collectors.toList());
          updateSelection(new HashSet<>(), new HashSet<>(filteredItems), true);
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

    initMoveItemsByDoubleClick();
    add(createContainerLayout());
    setSizeUndefined();
  }

  @SuppressWarnings("deprecation")
  private void initMoveItemsByDoubleClick() {
    setMoveItemsByDoubleClick(!(this instanceof LegacyTwinColGrid));
  }

  /**
   * Sets the component caption.
   *
   * @param captinText the component caption.
   */
  public void setCaption(String captionText) {
    if (captionText != null) {
      if (captionLabel == null) {
        captionLabel = new Label();
        addComponentAsFirst(captionLabel);
      }
      captionLabel.setText(captionText);
    } else {
      if (captionLabel != null) {
        remove(captionLabel);
        captionLabel = null;
      }
    }
  }

  /**
   * Returns the component caption.
   *
   * @return the component caption.
   */
  public String getCaption() {
    return Optional.ofNullable(captionLabel).map(Label::getText).orElse(null);
  }

  /**
   * Sets orientation for TwinColGridComponent
   *
   * @param orientation
   * @return
   */
  public TwinColGrid<T> withOrientation(Orientation orientation) {
    if (this.orientation != orientation) {
      this.orientation = orientation;
      updateContainerLayout();
      available.grid.getDataProvider().refreshAll();
      selection.grid.getDataProvider().refreshAll();
    }
    return this;
  }

  public Orientation getOrientation() {
    return orientation;
  }

  private void updateContainerLayout() {
    Component oldContainerComponent = available.layout.getParent().get();
    Component newContainerComponent = createContainerLayout();
    replace(oldContainerComponent, newContainerComponent);
  }

  private Component createContainerLayout() {
    switch (orientation) {
      case HORIZONTAL:
        addButton.setIcon(VaadinIcon.ANGLE_RIGHT.create());
        addAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_RIGHT.create());
        removeButton.setIcon(VaadinIcon.ANGLE_LEFT.create());
        removeAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_LEFT.create());
        return createHorizontalContainer(false);
      case HORIZONTAL_REVERSE:
        addButton.setIcon(VaadinIcon.ANGLE_LEFT.create());
        addAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_LEFT.create());
        removeButton.setIcon(VaadinIcon.ANGLE_RIGHT.create());
        removeAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_RIGHT.create());
        return createHorizontalContainer(true);
      case VERTICAL:
        addButton.setIcon(VaadinIcon.ANGLE_DOWN.create());
        addAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_DOWN.create());
        removeButton.setIcon(VaadinIcon.ANGLE_UP.create());
        removeAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_UP.create());
        return createVerticalContainer(false);
      case VERTICAL_REVERSE:
        addButton.setIcon(VaadinIcon.ANGLE_UP.create());
        addAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_UP.create());
        removeButton.setIcon(VaadinIcon.ANGLE_DOWN.create());
        removeAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_DOWN.create());
        return createVerticalContainer(true);
    }
    throw new IllegalStateException();
  }

  private HorizontalLayout createHorizontalContainer(boolean reverse) {
    buttonContainer = getVerticalButtonContainer();
    HorizontalLayout hl;
    if (reverse) {
      hl = new HorizontalLayout(selection.layout, buttonContainer, available.layout);
    } else {
      hl = new HorizontalLayout(available.layout, buttonContainer, selection.layout);
    }
    hl.getElement().getStyle().set("min-height", "0px");
    hl.getElement().getStyle().set("flex", "1 1 0px");
    hl.setMargin(false);
    hl.setWidthFull();
    return hl;
  }

  private VerticalLayout createVerticalContainer(boolean reverse) {
    buttonContainer = getHorizontalButtonContainer();
    VerticalLayout vl;
    if (reverse) {
      vl = new VerticalLayout(selection.layout, buttonContainer, available.layout);
    } else {
      vl = new VerticalLayout(available.layout, buttonContainer, selection.layout);
    }
    vl.getElement().getStyle().set("min-width", "0px");
    vl.getElement().getStyle().set("flex", "1 1 0px");
    vl.setMargin(false);
    vl.setPadding(false);
    vl.setHeightFull();
    return vl;
  }

  private VerticalLayout getVerticalButtonContainer() {
    fakeButtonContainerLabel.getElement().setProperty("innerHTML", "&nbsp;");
    fakeButtonContainerLabel.setVisible(false);

    VerticalLayout vButtonContainer =
        new VerticalLayout(
            fakeButtonContainerLabel, addAllButton, addButton, removeButton, removeAllButton);
    vButtonContainer.setPadding(false);
    vButtonContainer.setSpacing(false);
    vButtonContainer.setSizeUndefined();
    return vButtonContainer;
  }

  private HorizontalLayout getHorizontalButtonContainer() {
    HorizontalLayout hButtonContainer =
        new HorizontalLayout(
            addAllButton, addButton, removeButton, removeAllButton);
    hButtonContainer.setPadding(false);
    hButtonContainer.setSizeUndefined();
    return hButtonContainer;
  }

  /** Return the grid that contains the available items. */
  public Grid<T> getAvailableGrid() {
    return available.grid;
  }

  /** Return the grid that contains the selected items. */
  public Grid<T> getSelectionGrid() {
    return selection.grid;
  }

  private void forEachSide(Consumer<TwinColModel<T>> consumer) {
    consumer.accept(available);
    consumer.accept(selection);
  }

  public final void forEachGrid(Consumer<Grid<T>> consumer) {
    consumer.accept(available.grid);
    consumer.accept(selection.grid);
  }

  public void setItems(Collection<T> items) {
    setDataProvider(DataProvider.ofCollection(items));
  }

  public void setItems(Stream<T> items) {
    setDataProvider(DataProvider.fromStream(items));
  }

  public void clearAll() {
    updateSelection(new HashSet<>(), new HashSet<>(selection.getItems()), false);
  }

  protected void setDataProvider(ListDataProvider<T> dataProvider) {
    getAvailableGrid().setDataProvider(dataProvider);
    if (selection.getDataProvider() != null) {
      selection.getItems().clear();
      selection.getDataProvider().refreshAll();
    }
  }

  /**
   * Constructs a new TwinColGrid with the given options.
   *
   * @param options the options, cannot be {@code null}
   */
  public TwinColGrid(final Collection<T> options) {
    this();
    setDataProvider(DataProvider.ofCollection(new LinkedHashSet<>(options)));
  }

  /**
   * Sets the text shown above the grid with the available items. {@code null} clears the caption.
   *
   * @param caption The text to show, {@code null} to clear
   * @return this instance
   */
  public TwinColGrid<T> withAvailableGridCaption(final String caption) {
    available.columnLabel.setText(caption);
    available.columnLabel.setVisible(true);
    fakeButtonContainerLabel.setVisible(true);
    return this;
  }

  /**
   * Sets the text shown above the grid with the selected items. {@code null} clears the caption.
   *
   * @param caption The text to show, {@code null} to clear
   * @return this instance
   */
  public TwinColGrid<T> withSelectionGridCaption(final String caption) {
    selection.columnLabel.setText(caption);
    selection.columnLabel.setVisible(true);
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
    getAvailableGrid().addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header);
    getSelectionGrid().addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header);
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
    forEachGrid(grid -> grid
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
    forEachGrid(grid -> grid
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
    setWidthFull();
    getElement().getStyle().set("flex-grow", "1");
    return this;
  }

  /**
   * Adds drag n drop support between grids.
   *
   * @return this instance
   */
  public TwinColGrid<T> withDragAndDropSupport() {
    configDragAndDrop(available, selection);
    configDragAndDrop(selection, available);
    return this;
  }

  /**
   * Returns the text shown above the grid with the available items.
   *
   * @return The text shown or {@code null} if not set.
   */
  public String getAvailableGridCaption() {
    return available.columnLabel.getText();
  }

  /**
   * Returns the text shown above the grid with the selected items.
   *
   * @return The text shown or {@code null} if not set.
   */
  public String getSelectionGridCaption() {
    return selection.columnLabel.getText();
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
    final Set<T> oldValues = new LinkedHashSet<>(selection.getItems());
    oldValues.removeAll(newValues);
    updateSelection(newValues, oldValues, false);
  }

  /**
   * Returns the current value of this object which is an immutable set of the currently selected
   * items.
   *
   * @return the current selection
   */
  @Override
  public Set<T> getValue() {
    return Collections
        .unmodifiableSet(collectValue(Collectors.<T, Set<T>>toCollection(LinkedHashSet::new)));
  }

  /**
   * Collects the current value of this object according to the configured order.
   *
   * @return the current selection
   */
  <C> C collectValue(Collector<T, ?, C> collector) {
    Stream<T> stream = selection.getItems().stream();
    SerializableComparator<T> comparator = createSortingComparator(selection.grid);
    if (comparator != null) {
      return stream.sorted(comparator).collect(collector);
    } else {
      return stream.collect(collector);
    }
  }

  private static <T> SerializableComparator<T> createSortingComparator(Grid<T> grid) {
    // protected method copied from Grid::createSortingComparator
    BinaryOperator<SerializableComparator<T>> operator = (comparator1, comparator2) -> {
      /*
       * thenComparing is defined to return a serializable comparator as long as both original
       * comparators are also serializable
       */
      return comparator1.thenComparing(comparator2)::compare;
    };
    return grid.getSortOrder().stream()
        .map(order -> order.getSorted().getComparator(order.getDirection())).reduce(operator)
        .orElse(null);
  }

  @Override
  public Registration addValueChangeListener(
      ValueChangeListener<? super ValueChangeEvent<Set<T>>> listener) {
    return selection
        .getDataProvider()
        .addDataProviderListener(
            e -> {
              ComponentValueChangeEvent<TwinColGrid<T>, Set<T>> e2 =
                  new ComponentValueChangeEvent<>(TwinColGrid.this, TwinColGrid.this, null, isFromClient);
              listener.valueChanged(e2);
            });
  }

  @Override
  public boolean isReadOnly() {
    return getAvailableGrid().getSelectionModel() instanceof GridNoneSelectionModel;
  }

  @Override
  public boolean isRequiredIndicatorVisible() {
    return getElement().getAttribute("required") != null;
  }

  @Override
  public void setReadOnly(final boolean readOnly) {
    getAvailableGrid().setSelectionMode(readOnly ? SelectionMode.NONE : SelectionMode.MULTI);
    getSelectionGrid().setSelectionMode(readOnly ? SelectionMode.NONE : SelectionMode.MULTI);
    addButton.setEnabled(!readOnly);
    removeButton.setEnabled(!readOnly);
    addAllButton.setEnabled(!readOnly);
    removeAllButton.setEnabled(!readOnly);
  }

  @Override
  public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
    getElement().setAttribute("required", requiredIndicatorVisible);
  }

  private void updateSelection(final Set<T> addedItems, final Set<T> removedItems, boolean isFromClient) {
    this.isFromClient = isFromClient;
    available.getItems().addAll(removedItems);
    available.getItems().removeAll(addedItems);

    selection.getItems().addAll(addedItems);
    selection.getItems().removeAll(removedItems);

    forEachGrid(grid -> {
      grid.getDataProvider().refreshAll();
      grid.getSelectionModel().deselectAll();
        });
  }

  private void configDragAndDrop(
      final TwinColModel<T> sourceModel, final TwinColModel<T> targetModel) {

    final Set<T> draggedItems = new LinkedHashSet<>();

    sourceModel.grid.setRowsDraggable(true);
    sourceModel.grid.addDragStartListener(
        event -> {
          draggedGrid = sourceModel.grid;

          if (!(sourceModel.grid.getSelectionModel() instanceof GridNoneSelectionModel)) {
            draggedItems.addAll(event.getDraggedItems());
          }

          sourceModel.grid
              .setDropMode(sourceModel.isReorderingEnabled() ? GridDropMode.BETWEEN : null);
          targetModel.grid.setDropMode(
              targetModel.isReorderingEnabled() ? GridDropMode.BETWEEN : GridDropMode.ON_GRID);
        });

    sourceModel.grid.addDragEndListener(
        event -> {
          if (targetModel.droppedInsideGrid
              && sourceModel.grid == draggedGrid
              && !draggedItems.isEmpty()) {

            final ListDataProvider<T> dragGridSourceDataProvider = sourceModel.getDataProvider();

            dragGridSourceDataProvider.getItems().removeAll(draggedItems);
            dragGridSourceDataProvider.refreshAll();

            targetModel.droppedInsideGrid = false;

            draggedItems.clear();
            sourceModel.grid.deselectAll();

            sourceModel.grid.setDropMode(null);
            targetModel.grid.setDropMode(null);
          }
          draggedItems.clear();
        });

    targetModel.grid.addDropListener(
        event -> {
          if (!draggedItems.isEmpty()) {
            isFromClient = true;
            targetModel.droppedInsideGrid = true;
            T dropOverItem = event.getDropTargetItem().orElse(null);
            addItems(targetModel, draggedItems, dropOverItem, event.getDropLocation());
          }
        });

    sourceModel.grid.addDropListener(event -> {
      event.getDropTargetItem().ifPresent(dropOverItem -> {
        if (sourceModel.isReorderingEnabled()
            && event.getSource() == draggedGrid
            && !draggedItems.contains(dropOverItem)
            && !draggedItems.isEmpty()) {
          isFromClient = true;
          sourceModel.getItems().removeAll(draggedItems);
          addItems(sourceModel, draggedItems, dropOverItem, event.getDropLocation());
          draggedItems.clear();
          draggedGrid = null;
        }
      });
    });

  }

  private void addItems(TwinColModel<T> model, Collection<T> draggedItems,
      T dropOverItem, GridDropLocation dropLocation) {
    if (dropOverItem != null) {
      Collection<T> collection = model.getItems();
      List<T> list = new ArrayList<>(collection);
      int dropIndex = list.indexOf(dropOverItem) + (dropLocation == GridDropLocation.BELOW ? 1 : 0);
      list.addAll(dropIndex, draggedItems);
      model.getItems().clear();
      model.getItems().addAll(list);
      model.getDataProvider().refreshAll();
    } else {
      model.getItems().addAll(draggedItems);
      model.getDataProvider().refreshAll();
    }
  }

  /** Allow drag-and-drop within the selection grid. */
  public TwinColGrid<T> withSelectionGridReordering() {
    setSelectionGridReorderingAllowed(true);
    return this;
  }

  /** Configure whether drag-and-drop within the selection grid is allowed. */
  public void setSelectionGridReorderingAllowed(boolean value) {
    selection.allowReordering = value;
  }

  /** Return whether drag-and-drop within the selection grid is allowed. */
  public boolean isSelectionGridReorderingAllowed() {
    return selection.allowReordering;
  }

  public TwinColGrid<T> addFilterableColumn(
      final ItemLabelGenerator<T> itemLabelGenerator,
      SerializableFunction<T, String> filterableValue,
      final String header,
      String filterPlaceholder,
      boolean enableClearButton, String key) {
    forEachSide(
        side -> {
          Column<T> column =
              side.grid.addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header);

          Optional.ofNullable(key).ifPresent(column::setKey);

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
    return addFilterableColumn(itemLabelGenerator, itemLabelGenerator, header, filterPlaceholder,
        enableClearButton, null);
  }

  public TwinColGrid<T> addFilterableColumn(ItemLabelGenerator<T> itemLabelGenerator,
      SerializableFunction<T, String> filterableValue, String header, String filterPlaceholder,
      boolean enableClearButton) {
    return addFilterableColumn(itemLabelGenerator, filterableValue, header, filterPlaceholder,
        enableClearButton, null);
  }

  public TwinColGrid<T> addFilterableColumn(ItemLabelGenerator<T> itemLabelGenerator, String header,
      String filterPlaceholder, boolean enableClearButton, String key) {
    return addFilterableColumn(itemLabelGenerator, itemLabelGenerator, header, filterPlaceholder,
        enableClearButton, key);
  }

  public TwinColGrid<T> selectRowOnClick() {
    forEachGrid(grid -> {
      grid.addClassName("hide-selector-col");

      grid.addItemClickListener(
              c -> {
            if (grid.getSelectedItems().contains(c.getItem())) {
              grid.deselect(c.getItem());
                } else {
              grid.select(c.getItem());
                }
              });
        });
    return this;
  }

  public HasValue<? extends ValueChangeEvent<List<T>>, List<T>> asList() {
    return new TwinColGridListAdapter<>(this);
  }

  @Override
  public Set<T> getEmptyValue() {
    return Collections.emptySet();
  }

  private Button createActionButton() {
    Button button = new Button();
    button.addThemeName("twin-col-grid-button");
    return button;
  }

  /**
   * Return whether autoResize is set or not.
   */
  public boolean isAutoResize() {
    return autoResize;
  }

  /**
   * Sets whether component should update orientation on resize.
   *
   * @param autoResize if true, component will update orientation on resize
   */
  public void setAutoResize(boolean autoResize) {
    if (autoResize != this.autoResize) {
      if (autoResize) {
        getElement().executeJs("fcTwinColGridAutoResize.observe($0)", this);
      } else {
        getElement().executeJs("fcTwinColGridAutoResize.unobserve($0)", this);
      }
      this.autoResize = autoResize;
    }
  }

  /**
   * Sets whether a doubleclick event immediately moves an item to the other grid
   *
   * @param value if true, a a doubleclick event will immediately move an item to the other grid
   */
  public void setMoveItemsByDoubleClick(boolean value) {
    forEachSide(side -> {
      if (value && side.moveItemsByDoubleClick == null) {
        side.moveItemsByDoubleClick = side.grid.addItemDoubleClickListener(ev -> {
          Set<T> item = Collections.singleton(ev.getItem());
          if (side == available) {
            updateSelection(item, Collections.emptySet(), true);
          }
          if (side == selection) {
            updateSelection(Collections.emptySet(), item, true);
          }
        });
      }
      if (!value && side.moveItemsByDoubleClick != null) {
        side.moveItemsByDoubleClick.remove();
        side.moveItemsByDoubleClick = null;
      }
    });
  }

  @ClientCallable
  private void updateOrientationOnResize(int width, int height) {
    if (height > width) {
      this.withOrientation(Orientation.VERTICAL);
    } else {
      this.withOrientation(Orientation.HORIZONTAL);
    }
  }

}
