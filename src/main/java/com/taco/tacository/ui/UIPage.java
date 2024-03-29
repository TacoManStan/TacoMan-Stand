package com.taco.tacository.ui;

import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.ui.ui_internal.controllers.Controller;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

// TO-DOC
public abstract class UIPage<C extends UIPageController<?>>
        implements Displayable, Springable {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final UIBook owner;
    
    private final C controller;
    
    /**
     * <p>Constructs a new {@link UIPage} using the specified {@link Springable} parameter.</p>
     * <p><b>Construction Process</b></p>
     * <ol>
     *     <li>Configures <i>{@link #weaver()}</i> and <i>{@link #ctx()}</i> implementations of this {@link UIPage} to return the values of the specified {@link Springable}.</li>
     *     <li>Uses the {@link #weaver() FxWeaver} to {@link FxWeaver#loadController(Class) load} the {@link UIPageController controller} for this {@link UIPage} using the {@link #controllerDefinition() definition} defined by this {@link UIPage} implementation.</li>
     *     <li>
     *         {@link UIPageController#setPage(UIPage) Sets} the {@link UIPage} of the newly-created {@link UIPageController} to this {@link UIPage} object.
     *         // TO-UPDATE - Move to internals
     *     </li>
     * </ol>
     *
     * @param owner The {@link Springable} containing the {@link FxWeaver} and {@link ConfigurableApplicationContext Application Context} required to construct and configure this {@link UIPage}.
     *
     * @throws NullPointerException If the specified {@link Springable} is {@code null}.
     * @throws NullPointerException If the {@link FxWeaver} returned by this specified {@link Springable} is {@code null}.
     * @throws NullPointerException If the {@link ConfigurableApplicationContext} returned by the specified {@link Springable} is {@code null}.
     * @throws NullPointerException If the controller {@link #controllerDefinition() definition} is {@code null}.
     * @throws NullPointerException If the {@link UIPageController controller} loaded by the {@link #weaver() FxWeaver} using the controller {@link #controllerDefinition() definition} defined by this {@link UIPage} implementation is {@code null}.
     */
    public UIPage(@NotNull UIBook owner, Object... constructorParams) {
        Exc.nullCheck(owner, "Springable Parent (UIBook Owner)");
        
        this.weaver = Exc.nullCheck(owner.weaver(), "FxWeaver");
        this.ctx = Exc.nullCheck(owner.ctx(), "ApplicationContext");
        this.owner = owner;
        
        // Compound expression containing null checks for both the controller definition and the resulting constructor instance itself
        this.controller = Exc.nullCheckMessage(
                weaver().loadController(Exc.nullCheck(controllerDefinition(), "Controller Definition Class")),
                "Error Loading Controller of Type [" + controllerDefinition() + "] — Ensure controller class is defined in FXML file.");
        
        this.controller.setPage(this);
        this.initializePage(constructorParams);
    }
    
    //<editor-fold desc="--- PAGE TURNING ---">
    
    public final void turnToNew(@NotNull UIPage<?> page) { getOwner().getPageHandler().turnToNew(page); }
    public final void turnTo(@NotNull UIPage<?> page) { getOwner().getPageHandler().turnTo(page); }
    
    public final UIPage<?> back() { return getOwner().getPageHandler().back(); }
    public final UIPage<?> backUnchecked() { return getOwner().getPageHandler().backUnchecked(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    
    /**
     * <p>Returns the {@link UIBook} instance that contains this {@link UIPage} instance.
     *
     * @return The {@link UIBook} instance that contains this {@link UIPage} instance.
     */
    public final @NotNull UIBook getOwner() {
        return owner;
    }
    
    /**
     * <p>Returns the {@link UIPageController} that defines and manages the {@link #getContentPane() content} displayed by this {@link UIPage}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link UIPageController} returned by {@link #getController() this method} is automatically loaded in the {@link UIPage} {@link UIPage#UIPage(UIBook, Object...) constructor}.</li>
     * </ol>
     *
     * @return The {@link Controller} that defines and manages the {@link #getContentPane() content} displayed by this {@link UIPage}.
     */
    public @NotNull C getController() {
        return controller;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull FxWeaver weaver() {
        return weaver;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return ctx;
    }
    
    /**
     * <p>Returns the {@link Pane content} displayed by this {@link UIPage}.</p>
     * <blockquote><b>Passthrough Definition:</b> <i><code>return {@link #getController()}<b>.</b>{@link Controller#root() root()}</code></i></blockquote>
     *
     * @return The {@link Pane content} displayed by this {@link UIPage}.
     *
     * @see Displayable#getContentPane()
     * @see #getController()
     * @see Controller#root()
     */
    @Override
    public @Nullable Pane getContentPane() {
        return getController().root();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    /**
     * <p>Initializes any unique components of a {@link UIPage} implementation.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Called <i>after</i> {@link UIPageController} construction.</li>
     *     <li>Called <i>prior to</i> the {@link UIPageController controllers} {@link UIPage} being {@link UIPageController#setPage(UIPage) set}.</li>
     *     <li>Called <i>prior to</i> <code><i>{@link UIPageController#onPageBindingComplete()}</i></code></li>
     *     <li>Parameter array should never be {@code null}.</li>
     * </ol>
     *
     * @param constructorParams The array of {@link Object Objects} passed to the {@link UIPage} {@link UIPage#UIPage(UIBook, Object...) constructor} to be used to initialize/construct {@link UIPage} implementations.
     */
    protected abstract void initializePage(@NotNull Object[] constructorParams);
    
    /**
     * <p>Abstract method that is to define the {@link Class} representing the {@link UIPageController} implementation to be used to define and manage the UI of this {@link UIPage} implementation.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Used internally to automatically construct the {@link UIPageController} set to manage this {@link UIPage}.</li>
     *     <li>Automatic {@link UIPageController controller} construction is done in the {@link UIPage} {@link UIPage#UIPage(UIBook, Object...) constructor}</li>
     * </ol>
     *
     * @return The {@link Class} representing the {@link UIPageController} implementation to be used to define and manage the UI of this {@link UIPage} implementation.
     */
    protected abstract @NotNull Class<C> controllerDefinition();
    
    //</editor-fold>
}
