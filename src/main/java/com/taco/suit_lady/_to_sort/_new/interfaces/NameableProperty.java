package com.taco.suit_lady._to_sort._new.interfaces;

import javafx.beans.property.StringProperty;

public interface NameableProperty extends ReadOnlyNameableProperty
{
    @Override
    StringProperty nameProperty();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default void setName(String name)
    {
        nameProperty().set(name);
    }
    
    //</editor-fold>
}
