package com.taco.suit_lady.view.ui.jfx.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class DoubleField extends TextField {
    
    private final DoubleProperty valueProperty;
    private final DoubleProperty minValueProperty;
    private final DoubleProperty maxValueProperty;
    
    public DoubleField() {
        this(0.0, Double.MAX_VALUE, 0.0);
    }
    
    public DoubleField(double initialValue) {
        this(0.0, Double.MAX_VALUE, initialValue);
    }
    
    public DoubleField(double minValue, double maxValue) {
        this(minValue, maxValue, minValue);
    }
    
    public DoubleField(double minValue, double maxValue, double initialValue) {
        if (minValue > maxValue)
            throw new IllegalArgumentException("IntField min value " + minValue + " greater than max value " + maxValue);
        if (!((minValue <= initialValue) && (initialValue <= maxValue)))
            throw new IllegalArgumentException("IntField initialValue " + initialValue + " not between " + minValue + " and " + maxValue);
        
        // initialize the field values.
        this.valueProperty = new SimpleDoubleProperty(initialValue);
        this.minValueProperty = new SimpleDoubleProperty(minValue);
        this.maxValueProperty = new SimpleDoubleProperty(maxValue);
        
        setText(initialValue + "");
        
        // make sure the value property is clamped to the required range
        // and update the field's text to be in sync with the value.
        valueProperty.addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null)
                setText("");
            else if (newValue.doubleValue() < getMinValue())
                valueProperty.set(getMinValue());
            else if (newValue.doubleValue() > getMaxValue())
                valueProperty.set(getMaxValue());
            else if (newValue.doubleValue() != 0 || (textProperty().get() != null && !"".equals(textProperty().get())))
                setText(newValue.toString());
        });
        
        // restrict key input to numerals.
        this.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (getMinValue() < 0) {
                if (!"-0123456789.".contains(keyEvent.getCharacter()))
                    keyEvent.consume();
            } else {
                if (!"0123456789.".contains(keyEvent.getCharacter()))
                    keyEvent.consume();
            }
        });
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public DoubleProperty valueProperty() { return valueProperty; }
    
    public double getValue() { return valueProperty.get(); }
    
    public void setValue(double newValue) { valueProperty.set(newValue); }
    
    public DoubleProperty minValueProperty() {
        return minValueProperty;
    }
    
    public double setMinValue(double newValue) {
        double oldValue = getMinValue();
        minValueProperty.set(newValue);
        return oldValue;
    }
    
    public double getMinValue() {
        return minValueProperty.get();
    }
    
    public DoubleProperty maxValueProperty() {
        return maxValueProperty;
    }
    
    public double setMaxValue(double newValue) {
        double oldValue = getMaxValue();
        maxValueProperty.set(newValue);
        return oldValue;
    }
    
    public double getMaxValue() {
        return maxValueProperty.get();
    }
    
    //</editor-fold>
}