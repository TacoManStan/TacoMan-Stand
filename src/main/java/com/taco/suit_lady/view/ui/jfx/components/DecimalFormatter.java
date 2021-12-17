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
                      System.out.println("TO STRING - Input: " + value);
                      String str = strictZeroDecimalFormat.format(value);
                      System.out.println("TO STRING - Result: " + str);
                      return str;
                  }
            
                  @Override
                  public Double fromString(String string) {
                      try {
                          System.out.println("FROM STRING - Input: " + string);
                          Number num = strictZeroDecimalFormat.parse(string).doubleValue();
                          System.out.println("FROM STRING - Result: " + num);
                          return num.doubleValue();
                      } catch (ParseException e) {
                          return Double.NaN;
                      }
                  }
              },
              DEFAULT_VALUE,
              change -> {
                  try {
                      if (change.getControlNewText().isEmpty() || change.isDeleted()) {
                          System.out.println("Valid Non-Numeric Change... Returning.");
                          return change;
                      }
                      Number num = strictZeroDecimalFormat.parse(change.getControlNewText()).doubleValue();
                      System.out.println("CHANGE - Result: " + num);
                      return change;
                  } catch (ParseException e) {
                      System.out.println("Parse Exception");
                      return null;
                  }
              });
    }
}