package com.taco.tacository.ui.jfx.components.formatted_controls;

import com.taco.tacository.ui.jfx.components.formatted_controls.formatters.DecimalFormatter;
import com.taco.tacository.ui.jfx.components.formatted_controls.formatters.IntegerFormatter;
import javafx.scene.control.TextField;

/**
 * <p>An implementation of {@link TextField} that accepts {@link Number Numeric} input values only, <i>excluding</i> decimals ({@link Integer} values only).</p>
 * <blockquote><i>See {@link IntegerFormatter} for additional information.</i></blockquote>
 *
 * @see DoubleField
 * @see IntegerFormatter
 * @see DecimalFormatter
 */
public class IntField extends TextField {
    
    private final IntegerFormatter formatter;
    
    {
        this.formatter = new IntegerFormatter();
        init();
    }
    
    public IntField() {
        super();
    }
    
    public IntField(String text) {
        super(text);
    }
    
    //
    
    private void init() {
        setTextFormatter(getFormatter());
    }
    
    public final IntegerFormatter getFormatter() {
        return formatter;
    }
}
