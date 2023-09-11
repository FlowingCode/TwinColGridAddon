package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.function.SerializableBiPredicate;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class BookService {

  public static final int BOOKS_COUNT = 500;

  private final List<Book> availableBooks = new ArrayList<>();

  private final SerializableBiPredicate<Book, String> isbnFilterPredicate =
      (book, filter) -> filter == null ? true
          : StringUtils.containsIgnoreCase(book.getIsbn(), filter);

  private final SerializableBiPredicate<Book, String> titleFilterPredicate =
      (book, filter) -> filter == null ? true
          : StringUtils.containsIgnoreCase(book.getTitle(), filter);


  public BookService() {
    initializeData();
  }

  private void initializeData() {
    for (int i = 0; i < BOOKS_COUNT; i++) {
      availableBooks.add(new Book(RandomStringUtils.randomNumeric(8), "Vaadin Recipes " + i,
          (int) (Math.random() * 1000)));
    }
  }

  @SuppressWarnings("unchecked")
  public Stream<Book> fetch(int offset, int limit, BookFilter bookFilter) {
    List<Book> filtered = availableBooks.stream()
        .filter(book -> !bookFilter.getSelectedItems().contains(book))
        .filter(book -> isbnFilterPredicate.test(book, bookFilter.getIsbn()))
        .filter(book -> titleFilterPredicate.test(book, bookFilter.getTitle()))
        .collect(Collectors.toList());

    Comparator<Book> combinedComparator = bookFilter.getSorting().stream()
        .map(sorting -> {
          Comparator<Book> comparator = Comparator
              .<Book, Comparable>comparing(item -> toComparable(item, sorting.getSorted()));
          return SortDirection.ASCENDING.equals(sorting.getDirection()) ? comparator
              : comparator.reversed();
        })
        .reduce(Comparator::thenComparing)
        .orElse((a, b) -> 0);

    return filtered.subList(offset, Math.min(filtered.size(), offset + limit)).stream()
        .sorted(combinedComparator);
  }

  private Comparable<?> toComparable(Book book, String fieldName) {
    Field sortByField;
    try {

      sortByField = Book.class.getDeclaredField(fieldName);
      sortByField.setAccessible(true);
      Object fieldValue = sortByField.get(book);

      // This check still passes if the type of fieldValue implements Comparable<U>,
      // where U is an unrelated type from the type of fieldValue, but this is the
      // best we can do here, since we don't know the type of field at compile time
      if (!(fieldValue instanceof Comparable) && fieldValue != null) {
        return null;
      }
      return (Comparable<?>) fieldValue;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public int count(BookFilter bookFilter) {
    return (int) availableBooks.stream()
        .filter(book -> !bookFilter.getSelectedItems().contains(book))
        .filter(book -> isbnFilterPredicate.test(book, bookFilter.getIsbn()))
        .filter(book -> titleFilterPredicate.test(book, bookFilter.getTitle()))
        .count();
  }

  public Book getAny() {
    int index = (int) (Math.random() * (BookService.BOOKS_COUNT - 1));
    return availableBooks.get(index);
  }
}
