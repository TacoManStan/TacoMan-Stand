package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.jfx.components.BoundCanvas;
import com.taco.suit_lady.view.ui.jfx.fxtools.FXTools;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

@Component
@FxmlView("/fxml/content/mandelbrot/mandelbrot_content.fxml")
@Scope("prototype")
public class SLMandelbrotContentController extends SLContentController
{
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    
    @FXML private BorderPane borderPaneRoot;
    @FXML private Label titleLabel;
    
    @FXML private BoundCanvas canvas;
    @FXML private ProgressBar progressBar;
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    private Consumer<MouseDragData> dragConsumer;
    
    public SLMandelbrotContentController(FxWeaver weaver, ConfigurableApplicationContext ctx)
    {
        super(weaver, ctx);
        
        this.lock = new ReentrantLock();
        this.resetDragConsumer();
    }
    
    protected BoundCanvas canvas()
    {
        return canvas;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public Pane root()
    {
        return root;
    }
    
    @Override
    public void initialize()
    {
        //        progressBar.setVisible(false);
        //        progressBar.prefWidthProperty().bind(canvas().widthProperty());
        
        canvas().setOnMousePressed(event -> onMousePressed(event));
        canvas().setOnMouseReleased(event -> onMouseReleased(event));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ProgressBar getProgressBar()
    {
        return progressBar;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- RESIZING ---">
    
    private int mouseX = -1;
    private int mouseY = -1;
    
    private void onMousePressed(MouseEvent e)
    {
        lock.lock();
        try {
            //            ConsoleBB.CONSOLE.print("Mouse Pressed: [" + e.getX() + ", " + e.getY() + "]");
            this.mouseX = (int) e.getX();
            this.mouseY = (int) e.getY();
        } finally {
            lock.unlock();
        }
    }
    
    private void onMouseReleased(MouseEvent e)
    {
        lock.lock();
        try {
            if (FXTools.get().isMouseOnNode(canvas()))
                getDragConsumer().accept(new MouseDragData(mouseX, mouseY, e.getX(), e.getY()));
        } finally {
            lock.unlock();
        }
    }
    
    public final void setDragConsumer(Consumer<MouseDragData> dragConsumer)
    {
        this.dragConsumer = ExceptionTools.nullCheck(dragConsumer, "Mouse Drag Consumer Function");
    }
    
    public final void resetDragConsumer()
    {
        setDragConsumer(mouseDragData -> { });
    }
    
    private Consumer<MouseDragData> getDragConsumer()
    {
        return dragConsumer;
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
