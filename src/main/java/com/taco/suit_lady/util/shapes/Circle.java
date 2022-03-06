package com.taco.suit_lady.util.shapes;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.Bind;
import com.taco.suit_lady.util.tools.Calc;
import com.taco.suit_lady.util.tools.Props;
import com.taco.suit_lady.util.tools.fx_tools.FX;
import com.taco.suit_lady.util.values.NumberValuePair;
import com.taco.suit_lady.util.values.NumberValuePairable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Point2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.docx4j.wml.Numbering;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;

public class Circle extends Shape {
    
    private final ReadOnlyDoubleWrapper diameterProperty;
    private final DoubleBinding radiusBinding;
    
    public Circle(@NotNull Springable springable,
                  @Nullable Lock lock,
                  @Nullable BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> pixelGenerator) {
        super(springable, lock, LocType.CENTER, pixelGenerator);
        
        this.diameterProperty = new ReadOnlyDoubleWrapper();
        this.radiusBinding = Bind.doubleBinding(() -> getDiameter() / 2, diameterProperty);
    }
    public Circle(@NotNull Springable springable, @Nullable Lock lock) { this(springable, lock, null); }
    public Circle(@NotNull Springable springable, @Nullable BiFunction<NumberValuePairable<?>, NumberValuePairable<?>, Color> pixelGenerator) { this(springable, null, pixelGenerator); }
    public Circle(@NotNull Springable springable) { this(springable, null, null); }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    @Override public Circle init() {
        widthProperty().bindBidirectional(diameterProperty);
        heightProperty().bindBidirectional(diameterProperty);
        
        return (Circle) super.init();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyDoubleProperty readOnlyDiameterProperty() { return diameterProperty.getReadOnlyProperty(); }
    public final double getDiameter() { return diameterProperty.get(); }
    public final double setDiameter(@NotNull Number newValue) { return Props.setProperty(diameterProperty, newValue); }
    
    public final DoubleBinding radiusBinding() { return radiusBinding; }
    public final double getRadius() { return radiusBinding.get(); }
    public final double setRadius(@NotNull Number newValue) { return setDiameter(newValue.doubleValue() * 2); }
    
    public final double getPrecision() { return 9; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    //TODO: Note that collision checks should still work with this overwritten method removed, but, well... it doesn't. So, fix that, because it'll definitely crop up as an issue later on.
    @Override public boolean intersects(@NotNull Shape other, boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        final Circle circleImpl = generateCircle(translate, xMod, yMod);
        final Number xImpl = translate ? xMod : 0;
        final Number yImpl = translate ? yMod : 0;
        return sync(() -> {
            final Point2D center = circleImpl.getLocation(LocType.CENTER).applyEach(xImpl, yImpl).asPoint();
            if (other instanceof Circle otherCircle)
                return center.distance(other.getLocation(LocType.CENTER).asPoint()) < getRadius() + otherCircle.getRadius();
            else if (other instanceof Box otherBox)
                return other.borderPointsCopy(true, 0, 0).stream().anyMatch(otherBorderPoint -> center.distance(otherBorderPoint.asPoint()) < getRadius());
            else
                return super.intersects(other, translate, xMod, yMod);
        });
    }
    
    private Circle generateCircle(boolean translate, @NotNull Number x, @NotNull Number y) {
        if (translate)
            return this;
        else {
            final Circle circleImpl = new Circle(this);
            circleImpl.setLocation(x, y);
            circleImpl.setDiameter(getDiameter());
            return circleImpl;
        }
    }
    
    @Override public boolean contains(@NotNull Number x, @NotNull Number y) {
        return getLocation(LocType.CENTER).asPoint().distance(x.doubleValue(), y.doubleValue()) < getRadius();
    }
    
    @Override protected @NotNull List<NumberValuePair> generateBorderPoints(boolean translate, @NotNull Number xMod, @NotNull Number yMod) {
        final double xModD = xMod.doubleValue();
        final double yModD = yMod.doubleValue();
        
        final ArrayList<NumberValuePair> borderPoints = new ArrayList<>();
        
        final NumberValuePair locationImpl = translate ? getLocation(LocType.CENTER) : new NumberValuePair(xModD + getRadius(), yModD + getRadius());
        
        final double precision = getPrecision();
        for (int i = 0; i < 360 / precision; i++)
            borderPoints.add(Calc.pointOnCircle(locationImpl, getRadius(), i * precision));
        
        return borderPoints;
    }
    
    @Override protected @NotNull Image regenerateImage() {
        return syncRequireFX(() -> {
            final Canvas canvas = new Canvas(getWidth(), getHeight());
            canvas.getGraphicsContext2D().setFill(Color.BLACK);
            canvas.getGraphicsContext2D().fillOval(getLocation(Axis.X_AXIS, LocType.MIN), getLocation(Axis.Y_AXIS, LocType.MIN), getWidth(), getHeight());
            return canvas.snapshot(new SnapshotParameters(), null);
        });
    }
    
    //
    
    @Override public String toString() {
        return "Circle{" +
               "diameter=" + (int) getDiameter() +
               ", radius=" + (int) getRadius() +
               ", location=" + getLocation().getString(true) +
               ", locationCenter=" + getLocation(LocType.CENTER).getString(true) +
               ", locationMin=" + getLocation(LocType.MIN).getString(true) +
               ", locationMax=" + getLocation(LocType.MAX).getString(true) +
               ", dimensions=" + getDimensions().getString(true) +
               ", borderPoints=" + borderPointsCopy(true, 0, 0) +
               '}';
    }
    
    
    //</editor-fold>
}
