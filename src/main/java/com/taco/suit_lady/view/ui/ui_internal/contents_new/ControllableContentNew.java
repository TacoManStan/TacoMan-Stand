package com.taco.suit_lady.view.ui.ui_internal.contents_new;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.ui_internal.controllers.Controller;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;

public abstract class ControllableContentNew<C extends Controller> extends ContentNew
{
    private final C controller;
    
    public ControllableContentNew(@NotNull Springable springable)
    {
        super(springable);
        
        this.controller = ExceptionTools.nullCheckMessage(
                weaver().loadController(ExceptionTools.nullCheck(controllerDefinition(), "Controller Definition Class")),
                "Error Loading Controller of Type [" + controllerDefinition() + "] — Ensure controller class is defined in FXML file."
        );
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected C getController()
    {
        return controller;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract @NotNull Class<C> controllerDefinition();
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected Region getRoot()
    {
        return getController().root();
    }
    
    //</editor-fold>
}
