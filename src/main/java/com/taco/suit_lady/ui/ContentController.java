package com.taco.suit_lady.ui;

import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.logic.TaskManager;
import com.taco.suit_lady.logic.triggers.Galaxy;
import com.taco.suit_lady.ui.ui_internal.controllers.Controller;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.TasksSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
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

public abstract class ContentController<T extends Content<T, D, C>, D extends ContentData<T, D, C>, C extends ContentController<T, D, C>> extends Controller
        implements GFXObject<C> {
    
    private final ReentrantLock gfxLock;
    private TaskManager<C> taskManager;
    
    private T content;
    private final StackPane overlayStackPane;
    
    private final ArrayList<Runnable> gfxOperations;
    private boolean needsUpdate;
    
    private final ReadOnlyObjectWrapper<Point2D> mouseOnContentProperty;
    private final ReadOnlyObjectWrapper<Point2D> mouseOnSceneProperty;
    private final ReadOnlyObjectWrapper<Point2D> mouseOnScreenProperty;
    
    public ContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        this.gfxLock = new ReentrantLock();
        
        this.overlayStackPane = new StackPane();
        this.content = null;
        
        this.gfxOperations = new ArrayList<>();
        this.needsUpdate = false;
        
        this.mouseOnContentProperty = new ReadOnlyObjectWrapper<>();
        this.mouseOnSceneProperty = new ReadOnlyObjectWrapper<>();
        this.mouseOnScreenProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public void initialize() { }
    
    public C init(@NotNull T content) {
        if (this.content != null)
            throw ExceptionsSL.unsupported("Content has already been set (" + getContent() + ")");
        this.content = content;
        
        this.taskManager = new TaskManager<>((C) this).init();
        initMouseEventHandling();
        
        return (C) this;
    }
    
    private void initMouseEventHandling() {
        getContentPane().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            updateMouseLocation(event);
            if (getContent().handleMousePressEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> getContent().handleMousePressEvent(event, false)));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            updateMouseLocation(event);
            if (getContent().handleMouseReleaseEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> getContent().handleMouseReleaseEvent(event, false)));
        });
        
        getContentPane().addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            updateMouseLocation(event);
            if (getContent().handleMouseMoveEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> getContent().handleMouseMoveEvent(event, false)));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            updateMouseLocation(event);
            if (getContent().handleMouseDragEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> getContent().handleMouseDragEvent(event, false)));
        });
        
        getContentPane().addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
            updateMouseLocation(event);
            if (getContent().handleMouseEnterEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> getContent().handleMouseEnterEvent(event, false)));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            updateMouseLocation(event);
            if (getContent().handleMouseExitEvent(event, true))
                event.consume();
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> getContent().handleMouseExitEvent(event, false)));
        });
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final StackPane getOverlayPane() { return overlayStackPane; }
    public abstract <P extends Pane> P getContentPane();
    
    public final T getContent() { return content; }
    public final D getData() { return getContent().getData(); }
    
    //<editor-fold desc="> Mouse Location Properties">
    
    //    private @NotNull ReadOnlyObjectWrapper<Point2D> mouseOnContentProperty() { return ToolsFX.requireFX(() -> mouseOnScreenProperty); }
    private @NotNull ReadOnlyObjectWrapper<Point2D> mouseOnContentProperty() { return mouseOnContentProperty; }
    private Point2D setMouseOnContent(@Nullable Point2D newValue) { return PropertiesSL.setProperty(mouseOnContentProperty(), newValue); }
    public final @NotNull ReadOnlyObjectProperty<Point2D> readOnlyMouseOnContentProperty() { return mouseOnContentProperty().getReadOnlyProperty(); }
    public final Point2D getMouseOnContent() { return mouseOnContentProperty().get(); }
    
    //    private @NotNull ReadOnlyObjectWrapper<Point2D> mouseOnSceneProperty() { return ToolsFX.requireFX(() -> mouseOnSceneProperty); }
    private @NotNull ReadOnlyObjectWrapper<Point2D> mouseOnSceneProperty() { return mouseOnSceneProperty; }
    private Point2D setMouseOnScene(@Nullable Point2D newValue) { return PropertiesSL.setProperty(mouseOnSceneProperty(), newValue); }
    public final @NotNull ReadOnlyObjectProperty<Point2D> readOnlyMouseOnSceneProperty() { return mouseOnSceneProperty().getReadOnlyProperty(); }
    public final Point2D getMouseOnScene() { return mouseOnSceneProperty().get(); }
    
    //    private @NotNull ReadOnlyObjectWrapper<Point2D> mouseOnScreenProperty() { return ToolsFX.requireFX(() -> mouseOnScreenProperty); }
    private @NotNull ReadOnlyObjectWrapper<Point2D> mouseOnScreenProperty() { return mouseOnScreenProperty; }
    private Point2D setMouseOnScreen(@Nullable Point2D newValue) { return PropertiesSL.setProperty(mouseOnScreenProperty(), newValue); }
    public final @NotNull ReadOnlyObjectProperty<Point2D> readOnlyMouseOnScreenProperty() { return mouseOnScreenProperty().getReadOnlyProperty(); }
    public final Point2D getMouseOnScreen() { return mouseOnScreenProperty().get(); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public void onGfxUpdate() {
        TasksSL.sync(gfxLock, () -> {
            gfxOperations.forEach(Runnable::run);
            gfxOperations.clear();
            needsUpdate = false;
        });
    }
    @Override public boolean needsGfxUpdate() { return needsUpdate; }
    private void addOperation(@NotNull Runnable operation) {
        TasksSL.sync(gfxLock, () -> {
            gfxOperations.add(operation);
            needsUpdate = true;
        });
    }
    
    @Override public @NotNull TaskManager<C> taskManager() { return taskManager; }
    
    //</editor-fold>
    
    private void updateMouseLocation(@NotNull InputEvent event) {
        final Point2D contentLocation;
        final Point2D sceneLocation;
        final Point2D screenLocation;
        
        if (event instanceof MouseEvent mouseEvent) {
            contentLocation = new Point2D(mouseEvent.getX(), mouseEvent.getY());
            sceneLocation = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            screenLocation = new Point2D(mouseEvent.getScreenX(), mouseEvent.getScreenY());
        } else if (event instanceof DragEvent dragEvent) {
            contentLocation = new Point2D(dragEvent.getX(), dragEvent.getY());
            sceneLocation = new Point2D(dragEvent.getSceneX(), dragEvent.getSceneY());
            screenLocation = new Point2D(dragEvent.getScreenX(), dragEvent.getScreenY());
        } else
            throw ExceptionsSL.unsupported("Unknown InputEvent Type: " + event);
        
        setMouseOnContent(contentLocation);
        setMouseOnScene(sceneLocation);
        setMouseOnScreen(screenLocation);
    }
}
