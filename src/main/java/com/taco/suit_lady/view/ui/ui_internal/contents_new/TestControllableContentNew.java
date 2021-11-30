package com.taco.suit_lady.view.ui.ui_internal.contents_new;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.util.quick.ConsoleBB;
import org.jetbrains.annotations.NotNull;

public class TestControllableContentNew extends ControllableContentNew<TestContentNewController>
{
    public TestControllableContentNew(@NotNull Springable springable)
    {
        super(springable);
    }
    
    @Override
    protected void onSet() {
        ConsoleBB.CONSOLE.print("TestControllableContentNew has been set.");
    }
    
    @Override
    protected void onRemoved() { }
    
    @Override
    protected @NotNull Class<TestContentNewController> controllerDefinition()
    {
        return TestContentNewController.class;
    }
}
