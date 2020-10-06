package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class FilterableDemo extends VerticalLayout {
	public FilterableDemo() {
		final Set<Book> selectedBooks = new HashSet<>();
		selectedBooks.add(new Book("1478375108", "Vaadin Recipes"));
		selectedBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 "));

		final List<Book> availableBooks = new ArrayList<>();
		availableBooks.add(new Book("1478375108", "Vaadin Recipes"));
		availableBooks.add(new Book("9781849515221", "Learning Vaadin"));
		availableBooks.add(new Book("9781782162261", "Vaadin 7 UI Design By Example: Beginner’s Guide"));
		availableBooks.add(new Book("9781849518802", "Vaadin 7 Cookbook"));
		availableBooks.add(new Book("9526800605", "Book of Vaadin: 7th Edition, 1st Revision"));
		availableBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 "));
		availableBooks.add(new Book("9529267533", "Book of Vaadin"));
		availableBooks.add(new Book("1782169776", "Learning Vaadin 7, Second Edition"));

		// Filterable
		final TwinColGrid<Book> twinFilterableColGrid = new TwinColGrid<>(availableBooks,
				"TwinColGrid demo with filtering support")
						.addFilterableColumn(Book::getIsbn, Book::getIsbn, "ISBN", "ISBN Filter", true)
						.addFilterableColumn(Book::getTitle, "Title", "Title filter", false)
						.withLeftColumnCaption("Available books").withRightColumnCaption("Added books")
						.withoutAddAllButton().withSizeFull();
		twinFilterableColGrid.setValue(selectedBooks);

		add(twinFilterableColGrid);
		setSizeFull();
	}
}
