package com.taco.suit_lady.view.ui.ui_internal.contents_new;

import com.taco.suit_lady.util.springable.Springable;
import org.jetbrains.annotations.NotNull;

public class TestControllableContentNew extends ControllableContentNew<TestContentNewController>
{
    public TestControllableContentNew(@NotNull Springable springable)
    {
        super(springable);
    }
    
    @Override
    protected void onSet() { }
    
    @Override
    protected void onRemoved() { }
    
    @Override
    protected @NotNull Class<TestContentNewController> controllerDefinition()
    {
        return TestContentNewController.class;
    }
}
