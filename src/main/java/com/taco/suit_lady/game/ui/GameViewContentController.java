package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.ui.ContentController;
import com.taco.suit_lady.ui.jfx.util.Dimensions;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import com.taco.suit_lady.ui.ui_internal.drag_and_drop.DragAndDropHandler;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.tools.BindingsSL;
import com.taco.suit_lady.util.tools.CalculationsSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@FxmlView("/fxml/game/content/game_view_content.fxml")
@Scope("prototype")
public class GameViewContentController
        extends ContentController<GameViewContent, GameViewContentData, GameViewContentController, GameFooter, GameFooterController>
        implements Lockable, GameComponent {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    @FXML private BorderPane borderPaneRoot;
    @FXML private AnchorPane mapPane;
    
    //</editor-fold>
    
    private GameViewContent content;
    private final ReentrantLock lock;
    
    //
    
    private DragAndDropHandler<GameObject> testDDHandler;
    
    private final ReadOnlyObjectWrapper<Point2D> mouseOnMapProperty;
    private final ReadOnlyObjectWrapper<Point2D> mouseOnMapPropertySafe;
    
    private DoubleBinding mouseOnMapBindingX;
    private DoubleBinding mouseOnMapBindingY;
    private DoubleBinding mouseOnMapBindingSafeX;
    private DoubleBinding mouseOnMapBindingSafeY;
    
    public GameViewContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        
        this.lock = new ReentrantLock();
        
        this.mouseOnMapProperty = new ReadOnlyObjectWrapper<>();
        this.mouseOnMapPropertySafe = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public GameViewContentController init(@NotNull GameViewContent content) {
        super.init(content);
        this.setGame(content);
        initMouseTracking();
        return this;
    }
    
    private void initMouseTracking() {
        this.mouseOnMapBindingX = BindingsSL.doubleBinding(() -> getMouseOnMap().getX(), mouseOnMapProperty());
        this.mouseOnMapBindingY = BindingsSL.doubleBinding(() -> getMouseOnMap().getY(), mouseOnMapProperty());
        
        this.mouseOnMapBindingSafeX = BindingsSL.doubleBinding(() -> getMouseOnMapSafe().getX(), mouseOnMapPropertySafe());
        this.mouseOnMapBindingSafeY = BindingsSL.doubleBinding(() -> getMouseOnMapSafe().getY(), mouseOnMapPropertySafe());
    }
    
    @Override public void initialize() {
        super.initialize();
        
        this.testDDHandler = new DragAndDropHandler<>(this, getLock(), root(), CellController.TEST_FORMAT, TransferMode.MOVE);
        testDDHandler.init();
        
        
        testDDHandler.setDragDetectedHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContent()));
        testDDHandler.setDragDoneHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContent()));
        
        testDDHandler.setDragEnteredHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContent()));
        testDDHandler.setDragExitedHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContent()));
        testDDHandler.setDragDroppedHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContent()));
    }
    
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    protected final void setGame(@NotNull GameViewContent content) { this.content = content; }
    
    public final AnchorPane getMapPane() { return mapPane; }
    
    //<editor-fold desc="> Mouse Properties">
    
    private @NotNull ReadOnlyObjectWrapper<Point2D> mouseOnMapProperty() { return mouseOnMapProperty; }
    public final @NotNull ReadOnlyObjectProperty<Point2D> readOnlyMouseOnMapProperty() { return mouseOnMapProperty().getReadOnlyProperty(); }
    public final @Nullable Point2D getMouseOnMap() { return mouseOnMapProperty().get(); }
    
    private @NotNull ReadOnlyObjectWrapper<Point2D> mouseOnMapPropertySafe() { return mouseOnMapPropertySafe; }
    public final @NotNull ReadOnlyObjectProperty<Point2D> readOnlyMouseOnMapPropertySafe() { return mouseOnMapPropertySafe().getReadOnlyProperty(); }
    public final @Nullable Point2D getMouseOnMapSafe() { return mouseOnMapPropertySafe().get(); }
    
    //
    
    public final @NotNull DoubleBinding mouseOnMapBindingX() { return mouseOnMapBindingX; }
    public final double getMouseOnMapX() { return mouseOnMapBindingX().get(); }
    
    public final @NotNull DoubleBinding mouseOnMapBindingY() { return mouseOnMapBindingY; }
    public final double getMouseOnMapY() { return mouseOnMapBindingY().get(); }
    
    
    public final @NotNull DoubleBinding mouseOnMapBindingSafeX() { return mouseOnMapBindingSafeX; }
    public final double getMouseOnMapSafeX() { return mouseOnMapBindingSafeX().get(); }
    
    public final @NotNull DoubleBinding mouseOnMapBindingSafeY() { return mouseOnMapBindingSafeY; }
    public final double getMouseOnMapSafeY() { return mouseOnMapBindingSafeY().get(); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return content; }
    
    @Override public void onGfxUpdate() { }
    @Override public void onGfxUpdateAlways() {
        ToolsFX.requireFX(() -> {
            //            Print.err("Mouse On Content Location Safe: " + getMouseOnContentSafe());
            final Point2D mouseOnContent = getMouseOnContent();
            final Point2D viewToMap = getContent().getCamera().viewToMap(mouseOnContent);
            final int xOffset = (int) Math.ceil(getContent().getTestObject().getWidth() / 2D);
            final int yOffset = (int) Math.ceil(getContent().getTestObject().getHeight() / 2D);
            final Dimensions minBounds = new Dimensions(xOffset, yOffset);
            final Dimensions maxBounds = new Dimensions(getGameMap().getPixelWidth() - xOffset, getGameMap().getPixelHeight() - yOffset);
            mouseOnMapProperty.set(viewToMap);
            mouseOnMapPropertySafe.set(CalculationsSL.getPointInBounds(viewToMap, minBounds, maxBounds));
//            Print.err("Mouse on Map Safe: " + getMouseOnMapSafe() + "  |  " + getGameMap().getPixelDimensions(), false);
            //            Print.err("Mouse On Map Location Safe: " + getMouseOnMapSafe());
        });
    }
    
    @Override protected boolean hasFooter() { return true; }
    //
    
    @Override public @NotNull Lock getLock() { return lock; }
    
    @Override public Pane root() { return root; }
    @Override public AnchorPane getContentPane() { return mapPane; }
    
    //</editor-fold>
}
