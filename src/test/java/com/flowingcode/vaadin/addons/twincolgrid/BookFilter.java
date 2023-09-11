package com.flowingcode.vaadin.addons.twincolgrid;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookFilter extends BaseLazyFilter<Book> {

  private String isbn;

  private String title;

}
