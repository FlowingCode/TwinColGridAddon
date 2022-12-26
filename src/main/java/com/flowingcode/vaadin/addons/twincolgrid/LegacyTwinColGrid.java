package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.selection.SelectionListener;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Supplier;
import lombok.NonNull;

/** Implementation of {@code TwinColGrid} with deprecated methods from version 2.9.0. */
@SuppressWarnings("serial")
@Deprecated
public class LegacyTwinColGrid<T> extends TwinColGrid<T> {

  /** @deprecated Use getAvailableGrid() */
  @Deprecated protected final Grid<T> leftGrid;

  /** @deprecated Use getSelectionGrid() */
  @Deprecated protected final Grid<T> rightGrid;

  /** @deprecated Use getAvailableGrid().getDataProvider() */
  @Deprecated protected ListDataProvider<T> leftGridDataProvider;

  /** @deprecated Use getSelectionGrid().getDataProvider() */
  @Deprecated protected ListDataProvider<T> rightGridDataProvider;

  /** Constructs a new TwinColGrid with an empty {@link ListDataProvider}. */
  @SuppressWarnings("unchecked")
  public LegacyTwinColGrid() {
    super();
    leftGrid = getAvailableGrid();
    rightGrid = getSelectionGrid();
    leftGridDataProvider = (ListDataProvider<T>) getAvailableGrid().getDataProvider();
    rightGridDataProvider = (ListDataProvider<T>) getSelectionGrid().getDataProvider();
  }

  /**
   * Constructs a new empty TwinColGrid, using the specified supplier for instantiating both grids.
   *
   * @param gridSupplier a supplier for instantiating both grids
   */
  @SuppressWarnings("unchecked")
  public LegacyTwinColGrid(Supplier<Grid<T>> gridSupplier) {
    super(gridSupplier);
    leftGrid = getAvailableGrid();
    rightGrid = getSelectionGrid();
    leftGridDataProvider = (ListDataProvider<T>) getAvailableGrid().getDataProvider();
    rightGridDataProvider = (ListDataProvider<T>) getSelectionGrid().getDataProvider();
  }

  /**
   * Constructs a new empty TwinColGrid, using the specified grids for each side.
   *
   * @param availableGrid the grid that contains the available items
   * @param selectionGrid the grid that contains the selected items
   */
  @SuppressWarnings("unchecked")
  public LegacyTwinColGrid(@NonNull Grid<T> availableGrid, @NonNull Grid<T> selectionGrid) {
    super(availableGrid, selectionGrid);
    leftGrid = getAvailableGrid();
    rightGrid = getSelectionGrid();
    leftGridDataProvider = (ListDataProvider<T>) getAvailableGrid().getDataProvider();
    rightGridDataProvider = (ListDataProvider<T>) getSelectionGrid().getDataProvider();
  }

  /**
   * Constructs a new empty TwinColGrid with caption
   *
   * @param caption the component caption
   * @deprecated Use {@link TwinColGrid#TwinColGrid()} and {{@link #setCaption(String)}
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public LegacyTwinColGrid(String caption) {
    super(Grid::new);
    setCaption(caption);
    leftGrid = getAvailableGrid();
    rightGrid = getSelectionGrid();
    leftGridDataProvider = (ListDataProvider<T>) getAvailableGrid().getDataProvider();
    rightGridDataProvider = (ListDataProvider<T>) getSelectionGrid().getDataProvider();
  }

  /**
   * Constructs a new TwinColGrid with data provider for options.
   *
   * @param dataProvider the data provider, not {@code null}
   * @param caption the component caption
   * @deprecated Use {@link #TwinColGrid()} and {@link #setDataProvider(ListDataProvider)}, {@link
   *     #setCaption(String)}
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public LegacyTwinColGrid(final ListDataProvider<T> dataProvider, String caption) {
    super(Grid::new);
    setDataProvider(dataProvider);
    setCaption(caption);
    leftGrid = getAvailableGrid();
    rightGrid = getSelectionGrid();
    leftGridDataProvider = (ListDataProvider<T>) getAvailableGrid().getDataProvider();
    rightGridDataProvider = (ListDataProvider<T>) getSelectionGrid().getDataProvider();
  }

  /**
   * Constructs a new empty TwinColGrid, using the specified supplier for instantiating both grids.
   *
   * @param caption the component caption
   * @param gridSupplier a supplier for instantiating both grids
   * @deprecated Use {@link TwinColGrid#TwinColGrid(Supplier)} and {@link #setCaption(String)}
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public LegacyTwinColGrid(String caption, Supplier<Grid<T>> gridSupplier) {
    super(gridSupplier.get(), gridSupplier.get());
    setCaption(caption);
    leftGrid = getAvailableGrid();
    rightGrid = getSelectionGrid();
    leftGridDataProvider = (ListDataProvider<T>) getAvailableGrid().getDataProvider();
    rightGridDataProvider = (ListDataProvider<T>) getSelectionGrid().getDataProvider();
  }

  /**
   * Constructs a new empty TwinColGrid, using the specified grids for each side.
   *
   * @param caption the component caption
   * @param availableGrid the grid that contains the available items
   * @param selectionGrid the grid that contains the selected items
   * @deprecated Use {@link TwinColGrid#TwinColGrid(Grid, Grid)} and {@link #setCaption(String)}
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public LegacyTwinColGrid(
      String caption, @NonNull Grid<T> availableGrid, @NonNull Grid<T> selectionGrid) {
    super(availableGrid, selectionGrid);
    setCaption(caption);
    leftGrid = getAvailableGrid();
    rightGrid = getSelectionGrid();
    leftGridDataProvider = (ListDataProvider<T>) getAvailableGrid().getDataProvider();
    rightGridDataProvider = (ListDataProvider<T>) getSelectionGrid().getDataProvider();
  }

  /**
   * Constructs a new TwinColGrid with caption and the given options.
   *
   * @param caption the caption to set, can be {@code null}
   * @param options the options, cannot be {@code null}
   * @deprecated Use {@link #TwinColGrid(Collection)} and {{@link #setCaption(String)}
   */
  @Deprecated
  @SuppressWarnings("unchecked")
  public LegacyTwinColGrid(final Collection<T> options, final String caption) {
    super(options);
    setCaption(caption);
    leftGrid = getAvailableGrid();
    rightGrid = getSelectionGrid();
    leftGridDataProvider = (ListDataProvider<T>) getAvailableGrid().getDataProvider();
    rightGridDataProvider = (ListDataProvider<T>) getSelectionGrid().getDataProvider();
  }

