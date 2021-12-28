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
import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.data.selection.SelectionListener;
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
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@CssImport(value = "./styles/multiselect-cb-hide.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/twin-col-grid-button.css")
@CssImport(value = "./styles/twincol-grid.css")
public class TwinColGrid<T> extends VerticalLayout
    implements HasValue<ValueChangeEvent<Set<T>>, Set<T>>, HasComponents, HasSize {

  private static final class GridEx<T> extends Grid<T> {
    @Override
    protected SerializableComparator<T> createSortingComparator() {
      return super.createSortingComparator();
    }
  }

  private static final class TwinColModel<T> implements Serializable {
    final GridEx<T> grid = new GridEx<>();
    final Label columnLabel = new Label();
    final VerticalLayout layout = new VerticalLayout(columnLabel, grid);
    HeaderRow headerRow;
    boolean droppedInsideGrid = false;
    boolean allowReordering = false;

    TwinColModel(String className) {
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

  /** @deprecated Use getAvailableGrid() */
  @Deprecated protected final Grid<T> leftGrid;

  /** @deprecated Use getSelectionGrid() */
  @Deprecated protected final Grid<T> rightGrid;

  /** @deprecated Use getAvailableGrid().getDataProvider() */
  @Deprecated protected ListDataProvider<T> leftGridDataProvider;

  /** @deprecated Use getSelectionGrid().getDataProvider() */
  @Deprecated protected ListDataProvider<T> rightGridDataProvider;

  private final Button addAllButton = createActionButton();

  private final Button addButton = createActionButton();

  private final Button removeButton = createActionButton();

  private final Button removeAllButton = createActionButton();

  private Component buttonContainer;

  private Grid<T> draggedGrid;

  private Label fakeButtonContainerLabel = new Label();

  private Orientation orientation = Orientation.HORIZONTAL;

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
    available = new TwinColModel<>("twincol-grid-available");
    selection = new TwinColModel<>("twincol-grid-selection");

    leftGrid = available.grid;
    rightGrid = selection.grid;

    setClassName("twincol-grid");

    setMargin(false);
    setPadding(false);
    if (caption != null) {
      add(new Label(caption));
    }

    setDataProvider(dataProvider);

    rightGridDataProvider = DataProvider.ofCollection(new LinkedHashSet<>());
    getSelectionGrid().setDataProvider(rightGridDataProvider);

    getAvailableGrid().setWidth("100%");
    getSelectionGrid().setWidth("100%");

    addAllButton.addClickListener(
        e -> {
          List<T> filteredItems = available.getDataProvider().withConfigurableFilter()
              .fetch(new Query<>()).collect(Collectors.toList());
          updateSelection(new LinkedHashSet<>(filteredItems), new HashSet<>());
        });

    addButton.addClickListener(
        e ->
            updateSelection(
            new LinkedHashSet<>(getAvailableGrid().getSelectedItems()), new HashSet<>()));

    removeButton.addClickListener(
        e -> updateSelection(new HashSet<>(), getSelectionGrid().getSelectedItems()));

    removeAllButton.addClickListener(
        e -> {
          List<T> filteredItems= selection.getDataProvider().withConfigurableFilter().fetch(new Query<>()).collect(Collectors.toList());
          updateSelection(new HashSet<>(), new HashSet<>(filteredItems));
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

    add(createContainerLayout());
    setSizeUndefined();
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

  /**
   * Return the left grid component.
   *
   * @deprecated Use {@link #getAvailableGrid()}. Depending on the orientation, the "left grid" may
   *             not be located at the left side.
   */
  @Deprecated
  public Grid<T> getLeftGrid() {
    return leftGrid;
  }

  /**
   * Return the right grid component.
   *
   * @deprecated Use {@link #getSelectionGrid()}. Depending on the orientation, the "right grid" may
   *             not be located at the right side.
   */
  @Deprecated
  public Grid<T> getRightGrid() {
    return rightGrid;
  }

  private void forEachSide(Consumer<TwinColModel<T>> consumer) {
    consumer.accept(available);
    consumer.accept(selection);
  }

  public void setItems(Collection<T> items) {
    setDataProvider(DataProvider.ofCollection(items));
  }

  public void setItems(Stream<T> items) {
    setDataProvider(DataProvider.fromStream(items));
  }

  /** @deprecated Use {@code getAvailableGrid.getDataProvider()} */
  @Deprecated
  public void setLeftGridClassName(String classname) {
    getAvailableGrid().setClassName(classname);
  }

  /** @deprecated Use {@code getAvailableGrid().addClassName(classname)} */
  @Deprecated
  public void addLeftGridClassName(String classname) {
    getAvailableGrid().addClassName(classname);
  }

  /** @deprecated Use {@code getAvailableGrid().removeClassName(classname)} */
  @Deprecated
  public void removeLeftGridClassName(String classname) {
    getAvailableGrid().removeClassName(classname);
  }

  /** @deprecated Use {@code getSelectionGrid().setClassName(classname)} */
  @Deprecated
  public void setRightGridClassName(String classname) {
    getSelectionGrid().setClassName(classname);
  }

  /** @deprecated Use {@code getSelectionGrid().addClassName(classname)} */
  @Deprecated
  public void addRightGridClassName(String classname) {
    getSelectionGrid().addClassName(classname);
  }

  /** @deprecated Use {@code getSelectionGrid().removeClassName(classname)} */
  @Deprecated
  public void removeRightGridClassName(String classname) {
    getSelectionGrid().removeClassName(classname);
  }

  public void clearAll() {
    updateSelection(new HashSet<>(), new HashSet<>(selection.getItems()));
  }

  private void setDataProvider(ListDataProvider<T> dataProvider) {
    leftGridDataProvider = dataProvider;
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
   * Sets the text shown above the grid with the available items. {@code null} clears the caption.
   *
   * @param rightColumnCaption The text to show, {@code null} to clear
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
   * Sets the text shown above the grid with the available items. {@code null} clears the caption.
   *
   * @param rightColumnCaption The text to show, {@code null} to clear
   * @return this instance
   * @deprecated Use {@link #withAvailableGridCaption(String)}
   */
  @Deprecated
  public TwinColGrid<T> withRightColumnCaption(final String caption) {
    return withSelectionGridCaption(caption);
  }

  /**
   * Sets the text shown above the grid with the available items. {@code null} clears the caption.
   *
   * @param caption The text to show, {@code null} to clear
   * @return this instance
   * @deprecated Use {@link #withSelectionGridCaption(String)}
   */
  @Deprecated
  public TwinColGrid<T> withLeftColumnCaption(final String caption) {
    return withAvailableGridCaption(caption);
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
   * Returns the text shown above the right column.
   *
   * @return The text shown or {@code null} if not set.
   * @deprecated Use {@link #getSelectionGridCaption()}
   */
  @Deprecated
  public String getRightColumnCaption() {
    return getSelectionGridCaption();
  }

  /**
   * Returns the text shown above the left column.
   *
   * @return The text shown or {@code null} if not set.
   * @deprecated Use {@link #getAvailableGridCaption()}
   */
  @Deprecated
  public String getLeftColumnCaption() {
    return getAvailableGridCaption();
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
    updateSelection(newValues, new LinkedHashSet<>(getAvailableGrid().getSelectedItems()));
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
    SerializableComparator<T> comparator = selection.grid.createSortingComparator();
    if (comparator != null) {
      return stream.sorted(comparator).collect(collector);
    } else {
      return stream.collect(collector);
    }
  }

  @Override
  public Registration addValueChangeListener(
      ValueChangeListener<? super ValueChangeEvent<Set<T>>> listener) {
    return selection
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
    return getAvailableGrid().getSelectionModel() == SelectionMode.NONE;
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

  private void updateSelection(final Set<T> addedItems, final Set<T> removedItems) {
    available.getItems().addAll(removedItems);
    available.getItems().removeAll(addedItems);

    selection.getItems().addAll(addedItems);
    selection.getItems().removeAll(removedItems);

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

          sourceModel.grid.setDropMode(sourceModel.allowReordering ? GridDropMode.BETWEEN : null);
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

    sourceModel.grid.addDropListener(event -> {
      event.getDropTargetItem().ifPresent(dropOverItem -> {
        if (sourceModel.allowReordering
            && event.getSource() == draggedGrid
            && !draggedItems.contains(dropOverItem)
            && !draggedItems.isEmpty()) {
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

  /** @deprecated Use {@code getAvailableGrid().addSelectionListener(listener);} */
  @Deprecated
  public void addLeftGridSelectionListener(SelectionListener<Grid<T>, T> listener) {
    getAvailableGrid().addSelectionListener(listener);
  }

  /** @deprecated Use {@code getSelectionGrid().addSelectionListener(listener);} */
  @Deprecated
  public void addRightGridSelectionListener(SelectionListener<Grid<T>, T> listener) {
    getSelectionGrid().addSelectionListener(listener);
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

}
