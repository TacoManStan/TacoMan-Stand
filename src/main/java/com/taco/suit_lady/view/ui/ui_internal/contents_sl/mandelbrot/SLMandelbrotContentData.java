package com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot;

import com.taco.suit_lady.view.ui.ui_internal.contents_sl.SLContentData;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;

import java.util.concurrent.locks.ReentrantLock;

public class SLMandelbrotContentData extends SLContentData
{
    private final ReentrantLock lock;
    
    private final int MAX_ITERATIONS = 1000;
    
    private final IntegerProperty widthProperty;
    private final IntegerProperty heightProperty;
    
    private double scale;
    private double startX;
    private double endX;
    
    private boolean complete = false;
    
    private MandelbrotColor[][] colors;
    private final Color[] presetColors;
    
    public SLMandelbrotContentData(int initialWidth, int initialHeight, double scale)
    {
        this.lock = new ReentrantLock();
        
        this.widthProperty = new SimpleIntegerProperty(initialWidth);
        this.heightProperty = new SimpleIntegerProperty(initialHeight);
        
        System.out.println("Start X: " + getStart(true));
        System.out.println("Start Y: " + getStart(false));
        System.out.println("End X: " + getEnd(true));
        System.out.println("End Y: " + getEnd(false));
        
        this.presetColors = new Color[255 * 2];
        
        initColors();
    }
    
    public final IntegerProperty widthProperty()
    {
        return widthProperty;
    }
    
    public final IntegerProperty heightProperty()
    {
        return heightProperty;
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    private void initColors()
    {
        for (int i = 0; i < 255; i++) {
            presetColors[i] = Color.color(0, 0, i / 255.0);
            presetColors[i + 255] = Color.color(0, 0, (255 - i) / 255.0);
        }
    }
    
    public static void main(String[] args)
    {
        final int iterationNum = 1;
        final int xSize = 7;
        final int ySize = 4;
        
        System.out.println("iX: " + (iterationNum % xSize));
        System.out.println("iY: " + (iterationNum / xSize));
    }
    
    //</editor-fold>
    
    public boolean isComplete()
    {
        return complete;
    }
    
    public void reset()
    {
        this.iterationNum = 0;
        this.complete = false;
        this.colors = new MandelbrotColor[getSize(true)][getSize(false)];
        this.startX = -((4.0 * getSize(true) * scale) / (3.0 * getSize(false)));
        this.endX = (2.0 * getSize(true) * scale) / (3.0 * getSize(false));
    }
    
    private int iterationNum;
    
    public void increment()
    {
        iterationNum++;
        
        final int iX = Math.floorMod(iterationNum, getSize(true));
        final int iY = Math.floorDiv(iterationNum, getSize(true));
        
        if (iY == getSize(false))
            complete = true;
        else
            step(iX, iY);
    }
    
    private SLMandelbrotContentData step(int iX, int iY)
    {
        complete = false;
        
        final double xScaled = getScaled(iX, true);
        final double yScaled = getScaled(iY, false);
        
        double x = 0, y = 0;
        int n = 0;
        int N = (int) Math.pow(10, 100);
        
        if (escapes(iX, iY)) {
            while (Math.pow(x, 2) + Math.pow(y, 2) < N) {
                final double xTemp = Math.pow(x, 2) - Math.pow(y, 2) + xScaled;
                y = (2 * x * y) + yScaled;
                x = xTemp;
                n++;
            }
            colors[iX][iY] = new MandelbrotColor(n, N, x, y);
        } else
            colors[iX][iY] = new MandelbrotColor();
        
        return this;
    }
    
    @Deprecated
    public SLMandelbrotContentData regenerateOld()
    {
        for (int i = 0; i < getSize(true); i++)
            for (int j = 0; j < getSize(false); j++) {
                final double xScaled = getScaled(i, true);
                final double yScaled = getScaled(j, false);
                double x = 0, y = 0;
                int n = 0;
                int N = (int) Math.pow(10, 100);
                if (escapes(i, j)) {
                    while (Math.pow(x, 2) + Math.pow(y, 2) < N) {
                        final double xTemp = Math.pow(x, 2) - Math.pow(y, 2) + xScaled;
                        y = (2 * x * y) + yScaled;
                        x = xTemp;
                        n++;
                    }
                    colors[i][j] = new MandelbrotColor(n, N, x, y);
                } else
                    colors[i][j] = new MandelbrotColor();
            }
        return this;
    }
    
    public MandelbrotColor[][] getColors()
    {
        return colors;
    }
    
    private double getScaled(double v, boolean isX)
    {
        final double diff = getEnd(isX) - getStart(isX);
        final double perc = v / getSize(isX);
        return (diff * perc) + getStart(isX);
    }
    
    private boolean escapes(double iX, double iY)
    {
        final double xScaled = getScaled(iX, true);
        final double yScaled = getScaled(iY, false);
        double x = 0, y = 0;
        int n = 0;
        
        while (Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(2, 2) && n < MAX_ITERATIONS) {
            final double xTemp = Math.pow(x, 2) - Math.pow(y, 2) + xScaled;
            y = (2 * x * y) + yScaled;
            x = xTemp;
            n++;
            if (Math.pow(x, 2) + Math.pow(y, 2) > Math.pow(2, 2))
                return true;
        }
        
        return false;
    }
    
    private double getStart(boolean isX)
    {
        return isX ? startX : -scale;
    }
    
    private double getEnd(boolean isX)
    {
        return isX ? endX : scale;
    }
    
    private int getSize(boolean isX)
    {
        return isX ? widthProperty.get() : heightProperty.get();
    }
    
    //
    
    public class MandelbrotColor
    {
        private int bigN;
        private int smallN;
        private double x;
        private double y;
        
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
