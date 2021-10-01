package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.Springable;
import com.taco.suit_lady.view.ui.ui_internal.controllers.UIPageController;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class UIPage<U extends UIPageController<?>>
		implements Displayable, Springable
{

	private final ReadOnlyObjectWrapper<UINode> ownerProperty;
	private final ReadOnlyObjectWrapper<U> controllerProperty;

	public UIPage(UINode owner) {
		this.ownerProperty = new ReadOnlyObjectWrapper<>(owner);
		this.controllerProperty = new ReadOnlyObjectWrapper<>();
	}
	
	@Override
	public FxWeaver weaver()
	{
		return getOwner().weaver();
	}
	
	@Override
	public ConfigurableApplicationContext ctx()
	{
		return getOwner().ctx();
	}
	
	@Override
	public Pane getContent()
	{
		return getController().root();
	}
	
	//<editor-fold desc="Properties">

	public ReadOnlyObjectProperty<UINode> ownerProperty() {
		return ownerProperty.getReadOnlyProperty();
	}

	public UINode getOwner() {
		return ownerProperty.get();
	}

	//

	public ReadOnlyObjectProperty<U> controllerProperty() {
		return controllerProperty.getReadOnlyProperty();
	}

	public U getController() {
		return controllerProperty.get();
	}

	protected void setController(U controller) {
		controllerProperty.set(controller);
	}

	//</editor-fold>

	//<editor-fold desc="Abstract">

	//</editor-fold>
}
