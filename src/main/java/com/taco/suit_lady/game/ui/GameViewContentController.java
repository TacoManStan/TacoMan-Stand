package com.taco.suit_lady.game.ui;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.logic.GameTask;
import com.taco.suit_lady.logic.Tickable;
import com.taco.suit_lady.ui.ContentController;
import com.taco.suit_lady.ui.ui_internal.controllers.CellController;
import com.taco.suit_lady.ui.ui_internal.drag_and_drop.DragAndDropHandler;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Calc;
import com.taco.suit_lady.util.values.numbers.Num2D;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
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
        implements Lockable, GameComponent, Tickable<GameViewContentController> {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    @FXML private BorderPane borderPaneRoot;
    @FXML private AnchorPane mapPane;
    
    //</editor-fold>
    
    private GameViewContent content;
    private final ReentrantLock lock;
    
    private GameTask<GameViewContentController> updateTask;
    
    //
    
    private DragAndDropHandler<GameObject> testDDHandler;
    
    private final ReadOnlyObjectWrapper<Num2D> mouseOnMapProperty;
    private final ReadOnlyObjectWrapper<Num2D> mouseOnMapPropertySafe;
    
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
        
        setGame(content);
        
        initMouseTracking();
        initUpdateTask();
        
        return this;
    }
    
    private void initMouseTracking() {
        this.mouseOnMapBindingX = Bind.doubleBinding(() -> getMouseOnMap().a(), mouseOnMapProperty());
        this.mouseOnMapBindingY = Bind.doubleBinding(() -> getMouseOnMap().b(), mouseOnMapProperty());
        
        this.mouseOnMapBindingSafeX = Bind.doubleBinding(() -> getMouseOnMapSafe().a(), mouseOnMapPropertySafe());
        this.mouseOnMapBindingSafeY = Bind.doubleBinding(() -> getMouseOnMapSafe().b(), mouseOnMapPropertySafe());
    }
    
    private void initUpdateTask() {
        this.updateTask = new GameTask<>(this) {
            @Override protected void tick() {
                final Num2D mouseOnContent = getMouseOnContent();
                final Num2D viewToMap = getContent().getCamera().viewToMap(mouseOnContent);
                final int xOffset = (int) Math.ceil(getContent().getTestObject1().getWidth() / 2D);
                final int yOffset = (int) Math.ceil(getContent().getTestObject1().getHeight() / 2D);
                final Num2D minBounds = new Num2D(xOffset, yOffset);
                final Num2D maxBounds = new Num2D(getGameMap().getPixelWidth() - xOffset, getGameMap().getPixelHeight() - yOffset);
            
                mouseOnMapProperty.set(viewToMap);
                mouseOnMapPropertySafe.set(Calc.getPointInBounds(viewToMap, minBounds, maxBounds));
            }
        
            @Override protected void shutdown() { }
            @Override protected boolean isDone() { return false; }
    
            @Override public @Nullable Lock getLock() { return lock; }
        };
        taskManager().addTask(updateTask);
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
    
    private @NotNull ReadOnlyObjectWrapper<Num2D> mouseOnMapProperty() { return mouseOnMapProperty; }
    public final @NotNull ReadOnlyObjectProperty<Num2D> readOnlyMouseOnMapProperty() { return mouseOnMapProperty().getReadOnlyProperty(); }
    public final @Nullable Num2D getMouseOnMap() { return mouseOnMapProperty().get(); }
    
    private @NotNull ReadOnlyObjectWrapper<Num2D> mouseOnMapPropertySafe() { return mouseOnMapPropertySafe; }
    public final @NotNull ReadOnlyObjectProperty<Num2D> readOnlyMouseOnMapPropertySafe() { return mouseOnMapPropertySafe().getReadOnlyProperty(); }
    public final @Nullable Num2D getMouseOnMapSafe() { return mouseOnMapPropertySafe().get(); }
    
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
        //        ToolsFX.requireFX(() -> {
        //            Print.err("Mouse On Content Location Safe: " + getMouseOnContentSafe());
//        taskManager().addTask(Galaxy.newOneTimeTask(this, () -> {
//            final Point2D mouseOnContent = getMouseOnContent();
//            final Point2D viewToMap = getContent().getCamera().viewToMap(mouseOnContent);
//            final int xOffset = (int) Math.ceil(getContent().getTestObject().getWidth() / 2D);
//            final int yOffset = (int) Math.ceil(getContent().getTestObject().getHeight() / 2D);
//            final Dimensions minBounds = new Dimensions(xOffset, yOffset);
//            final Dimensions maxBounds = new Dimensions(getGameMap().getPixelWidth() - xOffset, getGameMap().getPixelHeight() - yOffset);
//
//            mouseOnMapProperty.set(viewToMap);
//            mouseOnMapPropertySafe.set(Calc.getPointInBounds(viewToMap, minBounds, maxBounds));
//        }));
        //            Print.err("Mouse on Map Safe: " + getMouseOnMapSafe() + "  |  " + getGameMap().getPixelDimensions(), false);
        //            Print.err("Mouse On Map Location Safe: " + getMouseOnMapSafe());
        //        });
    }
    
    @Override protected boolean hasFooter() { return true; }
    
    //
    
    @Override public @NotNull Lock getLock() { return lock; }
    
    @Override public Pane root() { return root; }
    @Override public AnchorPane getContentPane() { return mapPane; }
    
    //</editor-fold>
}
