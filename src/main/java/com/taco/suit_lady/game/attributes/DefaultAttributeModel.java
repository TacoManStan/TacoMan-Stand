package com.taco.suit_lady.game.attributes;

import com.taco.suit_lady.util.tools.Bind;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

public class DefaultAttributeModel<T> extends AttributeModel<T, Label> {
    
    public DefaultAttributeModel(@NotNull Attribute<T> owner) {
        super(owner);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override protected Label constructDisplay() { return new Label(); }
    
    @Override protected Label refreshBindings() {
        getDisplay().textProperty().bind(Bind.stringBinding(this::getText, getOwner().valueProperty()));
        return getDisplay();
    }
    
    @Override protected Label pauseBindings() {
        getDisplay().textProperty().unbind();
        return getDisplay();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private String getText() {
        final T value = getOwner().getValue();
        if (value != null)
            return value.toString();
        return "Attribute Value is Null";
    }
    
    //</editor-fold>
}
