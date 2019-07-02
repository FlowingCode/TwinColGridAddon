package com.flowingcode.vaadin.addons.twincolgrid;

/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2018 FlowingCode S.A.
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridNoneSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;

@SuppressWarnings("serial")
public final class TwinColGrid<T> extends Div implements HasValue<ValueChangeEvent<Set<T>>,Set<T>>, HasComponents, HasSize {

    private final Grid<T> leftGrid = new Grid<>();

    private final Grid<T> rightGrid = new Grid<>();

    private ListDataProvider<T> leftGridDataProvider;

    private final ListDataProvider<T> rightGridDataProvider;

    private final Button addAllButton = new Button();

    private final Button addButton = new Button();

    private final Button removeButton = new Button();

    private final Button removeAllButton = new Button();

    private final VerticalLayout buttonContainer;

    private Grid<T> draggedGrid;

    /**
     * Constructs a new TwinColGrid with an empty {@link ListDataProvider}.
     */
    public TwinColGrid() {
    	this(DataProvider.ofCollection(new LinkedHashSet<>()));
    }
    
    /**
     * Constructs a new TwinColGrid with data provider for options.
     *
     * @param dataProvider the data provider, not {@code null}
     */
    public TwinColGrid(final ListDataProvider<T> dataProvider) {
    	setDataProvider(dataProvider);
    	
    	this.rightGridDataProvider = DataProvider.ofCollection(new LinkedHashSet<>());    	
        rightGrid.setDataProvider(this.rightGridDataProvider);

        leftGrid.setSelectionMode(SelectionMode.MULTI);        
        rightGrid.setSelectionMode(SelectionMode.MULTI);
        
        addButton.setIcon(VaadinIcon.ANGLE_RIGHT.create());
        addButton.setWidth("3em");
        addAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_RIGHT.create());
        addAllButton.setWidth("3em");
        removeButton.setIcon(VaadinIcon.ANGLE_LEFT.create());
        removeButton.setWidth("3em");
        removeAllButton.setIcon(VaadinIcon.ANGLE_DOUBLE_LEFT.create());
        removeAllButton.setWidth("3em");

        buttonContainer = new VerticalLayout(addButton, removeButton);
        buttonContainer.setSpacing(false);
        buttonContainer.setSizeUndefined();

