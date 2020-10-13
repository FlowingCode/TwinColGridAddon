package com.flowingcode.vaadin.addons.twincolgrid;

import com.flowingcode.vaadin.addons.DemoLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value = "", layout = DemoLayout.class)
public class DemoView extends VerticalLayout implements BeforeEnterObserver {

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		event.forwardTo(TwincolDemoView.class);
	}

}
