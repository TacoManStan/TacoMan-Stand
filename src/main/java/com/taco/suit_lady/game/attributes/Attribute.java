package com.taco.suit_lady.game.attributes;

import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.suit_lady.util.tools.PropertiesSL;
import javafx.beans.property.*;
import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.function.Function;

public class Attribute<T>
        implements WrappedGameComponent, Serializable, UIDProcessable {
    
    private final AttributeManager owner;
    
    private final ReadOnlyStringWrapper idProperty;
    private final ObjectProperty<T> valueProperty;
    
    private final Class<T> attributeType;
    
    private final DefaultAttributeModel<T> model;
    
    public Attribute(@NotNull AttributeManager owner, @NotNull String id, @NotNull Class<T> attributeType) {
        this(owner, id, null, attributeType);
    }
    
    public Attribute(@NotNull AttributeManager owner, @NotNull String id, @NotNull T value) {
        this(owner, id, value, null);
    }
    
    public Attribute(@NotNull AttributeManager owner, @Nullable String id, @Nullable T value, @Nullable Class<T> attributeType) {
        if (value == null && attributeType == null)
            throw ExceptionsSL.unsupported("Value and Attribute Type parameters must not both be null.");
        
        this.owner = owner;
        
        this.attributeType = value != null ? (Class<T>) value.getClass() : attributeType;
        
        this.idProperty = new ReadOnlyStringWrapper(id);
        this.valueProperty = new SimpleObjectProperty<>(value);
        
        this.model = new DefaultAttributeModel<>(this);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final AttributeManager getOwner() { return owner; }
    public final DefaultAttributeModel<T> getModel() { return model; }
    
    
    public final ReadOnlyStringProperty readOnlyIdProperty() { return idProperty.getReadOnlyProperty(); }
    public final String getId() { return idProperty.get(); }
    public final String setId(@NotNull String newValue) { return PropertiesSL.setProperty(idProperty, newValue); }
    
    public final ObjectProperty<T> valueProperty() { return valueProperty; }
    public final T getValue() { return valueProperty.get(); }
    public final T setValue(T newValue) { return PropertiesSL.setProperty(valueProperty, newValue); }
    
    
    public final Class<T> getAttributeType() { return attributeType; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    //
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("attributes");
        return uidProcessor;
    }
    
    //</editor-fold>
}
