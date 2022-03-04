package com.taco.suit_lady.ui.ui_internal.controllers;

import com.taco.suit_lady.ui.ui_internal.drag_and_drop.DragAndDropHandler;
import com.taco.suit_lady.util.tools.Obj;
import com.taco.suit_lady.util.tools.Props;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.Serializable;
import java.util.Objects;

public abstract class CellController<T extends Serializable> extends Controller {
    
    public static final DataFormat TEST_FORMAT = new DataFormat("test-format");
    
    private final ReadOnlyObjectWrapper<T> contentsProperty;
    private DragAndDropHandler<T> ddHandler;
    
    public CellController(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        super(weaver, ctx);
        
        this.contentsProperty = new ReadOnlyObjectWrapper<>();
        this.contentsProperty.addListener((observable, oldInstance, newInstance) -> {
            if (!Objects.equals(oldInstance, newInstance)) {
                Obj.doIfNonNull(() -> newInstance, t -> ddHandler.setValue(t));
                onContentChange(oldInstance, newInstance);
            }
        });
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyObjectProperty<T> contentsProperty() { return contentsProperty.getReadOnlyProperty(); }
    public final T getContents() { return contentsProperty.get(); }
    public final T setContents(T newValue) { return Props.setProperty(contentsProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract void onContentChange(T oldCellContents, T newCellContents);
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public void initialize() {
        this.ddHandler = new DragAndDropHandler<T>(this, null, root(), CellController.TEST_FORMAT, TransferMode.MOVE).init();
        
        this.ddHandler.setDragDetectedHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContents()));
        this.ddHandler.setDragDoneHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContents()));
        
        this.ddHandler.setDragEnteredHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContents()));
        this.ddHandler.setDragExitedHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContents()));
        this.ddHandler.setDragDroppedHandler(eventData -> System.out.println("Drag " + eventData.eventType() + " for " + getContents()));
    }
    
    //</editor-fold>
}
