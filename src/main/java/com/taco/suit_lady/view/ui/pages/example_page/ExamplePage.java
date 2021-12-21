package com.taco.suit_lady.view.ui.pages.example_page;

import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public class ExamplePage extends UIPage<ExamplePageController>
{
	private String color;

	public ExamplePage(UIBook owner, String color) {
		super(owner, color);
	}
	
	public String getColor()
	{
		return this.color;
	}
	
	@Override
	protected void initializePage(@NotNull Object[] constructorParams)
	{
		this.color = (String) constructorParams[0];
	}
	
	@Override
	protected @NotNull Class<ExamplePageController> controllerDefinition()
	{
		return ExamplePageController.class;
	}
}
