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
