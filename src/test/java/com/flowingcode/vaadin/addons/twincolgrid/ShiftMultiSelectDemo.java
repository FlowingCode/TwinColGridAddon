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

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class ShiftMultiSelectDemo extends VerticalLayout {

  public ShiftMultiSelectDemo() {
    final Set<Book> selectedBooks = new HashSet<>();
    selectedBooks.add(new Book("1478375108", "Vaadin Recipes"));
    selectedBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 "));
    final Library library = new Library("Public Library", selectedBooks);

    final List<Book> availableBooks = new ArrayList<>();
    availableBooks.add(new Book("1478375108", "Vaadin Recipes"));
    availableBooks.add(new Book("9781849515221", "Learning Vaadin"));
    availableBooks.add(
        new Book("9781782162261", "Vaadin 7 UI Design By Example: Beginner’s Guide"));
    availableBooks.add(new Book("9781849518802", "Vaadin 7 Cookbook"));
    availableBooks.add(new Book("9526800605", "Book of Vaadin: 7th Edition, 1st Revision"));
    availableBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 "));
    availableBooks.add(new Book("9529267533", "Book of Vaadin"));
    availableBooks.add(new Book("1782169776", "Learning Vaadin 7, Second Edition"));

    // Binded
    final TwinColGrid<Book> bindedTwinColGrid =
        new TwinColGrid<>(
                availableBooks, "TwinColGrid demo with Binder and row select without checkbox")
            .addFilterableColumn(Book::getIsbn, Book::getIsbn, "ISBN", "ISBN Filter", true)
            .withLeftColumnCaption("ISBN")
            .addSortableColumn(Book::getTitle, Comparator.comparing(Book::getTitle), "Title")
            .withLeftColumnCaption("Available books")
            .withRightColumnCaption("Added books")
            .withoutRemoveAllButton()
            .withSizeFull()
            .withShiftMultiselect();

    final Binder<Library> binder = new Binder<>();
    binder.bind(bindedTwinColGrid, Library::getBooks, Library::setBooks);
    binder.setBean(library);

    add(bindedTwinColGrid);
    setSizeFull();
  }
}
