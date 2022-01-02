package com.taco.suit_lady.ui.jfx.components;

import com.taco.suit_lady.ui.jfx.components.formatters.DecimalFormatter;
import javafx.scene.control.TextField;

public class DoubleField2 extends TextField {
    
    private final DecimalFormatter formatter;
    
    public DoubleField2() {
        super();
        this.formatter = new DecimalFormatter();
        init();
    }
    
    public DoubleField2(String text) {
        super(text);
        this.formatter = new DecimalFormatter();
        init();
    }
    
    private void init() {
        setTextFormatter(getFormatter());
    }
    
    public final DecimalFormatter getFormatter() {
        return formatter;
    }
}
