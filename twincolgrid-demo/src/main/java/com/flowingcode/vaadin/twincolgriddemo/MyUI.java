package com.flowingcode.vaadin.twincolgriddemo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Binder;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
public class MyUI extends UI {

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
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
                .addColumn(Book::getIsbn, "ISBN")
                .addColumn(Book::getTitle, "Title")
                .withLeftColumnCaption("Available books")
                .withRightColumnCaption("Added books")
                .withRows(availableBooks.size() - 3)
                .showRemoveAllButton()
                .withSizeFull();

        final HorizontalLayout bindedTwinColGridContainer = new HorizontalLayout(bindedTwinColGrid);
        bindedTwinColGridContainer.setSizeFull();
        bindedTwinColGridContainer.setMargin(true);
        container.addComponent(new Panel(bindedTwinColGridContainer));

        final Binder<Library> binder = new Binder<>();
        binder.bind(bindedTwinColGrid, Library::getBooks, Library::setBooks);
        binder.setBean(library);

        final TwinColGrid<Book> twinColGrid = new TwinColGrid<>("TwinColGrid no binding demo and drag and drop support", availableBooks)
                .addColumn(Book::getIsbn, "ISBN")
                .addColumn(Book::getTitle, "Title")
                .withLeftColumnCaption("Available books")
                .withRightColumnCaption("Added books")
                .withRows(availableBooks.size() - 3)
                .showAddAllButton()
                .withSizeFull()
                .withDragAndDropSupport();
        twinColGrid.setValue(selectedBooks);

        final Label countLabel = new Label("Selected items: 0");
        twinColGrid.addLeftGridSelectionListener(e -> countLabel.setValue("Selected items: " + e.getAllSelectedItems().size()));
        twinColGrid.addValueChangeListener(e -> countLabel.setValue("Selected items: 0"));

        final HorizontalLayout twinColGridContainer = new HorizontalLayout(
                twinColGrid);
        twinColGridContainer.setSizeFull();
        twinColGridContainer.setMargin(false);

        final VerticalLayout bottom = new VerticalLayout(twinColGridContainer, countLabel);
        container.addComponent(new Panel(bottom));

        setContent(container);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