        leftGrid.setWidth("100%");
        rightGrid.setWidth("100%");

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
        
      
//        setCompositionRoot(container);
        getElement().getStyle().set("display","flex");
        add(leftGrid, buttonContainer, rightGrid);
        setSizeUndefined();
    }

    public void setItems(Collection<T> items) {
    	setDataProvider(DataProvider.ofCollection(items));
    }
    
    public void setItems(Stream<T> items) {
    	setDataProvider(DataProvider.fromStream(items));
    }
    
    private void setDataProvider(ListDataProvider<T> dataProvider) {
    	this.leftGridDataProvider = dataProvider;
        leftGrid.setDataProvider(dataProvider);
        if (rightGridDataProvider!=null) {
        	rightGridDataProvider.getItems().clear();
        	rightGridDataProvider.refreshAll();
        }
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
//        setCaption(caption);
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

//    /**
//     * Returns the number of rows in the selects.
//     *
//     * @return the number of rows visible
//     */
//    public int getRows() {
//        return (int) leftGrid.getHeightByRows();
//    }

    /**
     * Sets the number of rows in the selects. If the number of rows is set to 0 or less, the actual number of displayed rows is determined implicitly by the
     * selects.
     * <p>
     * If a height if set (using {@link #setHeight(String)} or {@link #setHeight(float, Unit)}) it overrides the number of rows. Leave the height undefined to
     * use this method.
     *
     * @param rows the number of rows to set.
     */
//    public TwinColGrid<T> withRows(int rows) {
//        if (rows < 0) {
//            rows = 0;
//        }
//        leftGrid.setHeightByRows(rows);
//        rightGrid.setHeightByRows(rows);
////        markAsDirty();
//        return this;
//    }

    /**
     * Sets the text shown above the right column. {@code null} clears the caption.
     *
     * @param rightColumnCaption The text to show, {@code null} to clear
     */
    public TwinColGrid<T> withRightColumnCaption(final String rightColumnCaption) {
//        rightGrid.setCaption(rightColumnCaption);
//        markAsDirty();
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
        leftGrid.addColumn(new TextRenderer<>(item->String.valueOf(valueProvider.apply(item))));/*.setCaption(caption);*/
        rightGrid.addColumn(new TextRenderer<>(item->String.valueOf(valueProvider.apply(item))));
        return this;
    }

    public TwinColGrid<T> showAddAllButton() {
        buttonContainer.add(addAllButton/*, 0*/);
        return this;
    }

    public TwinColGrid<T> showRemoveAllButton() {
        buttonContainer.add(removeAllButton/*, buttonContainer.getComponentCount()*/);
        return this;
    }

    public TwinColGrid<T> withSizeFull() {
        setSizeFull();
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
        return "";/*rightGrid.getCaption();*/
    }

    /**
     * Sets the text shown above the left column. {@code null} clears the caption.
     *
     * @param leftColumnCaption The text to show, {@code null} to clear
     */
    public TwinColGrid<T> withLeftColumnCaption(final String leftColumnCaption) {
//        leftGrid.setCaption(leftColumnCaption);
//        markAsDirty();
        return this;
    }

    /**
     * Returns the text shown above the left column.
     *
     * @return The text shown or {@code null} if not set.
     */
    public String getLeftColumnCaption() {
        return "";/*leftGrid.getCaption();*/
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
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Set<T>>> listener) {
        return rightGridDataProvider.addDataProviderListener(
                e -> {
                	//ComponentValueChangeEvent<TwinColGrid<T>, Set<T>> e2 = new ComponentValueChangeEvent<>(TwinColGrid.this, new LinkedHashSet<>(rightGridDataProvider.getItems()), null, true)
                	ComponentValueChangeEvent<TwinColGrid<T>, Set<T>> e2 = new ComponentValueChangeEvent<>(TwinColGrid.this, TwinColGrid.this, null, true);
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
    	leftGrid.setSelectionMode(readOnly?SelectionMode.NONE:SelectionMode.MULTI);
    	rightGrid.setSelectionMode(readOnly?SelectionMode.NONE:SelectionMode.MULTI);
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
//        final GridDragSource<T> dragSource = new GridDragSource<>(sourceGrid);
//    	sourceGrid.setEffectAllowed(EffectAllowed.MOVE);
//    	sourceGrid.setDragImage(VaadinIcon.COPY.create());

        final Set<T> draggedItems = new LinkedHashSet<>();
        
        sourceGrid.setRowsDraggable(true);
        sourceGrid.addDragStartListener(event -> {
            draggedGrid = sourceGrid;
            if (!(draggedGrid.getSelectionModel() instanceof GridNoneSelectionModel)) {
//	            if (event.getComponent().getSelectedItems().isEmpty()) {
	                draggedItems.addAll(event.getDraggedItems());
//	            } else {
//	                draggedItems.addAll(event.getComponent().getSelectedItems());
//	            }
            }
        });

        sourceGrid.addDragEndListener(event -> {
//            if (event.getDropEffect() == DropEffect.MOVE) {
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
//            }
        });

//        final GridDropTarget<T> dropTarget = new GridDropTarget<>(targetGrid, DropMode.ON_TOP);
//        dropTarget.setDropEffect(DropEffect.MOVE);
        targetGrid.addDropListener(event -> {
//            event.getSource().getDragSourceExtension().ifPresent(source -> {
//                if (source instanceof GridDragSource && draggedGrid != event.getComponent()) {
                    final ListDataProvider<T> dragGridTargetDataProvider = (ListDataProvider<T>) event.getSource().getDataProvider();
                    dragGridTargetDataProvider.getItems().addAll(draggedItems);
                    dragGridTargetDataProvider.refreshAll();
//                } else {
//                    draggedGrid = null;
//                }
//            });
        });
    }

//    public Registration addLeftGridSelectionListener(final SelectionListener<T> listener) {
//        return leftGrid.addSelectionListener(listener);
//    }
//
//    public Registration addRightGridSelectionListener(final SelectionListener<T> listener) {
//        return rightGrid.addSelectionListener(listener);
//    }


   
}