  /** @deprecated Use {@code getAvailableGrid().setClassName(classname)} */
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

  /**
   * Sets the text shown above the grid with the available items. {@code null} clears the caption.
   *
   * @param caption The text to show, {@code null} to clear
   * @return this instance
   * @deprecated Use {@link #withAvailableGridCaption(String)}
   */
  @Deprecated
  public LegacyTwinColGrid<T> withRightColumnCaption(final String caption) {
    withSelectionGridCaption(caption);
    return this;
  }

  /**
   * Sets the text shown above the grid with the available items. {@code null} clears the caption.
   *
   * @param caption The text to show, {@code null} to clear
   * @return this instance
   * @deprecated Use {@link #withSelectionGridCaption(String)}
   */
  @Deprecated
  public LegacyTwinColGrid<T> withLeftColumnCaption(final String caption) {
    withAvailableGridCaption(caption);
    return this;
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

  /**
   * Return the left grid component.
   *
   * @deprecated Use {@link #getAvailableGrid()}. Depending on the orientation, the "left grid" may
   *     not be located at the left side.
   */
  @Deprecated
  public Grid<T> getLeftGrid() {
    return leftGrid;
  }

  /**
   * Return the right grid component.
   *
   * @deprecated Use {@link #getSelectionGrid()}. Depending on the orientation, the "right grid" may
   *     not be located at the right side.
   */
  @Deprecated
  public Grid<T> getRightGrid() {
    return rightGrid;
  }

  @Override
  protected void setDataProvider(ListDataProvider<T> dataProvider) {
    leftGridDataProvider = dataProvider;
    super.setDataProvider(dataProvider);
  }

  /**
   * Adds a new text column to this {@link Grid} with a value provider. The column will use a
   * {@link TextRenderer}. The value is converted to a String using the provided {@code
   * itemLabelGenerator}.
   *
   * @deprecated Use {@link #addColumn(ItemLabelGenerator)}{@code .setHeader(header)}
   *
   * @param itemLabelGenerator the value provider
   * @param header the column header
   * @return this instance
   */
  @Deprecated
  public LegacyTwinColGrid<T> addColumn(
      final ItemLabelGenerator<T> itemLabelGenerator, final String header) {
    addColumn(itemLabelGenerator).setHeader(header);
    return this;
  }

  /**
   * Adds a new sortable text column to this {@link Grid} with a value provider. The column will use
   * a {@link TextRenderer}. The value is converted to a String using the provided {@code
   * itemLabelGenerator}.
   *
   * @deprecated Use
   *             {@link #addColumn(ItemLabelGenerator)}{@code .setHeader(header).setComparator(comparator)}
   *
   * @param itemLabelGenerator the value provider
   * @param comparator the in-memory comparator
   * @param header the column header
   * @return this instance
   */
  @Deprecated
  public LegacyTwinColGrid<T> addSortableColumn(
      final ItemLabelGenerator<T> itemLabelGenerator,
      Comparator<T> comparator,
      final String header) {
    addColumn(itemLabelGenerator)
    .setHeader(header)
    .setComparator(comparator)
    .setSortable(true);
    return this;
  }

  /**
   * Adds a new sortable text column to this {@link Grid} with a value provider. The column will use
   * a {@link TextRenderer}. The value is converted to a String using the provided {@code
   * itemLabelGenerator}.
   *
   * @deprecated Use
   *             {@link #addColumn(ItemLabelGenerator)}{@code .setHeader(header).setComparator(comparator).setKey(key)}
   *
   * @param itemLabelGenerator the value provider
   * @param comparator the in-memory comparator
   * @param header the column header
   * @param header the column key
   * @return this instance
   */
  @Deprecated
  public LegacyTwinColGrid<T> addSortableColumn(
      final ItemLabelGenerator<T> itemLabelGenerator,
      Comparator<T> comparator,
      final String header,
      final String key) {
    addColumn(itemLabelGenerator)
    .setHeader(header)
    .setComparator(comparator)
    .setSortable(true)
    .setKey(key);
    return this;
  }
}
