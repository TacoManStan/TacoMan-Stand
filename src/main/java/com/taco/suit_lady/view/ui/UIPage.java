package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.view.ui.ui_internal.controllers.Controller;
import com.taco.suit_lady.view.ui.ui_internal.controllers.UIPageController;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class UIPage<U extends UIPageController<?>>
        implements Displayable, Springable
{
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final U controller;
    
    /**
     * <p>Constructs a new {@link UIPage} using the specified {@link Springable} parameter.</p>
     * <p><b>Construction Process</b></p>
     * <ol>
     *     <li>Configures the {@link #weaver()} and {@link #ctx()} implementations of this {@link UIPage} to return the values of the specified {@link Springable}.</li>
     *     <li></li>
     * </ol>
     *
     * @param springable The {@link Springable} containing the {@link FxWeaver} and {@link ConfigurableApplicationContext Application Context} required to construct and configure this {@link UIPage}.
     */
    public UIPage(@NotNull Springable springable)
    {
        ExceptionTools.nullCheck(springable, "Springable Parent");
        
        this.weaver = ExceptionTools.nullCheck(springable.weaver(), "FxWeaver");
        this.ctx = ExceptionTools.nullCheck(springable.ctx(), "ApplicationContext");
        
        // Compound expression containing null checks for both the controller definition and the resulting constructor instance itself
        this.controller = ExceptionTools.nullCheckMessage(
                weaver().loadController(ExceptionTools.nullCheck(controllerDefinition(), "Controller Definition Class")),
                "Error Loading Controller of Type [" + controllerDefinition() + "] — Ensure controller class is defined in FXML file."
        );
        this.controller.setPage(this);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link Controller} that defines and manages the {@link #getContent() content} displayed by this {@link UIPage}.</p>
     *
     * @return The {@link Controller} that defines and manages the {@link #getContent() content} displayed by this {@link UIPage}.
     */
    public U getController()
    {
        return controller;
    }
    
    //</editor-fold>
    
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
    
    /**
     * <p>Returns the {@link Pane content} displayed by this {@link UIPage}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>return {@link #getController()}<b>.</b>{@link Controller#root() root()}</code></i></blockquote>
     *
     * @return The {@link Pane content} displayed by this {@link UIPage}.
     *
     * @see Displayable#getContent()
     * @see #getController()
     * @see Controller#root()
     */
    @Override
    public Pane getContent()
    {
        return getController().root();
    }
    
    //</editor-fold>
    
    /**
     * <p>Abstract method that is to define the {@link Class} representing the {@link UIPageController} implementation to be used to define and manage the UI of this {@link UIPage} implementation.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Used internally to automatically construct the {@link UIPageController} set to manage this {@link UIPage}.</li>
     *     <li>Automatic {@link UIPageController controller} construction is done in the {@link UIPage} {@link UIPage#UIPage(Springable) constructor}</li>
     * </ol>
     *
     * @return The {@link Class} representing the {@link UIPageController} implementation to be used to define and manage the UI of this {@link UIPage} implementation.
     */
    protected abstract @NotNull Class<U> controllerDefinition();
}
