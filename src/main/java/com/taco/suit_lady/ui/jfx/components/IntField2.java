package com.taco.suit_lady.ui.jfx.components;

import com.taco.suit_lady.ui.jfx.components.formatters.IntegerFormatter;
import javafx.scene.control.TextField;

public class IntField2 extends TextField {
    
    private final IntegerFormatter formatter;
    
    public IntField2() {
        super();
        this.formatter = new IntegerFormatter();
        init();
    }
    
    public IntField2(String text) {
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
