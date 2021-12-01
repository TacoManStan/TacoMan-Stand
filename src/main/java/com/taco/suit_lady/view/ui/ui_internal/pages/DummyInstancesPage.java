package com.taco.suit_lady.view.ui.ui_internal.pages;

import com.taco.suit_lady.view.ui.UIBook;
import com.taco.suit_lady.view.ui.UIPage;
import com.taco.suit_lady.view.ui.ui_internal.contents.DummyContentsHandler;
import org.jetbrains.annotations.NotNull;

public class DummyInstancesPage extends UIPage<DummyInstancesPageController>
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
        final DummyInstancesPageController controller = getController();
    }
    
    @Override
    protected @NotNull Class<DummyInstancesPageController> controllerDefinition()
    {
        return DummyInstancesPageController.class;
    }
}
