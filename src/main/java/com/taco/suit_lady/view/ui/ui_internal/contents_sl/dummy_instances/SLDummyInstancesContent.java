package com.taco.suit_lady.view.ui.ui_internal.contents_sl.dummy_instances;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.SidebarBookshelf;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SLDummyInstancesContent extends SLContent<SLDummyInstancesContentData, SLDummyInstancesContentController>
{
    public SLDummyInstancesContent(@NotNull Springable springable)
    {
        super(springable);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @Nullable List<SidebarBookshelf> loadBookshelves()
    {
        return null;
    }
    
    @Override
    protected @NotNull SLDummyInstancesContentData loadData()
    {
        return new SLDummyInstancesContentData();
    }
    
    @Override
    protected @NotNull Class<SLDummyInstancesContentController> controllerDefinition()
    {
        return SLDummyInstancesContentController.class;
    }
    
    @Override
    protected void onActivate() { }
    
    @Override
    protected void onDeactivate() { }
    
    @Override
    protected void onShutdown() { }
    
    //</editor-fold>
}
