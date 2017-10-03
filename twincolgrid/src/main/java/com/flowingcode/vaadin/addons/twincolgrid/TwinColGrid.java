package com.flowingcode.vaadin.addons.twincolgrid;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.components.grid.GridDropTarget;
import com.vaadin.ui.renderers.TextRenderer;

public final class TwinColGrid<T> extends CustomComponent implements HasValue<Set<T>> {

    private final Grid<T> leftGrid = new Grid<>();

    private final Grid<T> rightGrid = new Grid<>();

    private final ListDataProvider<T> leftGridDataProvider;

    private final ListDataProvider<T> rightGridDataProvider;

    private final Button addAllButton = new Button();

    private final Button addButton = new Button();

    private final Button removeButton = new Button();

    private final Button removeAllButton = new Button();

    private final VerticalLayout buttonContainer;

    private Grid<T> draggedGrid;

    /**
     * Constructs a new TwinColGrid with data provider for options.
     *
     * @param dataProvider the data provider, not {@code null}
     */
    public TwinColGrid(final ListDataProvider<T> dataProvider) {
        this.leftGridDataProvider = dataProvider;
        leftGrid.setDataProvider(dataProvider);

        this.rightGridDataProvider = DataProvider.ofCollection(new LinkedHashSet<>());
        rightGrid.setDataProvider(this.rightGridDataProvider);

        leftGrid.setSelectionMode(SelectionMode.MULTI);
        rightGrid.setSelectionMode(SelectionMode.MULTI);

        addButton.setIcon(VaadinIcons.ANGLE_RIGHT, ">");
        addButton.setWidth(3f, Unit.EM);
        addAllButton.setIcon(VaadinIcons.ANGLE_DOUBLE_RIGHT, ">>");
        addAllButton.setWidth(3f, Unit.EM);
        removeButton.setIcon(VaadinIcons.ANGLE_LEFT, "<");
        removeButton.setWidth(3f, Unit.EM);
        removeAllButton.setIcon(VaadinIcons.ANGLE_DOUBLE_LEFT, "<<");
        removeAllButton.setWidth(3f, Unit.EM);

        buttonContainer = new VerticalLayout(addButton, removeButton);
        buttonContainer.setSpacing(false);
        buttonContainer.setSizeUndefined();

        final HorizontalLayout container = new HorizontalLayout(leftGrid, buttonContainer, rightGrid);
        container.setSizeFull();
        leftGrid.setSizeFull();
        rightGrid.setSizeFull();
        container.setExpandRatio(leftGrid, 1f);
        container.setExpandRatio(rightGrid, 1f);

        addAllButton.addClickListener(e -> {
            leftGridDataProvider.getItems().stream().forEach(leftGrid.getSelectionModel()::select);
            updateSelection(new LinkedHashSet<>(leftGrid.getSelectedItems()), new HashSet<>());
        });

        addButton.addClickListener(e -> {
            updateSelection(new LinkedHashSet<>(leftGrid.getSelectedItems()), new HashSet<>());
        });

        removeButton.addClickListener(e -> {
            updateSelection(new HashSet<>(), rightGrid.getSelectedItems());
        });

        removeAllButton.addClickListener(e -> {
            rightGridDataProvider.getItems().stream().forEach(rightGrid.getSelectionModel()::select);
            updateSelection(new HashSet<>(), rightGrid.getSelectedItems());
        });

        setCompositionRoot(container);
        setSizeUndefined();
    }

    /**
     * Constructs a new TwinColGrid with the given options.
     *
     * @param options the options, cannot be {@code null}
     */
    public TwinColGrid(final Collection<T> options) {
        this(DataProvider.ofCollection(new LinkedHashSet<>(options)));
    }

    /**
     * Constructs a new TwinColGrid with caption and data provider for options.
     *
     * @param caption the caption to set, can be {@code null}
     * @param dataProvider the data provider, not {@code null}
     */
    public TwinColGrid(final String caption, final ListDataProvider<T> dataProvider) {
        this(dataProvider);
        setCaption(caption);
    }

    /**
     * Constructs a new TwinColGrid with caption and the given options.
     *
     * @param caption the caption to set, can be {@code null}
     * @param options the options, cannot be {@code null}
     */
    public TwinColGrid(final String caption, final Collection<T> options) {
        this(caption, DataProvider.ofCollection(new LinkedHashSet<>(options)));
    }

    /**
     * Returns the number of rows in the selects.
     *
     * @return the number of rows visible
     */
    public int getRows() {
        return (int) leftGrid.getHeightByRows();
    }

    /**
     * Sets the number of rows in the selects. If the number of rows is set to 0 or less, the actual number of displayed rows is determined implicitly by the
     * selects.
     * <p>
     * If a height if set (using {@link #setHeight(String)} or {@link #setHeight(float, Unit)}) it overrides the number of rows. Leave the height undefined to
     * use this method.
     *
     * @param rows the number of rows to set.
     */
    public TwinColGrid<T> withRows(int rows) {
        if (rows < 0) {
            rows = 0;
        }
        leftGrid.setHeightByRows(rows);
        rightGrid.setHeightByRows(rows);
        markAsDirty();
        return this;
    }

    /**
     * Sets the text shown above the right column. {@code null} clears the caption.
     *
     * @param rightColumnCaption The text to show, {@code null} to clear
     */
    public TwinColGrid<T> withRightColumnCaption(final String rightColumnCaption) {
        rightGrid.setCaption(rightColumnCaption);
        markAsDirty();
        return this;
    }

