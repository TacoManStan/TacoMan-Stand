package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContentData;
import javafx.scene.paint.Color;

public class SLMandelbrotContentData extends SLContentData
{
    private final Color[] presetColors;
    private Color[][] colors;
    
    public SLMandelbrotContentData()
    {
        this.colors = new Color[600][400];
    
        this.presetColors = new Color[255 * 2];
        
        // Init Preset Colors
        for (int i = 0; i < 255; i++) {
            presetColors[i] = Color.color(0, 0, i);
            presetColors[i + 255] = Color.color(0, 0, 255 - i);
        }
    }
    
    //
    
    public class MandelbrotColor
    {
        private int bigN;
        private int smallN;
        private double x;
        private double y;
        
        private MandelbrotColor(int smallN, int bigN, double x, double y)
        {
            this.bigN = bigN;
            this.smallN = smallN;
            this.x = x;
            this.y = y;
        }
        
        private MandelbrotColor(Color color)
        {
            if (color.equals(Color.BLACK)) {
                this.bigN = -1;
                this.smallN = 0;
                this.x = 0;
                this.y = 0;
            }
        }
        
        public int getBigN()
        {
            return bigN;
        }
        
        public int getSmallN()
        {
            return smallN;
        }
        
        public double getX()
        {
            return x;
        }
        
        public double getY()
        {
            return y;
        }
        
        public Color getColor()
        {
            if (getBigN() == -1)
                return Color.BLACK;
            
            final double z = Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
            
            final double part1 = Math.log10(z);
            final double part2 = Math.log10(getBigN());
            final double partWhole = Math.log(part1 / part2) / Math.log(2);
            
            final int whole = (int) ((getSmallN() - partWhole) * 20);
            
            final int total = presetColors.length;
            final int value = whole % total;
            
            return presetColors[value];
        }
        
        @Override
        public String toString()
        {
            return "\nn: " + getSmallN()
                   + "\nN: " + getBigN()
                   + "\nX: " + getX()
                   + "\nY: " + getY();
        }
    }
}
