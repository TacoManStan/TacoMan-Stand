package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.ExampleSidebarController;
import org.jetbrains.annotations.NotNull;

public class ExamplePage extends UIPage<ExampleSidebarController>
{
	private String color;

	public ExamplePage(UINode owner, String color) {
		super(owner, color);
	}
	
	public String getColor()
	{
		return this.color;
	}
	
	@Override
	protected void initializePage(Object[] constructorParams)
	{
		this.color = (String) constructorParams[0];
	}
	
	@Override
	protected @NotNull Class<ExampleSidebarController> controllerDefinition()
	{
		return ExampleSidebarController.class;
	}
}
