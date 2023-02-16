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

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@PageTitle("Filterable")
@Route(value = "twincolgrid/filterable", layout = TwincolDemoView.class)
public class FilterableDemo extends VerticalLayout {

  private final Set<Book> selectedBooks = new HashSet<>();
  private final List<Book> availableBooks = new ArrayList<>();

  public FilterableDemo() {
    initializeData();

    final TwinColGrid<Book> twinColGrid =
        new TwinColGrid<>(availableBooks)
            .withAvailableGridCaption("Available books")
            .withSelectionGridCaption("Added books")
            .withoutAddAllButton()
            .withSizeFull();

    TwinColumn<Book> isbnColumn =
        twinColGrid.addColumn(Book::getIsbn).setHeader("ISBN").setSortable(true);
    EagerFilterableColumn<Book> isbnFilterableColumn =
        new EagerFilterableColumn<>(isbnColumn, "ISBN Filter", true,
            (item, filter) -> StringUtils.isBlank(filter)
                || StringUtils.containsIgnoreCase(item.getIsbn(), filter));

    TwinColumn<Book> titleColumn = twinColGrid.addColumn(Book::getTitle).setHeader("Title");
    EagerFilterableColumn<Book> titleFilterableColumn =
        new EagerFilterableColumn<>(titleColumn, "Title Filter", true,
            (item, filter) -> StringUtils.isBlank(filter)
                || StringUtils.containsIgnoreCase(item.getTitle(), filter));

    twinColGrid.addColumn(Book::getPrice).setHeader("Price").setSortable(true);

    EagerFilterConfiguration<Book> filterConfig = new EagerFilterConfiguration<>();
    filterConfig.addFilteredColumn(isbnFilterableColumn);
    filterConfig.addFilteredColumn(titleFilterableColumn);
    twinColGrid.withFilter(filterConfig);

    twinColGrid.setCaption("TwinColGrid demo with filtering support");
    twinColGrid.setValue(selectedBooks);

    add(twinColGrid);
    setSizeFull();
  }

  private void initializeData() {
    selectedBooks.add(new Book("1478375108", "Vaadin Recipes", 222));
    selectedBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 ", 121));

    availableBooks.add(new Book("1478375108", "Vaadin Recipes", 232));
    availableBooks.add(new Book("9781849515221", "Learning Vaadin", 333));
    availableBooks
        .add(
            new Book("9781782162261", "Vaadin 7 UI Design By Example: Beginner\u2019s Guide", 991));
    availableBooks.add(new Book("9781849518802", "Vaadin 7 Cookbook", 121));
    availableBooks.add(new Book("9526800605", "Book of Vaadin: 7th Edition, 1st Revision", 244));
    availableBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 ", 555));
    availableBooks.add(new Book("9529267533", "Book of Vaadin", 666));
    availableBooks.add(new Book("1782169776", "Learning Vaadin 7, Second Edition", 423));
  }

}
