package com.taco.tacository._to_sort.json;

import com.github.cliftonlabs.json_simple.JsonObject;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class ColorJObjectWrapper extends NonJObjectFieldWrapper<Color> {
    
    private double red;
    private double green;
    private double blue;
    private double alpha;
    
    public ColorJObjectWrapper(String jID) {
        this(jID, null);
    }
    
    public ColorJObjectWrapper(String jID, Color color) {
        super(jID);
        
        if (color != null)
            loadFromValue(color);
    }
    
    @Override
    public void doLoad(JsonObject parent) {
        red = JUtil.loadDouble(parent, "red");
        green = JUtil.loadDouble(parent, "green");
        blue = JUtil.loadDouble(parent, "blue");
        alpha = JUtil.loadDouble(parent, "alpha");
    }
    
    @Override
    public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("red", red),
                JUtil.create("green", green),
                JUtil.create("blue", blue),
                JUtil.create("alpha", alpha)
        };
    }
    
    @Override
    protected Color value() {
        return Color.color(red, green, blue, alpha);
    }
    
    @Override
    protected void loadFromValue(@NotNull Color value) {
        red = value.getRed();
        green = value.getGreen();
        blue = value.getBlue();
        alpha = value.getOpacity();
    }
}
