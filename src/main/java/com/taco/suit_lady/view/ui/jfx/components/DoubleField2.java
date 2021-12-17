package com.taco.suit_lady.view.ui.jfx.components;

import javafx.scene.control.TextField;

import java.util.Objects;

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
//        getFormatter().valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (!Objects.equals(oldValue, newValue)) {
//                System.out.println("DoubleField2 Internal:  [" + oldValue + " -> " + newValue + "]");
//                setText("" + newValue);
//            }
//        });
//        focusedProperty().addListener((observable, oldValue, newValue) -> {
//            getFormatter().setValue(Double.parseDouble(getText()));
//        });
    }
    
    public final DecimalFormatter getFormatter() {
        return formatter;
    }
}
