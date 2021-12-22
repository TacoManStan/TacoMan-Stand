package com.taco.suit_lady.ui.ui_internal.controllers;

import com.taco.suit_lady.util.springable.Springable;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class Controller
        implements Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    public Controller(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        this.weaver = weaver;
        this.ctx = ctx;
    }
    
    //
    
    //<editor-fold desc="--- SPRING ---">
    
    @Override
    public @NotNull FxWeaver weaver()
    {
        return this.weaver;
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx()
    {
        return this.ctx;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    public abstract Pane root();
    
    public abstract void initialize();
    
    //</editor-fold>
}