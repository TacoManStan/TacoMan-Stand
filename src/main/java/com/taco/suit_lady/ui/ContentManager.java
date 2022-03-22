package com.taco.suit_lady.ui;

import com.taco.suit_lady.ui.jfx.components.ContentPane;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasContentPane;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.printing.Printer;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Manages the currently {@link #getContent() Selected} {@link Content} as well as {@link Content} {@link #onChange(Content, Content) Transitions}.</p>
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
//TO-EXPAND: Examples?
public class ContentManager
        implements SpringableWrapper {
    
    private final StrictSpringable springable;
    
    //
    
    private final ContentPane contentBase;
    private final ReadOnlyObjectWrapper<Content<?, ?, ?, ?, ?>> contentProperty; // Add support for a list of overlapping Content, each overlapping on the Content Base StackPane?
    
    public ContentManager(@NotNull Springable springable) {
        this.springable = springable.asStrict();
        
        this.contentBase = new ContentPane(this) {
            @Override
            protected @NotNull StackPane loadForegroundPane() {
                return new CanvasContentPane(this);
            }
            
            @Override
            protected @NotNull StackPane loadContentPane() {
                return new CanvasContentPane(this);
            }
            
            @Override
            protected @NotNull StackPane loadBackgroundPane() {
                return new CanvasContentPane(this);
            }
        };
        this.contentProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.contentProperty.addListener((observable, oldValue, newValue) -> onChange(oldValue, newValue));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    //<editor-fold desc="--- INTERNAL UI PROPERTIES ---">
    
    protected ContentPane getContentBasePane() {
        return contentBase;
    }
    
    //
    
    protected final @NotNull CanvasContentPane getInternalForegroundBasePane() {
        return (CanvasContentPane) getContentBasePane().getForegroundPane();
    }
    
    protected final @NotNull CanvasContentPane getInternalContentBasePane() {
        return (CanvasContentPane) getContentBasePane().getContentPane();
    }
    
    protected final @NotNull CanvasContentPane getInternalBackgroundBasePane() {
        return (CanvasContentPane) getContentBasePane().getBackgroundPane();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CONTENT UI PROPERTIES ---">
    
    // NODE STACK ORDER: background pane -> backdrop canvas -> primary pane -> overlay canvas -> foreground pane
    
    public final @NotNull StackPane getContentForegroundPane() {
        return getInternalContentBasePane().getForegroundPane();
    }
    
    public final @NotNull StackPane getContentPrimaryPane() {
        return getInternalContentBasePane().getContentPane();
    }
    
    public final @NotNull StackPane getContentBackgroundPane() {
        return getInternalContentBasePane().getBackgroundPane();
    }
    
    //
    
    public final @NotNull CanvasSurface getContentOverlayCanvas() {
        return getInternalContentBasePane().getOverlayCanvas();
    }
    
    public final @NotNull CanvasSurface getContentBackdropCanvas() {
        return getInternalContentBasePane().getBackdropCanvas();
    }
    
    //</editor-fold>
    
    public @NotNull ReadOnlyObjectProperty<Content<?, ?, ?, ?, ?>> contentProperty() {
        return contentProperty.getReadOnlyProperty();
    }
    
    public @Nullable Content<?, ?, ?, ?, ?> getContent() {
        return contentProperty.get();
    }
    
    public boolean setContent(@Nullable Content<?, ?, ?, ?, ?> newContent) {
        contentProperty.set(newContent);
        return true; // TODO - Add actual validity checks here
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return springable; }
    
    //</editor-fold>
    
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
