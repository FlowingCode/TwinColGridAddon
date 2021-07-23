/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2021 Flowing Code
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

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.shared.Registration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * Expose {@link TwinColGrid} as a {@code HasValue} of {@code List}
 *
 * @author Javier Godoy
 */
@SuppressWarnings("serial")
@RequiredArgsConstructor
class TwinColGridListAdapter<T> implements HasValue<ValueChangeEvent<List<T>>, List<T>> {

  private interface IDelegate {
    boolean isEmpty();

    void clear();

    void setReadOnly(boolean readOnly);

    boolean isReadOnly();

    void setRequiredIndicatorVisible(boolean requiredIndicatorVisible);

    boolean isRequiredIndicatorVisible();
  }

  @Getter
  @AllArgsConstructor
  private final class ValueChangeEventImpl implements ValueChangeEvent<List<T>> {

    boolean isFromClient;
    List<T> value;

    @Override
    public HasValue<?, List<T>> getHasValue() {
      return TwinColGridListAdapter.this;
    }

    @Override
    public List<T> getOldValue() {
      return null;
    }

  }

  @NonNull
  @Delegate(types = IDelegate.class)
  private final TwinColGrid<T> delegate;

  @Override
  public void setValue(List<T> value) {
    delegate.setValue(new LinkedHashSet<>(value));
  }

  @Override
  public List<T> getValue() {
    return Collections.unmodifiableList(delegate.collectValue(Collectors.toList()));
  }

  @Override
  public Registration addValueChangeListener(
      ValueChangeListener<? super ValueChangeEvent<List<T>>> listener) {

    List<Registration> registrations = new ArrayList<>();

    registrations.add(delegate.addValueChangeListener(ev -> {
      List<T> value = new ArrayList<>(ev.getValue());
      ValueChangeEvent<List<T>> listEvent;
      listEvent = new ValueChangeEventImpl(ev.isFromClient(), new ArrayList<>(value));
      listener.valueChanged(listEvent);
    }));

    // sorting the grid changes its value under List::equals
    registrations.add(delegate.getRightGrid().addSortListener(ev -> {
      List<T> value = getValue();
      ValueChangeEvent<List<T>> listEvent;
      listEvent = new ValueChangeEventImpl(ev.isFromClient(), value);
      listener.valueChanged(listEvent);
    }));

    return () -> registrations.forEach(Registration::remove);
  }

  @Override
  public List<T> getEmptyValue() {
    return Collections.emptyList();
  }

}
