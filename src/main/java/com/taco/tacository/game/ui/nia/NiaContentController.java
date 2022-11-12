package com.taco.tacository.game.ui.nia;

import com.taco.tacository.logic.GameTask;
import com.taco.tacository.logic.Tickable;
import com.taco.tacository.ui.ContentController;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.values.numbers.Num2D;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
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
@FxmlView("/fxml/nia/nia_content.fxml")
@Scope("prototype")
public class NiaContentController
        extends ContentController<NiaContent, NiaContentData, NiaContentController, NiaFooter, NiaFooterController>
        implements Lockable, Tickable<NiaContentController> {
    
    //<editor-fold desc="--- FXML FIELDS ---">
    
    @FXML private AnchorPane root;
    @FXML private BorderPane borderPaneRoot;
    @FXML private AnchorPane mapPane;
    
    //</editor-fold>
    
    private final ReentrantLock lock;
    
    private GameTask<NiaContentController> updateTask;
    
    //
    
    private final ReadOnlyObjectWrapper<Num2D> mouseOnMapProperty;
    private final ReadOnlyObjectWrapper<Num2D> mouseOnMapPropertySafe;
    
    private DoubleBinding mouseOnMapBindingX;
    private DoubleBinding mouseOnMapBindingY;
    private DoubleBinding mouseOnMapBindingSafeX;
    private DoubleBinding mouseOnMapBindingSafeY;
    
    public NiaContentController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        
        this.lock = new ReentrantLock();
        
        this.mouseOnMapProperty = new ReadOnlyObjectWrapper<>();
        this.mouseOnMapPropertySafe = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public NiaContentController init(@NotNull NiaContent content) {
        return super.init(content);
    }
    
    @Override public void initialize() {
        super.initialize();
    }
    
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
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
