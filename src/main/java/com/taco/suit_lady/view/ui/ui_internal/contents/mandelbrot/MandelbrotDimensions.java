package com.taco.suit_lady.view.ui.ui_internal.contents.mandelbrot;

import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;

public class MandelbrotDimensions
{
    private final DoubleProperty xMinProperty;
    private final DoubleProperty xMaxProperty;
    private final DoubleProperty yMinProperty;
    private final DoubleProperty yMaxProperty;
    
    private final IntegerProperty canvasWidthProperty;
    private final IntegerProperty canvasHeightProperty;
    
    private final DoubleBinding widthBinding;
    private final DoubleBinding heightBinding;
    
    private final DoubleBinding xScalingProperty;
    private final DoubleBinding yScalingProperty;
    
    private final DoubleBinding scaledWidthProperty;
    private final DoubleBinding scaledHeightProperty;
    
    private final DoubleBinding scaledXMinBinding;
    private final DoubleBinding scaledXMaxBinding;
    private final DoubleBinding scaledYMinBinding;
    private final DoubleBinding scaledYMaxBinding;
    
    public MandelbrotDimensions(double xMin, double yMin, double xMax, double yMax, double width, double height)
    {
        this.xMinProperty = new SimpleDoubleProperty(xMin);
        this.xMaxProperty = new SimpleDoubleProperty(xMax);
        this.yMinProperty = new SimpleDoubleProperty(yMin);
        this.yMaxProperty = new SimpleDoubleProperty(yMax);
        
        this.canvasWidthProperty = new SimpleIntegerProperty((int) width);
        this.canvasHeightProperty = new SimpleIntegerProperty((int) height);
        
        this.widthBinding = Bindings.createDoubleBinding(() -> getMaxX() - getMinX(), getObservablesArray());
        this.heightBinding = Bindings.createDoubleBinding(() -> getMaxY() - getMinY(), getObservablesArray());
        
        this.xScalingProperty = Bindings.createDoubleBinding(() -> {
            if (((double) getCanvasWidth() / (double) getCanvasHeight()) >= (getWidth() / getHeight()))
                return (getCanvasWidth() * getHeight()) / (getCanvasHeight() * getWidth());
            else
                return 1.0;
        }, getObservablesArray());
        this.yScalingProperty = Bindings.createDoubleBinding(() -> {
            if (((double) getCanvasWidth() / (double) getCanvasHeight()) <= (getWidth() / getHeight()))
                return (getCanvasHeight() * getWidth()) / (getCanvasWidth() * getHeight());
            else
                return 1.0;
        }, getObservablesArray());
        
        this.scaledWidthProperty = Bindings.createDoubleBinding(() -> getWidth() * getScalingX(), getObservablesArray());
        this.scaledHeightProperty = Bindings.createDoubleBinding(() -> getHeight() * getScalingY(), getObservablesArray());
        
        this.scaledXMinBinding = Bindings.createDoubleBinding(() -> getMinX() - ((getScaledWidth() - getWidth()) / 2), getObservablesArray());
        this.scaledXMaxBinding = Bindings.createDoubleBinding(() -> getMaxX() + ((getScaledWidth() - getWidth()) / 2), getObservablesArray());
        this.scaledYMinBinding = Bindings.createDoubleBinding(() -> getMinY() - ((getScaledHeight() - getHeight()) / 2), getObservablesArray());
        this.scaledYMaxBinding = Bindings.createDoubleBinding(() -> getMaxY() + ((getScaledHeight() - getHeight()) / 2), getObservablesArray());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final double getMinX()
    {
        return xMinProperty.get();
    }
    
    public final double getMaxX()
    {
        return xMaxProperty.get();
    }
    
    public final double getMinY()
    {
        return yMinProperty.get();
    }
    
    public final double getMaxY()
    {
        return yMaxProperty.get();
    }
    
    public final double getWidth()
    {
        return widthBinding.get();
    }
    
    public final double getHeight()
    {
        return heightBinding.get();
    }
    
    public final double getScalingX()
    {
        return xScalingProperty.get();
    }
    
    public final double getScalingY()
    {
        return yScalingProperty.get();
    }
    
    public final double getScaledWidth()
    {
        return scaledWidthProperty.get();
    }
    
    public final double getScaledHeight()
    {
        return scaledHeightProperty.get();
    }
    
    public final double getScaledMinX()
    {
        return scaledXMinBinding.get();
    }
    
    public final double getScaledMaxX()
    {
        return scaledXMaxBinding.get();
    }
    
    public final double getScaledMinY()
    {
        return scaledYMinBinding.get();
    }
    
    public final double getScaledMaxY()
    {
        return scaledYMaxBinding.get();
    }
    
    public final int getCanvasWidth()
    {
        return canvasWidthProperty.get();
    }
    
    public final int getCanvasHeight()
    {
        return canvasHeightProperty.get();
    }
    
    //</editor-fold>
    
    public final Point2D convertFromCanvas(Point2D point)
    {
        ExceptionTools.nullCheck(point, "Conversion Point");
        return convertFromCanvas(point.getX(), point.getY());
    }
    
    public final Point2D convertFromCanvas(double canvasX, double canvasY)
    {
        final int iCanvasX = (int) canvasX;
        final int iCanvasY = (int) canvasY;
        
        final double percX = canvasX / getCanvasWidth();
        final double percY = canvasY / getCanvasHeight();
        
        final double calcedX = (getScaledWidth() * percX) + getScaledMinX();
        final double calcedY = (getScaledHeight() * percY) + getScaledMinY();
        
        return new Point2D(calcedX, calcedY);
    }
    
    public final void zoomTo(int startX, int startY, int endX, int endY)
    {
        final Point2D scaledStartPoint = convertFromCanvas(startX, startY);
        final Point2D scaledEndPoint = convertFromCanvas(endX, endY);
        
        xMinProperty.set(scaledStartPoint.getX());
        yMinProperty.set(scaledStartPoint.getY());
        xMaxProperty.set(scaledEndPoint.getX());
        yMaxProperty.set(scaledEndPoint.getY());
    }
    
    public final void resizeTo(double width, double height)
    {
        canvasWidthProperty.set((int) width);
        canvasHeightProperty.set((int) height);
    }
    
    private Observable[] getObservablesArray()
    {
        return new Observable[]{
                xMinProperty,
                xMaxProperty,
                yMinProperty,
                yMaxProperty,
                canvasWidthProperty,
                canvasHeightProperty
        };
    }
    
    @Override
    public String toString()
    {
        return "MandelbrotDimensions{" +
               "xMin=" + xMinProperty.get() +
               ", xMax=" + xMaxProperty.get() +
               ", yMin=" + yMinProperty.get() +
               ", yMax=" + yMaxProperty.get() +
               ", canvasWidth=" + canvasWidthProperty.get() +
               ", canvasHeight=" + canvasHeightProperty.get() +
               ", width=" + widthBinding.get() +
               ", height=" + heightBinding.get() +
               ", xScaling=" + xScalingProperty.get() +
               ", yScaling=" + yScalingProperty.get() +
               ", scaledWidth=" + scaledWidthProperty.get() +
               ", scaledHeight=" + scaledHeightProperty.get() +
               ", scaledXMin=" + scaledXMinBinding.get() +
               ", scaledXMax=" + scaledXMaxBinding.get() +
               ", scaledYMin=" + scaledYMinBinding.get() +
               ", scaledYMax=" + scaledYMaxBinding.get() +
               '}';
    }
    
    public static MandelbrotDimensions newDefaultInstance(double width, double height)
    {
        return new MandelbrotDimensions(-2.2, -1.2, .8, 1.2, width, height);
    }
}
