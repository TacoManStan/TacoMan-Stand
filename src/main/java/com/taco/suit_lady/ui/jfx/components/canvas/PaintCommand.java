package com.taco.suit_lady.ui.jfx.components.canvas;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.fx_tools.FXTools;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public abstract class PaintCommand
        implements SpringableWrapper, Lockable, CanvasUICommand {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    
    private final ReadOnlyListWrapper<BoundCanvas> owners;
    
    private final ReadOnlyObjectWrapper<Predicate<BoundCanvas>> autoRemoveConditionProperty;
    private final BooleanProperty disabledProperty;
    
    public PaintCommand(@NotNull Springable springable, @Nullable ReentrantLock lock) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        
        this.owners = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
        
        this.autoRemoveConditionProperty = new ReadOnlyObjectWrapper<>();
        this.disabledProperty = new SimpleBooleanProperty(false);
        
        this.disabledProperty.addListener((observable, oldValue, newValue) -> repaintOwners());
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public PaintCommand init() {
        return this;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull ReadOnlyListProperty<BoundCanvas> owners() {
        return owners.getReadOnlyProperty();
    }
    
    
    public final @NotNull ReadOnlyObjectProperty<Predicate<BoundCanvas>> autoRemoveConditionProperty() {
        return autoRemoveConditionProperty.getReadOnlyProperty();
    }
    
    public final @Nullable Predicate<BoundCanvas> getAutoRemoveCondition() {
        return autoRemoveConditionProperty.get();
    }
    
    public final @Nullable Predicate<BoundCanvas> setAutoRemoveCondition(@Nullable Predicate<BoundCanvas> newValue) {
        Predicate<BoundCanvas> oldValue = getAutoRemoveCondition();
        autoRemoveConditionProperty.set(newValue);
        return oldValue;
    }
    
    
    public final @NotNull BooleanProperty disabledProperty() {
        return disabledProperty;
    }
    
    public final boolean isDisabled() {
        return disabledProperty.get();
    }
    
    public final boolean setDisabled(boolean newValue) {
        boolean oldValue = isDisabled();
        disabledProperty.set(newValue);
        return oldValue;
    }
    
    //</editor-fold>
    
    protected abstract void onPaint(BoundCanvas canvas);
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public void paint(BoundCanvas canvas) {
        System.out.println("Painting");
        if (!isDisabled())
            FXTools.runFX(() -> sync(() -> {
                Predicate<BoundCanvas> autoRemoveCondition = getAutoRemoveCondition();
                if (autoRemoveCondition != null && autoRemoveCondition.test(canvas))
                    canvas.removePaintCommand(this);
                else
                    onPaint(canvas);
            }), true);
    }
    
    
    @Override
    public void onAdd(BoundCanvas canvas) {
        sync(() -> { owners.add(canvas); });
    }
    
    @Override
    public void onRemove(BoundCanvas canvas) {
        sync(() -> { owners.remove(canvas); });
    }
    
    //
    
    @Override
    public @NotNull Springable springable() {
        return springable;
    }
    
    @Override
    public @NotNull Lock getLock() {
        return lock;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    protected void repaintOwners() {
        sync(() -> FXTools.runFX(() -> owners.forEach(canvas -> canvas.repaint()), true));
    }
    
    //</editor-fold>
}
