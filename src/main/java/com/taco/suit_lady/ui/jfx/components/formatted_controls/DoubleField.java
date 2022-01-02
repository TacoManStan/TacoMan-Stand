package com.taco.suit_lady.ui.jfx.components.formatted_controls;

import com.taco.suit_lady.ui.jfx.components.formatted_controls.formatters.DecimalFormatter;
import javafx.scene.control.TextField;

public class DoubleField extends TextField {
    
    private final DecimalFormatter formatter;
    
    public DoubleField() {
        super();
        this.formatter = new DecimalFormatter();
        init();
    }
    
    public DoubleField(String text) {
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
