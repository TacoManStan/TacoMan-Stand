package com.taco.suit_lady.view.ui.jfx.components;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class IntField extends TextField {
    
    private final IntegerProperty valueProperty;
    private final IntegerProperty minValueProperty;
    private final IntegerProperty maxValueProperty;
    
    public IntField() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
    }
    
    public IntField(int initialValue) {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE, initialValue);
    }
    
    public IntField(int minValue, int maxValue) {
        this(minValue, maxValue, minValue);
    }
    
    public IntField(int minValue, int maxValue, int initialValue) {
        if (minValue > maxValue)
            throw new IllegalArgumentException("IntField min value " + minValue + " greater than max value " + maxValue);
        if (!((minValue <= initialValue) && (initialValue <= maxValue)))
            throw new IllegalArgumentException("IntField initialValue " + initialValue + " not between " + minValue + " and " + maxValue);
        
        // initialize the field values.
        this.valueProperty = new SimpleIntegerProperty(initialValue);
        this.minValueProperty = new SimpleIntegerProperty(minValue);
        this.maxValueProperty = new SimpleIntegerProperty(maxValue);
        
        setText(initialValue + "");
        
        // make sure the value property is clamped to the required range
        // and update the field's text to be in sync with the value.
        valueProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null)
                setText("");
            else if (newValue.intValue() < getMinValue())
                valueProperty.set(getMinValue());
            else if (newValue.intValue() > getMaxValue())
                valueProperty.set(getMaxValue());
            else if (newValue.intValue() != 0 || (textProperty().get() != null && !"".equals(textProperty().get())))
                setText(newValue.toString());
        });
        
        // restrict key input to numerals.
        this.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (getMinValue() < 0) {
                if (!"-0123456789".contains(keyEvent.getCharacter()))
                    keyEvent.consume();
            } else {
                if (!"0123456789".contains(keyEvent.getCharacter()))
                    keyEvent.consume();
            }
        });
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public IntegerProperty valueProperty() { return valueProperty; }
    
    public int getValue() { return valueProperty.get(); }
    
    public void setValue(int newValue) { valueProperty.set(newValue); }
    
    public IntegerProperty minValueProperty() {
        return minValueProperty;
    }
    
    public int setMinValue(int newValue) {
        int oldValue = getMinValue();
        minValueProperty.set(newValue);
        return oldValue;
    }
    
    public int getMinValue() {
        return minValueProperty.get();
    }
    
    public IntegerProperty maxValueProperty() {
        return maxValueProperty;
    }
    
    public int setMaxValue(int newValue) {
        int oldValue = getMaxValue();
        maxValueProperty.set(newValue);
        return oldValue;
    }
    
    public int getMaxValue() {
        return maxValueProperty.get();
    }
    
    //</editor-fold>
}