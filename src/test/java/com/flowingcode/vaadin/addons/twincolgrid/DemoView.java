package com.flowingcode.vaadin.addons.twincolgrid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route("")
public class DemoView extends Div {

	public DemoView() {
		final VerticalLayout container = new VerticalLayout();

		final Set<Book> selectedBooks = new HashSet<>();
		selectedBooks.add(new Book("1478375108", "Vaadin Recipes"));
		selectedBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 "));
		final Library library = new Library("Public Library", selectedBooks);

		final List<Book> availableBooks = new ArrayList<>();
		availableBooks.add(new Book("1478375108", "Vaadin Recipes"));
		availableBooks.add(new Book("9781849515221", "Learning Vaadin"));
		availableBooks.add(new Book("9781782162261", "Vaadin 7 UI Design By Example: Beginner’s Guide"));
		availableBooks.add(new Book("9781849518802", "Vaadin 7 Cookbook"));
		availableBooks.add(new Book("9526800605", "Book of Vaadin: 7th Edition, 1st Revision"));
		availableBooks.add(new Book("9789526800677", "Book of Vaadin: Volume 2 "));
		availableBooks.add(new Book("9529267533", "Book of Vaadin"));
		availableBooks.add(new Book("1782169776", "Learning Vaadin 7, Second Edition"));

		final TwinColGrid<Book> bindedTwinColGrid = new TwinColGrid<>("TwinColGrid binding demo", availableBooks)
				.addColumn(Book::getIsbn, "ISBN").addColumn(Book::getTitle, "Title")
				.withLeftColumnCaption("Available books").withRightColumnCaption("Added books").showRemoveAllButton()
				.withSizeFull();
		// .withRows(availableBooks.size() - 3);

		final HorizontalLayout bindedTwinColGridContainer = new HorizontalLayout(bindedTwinColGrid);
		bindedTwinColGridContainer.setSizeFull();
		bindedTwinColGridContainer.setMargin(true);
		container.add(bindedTwinColGridContainer);

		final Binder<Library> binder = new Binder<>();
		binder.bind(bindedTwinColGrid, Library::getBooks, Library::setBooks);
		binder.setBean(library);

		final TwinColGrid<Book> twinColGrid = new TwinColGrid<>("TwinColGrid no binding demo and drag and drop support",
				availableBooks).addColumn(Book::getIsbn, "ISBN").addColumn(Book::getTitle, "Title")
						.withLeftColumnCaption("Available books").withRightColumnCaption("Added books")
						.showAddAllButton().withSizeFull()
						// .withRows(availableBooks.size() - 3)
						.withDragAndDropSupport();
		twinColGrid.setValue(selectedBooks);

		final HorizontalLayout twinColGridContainer = new HorizontalLayout(twinColGrid);
		twinColGridContainer.setWidth("100%");
		twinColGridContainer.setMargin(false);

		final Label countLabel = new Label("Selected items: 0");
//		twinColGrid.addLeftGridSelectionListener(
//				e -> countLabel.setValue("Selected items: " + e.getAllSelectedItems().size()));
		twinColGrid.addValueChangeListener(e -> countLabel.setText("Selected items: 0"));

		final VerticalLayout bottom = new VerticalLayout(twinColGridContainer, countLabel);
		container.add(bottom);

		add(container);
	}
}
