package com.taco.suit_lady.ui.ui_internal.drag_and_drop;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ArraysSL;
import com.taco.suit_lady.util.tools.Exceptions;
import com.taco.suit_lady.util.tools.ObjectsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import javafx.scene.input.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;

public class DragAndDropHandler<T extends Serializable>
        implements SpringableWrapper, Lockable {
    
    private final StrictSpringable springable;
    private final Lock lock;
    
    //
    
    private final Node owner;
    
    private final DataFormat dataFormat;
    private final TransferMode[] transferModes;
    
    private final ReadOnlyObjectWrapper<T> valueProperty;
    
    //
    
    private final ReadOnlyObjectWrapper<Consumer<DragEventData<T, MouseEvent>>> dragDetectedSourceHandlerProperty;
    private final ReadOnlyObjectWrapper<Consumer<DragEventData<T, DragEvent>>> dragDoneSourceHandlerProperty;
    
    private final ReadOnlyObjectWrapper<Consumer<DragEventData<T, DragEvent>>> dragOverTargetHandlerProperty;
    private final ReadOnlyObjectWrapper<Consumer<DragEventData<T, DragEvent>>> dragEnteredTargetHandlerProperty;
    private final ReadOnlyObjectWrapper<Consumer<DragEventData<T, DragEvent>>> dragExitedTargetHandlerProperty;
    private final ReadOnlyObjectWrapper<Consumer<DragEventData<T, DragEvent>>> dragDroppedTargetHandlerProperty;
    
    public DragAndDropHandler(@NotNull Springable springable, @Nullable Lock lock, @NotNull Node owner, @NotNull DataFormat dataFormat, @NotNull TransferMode... transferModes) {
        this.springable = springable.asStrict();
        this.lock = lock;
        
        this.owner = owner;
        
        this.dataFormat = dataFormat;
        if (ArraysSL.containsNull(transferModes))
            throw Exceptions.ex(new NullPointerException(), "TransferMode Array cannot contain null elements:  " + ArraysSL.toString(transferModes));
        this.transferModes = !ArraysSL.isEmpty(transferModes) ? transferModes : TransferMode.ANY;
        
        this.valueProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.dragDetectedSourceHandlerProperty = new ReadOnlyObjectWrapper<>();
        this.dragDoneSourceHandlerProperty = new ReadOnlyObjectWrapper<>();
        
        this.dragOverTargetHandlerProperty = new ReadOnlyObjectWrapper<>();
        this.dragEnteredTargetHandlerProperty = new ReadOnlyObjectWrapper<>();
        this.dragExitedTargetHandlerProperty = new ReadOnlyObjectWrapper<>();
        this.dragDroppedTargetHandlerProperty = new ReadOnlyObjectWrapper<>();
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final DragAndDropHandler<T> init() {
        getOwner().setOnDragDetected(this::onDragDetected);
        getOwner().setOnDragDone(this::onDragDone);
        
        getOwner().setOnDragOver(this::onDragOver);
        getOwner().setOnDragDropped(this::onDragDropped);
        getOwner().setOnDragEntered(this::onDragEntered);
        getOwner().setOnDragExited(this::onDragExited);
        
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull Node getOwner() { return owner; }
    
    public final @NotNull DataFormat getDataFormat() { return dataFormat; }
    public final @NotNull TransferMode[] getTransferModes() { return transferModes; }
    
    public final @NotNull ReadOnlyObjectProperty<T> valueProperty() { return valueProperty.getReadOnlyProperty(); }
    public final @Nullable T getValue() { return valueProperty.get(); }
    public final @Nullable T setValue(@Nullable T newValue) { return PropertiesSL.setProperty(valueProperty, newValue); }
    
    //<editor-fold desc="> Drag Handler Properties">
    
    public final @NotNull ReadOnlyObjectProperty<Consumer<DragEventData<T, MouseEvent>>> readOnlyDragDetectedHandlerProperty() { return dragDetectedSourceHandlerProperty.getReadOnlyProperty(); }
    public final @Nullable Consumer<DragEventData<T, MouseEvent>> getDragDetectedHandler() { return dragDetectedSourceHandlerProperty.get(); }
    public final @Nullable Consumer<DragEventData<T, MouseEvent>> setDragDetectedHandler(@Nullable Consumer<DragEventData<T, MouseEvent>> newValue) { return PropertiesSL.setProperty(dragDetectedSourceHandlerProperty, newValue); }
    
    public final @NotNull ReadOnlyObjectProperty<Consumer<DragEventData<T, DragEvent>>> readOnlyDragDoneHandlerProperty() { return dragDoneSourceHandlerProperty.getReadOnlyProperty(); }
    public final @Nullable Consumer<DragEventData<T, DragEvent>> getDragDoneHandler() { return dragDoneSourceHandlerProperty.get(); }
    public final @Nullable Consumer<DragEventData<T, DragEvent>> setDragDoneHandler(@Nullable Consumer<DragEventData<T, DragEvent>> newValue) { return PropertiesSL.setProperty(dragDoneSourceHandlerProperty, newValue); }
    
    
    public final @NotNull ReadOnlyObjectProperty<Consumer<DragEventData<T, DragEvent>>> readOnlyDragOverHandlerProperty() { return dragOverTargetHandlerProperty.getReadOnlyProperty(); }
    public final @Nullable Consumer<DragEventData<T, DragEvent>> getDragOverHandler() { return dragOverTargetHandlerProperty.get(); }
    public final @Nullable Consumer<DragEventData<T, DragEvent>> setDragOverHandler(@Nullable Consumer<DragEventData<T, DragEvent>> newValue) { return PropertiesSL.setProperty(dragOverTargetHandlerProperty, newValue); }
    
    public final @NotNull ReadOnlyObjectProperty<Consumer<DragEventData<T, DragEvent>>> readOnlyDragEnteredHandlerProperty() { return dragEnteredTargetHandlerProperty.getReadOnlyProperty(); }
    public final @Nullable Consumer<DragEventData<T, DragEvent>> getDragEnteredHandler() { return dragEnteredTargetHandlerProperty.get(); }
    public final @Nullable Consumer<DragEventData<T, DragEvent>> setDragEnteredHandler(@Nullable Consumer<DragEventData<T, DragEvent>> newValue) { return PropertiesSL.setProperty(dragEnteredTargetHandlerProperty, newValue); }
    
    public final @NotNull ReadOnlyObjectProperty<Consumer<DragEventData<T, DragEvent>>> readOnlyDragExitedHandlerProperty() { return dragExitedTargetHandlerProperty.getReadOnlyProperty(); }
    public final @Nullable Consumer<DragEventData<T, DragEvent>> getDragExitedHandler() { return dragExitedTargetHandlerProperty.get(); }
    public final @Nullable Consumer<DragEventData<T, DragEvent>> setDragExitedHandler(@Nullable Consumer<DragEventData<T, DragEvent>> newValue) { return PropertiesSL.setProperty(dragExitedTargetHandlerProperty, newValue); }
    
    public final @NotNull ReadOnlyObjectProperty<Consumer<DragEventData<T, DragEvent>>> readOnlyDragDroppedHandlerProperty() { return dragDroppedTargetHandlerProperty.getReadOnlyProperty(); }
    public final @Nullable Consumer<DragEventData<T, DragEvent>> getDragDroppedHandler() { return dragDroppedTargetHandlerProperty.get(); }
    public final @Nullable Consumer<DragEventData<T, DragEvent>> setDragDroppedHandler(@Nullable Consumer<DragEventData<T, DragEvent>> newValue) { return PropertiesSL.setProperty(dragDroppedTargetHandlerProperty, newValue); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return springable; }
    
    @Override public @Nullable Lock getLock() { return lock; }
    @Override public boolean isNullableLock() { return true; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    //<editor-fold desc="> Drag Event Handling">
    
    private void onDragDetected(@NotNull MouseEvent event) {
        syncFX(() -> {
            final T value = getValue();
            if (value != null) {
                final Dragboard db = getOwner().startDragAndDrop(getTransferModes());
                final ClipboardContent cbContent = new ClipboardContent();
                
                cbContent.put(getDataFormat(), value);
                db.setContent(cbContent);
                
                ObjectsSL.doIfNonNull(this::getDragDetectedHandler, handler -> handler.accept(new DragEventData<>(event, this, DragEventType.DETECTED)));
            } else
                System.err.println("WARNING: Attempting to drag null element.");
            
            event.consume();
        });
    }
    
    private void onDragDone(@NotNull DragEvent event) {
        syncFX(() -> ObjectsSL.doIfNonNull(this::getDragDoneHandler, handler -> handler.accept(new DragEventData<>(event, this, DragEventType.DONE))));
    }
    
    //
    
    private void onDragOver(@NotNull DragEvent event) {
        syncFX(() -> {
            if (event.getGestureSource() != getOwner()) {
                event.acceptTransferModes(getTransferModes());
                ObjectsSL.doIfNonNull(this::getDragOverHandler, handler -> handler.accept(new DragEventData<>(event, this, DragEventType.OVER)));
            }
            event.consume();
        });
    }
    
    private void onDragDropped(@NotNull DragEvent event) {
        syncFX(() -> {
            final Dragboard db = event.getDragboard();
            
            boolean success = false;
            if (db.hasContent(getDataFormat())) {
                ObjectsSL.doIfNonNull(this::getDragDroppedHandler, handler -> handler.accept(new DragEventData<>(event, this, DragEventType.DROPPED)));
                success = true;
            }
            
            event.setDropCompleted(success);
            event.consume();
        });
    }
    
    
    private void onDragEntered(@NotNull DragEvent event) {
        syncFX(() -> ObjectsSL.doIfNonNull(this::getDragEnteredHandler, handler -> handler.accept(new DragEventData<>(event, this, DragEventType.ENTERED))));
    }
    
    private void onDragExited(@NotNull DragEvent event) {
        syncFX(() -> ObjectsSL.doIfNonNull(this::getDragExitedHandler, handler -> handler.accept(new DragEventData<>(event, this, DragEventType.EXITED))));
    }
    
    //
    
    @Deprecated
    private void onDragEvent(@NotNull DragEvent event, @Nullable Consumer<DragEventData<T, DragEvent>> eventHandler, @NotNull DragEventType eventType) {
        ObjectsSL.doIfNonNull(
                () -> eventHandler,
                handler -> handler.accept(new DragEventData<>(event, this, eventType)),
                handler -> System.err.println("WARNING: Drag Handler is null [ " + event + " ]  |  [ " + eventType + " ]"));
        event.consume();
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