    /**
     * Adds a new text column to this {@link Grid} with a value provider. The column will use a {@link TextRenderer}. The value is converted to a String using
     * {@link Object#toString()}. In-memory sorting will use the natural ordering of elements if they are mutually comparable and otherwise fall back to
     * comparing the string representations of the values.
     *
     * @param valueProvider the value provider
     *
     * @return the new column
     */
    public <V> TwinColGrid<T> addColumn(final ValueProvider<T, V> valueProvider, final String caption) {
        leftGrid.addColumn(valueProvider, new TextRenderer()).setCaption(caption);
        rightGrid.addColumn(valueProvider, new TextRenderer()).setCaption(caption);
        return this;
    }

    public TwinColGrid<T> showAddAllButton() {
        buttonContainer.addComponent(addAllButton, 0);
        return this;
    }

    public TwinColGrid<T> showRemoveAllButton() {
        buttonContainer.addComponent(removeAllButton, buttonContainer.getComponentCount());
        return this;
    }

    public TwinColGrid<T> withSizeFull() {
        super.setSizeFull();
        return this;
    }

    /**
     * Adds drag n drop support between grids.
     * 
     * @return
     */
    public TwinColGrid<T> withDragAndDropSupport() {
        configDragAndDrop(leftGrid, rightGrid);
        configDragAndDrop(rightGrid, leftGrid);
        return this;
    }

    /**
     * Returns the text shown above the right column.
     *
     * @return The text shown or {@code null} if not set.
     */
    public String getRightColumnCaption() {
        return rightGrid.getCaption();
    }

    /**
     * Sets the text shown above the left column. {@code null} clears the caption.
     *
     * @param leftColumnCaption The text to show, {@code null} to clear
     */
    public TwinColGrid<T> withLeftColumnCaption(final String leftColumnCaption) {
        leftGrid.setCaption(leftColumnCaption);
        markAsDirty();
        return this;
    }

    /**
     * Returns the text shown above the left column.
     *
     * @return The text shown or {@code null} if not set.
     */
    public String getLeftColumnCaption() {
        return leftGrid.getCaption();
    }

    @Override
    public void setValue(final Set<T> value) {
        Objects.requireNonNull(value);
        final Set<T> newValues = value.stream().map(Objects::requireNonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        updateSelection(newValues, new LinkedHashSet<>(leftGrid.getSelectedItems()));
    }

    /**
     * Returns the current value of this object which is an immutable set of the currently selected items.
     *
     * @return the current selection
     */
    @Override
    public Set<T> getValue() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(rightGridDataProvider.getItems()));
    }

    @Override
    public Registration addValueChangeListener(final ValueChangeListener<Set<T>> listener) {
        return rightGridDataProvider.addDataProviderListener(
                e -> {
                    listener.valueChange(new ValueChangeEvent<>(TwinColGrid.this, new LinkedHashSet<>(rightGridDataProvider.getItems()), true));
                });
    }

    @Override
    public boolean isReadOnly() {
        return super.isReadOnly();
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return super.isRequiredIndicatorVisible();
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    @Override
    public void setRequiredIndicatorVisible(final boolean visible) {
        super.setRequiredIndicatorVisible(visible);
    }

    private void updateSelection(final Set<T> addedItems, final Set<T> removedItems) {
        leftGridDataProvider.getItems().addAll(removedItems);
        leftGridDataProvider.getItems().removeAll(addedItems);
        leftGridDataProvider.refreshAll();

        rightGridDataProvider.getItems().addAll(addedItems);
        rightGridDataProvider.getItems().removeAll(removedItems);
        rightGridDataProvider.refreshAll();

        leftGrid.getSelectionModel().deselectAll();
        rightGrid.getSelectionModel().deselectAll();
    }

    @SuppressWarnings("unchecked")
    private void configDragAndDrop(final Grid<T> sourceGrid, final Grid<T> targetGrid) {
        final GridDragSource<T> dragSource = new GridDragSource<>(sourceGrid);
        dragSource.setEffectAllowed(EffectAllowed.MOVE);
        dragSource.setDragImage(VaadinIcons.COPY);

        final Set<T> draggedItems = new LinkedHashSet<>();

        dragSource.addGridDragStartListener(event -> {
            draggedGrid = sourceGrid;
            if (event.getComponent().getSelectedItems().isEmpty()) {
                draggedItems.addAll(event.getDraggedItems());
            } else {
                draggedItems.addAll(event.getComponent().getSelectedItems());
            }
        });

        dragSource.addGridDragEndListener(event -> {
            if (event.getDropEffect() == DropEffect.MOVE) {
                if (draggedGrid == null) {
                    draggedItems.clear();
                    return;
                }
                final ListDataProvider<T> dragGridSourceDataProvider = (ListDataProvider<T>) draggedGrid.getDataProvider();
                dragGridSourceDataProvider.getItems().removeAll(draggedItems);
                dragGridSourceDataProvider.refreshAll();

                draggedItems.clear();

                draggedGrid.deselectAll();
                draggedGrid = null;
            }
        });

        final GridDropTarget<T> dropTarget = new GridDropTarget<>(targetGrid, DropMode.ON_TOP);
        dropTarget.setDropEffect(DropEffect.MOVE);
        dropTarget.addGridDropListener(event -> {
            event.getDragSourceExtension().ifPresent(source -> {
                if (source instanceof GridDragSource && draggedGrid != event.getComponent()) {
                    final ListDataProvider<T> dragGridTargetDataProvider = (ListDataProvider<T>) event.getComponent().getDataProvider();
                    dragGridTargetDataProvider.getItems().addAll(draggedItems);
                    dragGridTargetDataProvider.refreshAll();
                } else {
                    draggedGrid = null;
                }
            });
        });
    }

    public Registration addLeftGridSelectionListener(final SelectionListener<T> listener) {
        return leftGrid.addSelectionListener(listener);
    }

    public Registration addRightGridSelectionListener(final SelectionListener<T> listener) {
        return rightGrid.addSelectionListener(listener);
    }

}