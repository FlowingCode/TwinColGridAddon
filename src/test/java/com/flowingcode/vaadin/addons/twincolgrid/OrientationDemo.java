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
import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid.Orientation;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
@PageTitle("Orientation")
@DemoSource
@Route(value = "twincolgrid/orientation", layout = TwincolDemoView.class)
public class OrientationDemo extends VerticalLayout {

  private final Set<Book> selectedBooks = new HashSet<>();
  private final List<Book> availableBooks = new ArrayList<>();

  public OrientationDemo() {
    initializeData();

    final TwinColGrid<Book> twinColGrid =
        new TwinColGrid<>(availableBooks)
            .withAvailableGridCaption("Available books")
            .withSelectionGridCaption("Added books")
            .withSizeFull()
            .selectRowOnClick()
            .withOrientation(Orientation.VERTICAL);

    twinColGrid.addColumn(Book::getIsbn).setComparator(Book::getIsbn).setHeader("ISBN");
    twinColGrid.addColumn(Book::getTitle).setComparator(Book::getTitle).setHeader("Title");
    twinColGrid.setValue(selectedBooks);

    FormLayout formLayout = new FormLayout();
    Select<TwinColGrid.Orientation> orientationField = new Select<>();
    orientationField.setItems(Orientation.values());
    orientationField.addValueChangeListener(ev -> twinColGrid.withOrientation(ev.getValue()));
    orientationField.setValue(twinColGrid.getOrientation());
    orientationField.setWidth("225px");
    formLayout.addFormItem(orientationField, "Orientation");

    add(formLayout, twinColGrid);
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
}
