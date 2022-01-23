package com.taco.suit_lady.util.tools;

import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;

public class SLProperties {
    private SLProperties() { } //No Instance
    
    public static boolean setProperty(@NotNull BooleanProperty property, boolean newValue) {
        boolean oldValue = property.get();
        property.set(newValue);
        return oldValue;
    }
    
    public static int setProperty(@NotNull IntegerProperty property, int newValue) {
        int oldValue = property.get();
        property.set(newValue);
        return oldValue;
    }
    
    public static double setProperty(@NotNull DoubleProperty property, double newValue) {
        double oldValue = property.get();
        property.set(newValue);
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
}
