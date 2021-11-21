package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.Springable;
import com.taco.suit_lady.view.ui.ui_internal.controllers.UIPageController;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class UIPage<U extends UIPageController<?>>
        implements Displayable, Springable
{
    private FxWeaver weaver;
    private ConfigurableApplicationContext ctx;
    
    private final ReadOnlyObjectWrapper<U> controllerProperty;
    
    public UIPage(Springable springableParent)
    {
        this(springableParent.weaver(), springableParent.ctx());
    }
    
    public UIPage(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.controllerProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public ReadOnlyObjectProperty<U> controllerProperty()
    {
        return controllerProperty.getReadOnlyProperty();
    }
    
    public U getController()
    {
        return controllerProperty.get();
    }
    
    protected void setController(U controller)
    {
        controllerProperty.set(controller);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public FxWeaver weaver()
    {
        return weaver;
    }
    
    public void setWeaver(FxWeaver weaver)
    {
        this.weaver = weaver;
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return ctx;
    }
    
    public void setCtx(ConfigurableApplicationContext ctx)
    {
        this.ctx = ctx;
    }
    
    @Override
    public Pane getContent()
    {
        return getController().root();
    }
    
    //</editor-fold>
}
