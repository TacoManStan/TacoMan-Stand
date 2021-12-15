package com.taco.suit_lady.view.ui.ui_internal.contents.dummy_instances;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.Content;
import org.jetbrains.annotations.NotNull;

public class DummyInstancesContent extends Content<DummyInstancesContentData, DummyInstancesContentController>
{
    public DummyInstancesContent(@NotNull Springable springable)
    {
        super(springable);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected @NotNull DummyInstancesContentData loadData()
    {
        return new DummyInstancesContentData();
    }
    
    @Override
    protected @NotNull Class<DummyInstancesContentController> controllerDefinition()
    {
        return DummyInstancesContentController.class;
    }
    
    @Override
    protected void onActivate() { }
    
    @Override
    protected void onDeactivate() { }
    
    @Override
    protected void onShutdown() { }
    
    //</editor-fold>
}
