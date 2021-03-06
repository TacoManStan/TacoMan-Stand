package com.taco.tacository.ui.contents.mandelbrot;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum MandelbrotColorScheme {
    
    RED(i -> (i / 255.0), null, null,
        i -> ((255.0 - i) / 255.0), null, null),
    GREEN(null, i -> (i / 255.0), null,
          null, i -> ((255.0 - i) / 255.0), null),
    BLUE(null, null, i -> (i / 255.0),
         null, null, i -> ((255.0 - i) / 255.0)),
    MAGENTA(i -> (i / 255.0), null, i -> (i / 255.0),
            i -> ((255.0 - i) / 255.0), null, i -> ((255.0 - i) / 255.0)),
    AQUA(null, i -> (i / 255.0), i -> (i / 255.0),
         null, i -> ((255.0 - i) / 255.0), i -> ((255.0 - i) / 255.0)),
    YELLOW(i -> (i / 255.0), i -> (i / 255.0), null,
           i -> ((255.0 - i) / 255.0), i -> ((255.0 - i) / 255.0), null);
    
    
    private final Function<Integer, Double> red1;
    private final Function<Integer, Double> green1;
    private final Function<Integer, Double> blue1;
    private final Function<Integer, Double> red2;
    private final Function<Integer, Double> green2;
    private final Function<Integer, Double> blue2;
    
    private Color[] cache;
    private Color[] reverseCache;
    
    MandelbrotColorScheme(
            @Nullable Function<Integer, Double> red1, @Nullable Function<Integer, Double> green1, @Nullable Function<Integer, Double> blue1,
            @Nullable Function<Integer, Double> red2, @Nullable Function<Integer, Double> green2, @Nullable Function<Integer, Double> blue2) {
        this.red1 = red1 != null ? red1 : i -> 0.0;
        this.green1 = green1 != null ? green1 : i -> 0.0;
        this.blue1 = blue1 != null ? blue1 : i -> 0.0;
        this.red2 = red2 != null ? red2 : i -> 0.0;
        this.green2 = green2 != null ? green2 : i -> 0.0;
        this.blue2 = blue2 != null ? blue2 : i -> 0.0;
    }
    
    public @NotNull Color @NotNull [] getColors(boolean reverse) {
        if ((reverse && reverseCache == null) || (!reverse && cache == null)) {
            final Color[] colors = new Color[255 * 2];
            for (int i = 0; i < 255; i++) {
                colors[i] = Color.color(
                        reverse ? red2.apply(i) : red1.apply(i),
                        reverse ? green2.apply(i) : green1.apply(i),
                        reverse ? blue2.apply(i) : blue1.apply(i));
                colors[i + 255] = Color.color(
                        reverse ? red1.apply(i) : red2.apply(i),
                        reverse ? green1.apply(i) : green2.apply(i),
                        reverse ? blue1.apply(i) : blue2.apply(i));
            }
            if (reverse)
                reverseCache = colors;
            else
                cache = colors;
        }
        
        return reverse ? reverseCache : cache;
    }
}
