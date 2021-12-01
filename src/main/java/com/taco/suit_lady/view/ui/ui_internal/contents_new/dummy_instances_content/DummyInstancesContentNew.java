package com.taco.suit_lady.view.ui.ui_internal.contents_new.dummy_instances_content;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.Sidebar;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.ui_internal.AppUI;
import com.taco.suit_lady.view.ui.ui_internal.contents_new.ControllableContentNew;
import org.jetbrains.annotations.NotNull;

public class DummyInstancesContentNew extends ControllableContentNew<DummyInstancesContentNewController>
{
    public DummyInstancesContentNew(@NotNull Springable springable)
    {
        super(springable);
        initBookshelves();
    }
    
    private void initBookshelves()
    {
        FXTools.get().runFX(() -> {
            final Sidebar sidebar = ctx().getBean(AppUI.class).getSidebar();
            // TODO - SidebarBookshelf Implementations Go Here
        }, true);
    }
    
    @Override
    protected void onSet() { }
    
    @Override
    protected void onRemoved() { }
    
    @Override
    protected @NotNull Class<DummyInstancesContentNewController> controllerDefinition()
    {
        return DummyInstancesContentNewController.class;
    }
}
