package com.taco.suit_lady.ui;

import com.taco.suit_lady.ui.ui_internal.controllers.Controller;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class ContentController<T extends Content<T, D, C>, D extends ContentData<T, D, C>, C extends ContentController<T, D, C>> extends Controller {
    
    private T content;
    private final StackPane overlayStackPane;
    
    public ContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        this.overlayStackPane = new StackPane();
        this.content = null;
    }
    
    public C init(@NotNull T content) {
        if (this.content != null)
            throw ExceptionsSL.unsupported("Content has already been set (" + getContent() + ")");
        this.content = content;
        
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
        initMouseEventHandling();
    }
    
    private void initMouseEventHandling() {
        getContentPane().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (getContent().handleMousePressEvent(event))
                event.consume();
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            if (getContent().handleMouseReleaseEvent(event))
                event.consume();
        });
    
        getContentPane().addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            if (getContent().handleMouseMoveEvent(event))
                event.consume();
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (getContent().handleMouseDragEvent(event))
                event.consume();
        });
    
        getContentPane().addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
            if (getContent().handleMouseEnterEvent(event))
                event.consume();
        });
        getContentPane().addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            if (getContent().handleMouseExitEvent(event))
                event.consume();
        });
    }
}
