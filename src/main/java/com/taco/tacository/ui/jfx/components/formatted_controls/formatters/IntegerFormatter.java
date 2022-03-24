package com.taco.tacository.ui.jfx.components.formatted_controls.formatters;

import com.taco.tacository.ui.jfx.components.formatted_controls.DoubleField;
import com.taco.tacository.ui.jfx.components.formatted_controls.IntField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * <p>Defines a {@link TextFormatter} implementation that restricts input to {@link Integer} {@link Number Numeric} values only.</p>
 * <p>Unlike {@link DecimalFormatter}, {@link IntegerFormatter} is restricted to {@link Integer Whole Number} input only, and does not permit {@link Double Decimal} input.</p>
 *
 * @see DecimalFormatter
 * @see IntField
 * @see DoubleField
 */
public class IntegerFormatter extends TextFormatter<Integer> {
    
    private static final int DEFAULT_VALUE = 0;
    
    private static final DecimalFormat integerFormat = new DecimalFormat("###,##0");
    
    public IntegerFormatter() {
        super(new StringConverter<>() {
            
                  @Override public String toString(Integer value) {
                      return integerFormat.format(value);
                  }
            
                  @Override public Integer fromString(String string) {
                      try {
                          return integerFormat.parse(string).intValue();
                      } catch (ParseException e) {
                          return -1;
                      }
                  }
              },
              DEFAULT_VALUE,
              change -> {
                  try {
                      if (change.getControlNewText().isEmpty() || change.isDeleted())
                          return change;
                      integerFormat.parse(change.getControlNewText()).intValue();
                      return change;
                  } catch (ParseException e) {
                      return null;
                  }
              });
    }
}