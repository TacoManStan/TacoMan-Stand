package com.taco.suit_lady._to_sort._new.interfaces;

import com.taco.tacository.obj_traits.common.Nameable;
import javafx.beans.property.ReadOnlyStringProperty;

public interface ReadOnlyNameableProperty
        extends Nameable
{
    ReadOnlyStringProperty nameProperty();
    
    @Override
    default String getName()
    {
        return nameProperty().get();
    }
}
