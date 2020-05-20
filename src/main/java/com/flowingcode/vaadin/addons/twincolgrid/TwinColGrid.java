/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2020 Flowing Code
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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridNoneSelectionModel;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.shared.Registration;

@SuppressWarnings("serial")
public class TwinColGrid<T> extends VerticalLayout
		implements HasValue<ValueChangeEvent<Set<T>>, Set<T>>, HasComponents, HasSize {

	protected final Grid<T> leftGrid = new Grid<>();

	protected final Grid<T> rightGrid = new Grid<>();

	protected ListDataProvider<T> leftGridDataProvider;

	protected ListDataProvider<T> rightGridDataProvider;

	private final Button addAllButton = new Button();

	private final Button addButton = new Button();

	private final Button removeButton = new Button();

	private final Button removeAllButton = new Button();

	private final VerticalLayout buttonContainer;

	private Grid<T> draggedGrid;

	private VerticalLayout leftVL;

	private VerticalLayout rightVL;

	private Label rightColumnLabel = new Label();

	private Label leftColumnLabel = new Label();

	private Label fakeButtonContainerLabel = new Label();

	/**
	 * Constructs a new TwinColGrid with an empty {@link ListDataProvider}.
	 */
	public TwinColGrid() {
		this(DataProvider.ofCollection(new LinkedHashSet<>()), null);
	}

	/**
	 * Constructs a new TwinColGrid with data provider for options.
	 *
	 * @param dataProvider the data provider, not {@code null}
	 */
	public TwinColGrid(final ListDataProvider<T> dataProvider, String caption) {
		this.setMargin(false);
		this.setPadding(false);
		if (caption != null) {
			add(new Label(caption));
		}

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

		fakeButtonContainerLabel.getElement().setProperty("innerHTML", "&nbsp;");
		fakeButtonContainerLabel.setVisible(false);
		buttonContainer = new VerticalLayout(fakeButtonContainerLabel, addAllButton, addButton, removeButton,
				removeAllButton);
		buttonContainer.setPadding(false);
		buttonContainer.setSpacing(false);
		buttonContainer.setSizeUndefined();

		leftGrid.setWidth("100%");
		rightGrid.setWidth("100%");

		addAllButton.addClickListener(e -> {
			leftGridDataProvider.getItems().stream().forEach(leftGrid.getSelectionModel()::select);
			updateSelection(new LinkedHashSet<>(leftGrid.getSelectedItems()), new HashSet<>());
		});

		addButton.addClickListener(
				e -> updateSelection(new LinkedHashSet<>(leftGrid.getSelectedItems()), new HashSet<>()));

		removeButton.addClickListener(e -> updateSelection(new HashSet<>(), rightGrid.getSelectedItems()));

		removeAllButton.addClickListener(e -> {
			rightGridDataProvider.getItems().stream().forEach(rightGrid.getSelectionModel()::select);
			updateSelection(new HashSet<>(), rightGrid.getSelectedItems());
		});

		getElement().getStyle().set("display", "flex");
		leftColumnLabel.setVisible(false);
		rightColumnLabel.setVisible(false);
		leftVL = new VerticalLayout(leftColumnLabel, leftGrid);
		rightVL = new VerticalLayout(rightColumnLabel, rightGrid);
		leftVL.setSizeFull();
		leftVL.setMargin(false);
		leftVL.setPadding(false);
		leftVL.setSpacing(false);
		rightVL.setSizeFull();
		rightVL.setMargin(false);
		rightVL.setPadding(false);
		rightVL.setSpacing(false);
		HorizontalLayout hl = new HorizontalLayout(leftVL, buttonContainer, rightVL);
		hl.setMargin(false);
		hl.setWidthFull();
		add(hl);
		setSizeUndefined();
	}

	public void setItems(Collection<T> items) {
		setDataProvider(DataProvider.ofCollection(items));
	}

	public void setItems(Stream<T> items) {
		setDataProvider(DataProvider.fromStream(items));
	}
	
	public void setLeftGridClassName(String classname) {
		leftGrid.setClassName(classname);
	}

	public void addLeftGridClassName(String classname) {
		leftGrid.addClassName(classname);
	}

	public void removeLeftGridClassName(String classname) {
		leftGrid.removeClassName(classname);
	}

	public void setRightGridClassName(String classname) {
		rightGrid.setClassName(classname);
	}

	public void addRightGridClassName(String classname) {
		rightGrid.addClassName(classname);
	}

	public void removeRightGridClassName(String classname) {
		rightGrid.removeClassName(classname);
	}

	private void setDataProvider(ListDataProvider<T> dataProvider) {
		this.leftGridDataProvider = dataProvider;
		leftGrid.setDataProvider(dataProvider);
		if (rightGridDataProvider != null) {
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
	 */
	public TwinColGrid<T> withRightColumnCaption(final String rightColumnCaption) {
		rightColumnLabel.setText(rightColumnCaption);
		rightColumnLabel.setVisible(true);
		fakeButtonContainerLabel.setVisible(true);
		return this;
	}

	/**
	 * Sets the text shown above the left column. {@code null} clears the caption.
	 *
	 * @param leftColumnCaption The text to show, {@code null} to clear
	 */
	public TwinColGrid<T> withLeftColumnCaption(final String leftColumnCaption) {
		leftColumnLabel.setText(leftColumnCaption);
		leftColumnLabel.setVisible(true);
		fakeButtonContainerLabel.setVisible(true);
		return this;
	}

	/**
	 * Adds a new text column to this {@link Grid} with a value provider. The column
	 * will use a {@link TextRenderer}. The value is converted to a String using
	 * {@link Object#toString()}. In-memory sorting will use the natural ordering of
	 * elements if they are mutually comparable and otherwise fall back to comparing
	 * the string representations of the values.
	 *
	 * @param valueProvider the value provider
	 *
	 * @return the new column
	 */
	public TwinColGrid<T> addColumn(final ItemLabelGenerator<T> itemLabelGenerator, final String header) {
		leftGrid.addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header);
		rightGrid.addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header);
		return this;
	}

	public TwinColGrid<T> addSortableColumn(final ItemLabelGenerator<T> itemLabelGenerator, Comparator<T> comparator, final String header) {
		leftGrid.addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header).setComparator(comparator).setSortable(true);
		rightGrid.addColumn(new TextRenderer<>(itemLabelGenerator)).setHeader(header).setComparator(comparator).setSortable(true);;
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
		boolean atLeastOneIsVisible = removeButton.isVisible() || addButton.isVisible() || removeAllButton.isVisible()
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
		return rightColumnLabel.getText();
	}

	/**
	 * Returns the text shown above the left column.
	 *
	 * @return The text shown or {@code null} if not set.
	 */
	public String getLeftColumnCaption() {
		return leftColumnLabel.getText();
	}

	@Override
	public void setValue(final Set<T> value) {
		Objects.requireNonNull(value);
		final Set<T> newValues = value.stream().map(Objects::requireNonNull)
				.collect(Collectors.toCollection(LinkedHashSet::new));
		updateSelection(newValues, new LinkedHashSet<>(leftGrid.getSelectedItems()));
	}

	/**
	 * Returns the current value of this object which is an immutable set of the
	 * currently selected items.
	 *
	 * @return the current selection
	 */
	@Override
	public Set<T> getValue() {
		return Collections.unmodifiableSet(new LinkedHashSet<>(rightGridDataProvider.getItems()));
	}

	@Override
	public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<Set<T>>> listener) {
		return rightGridDataProvider.addDataProviderListener(e -> {
			ComponentValueChangeEvent<TwinColGrid<T>, Set<T>> e2 = new ComponentValueChangeEvent<>(TwinColGrid.this,
					TwinColGrid.this, null, true);
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
		leftGrid.setSelectionMode(readOnly ? SelectionMode.NONE : SelectionMode.MULTI);
		rightGrid.setSelectionMode(readOnly ? SelectionMode.NONE : SelectionMode.MULTI);
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

		final Set<T> draggedItems = new LinkedHashSet<>();

		sourceGrid.setRowsDraggable(true);
		sourceGrid.addDragStartListener(event -> {
			draggedGrid = sourceGrid;
			if (!(draggedGrid.getSelectionModel() instanceof GridNoneSelectionModel)) {
				draggedItems.addAll(event.getDraggedItems());
			}
			targetGrid.setDropMode(GridDropMode.ON_GRID);
		});

		sourceGrid.addDragEndListener(event -> {
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
		});

		targetGrid.addDropListener(event -> {
			final ListDataProvider<T> dragGridTargetDataProvider = (ListDataProvider<T>) event.getSource()
					.getDataProvider();
			dragGridTargetDataProvider.getItems().addAll(draggedItems);
			dragGridTargetDataProvider.refreshAll();
		});
	}

	public void addLeftGridSelectionListener(SelectionListener<Grid<T>, T> listener) {
		leftGrid.addSelectionListener(listener);
	}

	public void addRightGridSelectionListener(SelectionListener<Grid<T>, T> listener) {
		rightGrid.addSelectionListener(listener);
	}

}
