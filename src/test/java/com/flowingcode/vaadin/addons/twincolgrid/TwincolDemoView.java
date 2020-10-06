package com.flowingcode.vaadin.addons.twincolgrid;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route("twincolgrid")
@CssImport("styles/shared-styles.css")
public class TwincolDemoView extends VerticalLayout {

	public TwincolDemoView() {
		Tabs tabs = new Tabs();
		Tab demo1 = new Tab("Bound");
		Tab demo2 = new Tab("Filterable");
		Tab demo3 = new Tab("Drag and Drop");


		tabs.add(demo1, demo2, demo3);
		add(tabs, new BoundTCGDemo());
		tabs.setSelectedTab(demo1);

		setSizeFull();

		tabs.addSelectedChangeListener(e -> {
			this.removeAll();
			switch (e.getSelectedTab().getLabel()) {
			case "Bound":
				add(tabs, new BoundTCGDemo());
				break;
			case "Filterable":
				add(tabs, new FilterableTCGDemo());
				break;
			case "Drag and Drop":
				add(tabs, new DragAndDropTCGDemo());
				break;
			default:
				add(tabs, new BoundTCGDemo());
				break;
			}
		});
	}
}
