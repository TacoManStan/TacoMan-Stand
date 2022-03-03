package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.values.NumberValuePair;
import com.taco.suit_lady.util.values.NumberValuePairable;
import org.jetbrains.annotations.NotNull;

public class Maths {
    private Maths() { } //No Instance
    
    //<editor-fold desc="--- BASIC ---">
    
    public static int ceil(@NotNull Number val1, @NotNull Number val2) {
        return (int) Math.ceil(val1.doubleValue() / val2.doubleValue());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- TRIGONOMETRY ---">
    
    public static double degreesToRads(@NotNull Number angle) { return angle.doubleValue() * (Math.PI / 180); }
    public static double radsToDegrees(@NotNull Number angle) { return angle.doubleValue() * (180 / Math.PI); }
    
    
    public static @NotNull NumberValuePair pointOnCircle(@NotNull Number x, @NotNull Number y, @NotNull Number radius, @NotNull Number degrees) {
        final double pX = (Math.cos(degreesToRads(degrees)) * radius.doubleValue()) + x.doubleValue();
        final double pY = (Math.sin(degreesToRads(degrees)) * radius.doubleValue()) + y.doubleValue();
        return new NumberValuePair(pX, pY);
    }
    public static @NotNull NumberValuePair pointOnCircle(@NotNull NumberValuePairable<?> offset, @NotNull Number radius, @NotNull Number degrees) { return pointOnCircle(offset.a(), offset.b(), radius, degrees); }
    public static @NotNull NumberValuePair pointOnCircle(@NotNull Number radius, @NotNull Number degrees) { return pointOnCircle(0, 0, radius, degrees); }
    
    //</editor-fold>
}
