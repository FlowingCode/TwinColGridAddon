package com.flowingcode.vaadin.twincolgriddemo;

import static org.junit.Assert.*;

import org.junit.Test;

import com.flowingcode.vaadin.twincolgriddemo.Book;
import com.flowingcode.vaadin.twincolgriddemo.ObjectUtils;

public class ObjectUtilsTest {

    @Test
    public void testEquals() {
        assertFalse(ObjectUtils.equals(null, null, Book::getIsbn, Book::getTitle));

        Book book1 = new Book("1", "Book1");
        Book book2 = new Book("1", "Book1");

        assertFalse(ObjectUtils.equals(book1, null, Book::getIsbn, Book::getTitle));

        assertFalse(ObjectUtils.equals(null, book1, Book::getIsbn, Book::getTitle));

        assertTrue(ObjectUtils.equals(book1, book2, Book::getIsbn, Book::getTitle));

        book2 = new Book("1", "Book2");
        assertFalse(ObjectUtils.equals(book1, book2, Book::getIsbn, Book::getTitle));

        book2 = new Book("2", "Book1");
        assertFalse(ObjectUtils.equals(book1, book2, Book::getIsbn, Book::getTitle));

        book1 = new Book("3324343g", "Book 1");
        book2 = new Book("332434g3", "Book 3");
        assertFalse(ObjectUtils.equals(book1, book2, Book::getIsbn, Book::getTitle));

        book1 = new Book("3324343g", "Book 1");
        book2 = new Book("3324343g", "Book1");
        assertFalse(ObjectUtils.equals(book1, book2, Book::getIsbn, Book::getTitle));
    }

}
