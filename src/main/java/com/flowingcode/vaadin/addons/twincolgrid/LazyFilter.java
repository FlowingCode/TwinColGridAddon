package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.data.provider.SortOrder;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface LazyFilter<T> extends Serializable {

  /**
   * Items already selected.
   * 
   * @return
   */
  Collection<T> getSelectedItems();

  /**
   * Sorting criterias.
   * 
   * @return
   */
  List<SortOrder<String>> getSorting();

}
