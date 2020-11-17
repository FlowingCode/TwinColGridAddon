/*-
 * #%L
 * TwinColGrid add-on
 * %%
 * Copyright (C) 2017 - 2020 Flowing Code
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

import com.flowingcode.vaadin.addons.DemoLayout;
import com.flowingcode.vaadin.addons.GithubLink;
import com.flowingcode.vaadin.addons.demo.TabbedDemo;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = "twincolgrid", layout = DemoLayout.class)
@StyleSheet("context://frontend/styles/demo-styles.css")
@GithubLink("https://github.com/FlowingCode/TwinColGridAddon")
public class TwincolDemoView extends TabbedDemo {

	private static final String BOUND_DEMO = "Bound";
	private static final String FILTERABLE_DEMO = "Filterable";
	private static final String DRAGNDROP_DEMO = "Drag and Drop";
	private static final String BOUND_SOURCE = "https://github.com/FlowingCode/TwinColGridAddon/blob/master/src/test/java/com/flowingcode/vaadin/addons/twincolgrid/BoundDemo.java";
	private static final String FILTERABLE_SOURCE = "https://github.com/FlowingCode/TwinColGridAddon/blob/master/src/test/java/com/flowingcode/vaadin/addons/twincolgrid/FilterableDemo.java";
	private static final String DRAGNDROP_SOURCE = "https://github.com/FlowingCode/TwinColGridAddon/blob/master/src/test/java/com/flowingcode/vaadin/addons/twincolgrid/DragAndDropDemo.java";

	public TwincolDemoView() {
		addDemo(new DragAndDropDemo(), DRAGNDROP_DEMO, DRAGNDROP_SOURCE);
		addDemo(new FilterableDemo(), FILTERABLE_DEMO, FILTERABLE_SOURCE);
		addDemo(new BoundDemo(), BOUND_DEMO, BOUND_SOURCE);
		setSizeFull();
	}
}
