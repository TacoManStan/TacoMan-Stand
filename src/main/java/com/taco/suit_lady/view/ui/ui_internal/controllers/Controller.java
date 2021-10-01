package com.taco.suit_lady.view.ui.ui_internal.controllers;

import com.taco.suit_lady.util.Springable;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class Controller
        implements Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    public Controller(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
    }
    
    //
    
    @Override
    public FxWeaver weaver()
    {
        return this.weaver;
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return this.ctx;
    }
    
    //<editor-fold desc="Properties">
    
    //
    
    //
    
    public abstract Pane root();
    
    //</editor-fold>
    
    /* *************************************************************************** *
     *                                                                             *
     * Loading                                                                     *
     *                                                                             *
     * *************************************************************************** */
    
    /* *************************************************************************** *
     *                                                                             *
     * Abstract                                                              *
     *                                                                             *
     * *************************************************************************** */
    
    /**
     * Initializes this {@code Controller}.
     */
    public abstract void initialize();
    
    /* *************************************************************************** *
     *                                                                             *
     * Misc.                                                                       *
     *                                                                             *
     * *************************************************************************** */
}