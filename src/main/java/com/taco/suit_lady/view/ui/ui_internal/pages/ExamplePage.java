package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UINode;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.controllers.ExampleSidebarController;
import com.taco.util.quick.ConsoleBB;

public class ExamplePage extends UIPage<ExampleSidebarController>
{

	private final String color;

	public ExamplePage(UINode owner, String color) {
		super(owner);
		
		this.color = color;
		
		ConsoleBB.CONSOLE.print(ConsoleBB.PrintType.HEADER, "ExamplePage Constructor 1");
		
		final ExampleSidebarController pageController = this.weaver().loadController(ExampleSidebarController.class);
		pageController.setPage(this);
		setController(pageController);
		
		ConsoleBB.CONSOLE.print(ConsoleBB.PrintType.HEADER, "ExamplePage Constructor 2");
	}
	
	public String getColor()
	{
		return this.color;
	}
}
