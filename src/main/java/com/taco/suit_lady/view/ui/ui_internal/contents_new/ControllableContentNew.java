package com.taco.suit_lady.view.ui.ui_internal.contents_new;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.ui_internal.controllers.Controller;
import org.jetbrains.annotations.NotNull;

public abstract class ControllableContentNew<C extends Controller> extends ContentNew
{
    public ControllableContentNew(@NotNull Springable springable)
    {
        super(springable);
    }
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract @NotNull Class<C> controllerDefinition();
    
    //</editor-fold>
}
