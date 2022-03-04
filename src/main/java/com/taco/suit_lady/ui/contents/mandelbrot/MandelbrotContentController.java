package com.taco.suit_lady.ui.contents.mandelbrot;

import com.taco.suit_lady.logic.TaskManager;
import com.taco.suit_lady.ui.ContentController;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasPane;
import com.taco.suit_lady.ui.jfx.util.Bounds;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@Component
@FxmlView("/fxml/content/mandelbrot/mandelbrot_content.fxml")
@Scope("prototype")
public class MandelbrotContentController extends ContentController<MandelbrotContent, MandelbrotContentData, MandelbrotContentController, MandelbrotFooter, MandelbrotFooterController>
        implements Lockable {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    
    @FXML private BorderPane borderPaneRoot;
    @FXML private Label titleLabel;
    
    @FXML private StackPane canvasStackPane;
    @FXML private AnchorPane canvasAnchorPane;
    
    private final CanvasPane canvasPane;
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    
    private Consumer<MouseDragData> dragConsumer;
    private Consumer<MouseDragData> moveConsumer;
    
    public MandelbrotContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        
        this.lock = new ReentrantLock();
        this.canvasPane = new CanvasPane(this, 0.0);
        
        this.resetDragConsumer();
    }
    
    protected CanvasSurface canvas() {
        return canvasPane.canvas();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Lock getLock() {
        return lock;
    }
    
    @Override
    public Pane root() {
        return root;
    }
    
    @Override public AnchorPane getContentPane() {
        return canvasPane;
    }
    
    @Override
    public void initialize() {
        canvasAnchorPane.getChildren().add(canvasPane);
        FX.setAnchors(canvasPane);
        
        canvas().setOnMousePressed(event -> onMousePressed(event));
        canvas().setOnMouseReleased(event -> onMouseReleased(event));
        //        canvas().setOnMouseMoved(event -> onMouseMoved(event));
        canvas().setOnMouseDragged(event -> onMouseDragged(event));
        
        super.initialize();
    }
    
    //
    
    private TaskManager<MandelbrotContentController> taskManager;
    @Override public @NotNull TaskManager<MandelbrotContentController> taskManager() {
        if (taskManager == null)
            taskManager = new TaskManager<>(this).init();
        return taskManager;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- RESIZING ---">
    
    private int mouseX = -1;
    private int mouseY = -1;
    
    private void onMousePressed(MouseEvent e) {
        sync(() -> {
            this.mouseX = (int) e.getX();
            this.mouseY = (int) e.getY();
        });
    }
    
    private void onMouseReleased(MouseEvent e) {
        sync(() -> {
            if (FX.isMouseOnNode(canvas(), e))
                getDragConsumer().accept(generateDragData(e));
        });
    }
    
    private void onMouseDragged(MouseEvent e) {
        sync(() -> {
            if (FX.isMouseOnNode(canvas(), e))
                getMoveConsumer().accept(generateDragData(e));
        });
    }
    
    @Contract("_ -> new")
    private @NotNull MouseDragData generateDragData(@NotNull MouseEvent e) {
        return new MouseDragData(mouseX, mouseY, e.getX(), e.getY());
    }
    
    public final void setDragConsumer(Consumer<MouseDragData> dragConsumer) {
        this.dragConsumer = Exc.nullCheck(dragConsumer, "Mouse DRAG Consumer");
    }
    
    public final void resetDragConsumer() {
        setDragConsumer(mouseDragData -> { });
    }
    
    private Consumer<MouseDragData> getDragConsumer() {
        return dragConsumer;
    }
    
    public final void setMoveConsumer(Consumer<MouseDragData> moveConsumer) {
        this.moveConsumer = Exc.nullCheck(moveConsumer, "Mouse MOVE Consumer");
    }
    
    public final void resetMoveConsumer() {
        setMoveConsumer(mouseDragData -> { });
    }
    
    private Consumer<MouseDragData> getMoveConsumer() {
        return moveConsumer;
    }
    @Override public void onGfxUpdate() { }
    
    //
    
    protected static class MouseDragData {
        private final int startX;
        private final int startY;
        
        private final int endX;
        private final int endY;
        
        public MouseDragData(double startX, double startY, double endX, double endY) {
            this.startX = (int) startX;
            this.startY = (int) startY;
            
            this.endX = (int) endX;
            this.endY = (int) endY;
        }
        
        public final int getStartX() {
            return startX;
        }
        
        public final int getStartY() {
            return startY;
        }
        
        public final int getEndX() {
            return endX;
        }
        
        public final int getEndY() {
            return endY;
        }
        
        public final int getWidth() {
            return endX - startX;
        }
        
        public final int getHeight() {
            return endY - startY;
        }
        
        //
        
        @Contract(" -> new")
        public final @NotNull Point2D getTopLeft() {
            return new Point2D(Math.min(getStartX(), getEndX()), Math.min(getStartY(), getEndY()));
        }
        
        @Contract(" -> new")
        public final @NotNull Point2D getDimensions() {
            return new Point2D(Math.abs(getWidth()), Math.abs(getHeight()));
        }
        
        @Contract(" -> new")
        public final @NotNull Bounds getBounds() {
            return Bounds.fromPoints(getTopLeft(), getDimensions());
        }
        
        public final boolean isValid() {
            return startX >= 0
                   && startY >= 0
                   && endX >= 0
                   && endY >= 0;
        }
        
        @Override
        public String toString() {
            return "MouseDragData{" +
                   "startX=" + startX +
                   ", startY=" + startY +
                   ", endX=" + endX +
                   ", endY=" + endY +
                   '}';
        }
    }
    
    //</editor-fold>
}
