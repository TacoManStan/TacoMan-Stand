package com.taco.suit_lady.view.ui.ui_internal.contents.mandelbrot;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.components.BoundCanvas;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import com.taco.suit_lady.view.ui.jfx.components.CanvasPane;
import com.taco.suit_lady.view.ui.ui_internal.ContentController;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
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
public class MandelbrotContentController extends ContentController
        implements Lockable
{
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
    
    public MandelbrotContentController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
        
        this.lock = new ReentrantLock();
        this.canvasPane = new CanvasPane(this);
        
        this.resetDragConsumer();
    }
    
    protected BoundCanvas canvas()
    {
        return canvasPane.canvas();
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Lock getLock()
    {
        return lock;
    }
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    @Override
    public void initialize()
    {
        canvasAnchorPane.getChildren().add(canvasPane);
        FXTools.get().setAnchors(canvasPane, 0, 0, 0, 0);
        
        canvas().setOnMousePressed(event -> onMousePressed(event));
        canvas().setOnMouseReleased(event -> onMouseReleased(event));
//        canvas().setOnMouseMoved(event -> onMouseMoved(event));
        canvas().setOnMouseDragged(event -> onMouseDragged(event));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- RESIZING ---">
    
    private int mouseX = -1;
    private int mouseY = -1;
    
    private void onMousePressed(MouseEvent e)
    {
        sync(() -> {
            this.mouseX = (int) e.getX();
            this.mouseY = (int) e.getY();
        });
    }
    
    private void onMouseReleased(MouseEvent e)
    {
        sync(() -> {
            if (FXTools.get().isMouseOnNode(canvas()))
                getDragConsumer().accept(generateDragData(e));
        });
    }
    
    private void onMouseDragged(MouseEvent e)
    {
        sync(() -> {
            if (FXTools.get().isMouseOnNode(canvas()))
                getMoveConsumer().accept(generateDragData(e));
        });
    }
    
    private MouseDragData generateDragData(MouseEvent e)
    {
        return new MouseDragData(mouseX, mouseY, e.getX(), e.getY());
    }
    
    public final void setDragConsumer(Consumer<MouseDragData> dragConsumer)
    {
        this.dragConsumer = ExceptionTools.nullCheck(dragConsumer, "Mouse DRAG Consumer");
    }
    
    public final void resetDragConsumer()
    {
        setDragConsumer(mouseDragData -> { });
    }
    
    private Consumer<MouseDragData> getDragConsumer()
    {
        return dragConsumer;
    }
    
    public final void setMoveConsumer(Consumer<MouseDragData> moveConsumer)
    {
        this.moveConsumer = ExceptionTools.nullCheck(moveConsumer, "Mouse MOVE Consumer");
    }
    
    public final void resetMoveConsumer()
    {
        setMoveConsumer(mouseDragData -> { });
    }
    
    private Consumer<MouseDragData> getMoveConsumer()
    {
        return moveConsumer;
    }
    
    //
    
    protected static class MouseDragData
    {
        private final int startX;
        private final int startY;
        
        private final int endX;
        private final int endY;
        
        public MouseDragData(double startX, double startY, double endX, double endY)
        {
            this.startX = (int) startX;
            this.startY = (int) startY;
            
            this.endX = (int) endX;
            this.endY = (int) endY;
        }
        
        public final int getStartX()
        {
            return startX;
        }
        
        public final int getStartY()
        {
            return startY;
        }
        
        public final int getEndX()
        {
            return endX;
        }
        
        public final int getEndY()
        {
            return endY;
        }
        
        public final int getWidth()
        {
            return endX - startX;
        }
        
        public final int getHeight()
        {
            return endY - startY;
        }
        
        //
        
        public final Point2D getTopLeft()
        {
            return new Point2D(Math.min(getStartX(), getEndX()), Math.min(getStartY(), getEndY()));
        }
        
        public final Point2D getDimensions()
        {
            return new Point2D(Math.abs(getWidth()), Math.abs(getHeight()));
        }
        
        public final Rectangle getAsPaintable()
        {
            final Point2D topLeftImpl = getTopLeft();
            final Point2D dimensionsImpl = getDimensions();
            return new Rectangle(topLeftImpl.getX(), topLeftImpl.getY(), dimensionsImpl.getX(), dimensionsImpl.getY());
        }
        
        public final boolean isValid()
        {
            return startX >= 0
                   && startY >= 0
                   && endX >= 0
                   && endY >= 0;
        }
        
        @Override
        public String toString()
        {
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
