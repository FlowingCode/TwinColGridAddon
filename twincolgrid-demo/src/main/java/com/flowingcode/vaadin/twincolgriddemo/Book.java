package com.flowingcode.vaadin.twincolgriddemo;

import java.util.Objects;

public class Book {

    private final String isbn;

    private final String title;

    public Book(final String isbn, final String title) {
        this.isbn = isbn;
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, title);
    }

    @Override
    public boolean equals(final Object obj) {
        return ObjectUtils.equals(this, (Book) obj, Book::getIsbn, Book::getTitle);
    }

    @Override
    public String toString() {
        return "[Book " + isbn + " - " + title + "]";
    }

}
