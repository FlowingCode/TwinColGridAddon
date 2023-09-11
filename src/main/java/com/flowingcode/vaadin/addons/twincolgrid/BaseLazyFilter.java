package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.data.provider.SortOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseLazyFilter<T> implements LazyFilter<T> {

  private Collection<T> selectedItems = new HashSet<>();

  private List<SortOrder<String>> sorting = new ArrayList<>();

}
