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

import com.flowingcode.vaadin.addons.demo.DemoSource;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
@PageTitle("Drag and Drop")
@DemoSource(
    "https://github.com/FlowingCode/TwinColGridAddon/blob/master/src/test/java/com/flowingcode/vaadin/addons/twincolgrid/DragAndDropDemo.java")
public class DragAndDropDemo extends VerticalLayout {

  private final Set<Book> selectedBooks = new HashSet<>();
  private final List<Book> availableBooks = new ArrayList<>();

  public DragAndDropDemo() {
    initializeData();

    final TwinColGrid<Book> twinColGrid =
        new TwinColGrid<>(availableBooks, "TwinColGrid demo with drag and drop support")
            .addSortableColumn(Book::getIsbn, Comparator.comparing(Book::getIsbn), "ISBN")
            .addSortableColumn(Book::getTitle, Comparator.comparing(Book::getTitle), "Title")
            .withAvailableGridCaption("Available books")
            .withSelectionGridCaption("Added books")
            .withoutAddAllButton()
            .withSizeFull()
            .withDragAndDropSupport()
            .withSelectionGridReordering()
            .selectRowOnClick();
    twinColGrid.setValue(selectedBooks);

    final Label countLabel = new Label("Selected items in left grid: 0");
    twinColGrid.getAvailableGrid().addSelectionListener(
        e -> countLabel.setText("Selected items in left grid: " + e.getAllSelectedItems().size()));
    twinColGrid.addValueChangeListener(e -> countLabel.setText("Selected items in left grid: 0"));

    add(twinColGrid, countLabel);
    setSizeFull();
  }

  private void initializeData() {
    selectedBooks.add(new Book("1478375108", "Vaadin Recipes"));
    selectedBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 "));


    availableBooks.add(new Book("1478375108", "Vaadin Recipes"));
    availableBooks.add(new Book("9781849515221", "Learning Vaadin"));
    availableBooks
        .add(new Book("9781782162261", "Vaadin 7 UI Design By Example: Beginner\u2019s Guide"));
    availableBooks.add(new Book("9781849518802", "Vaadin 7 Cookbook"));
    availableBooks.add(new Book("9526800605", "Book of Vaadin: 7th Edition, 1st Revision"));
    availableBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 "));
    availableBooks.add(new Book("9529267533", "Book of Vaadin"));
    availableBooks.add(new Book("1782169776", "Learning Vaadin 7, Second Edition"));
  }
}
