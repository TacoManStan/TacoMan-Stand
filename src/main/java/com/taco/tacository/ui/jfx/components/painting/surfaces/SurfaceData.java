package com.taco.tacository.ui.jfx.components.painting.surfaces;

import com.taco.tacository.ui.jfx.components.painting.paintables.Paintable;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import com.taco.tacository.util.springable.StrictSpringable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SurfaceData<P extends Paintable<P, S>, S extends Surface<P, S>>
        implements SpringableWrapper, Lockable {
    
    private final StrictSpringable springable;
    private final ReentrantLock lock;
    
    //
    
    private final S owner;
    private final ListProperty<P> paintables;
    
    private final IntegerBinding widthBinding;
    private final IntegerBinding heightBinding;
    
    public SurfaceData(
            @NotNull Springable springable, @Nullable ReentrantLock lock, @NotNull S owner,
            @NotNull ObservableNumberValue observableWidth, @NotNull ObservableNumberValue observableHeight) {
        this.springable = springable.asStrict();
        this.lock = lock != null ? lock : new ReentrantLock();
        
        this.owner = owner;
        this.paintables = new SimpleListProperty<>(FXCollections.observableArrayList());
        
        this.widthBinding = Bindings.createIntegerBinding(() -> observableWidth.intValue(), observableWidth);
        this.heightBinding = Bindings.createIntegerBinding(() -> observableHeight.intValue(), observableHeight);
        
//        this.paintables.addListener((observable, oldValue, newValue) -> {
//            System.out.println("Paintable Added: " + newValue);
//        });
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull S getOwner() { return owner; }
    
    public final @NotNull ListProperty<P> paintables() { return paintables; }
    
    public final @NotNull IntegerBinding widthBinding() { return widthBinding; }
    public final @NotNull IntegerBinding heightBinding() { return heightBinding; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Lock getLock() { return lock; }
    @Override public @NotNull Springable springable() { return springable; }
    
    //</editor-fold>
}
