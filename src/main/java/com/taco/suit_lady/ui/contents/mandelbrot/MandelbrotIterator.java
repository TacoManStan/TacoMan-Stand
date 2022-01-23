package com.taco.suit_lady.ui.contents.mandelbrot;

import com.taco.suit_lady._to_sort._new.MatrixIterator;
import com.taco.suit_lady.ui.jfx.components.painting.surfaces.canvas.CanvasSurface;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.SLArrays;
import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotIterator.MandelbrotColor;
import javafx.geometry.Point2D;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

public class MandelbrotIterator extends MatrixIterator<MandelbrotColor> {
    
    private MandelbrotContentData data;
    private CanvasSurface canvas;
    
    public MandelbrotIterator(@NotNull Springable springable, @Nullable ReentrantLock lock, @NotNull MandelbrotContentData data, @NotNull CanvasSurface canvas, @NotNull ProgressIndicator... progressIndicators) {
        super(springable, lock, SLArrays.concat(new Object[]{data, canvas}, progressIndicators));
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    protected MandelbrotColor step(int i, int j) {
        final Point2D scaledPoint = data.convertFromCanvas(i, j);
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
    
    public MandelbrotContentData getData() {
        return data;
    }
    
    @Override
    protected void onComplete() {
    
    }
    
    @Override
    protected IteratorTask newTask() {
        return new IteratorTask() {
            
            //<editor-fold desc="--- IMPLEMENTATIONS ---">
            
            @Override
            protected void onPreTask() {
            
            }
            
            @Override
            protected void onPostTask() {
            
            }
            
            @Override
            protected void onTaskStart() {
            
            }
            
            @Override
            protected void onTaskEnd(MandelbrotColor[][] result) {
                for (int i = 0; i < result.length; i++)
                    for (int j = 0; j < result[i].length; j++) {
                        final MandelbrotColor mandelbrotColor = result[i][j];
                        final Color color = mandelbrotColor != null ? mandelbrotColor.getColor() : Color.BLACK;
                        canvas.getGraphicsContext2D().getPixelWriter().setColor(i, j, color);
                    }
            }
            
            @Override
            protected void onSucceeded() {
                canvas.refreshImage();
            }
            
            @Override
            protected void onCancelled() {
            
            }
            
            @Override
            protected void onFailed() {
            
            }
            
            //</editor-fold>
        };
    }
    
    @Override
    protected void construct(@NotNull Object @NotNull ... params) {
        this.data = (MandelbrotContentData) params[0];
        this.canvas = (CanvasSurface) params[1];
    }
    
    @Override
    protected @NotNull MandelbrotColor[][] newMatrix() {
        return new MandelbrotColor[data.getCanvasWidth()][data.getCanvasHeight()];
    }
    
    // UNUSED
    protected void onConstruct(Object @NotNull ... params) {
        this.data = (MandelbrotContentData) params[0];
        if (data.getCanvasWidth() != getWidth() || data.getCanvasHeight() != getHeight())
            throw SLExceptions.ex(
                    "Dimension Mismatch:  " +
                    "Dimensions Data [" + data.getCanvasWidth() + ", " + data.getCanvasHeight() + "  " +
                    "Iterator Data [" + getWidth() + ", " + getHeight());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private boolean escapes(double iX, double iY) {
        final Point2D convertedPoint = data.convertFromCanvas(iX, iY);
        double x = 0, y = 0;
        int n = 0;
        
        while (Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(2, 2) && n < data.getPrecision()) {
            final double xTemp = Math.pow(x, 2) - Math.pow(y, 2) + convertedPoint.getX();
            y = (2 * x * y) + convertedPoint.getY();
            x = xTemp;
            n++;
            if (Math.pow(x, 2) + Math.pow(y, 2) > Math.pow(2, 2))
                return true;
        }
        
        return false;
    }
    
    //</editor-fold>
    
    private Color escapeColor = Color.BLACK;
    
    public class MandelbrotColor {
        private final int bigN;
        private final int smallN;
        private final double x;
        private final double y;
        
        private MandelbrotColor() {
            this(0, -1, 0, 0); // Black
        }
        
        private MandelbrotColor(int smallN, int bigN, double x, double y) {
            this.bigN = bigN;
            this.smallN = smallN;
            this.x = x;
            this.y = y;
        }
        
        public int getBigN() {
            return bigN;
        }
        
        public int getSmallN() {
            return smallN;
        }
        
        public double getX() {
            return x;
        }
        
        public double getY() {
            return y;
        }
        
        public Color getColor() {
            if (getBigN() == -1)
                return escapeColor;
            
            final double z = Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
            
            final double part1 = Math.log10(z);
            final double part2 = Math.log10(getBigN());
            final double partWhole = Math.log(part1 / part2) / Math.log(2);
            
            final int whole = (int) ((getSmallN() - partWhole) * 20);
            
            final int total = data.getColors().length;
            final int value = whole % total;
            
            return data.getColors()[value];
        }
        
        public java.awt.Color getAwtColor() {
            final Color colorFX = getColor();
            final int r = (int) (colorFX.getRed() * 255);
            final int g = (int) (colorFX.getGreen() * 255);
            final int b = (int) (colorFX.getBlue() * 255);
            return new java.awt.Color(r, g, b);
        }
        
        @Override
        public String toString() {
            return "\nn: " + getSmallN()
                   + "\nN: " + getBigN()
                   + "\nX: " + getX()
                   + "\nY: " + getY();
        }
    }
}
