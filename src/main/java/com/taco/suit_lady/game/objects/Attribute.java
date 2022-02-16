package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.Lock;

public class Attribute<T>
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final AttributeManager owner;
    
    private final ReadOnlyStringWrapper idProperty;
    private final ObjectProperty<T> valueProperty;
    
    public Attribute(@NotNull AttributeManager owner) {
        this(owner, null, null);
    }
    
    public Attribute(@NotNull AttributeManager owner, @Nullable String id, @Nullable T value) {
        this.owner = owner;
        
        this.idProperty = new ReadOnlyStringWrapper(id);
        this.valueProperty = new SimpleObjectProperty<>(value);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final AttributeManager getOwner() { return owner; }
    
    
    public final ReadOnlyStringProperty readOnlyIdProperty() { return idProperty.getReadOnlyProperty(); }
    public final String getId() { return idProperty.get(); }
    public final String setId(@NotNull String newValue) { return PropertiesSL.setProperty(idProperty, newValue); }
    
    public final ObjectProperty<T> valueProperty() { return valueProperty; }
    public final T getValue() { return valueProperty.get(); }
    public final T setValue(T newValue) { return PropertiesSL.setProperty(valueProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull Springable springable() { return owner; }
    @Override public @NotNull Lock getLock() { return owner.getLock(); }
    
    //</editor-fold>
}
