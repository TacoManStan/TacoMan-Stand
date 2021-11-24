package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.ExampleSidebarController;

public class ExamplePage extends UIPage<ExampleSidebarController>
{

	private final String color;

	public ExamplePage(UINode owner, String color) {
		super(owner);
		
		this.color = color;
	}
	
	public String getColor()
	{
		return this.color;
	}
	
	@Override
	protected Class<ExampleSidebarController> controllerDefinition()
	{
		return ExampleSidebarController.class;
	}
}
