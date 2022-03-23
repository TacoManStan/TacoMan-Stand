package com.taco.suit_lady.ui;

import com.taco.suit_lady.ui.jfx.components.ContentPane;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasContentPane;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.ui.ui_internal.controllers.Controller;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.printing.Printer;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;

/**
 * <p>Manages the currently {@link #getContent() Selected} {@link Content} as well as {@link Content} {@link #onChange(Content, Content) Transitions} for the {@link Content} Framework.</p>
 * <br><hr><br>
 * <p><b>{@link Content} Framework</b></p>
 * <p>The {@link Content} Framework is designed to provide abstracted, simplified, black-boxed, modular, and flexible baseline classes for building an application.</p>
 * <ol>
 *     <li>The {@link Content} displayed by the {@link AppController} is automatically loaded and managed by the {@link ContentManager}.</li>
 *     <li>Use <i>{@link #setContent(Content)}</i> to change the active {@link Content} displayed by this {@link ContentManager}.</li>
 *     <li>The internal <i>{@link #onChange(Content, Content)}</i> method handles the transition from one {@link Content} to another.</li>
 * </ol>
 * <p>The {@link Content} is defined by {@code 5} core generic properties:</p>
 * <ol>
 *     <li>
 *         <b>{@link Content}</b>
 *         <ul>
 *             <li>The backbone of every {@link Content} module.</li>
 *             <li>Contains all {@link Content} logic and execution.</li>
 *             <li>Serves as the central hub for all {@link Content} members below.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>{@link ContentData}</b>
 *         <ul>
 *             <li>Houses the {@code data} for a specific {@link Content} implementation.</li>
 *             <li>While not yet fully utilized, the main purpose of {@link ContentData} is to provide abstracted functions for saving, loading, storing, and manipulating a {@link Content} implementations {@code data}.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>{@link ContentController}</b>
 *         <ul>
 *             <li>The {@link ContentController} defines all {@code JavaFX Graphics/UI} information for a specific {@link Content} implementation.</li>
 *             <li>The {@link ContentController} is an instance of the parent {@link Controller} class and therefore must be configured as such.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>{@link Footer}</b>
 *         <ul>
 *             <li>
 *                 Defines the large area located at the far south of the UI window.
 *                 <ul>
 *                     <li>The {@link Footer} stretches for the entire width of the UI window.</li>
 *                 </ul>
 *             </li>
 *             <li>The {@link Footer} implementation should not handle any actual {@code UI Functionality}, but rather any {@code logic} pertaining to the {@link Footer} area.</li>
 *             <li>The {@code JavaFX UI} components of a {@link Footer} implementation are defined by a matching {@link FooterController} implementation, detailed below.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <b>{@link FooterController}</b>
 *         <ul>
 *             <li>Defines, manages, and handles the {@code JavaFX UI} components of a {@link Footer} implementation.</li>
 *             <li>All {@code JavaFX} operations for a {@link Footer} instance should be executed by the {@link FooterController} on the {@link FX#runFX(Runnable, boolean) JavaFX Application Thread}.</li>
 *         </ul>
 *     </li>
 * </ol>
 * <br><hr><br>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>{@link ContentManager} is stored as a {@code singleton member} of the {@link AppUI} class.</li>
 *     <li>The {@code singleton} {@link ContentManager} instance is accessed with <i>{@link AppUI#getContentManager()}</i>.</li>
 *     <li>
 *         {@link ContentManager} contains a variety of {@link Pane} instances, each serving a different purpose:
 *         <ul>
 *             <li>
 *                 <b>Internal Base Pane</b>
 *                 <ul>
 *                     <li><i>{@link #getContentBasePane() Content Base Pane}</i></li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 <b>Internal Canvas Pane Accessors</b>
 *                 <ul>
 *                     <li><i>{@link #getInternalContentBasePane() Internal Content Pane}</i></li>
 *                     <li><i>{@link #getInternalForegroundBasePane() Internal Foreground Canvas Pane}</i></li>
 *                     <li><i>{@link #getInternalBackgroundBasePane() Internal Background Canvas Pane}</i></li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 <b>Content Stack Pane Accessors</b>
 *                 <ul>
 *                     <li><i>{@link #getContentPrimaryPane() Primary Content Pane}</i></li>
 *                     <li><i>{@link #getContentForegroundPane() Foreground Content Pane}</i></li>
 *                     <li><i>{@link #getContentBackgroundPane() Background Content Pane}</i></li>
 *                 </ul>
 *             </li>
 *             <li>
 *                 <b>Canvas Content Accessors</b>
 *                 <ul>
 *                     <li><i>{@link #getContentOverlayCanvas() Overlay Canvas Pane}</i></li>
 *                     <li><i>{@link #getContentBackdropCanvas() Backdrop Canvas Pane}</i></li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 *     <li>Additionally, the {@link ContentManager} handles all {@link KeyEvent Key Events} via the <i>{@link #submitKeyEvent(KeyEvent, boolean)}</i> method.</li>
 * </ol>
 */
