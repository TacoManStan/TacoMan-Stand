package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.contents.DummyContentsHandler;
import com.taco.suit_lady.view.ui.ui_internal.controllers.DummyInstancesController;
import org.jetbrains.annotations.NotNull;

public class DummyInstancesPage extends UIPage<DummyInstancesController>
{
    public DummyInstancesPage(UIBook owner)
    {
        super(owner);
    }
    
    public DummyContentsHandler getHandler()
    {
        return ctx().getBean(DummyContentsHandler.class);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) {
        final DummyContentsHandler handler = getHandler();
        final DummyInstancesController controller = getController();
    }
    
    @Override
    protected @NotNull Class<DummyInstancesController> controllerDefinition()
    {
        return DummyInstancesController.class;
    }
}
