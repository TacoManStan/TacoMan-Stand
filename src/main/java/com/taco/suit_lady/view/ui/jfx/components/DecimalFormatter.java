package com.taco.suit_lady.view.ui.jfx.components;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;

public class DecimalFormatter extends TextFormatter<Double> {
    
    private static final double DEFAULT_VALUE = 0.0d;
    private static final String CURRENCY_SYMBOL = "\uFF04"; // british pound
    
    private static final DecimalFormat strictZeroDecimalFormat
            = new DecimalFormat("###,##0.00################");
    
    public DecimalFormatter() {
        super(new StringConverter<>() {
                  @Override
                  public String toString(Double value) {
                      return strictZeroDecimalFormat.format(value);
                  }
            
                  @Override
                  public Double fromString(String string) {
                      try {
                          return strictZeroDecimalFormat.parse(string).doubleValue();
                      } catch (ParseException e) {
                          return Double.NaN;
                      }
                  }
              },
              DEFAULT_VALUE,
              change -> {
                  try {
                      if (change.getControlNewText().isEmpty() || change.isDeleted())
                          return change;
                      strictZeroDecimalFormat.parse(change.getControlNewText()).doubleValue();
                      return change;
                  } catch (ParseException e) {
                      return null;
                  }
              });
    }
}