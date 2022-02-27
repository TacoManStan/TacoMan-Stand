package com.taco.suit_lady.ui;

import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.logic.TaskManager;
import com.taco.suit_lady.logic.triggers.Galaxy;
import com.taco.suit_lady.ui.ui_internal.controllers.Controller;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.Print;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.TasksSL;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Point2D;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ContentController<T extends Content<T, TD, TC, F, FC>, TD extends ContentData<T, TD, TC, F, FC>, TC extends ContentController<T, TD, TC, F, FC>,
        F extends Footer<F, FC, T, TD, TC>, FC extends FooterController<F, FC, T, TD, TC>>
        extends Controller
        implements GFXObject<TC> {
    
    private final ReentrantLock gfxLock;
    
    private TaskManager<TC> taskManager;
    private T content;
    
    //
    
    private final StackPane overlayStackPane;
    
    private final ArrayList<Runnable> gfxOperations;
    private boolean needsUpdate;
    
    public ContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        this.gfxLock = new ReentrantLock();
        
        this.overlayStackPane = new StackPane();
        this.content = null;
        
        this.gfxOperations = new ArrayList<>();
        this.needsUpdate = false;
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public void initialize() { }
    
    public TC init(@NotNull T content) {
        if (this.content != null)
            throw ExceptionsSL.unsupported("Content has already been set (" + getContent() + ")");
        this.content = content;
        
        this.taskManager = new TaskManager<>((TC) this).init();
        initMouseEventHandling();
        
        
        return (TC) this;
    }
    
    private void initMouseEventHandling() {
        getContentPane().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (getContent().handleMousePressEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMousePressEvent(event, false)));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            if (getContent().handleMouseReleaseEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMouseReleaseEvent(event, false)));
        });
        
        getContentPane().addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            if (getContent().handleMouseMoveEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMouseMoveEvent(event, false)));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (getContent().handleMouseDragEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMouseDragEvent(event, false)));
        });
        
        getContentPane().addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
            if (getContent().handleMouseEnterEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMouseEnterEvent(event, false)));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            if (getContent().handleMouseExitEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((TC) this, () -> getContent().handleMouseExitEvent(event, false)));
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final StackPane getOverlayPane() { return overlayStackPane; }
    public abstract <P extends Pane> P getContentPane();
    
    public final T getContent() { return content; }
    public final TD getData() { return getContent().getData(); }
    
    public final @NotNull Point2D getMouseOnContent() { return ui().getMouseOnRegion(root()); }
    public final @NotNull Point2D getMouseOnContentSafe() { return ui().getMouseOnRegionSafe(root()); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public boolean needsGfxUpdate() { return needsUpdate; }
    @Override public void updateGfx() {
        TasksSL.sync(gfxLock, () -> {
            onGfxUpdateAlways();
            if (needsGfxUpdate()) {
                gfxOperations.forEach(Runnable::run);
                gfxOperations.clear();
                onGfxUpdate();
                needsUpdate = false;
            }
        });
    }
    
    
    @Override public @NotNull TaskManager<TC> taskManager() { return taskManager; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected boolean hasFooter() { return false; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void addOperation(@NotNull Runnable operation) {
        TasksSL.sync(gfxLock, () -> {
            gfxOperations.add(operation);
            needsUpdate = true;
        });
    }
    
    //</editor-fold>
}
