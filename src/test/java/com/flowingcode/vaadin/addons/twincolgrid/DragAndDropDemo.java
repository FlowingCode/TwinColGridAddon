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

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
@PageTitle("Drag and Drop")
@DemoSource
@Route(value = "twincolgrid/drag-and-drop", layout = TwincolDemoView.class)
public class DragAndDropDemo extends VerticalLayout {

  private final Set<Book> selectedBooks = new HashSet<>();
  private final List<Book> availableBooks = new ArrayList<>();

  private TwinColGrid<Book> twinColGrid;

  public DragAndDropDemo() {
    initializeData();

    twinColGrid =
        new TwinColGrid<>(availableBooks)
            .withAvailableGridCaption("Available books")
            .withSelectionGridCaption("Added books")
            .withoutAddAllButton()
            .withSizeFull()
            .withDragAndDropSupport()
            .withSelectionGridReordering()
            .selectRowOnClick();

    twinColGrid.addColumn(Book::getIsbn).setComparator(Book::getIsbn).setHeader("ISBN");
    twinColGrid.addColumn(Book::getTitle).setComparator(Book::getTitle).setHeader("Title");

    twinColGrid.setCaption("TwinColGrid demo with drag and drop support");
    twinColGrid.setValue(selectedBooks);

    final Label countLabel = new Label("Selected items in left grid: 0");
    twinColGrid
        .getAvailableGrid()
        .addSelectionListener(
            e ->
                countLabel.setText(
                    "Selected items in left grid: " + e.getAllSelectedItems().size()));
    twinColGrid.addValueChangeListener(e -> countLabel.setText("Selected items in left grid: 0"));

    add(twinColGrid, countLabel);

    addReorderingToggle();
    setSizeFull();
  }

  private void initializeData() {
    selectedBooks.add(new Book("1478375108", "Vaadin Recipes"));
    selectedBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 "));

    availableBooks.add(new Book("1478375108", "Vaadin Recipes"));
    availableBooks.add(new Book("9781849515221", "Learning Vaadin"));
    availableBooks.add(
        new Book("9781782162261", "Vaadin 7 UI Design By Example: Beginner\u2019s Guide"));
    availableBooks.add(new Book("9781849518802", "Vaadin 7 Cookbook"));
    availableBooks.add(new Book("9526800605", "Book of Vaadin: 7th Edition, 1st Revision"));
    availableBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 "));
    availableBooks.add(new Book("9529267533", "Book of Vaadin"));
    availableBooks.add(new Book("1782169776", "Learning Vaadin 7, Second Edition"));
  }

  private void addReorderingToggle() {
    Checkbox checkbox = new Checkbox("Selection Grid reordering allowed", true);
    Span description = new Span("(Reordering is disabled while the grid is sorted)");
    description.setVisible(false);

    SerializableRunnable refresh =
        () -> {
          boolean sorted = !twinColGrid.getSelectionGrid().getSortOrder().isEmpty();
          boolean allowed = twinColGrid.isSelectionGridReorderingAllowed();
          description.setVisible(sorted && allowed);
        };

    checkbox.addValueChangeListener(
        ev -> {
          twinColGrid.setSelectionGridReorderingAllowed(ev.getValue());
          refresh.run();
        });

    twinColGrid.getSelectionGrid().addSortListener(ev -> refresh.run());
    add(new Div(checkbox, description));
  }
}
