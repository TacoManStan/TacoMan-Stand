package com.taco.suit_lady.view.ui.ui_internal;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.Sidebar;
import com.taco.suit_lady.view.ui.console.Console;
import com.taco.suit_lady.view.ui.ui_internal.contents_new.ContentManagerNew;
import com.taco.suit_lady.view.ui.ui_internal.contents_new.TestControllableContentNew;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppUI
        implements Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ReadOnlyObjectWrapper<AppController> controllerProperty;
    private final ReadOnlyObjectWrapper<Sidebar> sidebarProperty;
    
    private final ReadOnlyObjectWrapper<StackPane> contentStackPaneProperty;
    
    private ContentManagerNew contentManager;
    
    public AppUI(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.controllerProperty = new ReadOnlyObjectWrapper<>();
        this.sidebarProperty = new ReadOnlyObjectWrapper<>();
        
        this.contentStackPaneProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    protected void init() {
        ctx().getBean(Console.class).initialize();
        
        this.contentManager = new ContentManagerNew(getContentStackPane());
        this.contentManager.setContent(new TestControllableContentNew(this));
    }
    
    //</editor-fold>
    
    // <editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<AppController> controllerProperty()
    {
        return controllerProperty.getReadOnlyProperty();
    }
    
    public final AppController getController()
    {
        return controllerProperty.get();
    }
    
    protected final void setController(AppController controller)
    {
        controllerProperty.set(controller);
    }
    
    //
    
    public final ReadOnlyObjectProperty<Sidebar> sidebarProperty()
    {
        return sidebarProperty.getReadOnlyProperty();
    }
    
    public final Sidebar getSidebar()
    {
        return sidebarProperty.get();
    }
    
    protected final void setSidebar(Sidebar sidebar)
    {
        sidebarProperty.set(sidebar);
    }
    
    //
    
    public final ReadOnlyObjectProperty<StackPane> contentStackPaneProperty()
    {
        return contentStackPaneProperty.getReadOnlyProperty();
    }
    
    public final StackPane getContentStackPane()
    {
        return contentStackPaneProperty.get();
    }
    
    public final void setContentStackPane(StackPane contentStackPane)
    {
        contentStackPaneProperty.set(contentStackPane);
    }
    
    // </editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public FxWeaver weaver()
    {
        return weaver;
    }
    
    @Override
    public ConfigurableApplicationContext ctx()
    {
        return ctx;
    }
    
    //</editor-fold>
}
