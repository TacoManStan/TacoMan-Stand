package com.taco.tacository.ui.jfx.components.formatted_controls;

import com.taco.tacository.ui.jfx.components.formatted_controls.formatters.DecimalFormatter;
import com.taco.tacository.ui.jfx.components.formatted_controls.formatters.IntegerFormatter;
import javafx.scene.control.TextField;

/**
 * <p>An implementation of {@link TextField} that accepts {@link Number Numeric} input values only, <i>including</i> decimals.</p>
 * <blockquote><i>See {@link DecimalFormatter} for additional information.</i></blockquote>
 *
 * @see IntField
 * @see IntegerFormatter
 * @see DecimalFormatter
 */
public class DoubleField extends TextField {
    
    private final DecimalFormatter formatter;
    
    {
        this.formatter = new DecimalFormatter();
        init();
    }
    
    public DoubleField() {
        super();
    }
    
    public DoubleField(String text) {
        super(text);
    }
    
    private void init() {
        setTextFormatter(getFormatter());
    }
    
    public final DecimalFormatter getFormatter() {
        return formatter;
    }
}
