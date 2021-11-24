package com.taco.suit_lady.view.ui;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.ui_internal.controllers.Controller;
import com.taco.suit_lady.view.ui.ui_internal.controllers.UIPageController;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

// TO-DOC
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
     *     <li>Configures <i>{@link #weaver()}</i> and <i>{@link #ctx()}</i> implementations of this {@link UIPage} to return the values of the specified {@link Springable}.</li>
     *     <li>Uses the {@link #weaver() FxWeaver} to {@link FxWeaver#loadController(Class) load} the {@link UIPageController controller} for this {@link UIPage} using the {@link #controllerDefinition() definition} defined by this {@link UIPage} implementation.</li>
     *     <li>{@link UIPageController#setPage(UIPage) Sets} the {@link UIPage} of the newly-created {@link UIPageController} to this {@link UIPage} object.</li> // TO-UPDATE - Move to internals
     * </ol>
     *
     * @param springable The {@link Springable} containing the {@link FxWeaver} and {@link ConfigurableApplicationContext Application Context} required to construct and configure this {@link UIPage}.
     *
     * @throws NullPointerException If the specified {@link Springable} is {@code null}.
     * @throws NullPointerException If the {@link FxWeaver} returned by this specified {@link Springable} is {@code null}.
     * @throws NullPointerException If the {@link ConfigurableApplicationContext} returned by the specified {@link Springable} is {@code null}.
     * @throws NullPointerException If the controller {@link #controllerDefinition() definition} is {@code null}.
     * @throws NullPointerException If the {@link UIPageController controller} loaded by the {@link #weaver() FxWeaver} using the controller {@link #controllerDefinition() definition} defined by this {@link UIPage} implementation is {@code null}.
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
     * <p>Returns the {@link UIPageController} that defines and manages the {@link #getContent() content} displayed by this {@link UIPage}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link UIPageController} returned by {@link #getController() this method} is automatically loaded in the {@link UIPage} {@link UIPage#UIPage(Springable) constructor}.</li>
     * </ol>
     *
     * @return The {@link Controller} that defines and manages the {@link #getContent() content} displayed by this {@link UIPage}.
     */
    public @NotNull U getController()
    {
        return controller;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull FxWeaver weaver()
    {
        return weaver;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigurableApplicationContext ctx()
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
    public @Nullable Pane getContent()
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
