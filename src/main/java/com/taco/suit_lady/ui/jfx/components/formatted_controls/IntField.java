package com.taco.suit_lady.ui.jfx.components.formatted_controls;

import com.taco.suit_lady.ui.jfx.components.formatted_controls.formatters.IntegerFormatter;
import javafx.scene.control.TextField;

public class IntField extends TextField {
    
    private final IntegerFormatter formatter;
    
    public IntField() {
        super();
        this.formatter = new IntegerFormatter();
        init();
    }
    
    public IntField(String text) {
        super(text);
        this.formatter = new IntegerFormatter();
        init();
    }
    
    private void init() {
        setTextFormatter(getFormatter());
    }
    
    public final IntegerFormatter getFormatter() {
        return formatter;
    }
}
