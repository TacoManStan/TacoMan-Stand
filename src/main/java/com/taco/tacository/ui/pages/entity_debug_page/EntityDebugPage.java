package com.taco.tacository.ui.pages.entity_debug_page;

import com.taco.tacository.ui.UIBook;
import com.taco.tacository.ui.UIPage;
import org.jetbrains.annotations.NotNull;

public class EntityDebugPage extends UIPage<EntityDebugPageController>
{
    
    public EntityDebugPage(UIBook owner)
    {
        super(owner);
    }
    
    @Override
    protected void initializePage(@NotNull Object[] constructorParams) { }
    
    @Override
    protected @NotNull Class<EntityDebugPageController> controllerDefinition()
    {
        return EntityDebugPageController.class;
    }
}

/*
 * TODO LIST:
 * [S] Make this of much higher quality.
 */