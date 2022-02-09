package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.interfaces.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

public class Attribute<T>
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final GameObject owner;
    
    private final ReadOnlyStringWrapper idProperty;
    private final ObjectProperty<T> valueProperty;
    
    public Attribute(@NotNull GameObject owner) {
        this.owner = owner;
        
        this.valueProperty = new SimpleObjectProperty<>();
        this.idProperty = new ReadOnlyStringWrapper();
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() { return owner; }
    
    
    public final ReadOnlyStringProperty readOnlyIdProperty() { return idProperty.getReadOnlyProperty(); }
    public final String getId() { return idProperty.get(); }
    public final String setId(@NotNull String newValue) { return PropertiesSL.setProperty(idProperty, newValue); }
    
    public final ObjectProperty<T> valueProperty() { return valueProperty; }
    public final T getValue() { return valueProperty.get(); }
    public final T setValue(T newValue) { return PropertiesSL.setProperty(valueProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    @Override public @NotNull Springable springable() { return owner; }
    @Override public @NotNull Lock getLock() { return owner.getLock(); }
    
    //</editor-fold>
}
