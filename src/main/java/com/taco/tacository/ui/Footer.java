package com.taco.tacository.ui;

import com.taco.tacository.ui.content.ContentView;
import com.taco.tacository.ui.ui_internal.controllers.Controller;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.tools.printing.Printer;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>Defines the {@link Footer} of a {@link Content} implementation (of type {@link T}).</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>The {@link Footer} is the large area south of the {@link ContentView#getContent() Primary Content Pane} and {@link Sidebar}.</li>
 *     <li>Override <i>{@link #onContentChange(boolean)}</i> to define all logic required to transition to and from the {@link Content} associated with this {@link Footer}.</li>
 *     <li>
 *         Override <i>{@link #initializeFooter(Object[])}</i> to define the {@code initialization} logic for this {@link Footer} instance.
 *         <ul>
 *             <li>Use the {@code Object Array} parameter to specify any {@code parameters} needed to {@link #initializeFooter(Object[]) initialize} the {@link Footer}.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         The {@code Graphics} for a {@link Footer} implementation is defined by a {@link FooterController} implementation of type {@link FC}.
 *         <ul>
 *             <li>Override <i>{@link #controllerDefinition()}</i> to define the {@link FooterController} implementation.</li>
 *             <li>Be sure that both {@link FXML FXML File} and {@link FooterController} are linked to one another via {@link FxmlView} annotation and {@link Controller} class path.</li>
 *         </ul>
 *     </li>
 *     <li>Use <i>{@link #getContentPane()}</i> to access the {@link Pane JavaFX Pane} containing the {@code Graphics Content} of this {@link Footer} instance.</li>
 *     <li>Unlike the similar {@link Sidebar} UI area, the {@link Footer} area cannot be hidden, making {@link Footer Footers} the ideal location to place information that should always be visible to the user.</li>
 * </ol>
 *
 * @param <F>  The {@link Footer} type of this {@link Footer} implementation.
 * @param <FC> The type of {@link FooterController} implementation defining the {@code JavaFX Graphics} for this {@link Footer} implementation.
 * @param <T>  The type of {@link Content} implementation this {@link Footer} implementation is assigned to.
 * @param <TD> The type of {@link ContentData} implementation for the {@link Content} implementation this {@link Footer} implementation is assigned to.
 * @param <TC> The type of {@link ContentController} implementation defining the {@code JavaFX Graphics} for the {@link Content} implementation this {@link Footer} implementation is assigned to.
 *
 * @see FooterController
 * @see Content
 * @see ContentData
 * @see ContentController
 */
//TO-EXPAND - Examples
public abstract class Footer<F extends Footer<F, FC, T, TD, TC>, FC extends FooterController<F, FC, T, TD, TC>,
        T extends Content<T, TD, TC, F, FC>, TD extends ContentData<T, TD, TC, F, FC>, TC extends ContentController<T, TD, TC, F, FC>>
        implements Displayable, Springable {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final T content;
    private final FC controller;
    
    public Footer(@NotNull T content, Object... constructorParams) {
        this.weaver = Exc.nullCheck(content.weaver(), "FxWeaver");
        this.ctx = Exc.nullCheck(content.ctx(), "ApplicationContext");
        
        this.content = content;
        
        // Compound expression containing null checks for both the controller definition and the resulting constructor instance itself
        this.controller = Exc.nullCheckMessage(
                weaver().loadController(Exc.nullCheck(controllerDefinition(), "Controller Definition Class")),
                "Error Loading Controller of Type [" + controllerDefinition() + "] — Ensure controller class is defined in FXML file.");
        
        this.controller.setFooter(this);
        this.initializeFooter(constructorParams);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final T getContent() { return content; }
    public @NotNull FC getController() { return controller; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull FxWeaver weaver() { return weaver; }
    @Override public @NotNull ConfigurableApplicationContext ctx() { return ctx; }
    
    @Override public @Nullable Pane getContentPane() { return getController().root(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void initializeFooter(@NotNull Object[] constructorParams);
    protected abstract @NotNull Class<FC> controllerDefinition();
    
    protected void onContentChange(boolean shown) {
        Printer.print(shown ? "Content Shown (" + getContent() + ")" : "Content Hidden (" + getContent() + ")");
    }
    
    //</editor-fold>
}
