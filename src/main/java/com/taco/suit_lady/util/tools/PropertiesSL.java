package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.ui.console.Console;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class PropertiesSL {
    private PropertiesSL() { } //No Instance
    
    //<editor-fold desc="--- SET PROPERTY ---">
    
    public static boolean setProperty(@NotNull BooleanProperty property, boolean newValue) {
        boolean oldValue = property.get();
        property.set(newValue);
        return oldValue;
    }
    
    public static int setProperty(@NotNull IntegerProperty property, @NotNull Number newValue) {
        int oldValue = property.get();
        property.set(newValue.intValue());
        return oldValue;
    }
    
    public static long setProperty(@NotNull LongProperty property, @NotNull Number newValue) {
        long oldValue = property.get();
        property.set(newValue.longValue());
        return oldValue;
    }
    
    public static float setProperty(@NotNull FloatProperty property, @NotNull Number newValue) {
        float oldValue = property.get();
        property.set(newValue.floatValue());
        return oldValue;
    }
    
    public static double setProperty(@NotNull DoubleProperty property, @NotNull Number newValue) {
        double oldValue = property.get();
        property.set(newValue.doubleValue());
        return oldValue;
    }
    
    public static String setProperty(@NotNull StringProperty property, String newValue) {
        String oldValue = property.get();
        property.set(newValue);
        return oldValue;
    }
    
    public static <T> T setProperty(@NotNull ObjectProperty<T> property, T newValue) {
        T oldValue = property.get();
        property.set(newValue);
        return oldValue;
    }
    
    //</editor-fold>
}
