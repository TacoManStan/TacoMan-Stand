package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady._to_sort._new.MatrixIterator;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot.MandelbrotIterator.MandelbrotColor;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.concurrent.locks.ReentrantLock;

public class MandelbrotIterator extends MatrixIterator<MandelbrotColor>
{
    private final int PRECISION = 1000;
    
    private final MandelbrotDimensions dimensions;
    
    private final Color[] presetColors;
    
    public MandelbrotIterator(MandelbrotColor[][] targetArray, ReentrantLock lock)
    {
        this(targetArray, null, lock);
    }
    
    public MandelbrotIterator(MandelbrotColor[][] targetArray, MandelbrotDimensions dimensions, ReentrantLock lock)
    {
        super(targetArray, lock);
        
        this.dimensions = dimensions != null ? dimensions : MandelbrotDimensions.newDefaultInstance(getWidth(), getHeight());
        
        if (dimensions.getCanvasWidth() != getWidth() || dimensions.getCanvasHeight() != getHeight())
            throw ExceptionTools.ex("Dimension Mismatch:  " +
                                    "Dimensions Data [" + dimensions.getCanvasWidth() + ", " + dimensions.getCanvasHeight() + "  " +
                                    "Iterator Data [" + getWidth() + ", " + getHeight());
        
        this.presetColors = new Color[255 * 2];
        this.initColors();
    }
    
    @Override
    protected MandelbrotColor step(int i, int j)
    {
        final Point2D scaledPoint = dimensions.convertFromCanvas(i, j);
        double x = 0, y = 0;
        int n = 0;
        int N = (int) Math.pow(10, 100);
        if (escapes(i, j)) {
            while (Math.pow(x, 2) + Math.pow(y, 2) < N) {
                final double xTemp = Math.pow(x, 2) - Math.pow(y, 2) + scaledPoint.getX();
                y = (2 * x * y) + scaledPoint.getY();
                x = xTemp;
                n++;
            }
            return new MandelbrotColor(n, N, x, y);
        } else
            return new MandelbrotColor();
    }
    
    @Override
    protected void onComplete() { }
    
    private boolean escapes(double iX, double iY)
    {
        final Point2D convertedPoint = dimensions.convertFromCanvas(iX, iY);
        double x = 0, y = 0;
        int n = 0;
        
        while (Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(2, 2) && n < PRECISION) {
            final double xTemp = Math.pow(x, 2) - Math.pow(y, 2) + convertedPoint.getX();
            y = (2 * x * y) + convertedPoint.getY();
            x = xTemp;
            n++;
            if (Math.pow(x, 2) + Math.pow(y, 2) > Math.pow(2, 2))
                return true;
        }
        
        return false;
    }
    
    private Color escapeColor = Color.BLACK;
    
    private void initColors()
    {
        for (int i = 0; i < 255; i++) {
            presetColors[i] = Color.color(i / 255.0, 0, 0);
            presetColors[i + 255] = Color.color((255 - i) / 255.0, 0, 0);
            
            // Green
            // presetColors[i] = Color.color(0, i / 255.0, 0);
            // presetColors[i + 255] = Color.color(0, (255 - i) / 255.0, 0);
            
            // Purple
            // presetColors[i] = Color.color(i / 255.0, 0, i / 255.0);
            // presetColors[i + 255] = Color.color((255 - i) / 255.0, 0, (255 - i) / 255.0);
            
            // Teal
            // presetColors[i] = Color.color(0, i / 255.0, i / 255.0);
            // presetColors[i + 255] = Color.color(0, (255 - i) / 255.0, (255 - i) / 255.0);
            
            // Blue
            // presetColors[i] = Color.color(0, 0, (255 - i) / 255.0);
            // presetColors[i + 255] = Color.color(0, 0, i / 255.0);
            
            // Green & Blue 1
            // presetColors[i] = Color.color(0, i / 255.0, (255 - i) / 255.0);
            // presetColors[i + 255] = Color.color(0, (255 - i) / 255.0, i / 255.0);
            
            // Red & Green 1
            // presetColors[i] = Color.color((255 - i) / 255.0, i / 255.0, 0);
            // presetColors[i + 255] = Color.color(i / 255.0, (255 - i) / 255.0, 0);
        }
    }
    
    public class MandelbrotColor
    {
        private final int bigN;
        private final int smallN;
        private final double x;
        private final double y;
        
        private MandelbrotColor()
        {
            this(0, -1, 0, 0); // Black
        }
        
        private MandelbrotColor(int smallN, int bigN, double x, double y)
        {
            this.bigN = bigN;
            this.smallN = smallN;
            this.x = x;
            this.y = y;
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
                return escapeColor;
            
            final double z = Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
            
            final double part1 = Math.log10(z);
            final double part2 = Math.log10(getBigN());
            final double partWhole = Math.log(part1 / part2) / Math.log(2);
            
            final int whole = (int) ((getSmallN() - partWhole) * 20);
            
            final int total = presetColors.length;
            final int value = whole % total;
            
            return presetColors[value];
        }
        
        public java.awt.Color getAwtColor()
        {
            final Color colorFX = getColor();
            final int r = (int) (colorFX.getRed() * 255);
            final int g = (int) (colorFX.getGreen() * 255);
            final int b = (int) (colorFX.getBlue() * 255);
            return new java.awt.Color(r, g, b);
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
