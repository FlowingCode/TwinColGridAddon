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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.SortOrder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@PageTitle("Lazy Filterable")
@Route(value = "twincolgrid/lazyfilterable", layout = TwincolDemoView.class)
public class LazyFilterableDemo extends VerticalLayout {

  public LazyFilterableDemo() {
    BookService bookService = new BookService();

    DataProvider<Book, BookFilter> availableDataProvider =
        DataProvider.fromFilteringCallbacks(
            query -> {
              BookFilter filter = query.getFilter().orElseGet(BookFilter::new);
              filter.setSorting(query.getSortOrders().stream()
                  .map(q -> new SortOrder<>(q.getSorted(), q.getDirection()))
                  .collect(Collectors.toList()));
              return bookService.fetch(query.getOffset(), query.getLimit(), filter);
            },
            query -> bookService.count(query.getFilter().orElseGet(BookFilter::new)));

    Grid<Book> availableGrid = new Grid<>();
    availableGrid.setDataProvider(availableDataProvider);

    Grid<Book> selectionGrid = new Grid<>();
    BookFilter bookFilter = new BookFilter();

    final TwinColGrid<Book> twinColGrid =
        new TwinColGrid<>(availableGrid, selectionGrid)
            .withAvailableGridCaption("Available books")
            .withSelectionGridCaption("Added books")
            .withDragAndDropSupport()
            .withSelectionGridReordering()
            .withSizeFull();

    TwinColumn<Book> isbnColumn = twinColGrid.addColumn(Book::getIsbn).setHeader("ISBN")
        .setSortable(true).setSortProperty("isbn");
    LazyFilterableColumn<Book> isbnFilterableColumn =
        new LazyFilterableColumn<>(isbnColumn, "ISBN Filter", true, bookFilter::setIsbn,
            (item, filter) -> StringUtils.isBlank(filter)
                || StringUtils.containsIgnoreCase(item.getIsbn(), filter));

    TwinColumn<Book> titleColumn =
        twinColGrid.addColumn(Book::getTitle).setHeader("Title").setSortable(true)
            .setSortProperty("title");
    LazyFilterableColumn<Book> titleFilterableColumn =
        new LazyFilterableColumn<>(titleColumn, "Title Filter", true, bookFilter::setTitle,
            (item, filter) -> StringUtils.isBlank(filter)
                || StringUtils.containsIgnoreCase(item.getTitle(), filter));

    twinColGrid.addColumn(Book::getPrice).setHeader("Price").setSortProperty("price");

    LazyFilterConfiguration<Book> filterConfig = new LazyFilterConfiguration<>(bookFilter);
    filterConfig.addFilteredColumn(isbnFilterableColumn);
    filterConfig.addFilteredColumn(titleFilterableColumn);
    twinColGrid.withFilter(filterConfig);

    twinColGrid.setCaption("TwinColGrid demo with lazy loading, binder and drag and drop support");
    twinColGrid.setMoveItemsByDoubleClick(true);

    Set<Book> selectedBooks = new HashSet<>();
    selectedBooks.add(bookService.getAny());
    selectedBooks.add(bookService.getAny());
    selectedBooks.add(bookService.getAny());

    Binder<Library> binder = new Binder<>();
    binder.forField(twinColGrid.asList()).asRequired().bind(Library::getBooks, Library::setBooks);
    Library library = new Library("Public Library", new ArrayList<>(selectedBooks));
    binder.setBean(library);

    add(twinColGrid);
    add(new Button("Get values", ev -> {
      binder.getBean().getBooks()
          .forEach(book -> Notification.show(book.getTitle(), 3000, Position.BOTTOM_START));
    }));

    add(new Button("Clear TwinColGrid", ev -> twinColGrid.clear()));

    setSizeFull();
  }

}
