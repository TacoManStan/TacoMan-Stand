package com.taco.suit_lady.ui;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
// TO-DOC
public class AppUI
        implements Springable {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ReadOnlyObjectWrapper<AppController> controllerProperty;
    private final ReadOnlyObjectWrapper<Sidebar> sidebarProperty;
    
    private ContentManager contentManager;
    
    /**
     * <p>Constructs a new {@link AppUI} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link AppUI} is constructed as an injected {@code singleton} {@link Springable Spring} {@link Component}.</li>
     *     <li>Both {@link FxWeaver} and {@link ConfigurableApplicationContext ApplicationContext} parameters are {@link Autowired}.</li>
     * </ol>
     *
     * @param weaver The {@link Autowired autowired} {@link FxWeaver} variable for this {@link Springable} object.
     * @param ctx    The {@link Autowired autowired} {@link ConfigurableApplicationContext ApplicationContext} variable for this {@link Springable} object.
     */
    public AppUI(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.controllerProperty = new ReadOnlyObjectWrapper<>();
        this.sidebarProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    protected void init() {
        console().initialize();
        this.contentManager = new ContentManager(this);
    }
    
    //</editor-fold>
    
    // <editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the {@link AppController} in charge of the {@code FXML UI} of this {@link AppUI}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the {@link AppController} in charge of the {@code FXML UI} of this {@link AppUI}.
     */
    public final ReadOnlyObjectProperty<AppController> controllerProperty() { return controllerProperty.getReadOnlyProperty(); }
    public final AppController getController() { return controllerProperty.get(); }
    protected final AppController setController(AppController newValue) { return PropertiesSL.setProperty(controllerProperty, newValue); }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the {@link Sidebar} of this {@link AppUI}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the {@link Sidebar} of this {@link AppUI}.
     */
    // TO-EXPAND
    public final ReadOnlyObjectProperty<Sidebar> sidebarProperty() { return sidebarProperty.getReadOnlyProperty(); }
    public final @NotNull Sidebar getSidebar() { return sidebarProperty.get(); }
    protected final Sidebar setSidebar(Sidebar newValue) { return PropertiesSL.setProperty(sidebarProperty, newValue); }
    
    /**
     * <p>Returns the {@link ContentManager Content Manager} in charge of managing the {@link Content} of this {@link AppUI} instance.</p>
     *
     * @return The {@link ContentManager Content Manager} in charge of managing the {@link Content} of this {@link AppUI} instance.
     */
    // TO-EXPAND
    public final ContentManager getContentManager() { return contentManager; }
    
    // </editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return weaver;
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return ctx;
    }
    
    @Override
    public final @NotNull AppUI ui() {
        return this;
    }
    
    @Override
    public final @NotNull Sidebar sidebar() {
        return getSidebar();
    }
    
    //</editor-fold>
}
