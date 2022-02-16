package com.taco.suit_lady.game.objects;

import com.taco.suit_lady.game.interfaces.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import javafx.beans.property.MapProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class AttributeManager
        implements WrappedGameComponent {
    
    private final GameObject owner;
    private final MapProperty<String, Attribute<?>> attributeMap;
    
    public AttributeManager(@NotNull GameObject owner) {
        this.owner = owner;
        this.attributeMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final GameObject getOwner() { return owner; }
    
    //<editor-fold desc="> Attributes">
    
    //<editor-fold desc=">> Attribute Add Methods">
    
    public final @Nullable Attribute<?> addAttribute(@Nullable Attribute<?> attribute) {
        return sync(() -> {
            if (attribute != null && attribute.getId() != null)
                if (attributeMap.containsValue(attribute))
                    System.err.println("WARNING: AttributeManager already contains Attribute [" + attribute + "]");
                else if (attributeMap.containsKey(attribute.getId()))
                    System.err.println("WARNING: Attribute with ID \"" + attribute.getId() + "\" is already present in this AttributeManager [" + attributeMap.get(attribute.getId()));
                else
                    attributeMap.put(attribute.getId(), attribute);
            return attribute;
        });
    }
    
    public final <T> @NotNull Attribute<T> addAttribute(@NotNull String id, @NotNull T value) { return (Attribute<T>) addAttribute(new Attribute<>(this, id, value)); }
    
    //
    
    public final @NotNull Attribute<Boolean> addBooleanAttribute(@NotNull String id, boolean value) { return addAttribute(id, value); }
    
    public final @NotNull Attribute<Integer> addIntegerAttribute(@NotNull String id, int value) { return addAttribute(id, value); }
    public final @NotNull Attribute<Long> addLongAttribute(@NotNull String id, long value) { return addAttribute(id, value); }
    public final @NotNull Attribute<Float> addFloatAttribute(@NotNull String id, float value) { return addAttribute(id, value); }
    public final @NotNull Attribute<Double> addDoubleAttribute(@NotNull String id, double value) { return addAttribute(id, value); }
    
    public final @NotNull Attribute<Character> addCharacterAttribute(@NotNull String id, char value) { return addAttribute(id, value); }
    public final @NotNull Attribute<String> addStringAttribute(@NotNull String id, String value) { return addAttribute(id, value); }
    
    //</editor-fold>
    
    //<editor-fold desc=">> Attribute Accessor Methods">
    
    public final <T> Attribute<T> getAttribute(@NotNull String id, @NotNull Class<T> type) { return sync(() -> (Attribute<T>) attributeMap.get(id)); }
    
    
    public final Attribute<Boolean> getBooleanAttribute(@NotNull String id) { return getAttribute(id, Boolean.class); }
    
    public final Attribute<Integer> getIntegerAttribute(@NotNull String id) { return getAttribute(id, Integer.class); }
    public final Attribute<Long> getLongAttribute(@NotNull String id) { return getAttribute(id, Long.class); }
    public final Attribute<Float> getFloatAttribute(@NotNull String id) { return getAttribute(id, Float.class); }
    public final Attribute<Double> getDoubleAttribute(@NotNull String id) { return getAttribute(id, Double.class); }
    
    public final Attribute<Character> getCharacterAttribute(@NotNull String id) { return getAttribute(id, Character.class); }
    public final Attribute<String> getStringAttribute(@NotNull String id) { return getAttribute(id, String.class); }
    
    //</editor-fold>
    
    //<editor-fold desc=">> Attribute Property Accessor Methods">
    
    public final <T> Property<T> getProperty(@NotNull String id, @NotNull Class<T> type) { return getAttribute(id, type).valueProperty(); }
    public final <T> @Nullable T getValue(@NotNull String id, @NotNull Class<T> type, @NotNull Supplier<T> defaultValueSupplier) {
        final Attribute<T> attribute = getAttribute(id, type);
        if (attribute != null)
            return attribute.getValue();
        final T factoryValue = defaultValueSupplier.get();
        addAttribute(new Attribute<>(this, id, factoryValue));
        return factoryValue;
    }
    
    //
    
    public final Property<Boolean> getBooleanProperty(@NotNull String id) { return getBooleanAttribute(id).valueProperty(); }
    public final boolean getBooleanValue(@NotNull String id) { return getBooleanValue(id, this::defaultBooleanValue); }
    public final boolean getBooleanValue(@NotNull String id, @NotNull Supplier<Boolean> defaultValueSupplier) { return getValue(id, Boolean.class, defaultValueSupplier); }
    
    
    public final Property<Integer> getIntegerProperty(@NotNull String id) { return getIntegerAttribute(id).valueProperty(); }
    public final int getIntegerValue(@NotNull String id) { return getIntegerValue(id, this::defaultIntValue); }
    public final int getIntegerValue(@NotNull String id, @NotNull Supplier<Integer> defaultValueSupplier) { return getValue(id, Integer.class, defaultValueSupplier); }
    
    public final Property<Long> getLongProperty(@NotNull String id) { return getLongAttribute(id).valueProperty(); }
    public final long getLongValue(@NotNull String id) { return getLongValue(id, this::defaultLongValue); }
    public final long getLongValue(@NotNull String id, @NotNull Supplier<Long> defaultValueSupplier) { return getValue(id, Long.class, defaultValueSupplier); }
    
    public final Property<Float> getFloatProperty(@NotNull String id) { return getFloatAttribute(id).valueProperty(); }
    public final float getFloatValue(@NotNull String id) { return getFloatValue(id, this::defaultFloatValue); }
    public final float getFloatValue(@NotNull String id, @NotNull Supplier<Float> defaultValueSupplier) { return getValue(id, Float.class, defaultValueSupplier); }
    
    public final Property<Double> getDoubleProperty(@NotNull String id) { return getDoubleAttribute(id).valueProperty(); }
    public final double getDoubleValue(@NotNull String id) { return getDoubleValue(id, this::defaultDoubleValue); }
    public final double getDoubleValue(@NotNull String id, @NotNull Supplier<Double> defaultValueSupplier) { return getValue(id, Double.class, defaultValueSupplier); }
    
    
    public final Property<Character> getCharacterProperty(@NotNull String id) { return getCharacterAttribute(id).valueProperty(); }
    public final char getCharacterValue(@NotNull String id) { return getCharacterValue(id, this::defaultCharValue); }
    public final char getCharacterValue(@NotNull String id, @NotNull Supplier<Character> defaultValueSupplier) { return getValue(id, Character.class, defaultValueSupplier); }
    
    public final Property<String> getStringProperty(@NotNull String id) { return getStringAttribute(id).valueProperty(); }
    public final String getStringValue(@NotNull String id) { return getStringValue(id, this::defaultStringValue); }
    public final String getStringValue(@NotNull String id, @NotNull Supplier<String> defaultValueSupplier) { return getValue(id, String.class, defaultValueSupplier); }
    
    //<editor-fold desc=">>> Default Value Accessors">
    
    public final boolean defaultBooleanValue() { return false; }
    
    public final int defaultIntValue() { return -1; }
    public final long defaultLongValue() { return defaultIntValue(); }
    public final float defaultFloatValue() { return defaultIntValue(); }
    public final double defaultDoubleValue() { return defaultIntValue(); }
    
    public final char defaultCharValue() { return ' '; }
    @Contract(pure = true) public final @NotNull String defaultStringValue() { return ""; }
    
    public final @Nullable <T> T defaultObjValue() { return null; }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return owner.getGame(); }
    
    //</editor-fold>
}
