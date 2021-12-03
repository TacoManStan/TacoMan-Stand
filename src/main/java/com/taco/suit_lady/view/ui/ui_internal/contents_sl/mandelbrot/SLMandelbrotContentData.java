package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContentData;
import javafx.scene.paint.Color;

public class SLMandelbrotContentData extends SLContentData
{
    public static class MandelbrotColor
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
