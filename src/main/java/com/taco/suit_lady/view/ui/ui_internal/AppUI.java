package com.taco.suit_lady.view.ui.ui_internal;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.Sidebar;
import com.taco.suit_lady.view.ui.console.Console;
import com.taco.suit_lady.view.ui.ui_internal.contents_new.ContentManagerNew;
import com.taco.suit_lady.view.ui.ui_internal.contents_new.SLContent;
import com.taco.suit_lady.view.ui.ui_internal.contents_new.test_content.TestSLContent;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
// TO-DOC
public class AppUI
        implements Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ReadOnlyObjectWrapper<AppController> controllerProperty;
    private final ReadOnlyObjectWrapper<Sidebar> sidebarProperty;
    
    private final ReadOnlyObjectWrapper<StackPane> contentStackPaneProperty;
    
    private ContentManagerNew contentManager;
    
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
    public AppUI(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.controllerProperty = new ReadOnlyObjectWrapper<>();
        this.sidebarProperty = new ReadOnlyObjectWrapper<>();
        
        this.contentStackPaneProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    /**
     * <p>Initializes this {@link AppUI}.</p>
     * <p><b>Initialization Process</b></p>
     * <ol>
     *     <li>{@link Console#initialize() Initializes} the {@code singleton} {@link Console} object.</li>
     *     <li>
     *         Sets the {@link #contentManager} of this {@link AppUI} to a new {@link ContentManagerNew}.
     *         <ul>
     *             <li>The {@link #getContentStackPane() Content Stack Pane} of this {@link AppUI} is used as the {@link ContentManagerNew#getContentBase() Content Base} for the constructed {@link ContentManagerNew} object.</li>
     *         </ul>
     *     </li>
     *     <li>
     *         Sets the {@link ContentManagerNew#getContent() content} of the constructed {@link ContentManagerNew} to a new {@link TestSLContent}.
     *         <ul>
     *             <li>This {@link AppUI} instance is used only to pass its {@link Springable} values to the constructed {@link TestSLContent}.</li>
     *         </ul>
     *     </li>
     * </ol>
     */
    protected void init()
    {
        ctx().getBean(Console.class).initialize();
        
        this.contentManager = new ContentManagerNew(weaver(), ctx(), getContentStackPane());
        this.contentManager.setContent(new TestSLContent(this));
    }
    
    //</editor-fold>
    
    // <editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the {@link AppController} in charge of the {@code FXML UI} of this {@link AppUI}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the {@link AppController} in charge of the {@code FXML UI} of this {@link AppUI}.
     */
    public final ReadOnlyObjectProperty<AppController> controllerProperty()
    {
        return controllerProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link AppController} in charge of the {@code FXML UI} of this {@link AppUI}.</p>
     *
     * @return The {@link AppController} in charge of the {@code FXML UI} of this {@link AppUI}.
     */
    public final AppController getController()
    {
        return controllerProperty.get();
    }
    
    /**
     * <p>Sets the {@link AppController} in charge of the {@code FXML UI} of this {@link AppUI} to the specified value.</p>
     *
     * @param controller The {@link AppController} to be set as the {@link AppController} in charge of the {@code FXML UI} of this {@link AppUI}.
     */
    protected final void setController(AppController controller)
    {
        controllerProperty.set(controller);
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the {@link Sidebar} of this {@link AppUI}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the {@link Sidebar} of this {@link AppUI}.
     */
    // TO-EXPAND (same with corresponding getter and setter)
    public final ReadOnlyObjectProperty<Sidebar> sidebarProperty()
    {
        return sidebarProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link Sidebar} of this {@link AppUI}.</p>
     *
     * @return The {@link Sidebar} of this {@link AppUI}.
     */
    public final Sidebar getSidebar()
    {
        return sidebarProperty.get();
    }
    
    /**
     * <p>Sets the {@link Sidebar} of this {@link AppUI} to the specified value.</p>
     *
     * @param sidebar The {@link Sidebar} to be set as the {@link Sidebar} of this {@link AppUI}.
     */
    protected final void setSidebar(Sidebar sidebar)
    {
        sidebarProperty.set(sidebar);
    }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the {@link StackPane} used to display the primary {@link SLContent} of this {@link AppUI}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the {@link StackPane} used to display the primary {@link SLContent} of this {@link AppUI}.
     */
    // TO-EXPAND (same with corresponding getter and setter)
    public final ReadOnlyObjectProperty<StackPane> contentStackPaneProperty()
    {
        return contentStackPaneProperty.getReadOnlyProperty();
    }
    
    /**
     * <p>Returns the {@link StackPane} used to display the primary {@link SLContent} of this {@link AppUI}.</p>
     *
     * @return The {@link StackPane} used to display the primary {@link SLContent} of this {@link AppUI}.
     */
    public final StackPane getContentStackPane()
    {
        return contentStackPaneProperty.get();
    }
    
    /**
     * <p>Sets the {@link StackPane} used to display the primary {@link SLContent} of this {@link AppUI} to the specified value.</p>
     *
     * @param contentStackPane The {@link StackPane} to be set as the {@link StackPane} used to display the primary {@link SLContent} of this {@link AppUI}.
     */
    public final void setContentStackPane(StackPane contentStackPane)
    {
        contentStackPaneProperty.set(contentStackPane);
    }
    
    /**
     * <p>Returns the {@link ContentManagerNew Content Manager} in charge of managing the {@link SLContent} of this {@link AppUI} instance.</p>
     *
     * @return The {@link ContentManagerNew Content Manager} in charge of managing the {@link SLContent} of this {@link AppUI} instance.
     */
    // TO-EXPAND
    public final ContentManagerNew getContentManager()
    {
        return contentManager;
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
