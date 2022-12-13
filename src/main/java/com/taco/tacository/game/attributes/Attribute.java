package com.taco.tacository.game.attributes;

import com.taco.tacository.game.GameObjectComponent;
import com.taco.tacository.game.objects.GameObject;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.UIDProcessable;
import com.taco.tacository.util.UIDProcessor;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.tools.Props;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;

/**
 * <p>Contains a {@link #valueProperty() Property} and its {@link #getValue() Value} for a {@link GameObject} {@link #getOwner() owner}.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>To construct a new {@link Attribute}, use any of the available {@link Attribute#Attribute(AttributeManager, String, Object, Class) Constructors} or {@link AttributeManager} {@link AttributeManager#addAttribute(String, Object) Factory Method}.</li>
 *     <li>
 *         Each {@link Attribute} is a member of an {@link #getManager() AttributeManager} instance.
 *         <ul>
 *             <li>Use <i>{@link #getManager()}</i> to access the {@link AttributeManager} parent this {@link Attribute} is a member of.</li>
 *             <li>The {@link #getManager() AttributeManager} for this {@link Attribute} is always the same as the {@link GameObject#attributes() AttributeManager} assigned to this {@link Attribute Attributes} {@link #getOwner() owner}.</li>
 *         </ul>
 *     </li>
 *     <li>The {@link #getAttributeType() Attribute Type} of an {@link Attribute} defines the {@link T Type} of {@link #getValue() Value} this {@link Attribute} contains.</li>
 *     <li>The {@link #readOnlyIdProperty() Attribute ID} defines the {@link AttributeManager#containsKey(String) key value} used to {@link AttributeManager#getAttribute(String, Class) access} this {@link Attribute} from its {@link AttributeManager} parent.</li>
 *     <li>The {@link #getModel() AttributeModel} of an {@link Attribute} defines how the {@link Attribute} displays its {@link #getValue() Value} on its {@link AttributeManager} parents {@link AttributeManager#getValuePaneFactory() Value Pane}.</li>
 * </ol>
 * <p><i>See {@link AttributeManager} for additional information.</i></p>
 *
 * @param <T> The type of {@link #getValue() Value} the {@link #valueProperty() Value Property} of this {@link Attribute} contains.
 */
//TO-EXPAND: Examples
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
    
    @Override public @NotNull GameViewContent getContent() { return manager.getGame(); }
    
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
