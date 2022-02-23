package com.taco.suit_lady.ui;

import com.taco.suit_lady.game.ui.GFXObject;
import com.taco.suit_lady.logic.OneTimeTask;
import com.taco.suit_lady.logic.TaskManager;
import com.taco.suit_lady.logic.triggers.Galaxy;
import com.taco.suit_lady.ui.ui_internal.controllers.Controller;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.TasksSL;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
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
    
    public ContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        this.gfxLock = new ReentrantLock();
        
        this.overlayStackPane = new StackPane();
        this.content = null;
        
        this.gfxOperations = new ArrayList<>();
        this.needsUpdate = false;
    }
    
    public C init(@NotNull T content) {
        if (this.content != null)
            throw ExceptionsSL.unsupported("Content has already been set (" + getContent() + ")");
        this.content = content;
        
        this.taskManager = new TaskManager<>((C) this).init();
        initMouseEventHandling();
        
        return (C) this;
    }
    
    public final StackPane getOverlayPane() {
        return overlayStackPane;
    }
    
    public abstract <P extends Pane> P getContentPane();
    public final T getContent() { return content; }
    public final D getData() { return getContent().getData(); }
    
    //
    
    @Override public void initialize() {
    
    }
    
    private void initMouseEventHandling() {
        getContentPane().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> {
                if (getContent().handleMousePressEvent(event))
                    event.consume();
            }));
            //            addOperation(() -> { });
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> {
                if (getContent().handleMouseReleaseEvent(event))
                    event.consume();
            }));
        });
        
        getContentPane().addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> {
                if (getContent().handleMouseMoveEvent(event))
                    event.consume();
            }));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> {
                if (getContent().handleMouseDragEvent(event))
                    event.consume();
            }));
        });
        
        getContentPane().addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> {
                if (getContent().handleMouseEnterEvent(event))
                    event.consume();
            }));
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            taskManager().addTask(Galaxy.newOneTimeTask((C) this, () -> {
                if (getContent().handleMouseExitEvent(event))
                    event.consume();
            }));
        });
    }
    
    @Override public boolean needsGfxUpdate() {
        return needsUpdate;
    }
    @Override public void onGfxUpdate() {
        TasksSL.sync(gfxLock, () -> {
            gfxOperations.forEach(Runnable::run);
            gfxOperations.clear();
            needsUpdate = false;
        });
    }
    private void addOperation(@NotNull Runnable operation) {
        TasksSL.sync(gfxLock, () -> {
            gfxOperations.add(operation);
            needsUpdate = true;
        });
    }
    
    @Override public @NotNull TaskManager<C> taskManager() { return taskManager; }
}
