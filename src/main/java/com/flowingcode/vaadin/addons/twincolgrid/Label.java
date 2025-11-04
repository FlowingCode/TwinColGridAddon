/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2025 Flowing Code
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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;

@Tag(Tag.LABEL)
class Label extends Component implements HasText, HasStyle {

  public Label() {}

  public Label(String text) {
    getElement().appendChild(new Text(text).getElement());
  }

  public void setFor(Component forComponent) {
    if (forComponent == null) {
      throw new IllegalArgumentException("The provided component cannot be null");
    }
    setFor(forComponent.getId()
        .orElseThrow(() -> new IllegalArgumentException("The provided component must have an id")));
  }

  public void setFor(String forId) {
    getElement().setAttribute("for", forId);
  }

}