//TO-EXPAND
public class ContentManager
        implements SpringableWrapper {
    
    private final StrictSpringable springable;
    
    //
    
    private final ContentPane contentBase;
    private final ReadOnlyObjectWrapper<Content<?, ?, ?, ?, ?>> contentProperty; // Add support for a list of overlapping Content, each overlapping on the Content Base StackPane?
    
    /**
     * <p>Constructs a new {@link Springable} {@link ContentManager} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>{@link ContentManager} is a {@code singleton} instance.</li>
     *     <li>The {@code singleton} {@link ContentManager} is constructed upon {@link AppUI} {@link AppUI#init() Initialization}.</li>
     *     <li>Use either the direct accessor <i>{@link AppUI#getContentManager()}</i> method or {@link Springable} <i>{@link Springable#manager()}</i> passthrough accessor.</li>
     * </ol>
     *
     * @param springable The {@link Springable} instance used to configure this {@link ContentManager} {@link SpringableWrapper} implementation.
     */
    public ContentManager(@NotNull Springable springable) {
        this.springable = springable.asStrict();
        
        this.contentBase = new ContentPane(this) {
            @Override protected @NotNull StackPane loadForegroundPane() { return new CanvasContentPane(this); }
            @Override protected @NotNull StackPane loadContentPane() { return new CanvasContentPane(this); }
            @Override protected @NotNull StackPane loadBackgroundPane() { return new CanvasContentPane(this); }
        };
        this.contentProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.contentProperty.addListener((observable, oldValue, newValue) -> onChange(oldValue, newValue));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="--- INTERNAL UI PROPERTIES ---">
    
    /**
     * <p>Returns the base {@link ContentPane} for this {@link ContentManager} instance.</p>
     *
     * @return The base {@link ContentPane} for this {@link ContentManager} instance.
     */
    //TO-EXPAND: Details
    protected ContentPane getContentBasePane() { return contentBase; }
    
    
    /**
     * <p>Returns the {@link ContentPane#getForegroundPane() Internal Foreground CanvasPane} of the {@link #getContentBasePane() Base Content Pane} assigned to this {@link ContentManager} instance.</p>
     *
     * @return The {@link ContentPane#getForegroundPane() Internal Foreground CanvasPane} of the {@link #getContentBasePane() Base Content Pane} assigned to this {@link ContentManager} instance.
     */
    protected final @NotNull CanvasContentPane getInternalForegroundBasePane() { return (CanvasContentPane) getContentBasePane().getForegroundPane(); }
    
    /**
     * <p>Returns the {@link ContentPane#getContentPane() Internal Primary Content CanvasPane} of the {@link #getContentBasePane() Base Content Pane} assigned to this {@link ContentManager} instance.</p>
     *
     * @return The {@link ContentPane#getContentPane() Internal Primary Content CanvasPane} of the {@link #getContentBasePane() Base Content Pane} assigned to this {@link ContentManager} instance.
     */
    protected final @NotNull CanvasContentPane getInternalContentBasePane() { return (CanvasContentPane) getContentBasePane().getContentPane(); }
    
    /**
     * <p>Returns the {@link ContentPane#getBackgroundPane() Internal Background CanvasPane} of the {@link #getContentBasePane() Base Content Pane} assigned to this {@link ContentManager} instance.</p>
     *
     * @return The {@link ContentPane#getBackgroundPane() Internal Background CanvasPane} of the {@link #getContentBasePane() Base Content Pane} assigned to this {@link ContentManager} instance.
     */
    protected final @NotNull CanvasContentPane getInternalBackgroundBasePane() { return (CanvasContentPane) getContentBasePane().getBackgroundPane(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CONTENT UI PROPERTIES ---">
    
    // NODE STACK ORDER: background pane -> backdrop canvas -> primary pane -> overlay canvas -> foreground pane
    
    /**
     * <p>Returns the {@link ContentPane#getForegroundPane() Foreground Pane} of the {@link #getInternalContentBasePane() Internal Primary Content Pane} assigned to this {@link ContentManager} instance.</p>
     *
     * @return The {@link ContentPane#getForegroundPane() Foreground Pane} of the {@link #getInternalContentBasePane() Internal Primary Content Pane} assigned to this {@link ContentManager} instance.
     */
    public final @NotNull StackPane getContentForegroundPane() { return getInternalContentBasePane().getForegroundPane(); }
    
    /**
     * <p>Returns the {@link ContentPane#getContentPane() Primary Content Pane} of the {@link #getInternalContentBasePane() Internal Primary Content Pane} assigned to this {@link ContentManager} instance.</p>
     *
     * @return The {@link ContentPane#getContentPane() Primary Content Pane} of the {@link #getInternalContentBasePane() Internal Primary Content Pane} assigned to this {@link ContentManager} instance.
     */
    public final @NotNull StackPane getContentPrimaryPane() { return getInternalContentBasePane().getContentPane(); }
    
    /**
     * <p>Returns the {@link ContentPane#getBackgroundPane() Background Pane} of the {@link #getInternalContentBasePane() Internal Primary Content Pane} assigned to this {@link ContentManager} instance.</p>
     *
     * @return The {@link ContentPane#getBackgroundPane() Background Pane} of the {@link #getInternalContentBasePane() Internal Primary Content Pane} assigned to this {@link ContentManager} instance.
     */
    public final @NotNull StackPane getContentBackgroundPane() { return getInternalContentBasePane().getBackgroundPane(); }
    
    /**
     * <p>Returns the {@link CanvasContentPane#getOverlayCanvas() Overlay CanvasSurface} of the {@link #getInternalContentBasePane() Internal Primary Content Pane} assigned to this {@link ContentManager} instance.</p>
     *
     * @return The {@link CanvasContentPane#getOverlayCanvas() Overlay CanvasSurface} of the {@link #getInternalContentBasePane() Internal Primary Content Pane} assigned to this {@link ContentManager} instance.
     */
    public final @NotNull CanvasSurface getContentOverlayCanvas() { return getInternalContentBasePane().getOverlayCanvas(); }
    
    /**
     * <p>Returns the {@link CanvasContentPane#getBackdropCanvas() Backdrop CanvasSurface} of the {@link #getInternalContentBasePane() Internal Primary Content Pane} assigned to this {@link ContentManager} instance.</p>
     *
     * @return The {@link CanvasContentPane#getBackdropCanvas() Backdrop CanvasSurface} of the {@link #getInternalContentBasePane() Internal Primary Content Pane} assigned to this {@link ContentManager} instance.
     */
    public final @NotNull CanvasSurface getContentBackdropCanvas() { return getInternalContentBasePane().getBackdropCanvas(); }
    
    //</editor-fold>
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the {@link Content} instance currently selected & displayed by this {@link ContentManager} instance.</p>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the {@link Content} instance currently selected & displayed by this {@link ContentManager} instance.
     */
    public @NotNull ReadOnlyObjectProperty<Content<?, ?, ?, ?, ?>> contentProperty() { return contentProperty.getReadOnlyProperty(); }
    public @Nullable Content<?, ?, ?, ?, ?> getContent() { return contentProperty.get(); }
    public @Nullable Content<?, ?, ?, ?, ?> setContent(@Nullable Content<?, ?, ?, ?, ?> newContent) { return Props.setProperty(contentProperty, newContent); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return springable; }
    
    //</editor-fold>
    
    //TO-DOC
    private void onChange(@Nullable Content<?, ?, ?, ?, ?> oldContent, @Nullable Content<?, ?, ?, ?, ?> newContent) {
        // TODO - Execute onRemoved() and onSet via a JavaFX Task implementation. For now, though, this will work.
        // When the above is completed, don't forget to update the onRemoved() and onSet() Javadocs as well.
        FX.runFX(() -> {
            if (oldContent != null) {
                getContentPrimaryPane().getChildren().remove(oldContent.getController().root());
                oldContent.getController().root().prefWidthProperty().unbind();
                oldContent.getController().root().prefHeightProperty().unbind();
                oldContent.getController().root().maxWidthProperty().unbind();
                oldContent.getController().root().maxHeightProperty().unbind();
                
                getContentPrimaryPane().getChildren().remove(oldContent.getOverlayHandler().root());
                oldContent.getOverlayHandler().root().prefWidthProperty().unbind();
                oldContent.getOverlayHandler().root().prefHeightProperty().unbind();
                oldContent.getOverlayHandler().root().maxWidthProperty().unbind();
                oldContent.getOverlayHandler().root().maxHeightProperty().unbind();
                
                if (oldContent.getController().hasFooter()) {
                    ui().getController().footerPane().getChildren().remove(oldContent.getFooter().getController().root());
                    oldContent.getFooter().onContentChange(false);
                }
                
                ui().stopTrackingRegion(oldContent.getController().root());
                logiCore().execute(oldContent::onRemovedInternal);
            }
            if (newContent != null) {
                FX.bindToParent(newContent.getController().root(), getContentPrimaryPane(), true);
                FX.bindToParent(newContent.getOverlayHandler().root(), getContentPrimaryPane(), true);
                
                if (newContent.getController().hasFooter()) {
                    ui().getController().footerPane().getChildren().add(newContent.getFooter().getController().root());
                    newContent.getFooter().onContentChange(true);
                }
                
                ui().trackRegion(newContent.getController().root());
                logiCore().execute(newContent::onSetInternal);
            }
        }, true);
    }
    
    //TO-DOC
    protected boolean submitKeyEvent(@NotNull KeyEvent keyEvent, boolean fx) {
        Content<?, ?, ?, ?, ?> content = getContent();
        
        Printer.print("Submitting Key Event:  [" + keyEvent.getCode() + "  |  " + content.getClass().getSimpleName() + "  |  " + fx + "]");
        if (content != null)
            return content.handleKeyEvent(keyEvent, fx);
        else
            Printer.err("Content is Null");
        
        return false;
    }
}
