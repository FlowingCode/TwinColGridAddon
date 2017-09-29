package com.flowingcode.vaadin.twincolgriddemo;

import java.util.Set;

public class Library {

    private final String name;

    private Set<Book> books;

    public Library(final String name, final Set<Book> books) {
        this.name = name;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(final Set<Book> books) {
        this.books = books;
    }

}
