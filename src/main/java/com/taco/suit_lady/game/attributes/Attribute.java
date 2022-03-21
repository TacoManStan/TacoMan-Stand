package com.taco.suit_lady.game.attributes;

import com.taco.suit_lady.game.GameObjectComponent;
import com.taco.suit_lady.game.objects.GameObject;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Props;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;

/**
 * <p>i</p>
 * @param <T>
 */
public class Attribute<T>
        implements SpringableWrapper, Lockable, GameObjectComponent, Serializable, UIDProcessable {
    
    private final AttributeManager manager;
    
    private final ReadOnlyStringWrapper idProperty;
    private final ObjectProperty<T> valueProperty;
    
    private final Class<T> attributeType;
    
    private final DefaultAttributeModel<T> model;
    
    public Attribute(@NotNull AttributeManager manager, @NotNull String id, @NotNull Class<T> attributeType) {
        this(manager, id, null, attributeType);
    }
    
    public Attribute(@NotNull AttributeManager manager, @NotNull String id, @NotNull T value) {
        this(manager, id, value, null);
    }
    
    public Attribute(@NotNull AttributeManager manager, @Nullable String id, @Nullable T value, @Nullable Class<T> attributeType) {
        if (value == null && attributeType == null)
            throw Exc.unsupported("Value and Attribute Type parameters must not both be null.");
        
        this.manager = manager;
        
        this.attributeType = value != null ? (Class<T>) value.getClass() : attributeType;
        
        this.idProperty = new ReadOnlyStringWrapper(id);
        this.valueProperty = new SimpleObjectProperty<>(value);
        
        this.model = new DefaultAttributeModel<>(this);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    
    public final AttributeManager getManager() { return manager; }
    @Override public final @NotNull GameObject getOwner() { return manager.getOwner(); }
    public final DefaultAttributeModel<T> getModel() { return model; }
    
    
    public final ReadOnlyStringProperty readOnlyIdProperty() { return idProperty.getReadOnlyProperty(); }
    public final String getId() { return idProperty.get(); }
    public final String setId(@NotNull String newValue) { return Props.setProperty(idProperty, newValue); }
    
    public final ObjectProperty<T> valueProperty() { return valueProperty; }
    public final T getValue() { return valueProperty.get(); }
    public final T setValue(T newValue) { return Props.setProperty(valueProperty, newValue); }
    
    
    public final Class<T> getAttributeType() { return attributeType; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return manager.getGame(); }
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    @Override public @Nullable Lock getLock() { return getOwner().getLock(); }
    
    //
    
    private UIDProcessor uidProcessor;
    @Override public UIDProcessor getUIDProcessor() {
        if (uidProcessor == null)
            uidProcessor = new UIDProcessor("attributes");
        return uidProcessor;
    }
    
    //</editor-fold>
}
