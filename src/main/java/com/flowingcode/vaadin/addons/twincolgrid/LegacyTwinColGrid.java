package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableFunction;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Supplier;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

/** Implementation of {@code TwinColGrid} with deprecated methods from version 2.9.0. */
@SuppressWarnings("serial")
@Deprecated
public class LegacyTwinColGrid<T> extends TwinColGrid<T> {

  private static final String COMPONENT_DATA_FILTER = "TwinColGrid#filterTF";
  
  /** @deprecated Use getAvailableGrid() */
  @Deprecated protected final Grid<T> leftGrid;

  /** @deprecated Use getSelectionGrid() */
  @Deprecated protected final Grid<T> rightGrid;

  /** @deprecated Use getAvailableGrid().getDataProvider() */
  @Deprecated protected ListDataProvider<T> leftGridDataProvider;

  /** @deprecated Use getSelectionGrid().getDataProvider() */
  @Deprecated protected ListDataProvider<T> rightGridDataProvider;

  private boolean explicitHeaderRow = true;
  
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
   * Adds a column to each grids. Both columns will use a {@link TextRenderer} and the value
   * will be converted to a String by using the provided {@code itemLabelGenerator}.
   *
   * @param itemLabelGenerator the value provider
   * @return the pair of columns
   */
  public TwinColumn<T> addColumn(ItemLabelGenerator<T> itemLabelGenerator) {
    createFirstHeaderRowIfNeeded();
    Column<T> availableColumn =
        getAvailableGrid().addColumn(new TextRenderer<>(itemLabelGenerator));
    Column<T> selectionColumn =
        getSelectionGrid().addColumn(new TextRenderer<>(itemLabelGenerator));
    return new TwinColumn<>(availableColumn, selectionColumn);
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

  /**
   * Adds a new filterable text column to this {@link TwinColGrid}, with a key. The value is
   * converted to a String using the provided {@code itemLabelGenerator} and matches are computed
   * against the result of {@code filterableValue}.
   *
   * @deprecated Use {@code addFilterableColumn(itemLabelGenerator, filterableValue)} and configure
   *             the other properties on the returned {@link TwinColumn}.
   */
  @Deprecated
  public LegacyTwinColGrid<T> addFilterableColumn(final ItemLabelGenerator<T> itemLabelGenerator,
      SerializableFunction<T, String> filterableValue, final String header,
      String filterPlaceholder, boolean enableClearButton, String key) {
    TwinColumn<?> column =
        addFilterableColumn(itemLabelGenerator, filterableValue).setHeader(header)
        .setFilterPlaceholder(filterPlaceholder).setClearButtonVisible(enableClearButton);
    if (key != null) {
      column.setKey(key);
    }
    return this;
  }

  /**
   * Adds a new filterable text column to this {@link TwinColGrid}, with no key. The value is
   * converted to a String using the provided {@code itemLabelGenerator} and matches are computed
   * against the result of the same {@code itemLabelGenerator}.
   *
   * @deprecated Use {@code addFilterableColumn(itemLabelGenerator)} and configure the other
   *             properties on the returned {@link TwinColumn}.
   */
  @Deprecated
  public LegacyTwinColGrid<T> addFilterableColumn(final ItemLabelGenerator<T> itemLabelGenerator,
      final String header, String filterPlaceholder, boolean enableClearButton) {
    return addFilterableColumn(itemLabelGenerator, itemLabelGenerator, header, filterPlaceholder,
        enableClearButton, null);
  }

  /**
   * Adds a new filterable text column to this {@link TwinColGrid}, with no key. The value is
   * converted to a String using the provided {@code itemLabelGenerator} and matches are computed
   * against the result of {@code filterableValue}.
   *
   * @deprecated Use {@code addFilterableColumn(itemLabelGenerator, filterableValue)} and configure
   *             the other properties on the returned {@link TwinColumn}.
   */
  @Deprecated
  public LegacyTwinColGrid<T> addFilterableColumn(ItemLabelGenerator<T> itemLabelGenerator,
      SerializableFunction<T, String> filterableValue, String header, String filterPlaceholder,
      boolean enableClearButton) {
    return addFilterableColumn(itemLabelGenerator, filterableValue, header, filterPlaceholder,
        enableClearButton, null);
  }

  /**
   * Adds a new filterable text column to this {@link TwinColGrid}, with a key. The value is
   * converted to a String using the provided {@code itemLabelGenerator} and matches are computed
   * against the result of the same {@code itemLabelGenerator}.
   *
   * @deprecated Use {@code addFilterableColumn(itemLabelGenerator)} and configure the other
   *             properties on the returned {@link TwinColumn}.
   */
  @Deprecated
  public LegacyTwinColGrid<T> addFilterableColumn(ItemLabelGenerator<T> itemLabelGenerator,
      String header, String filterPlaceholder, boolean enableClearButton, String key) {
    return addFilterableColumn(itemLabelGenerator, itemLabelGenerator, header, filterPlaceholder,
        enableClearButton, key);
  }

  public FilterableTwinColumn<T> addFilterableColumn(ItemLabelGenerator<T> itemLabelGenerator) {
    return addFilterableColumn(itemLabelGenerator, itemLabelGenerator);
  }

  private void createFirstHeaderRowIfNeeded() {
    if (explicitHeaderRow) {
      forEachGrid(grid -> {
        if (grid.getColumns().isEmpty() && grid.getHeaderRows().isEmpty()) {
          grid.appendHeaderRow();
        }
      });
    }
  }

  /**
   * Configure this component to create the first header row (for column header labels). If no
   * column will have a header, this property must be set to {@code false}.
   *
   * <p>
   * When this property is {@code true} (default), the first column added through this component
   * will {@linkplain Grid#appendHeaderRow() append} a header row, which will be the "default header
   * row" (used by {@code Column.setHeader}). If no headers are set, then the default header row
   * will be empty.
   *
   * <p>
   * When this property is {@code false}, then {@code Column.setHeader} will allocate a header row
   * when called (which prevents an empty row if no headers are set, but also replaces the filter
   * componentes).
   *
   * @param value whether the first header row will be created when a column is added.
   * @return this instance
   */
  public TwinColGrid<T> createFirstHeaderRow(boolean value) {
    explicitHeaderRow = value;
    return this;
  }

  public FilterableTwinColumn<T> addFilterableColumn(ItemLabelGenerator<T> itemLabelGenerator,
      SerializableFunction<T, String> filterableValue) {

    createFirstHeaderRowIfNeeded();

    Column<T> availableColumn =
        createFilterableColumn(availableAsEager(), itemLabelGenerator, filterableValue);
    Column<T> selectionColumn =
        createFilterableColumn(selection, itemLabelGenerator, filterableValue);

    return new FilterableTwinColumn<>(availableColumn, selectionColumn);
  }
  
  private Column<T> createFilterableColumn(EagerTwinColModel<T> side,
      ItemLabelGenerator<T> itemLabelGenerator,
      SerializableFunction<T, String> filterableValue) {
    Column<T> column = side.grid.addColumn(new TextRenderer<>(itemLabelGenerator));
    TextField filterTF = new TextField();

    filterTF.addValueChangeListener(
        event -> side.getDataProvider()
            .addFilter(
                filterableEntity -> StringUtils.containsIgnoreCase(
                    filterableValue.apply(filterableEntity), filterTF.getValue())));

    if (side.headerRow == null) {
      side.headerRow = side.grid.appendHeaderRow();
    }

    side.headerRow.getCell(column).setComponent(filterTF);

    filterTF.setValueChangeMode(ValueChangeMode.EAGER);
    filterTF.setSizeFull();

    ComponentUtil.setData(column, COMPONENT_DATA_FILTER, filterTF);
    return column;
  }

  static TextField getFilterTextField(Column<?> column) {
    return (TextField) ComponentUtil.getData(column, COMPONENT_DATA_FILTER);
  }
  
}
