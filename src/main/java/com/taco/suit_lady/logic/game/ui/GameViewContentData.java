package com.taco.suit_lady.logic.game.ui;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.taco.suit_lady._to_sort._new.interfaces.ObservablePropertyContainable;
import com.taco.suit_lady.ui.ContentData;
import com.taco.suit_lady.ui.contents.mandelbrot.MandelbrotColorScheme;
import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.StrictSpringable;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import com.taco.tacository.json.JElement;
import com.taco.tacository.json.JLoadable;
import com.taco.tacository.json.JObject;
import com.taco.tacository.json.JUtil;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxWeaver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ConfigurableApplicationContext;

public class GameViewContentData extends ContentData
        implements ObservablePropertyContainable, Springable, JObject, JLoadable, UIDProcessable {
    
    private final StrictSpringable springable;
    
    private final ObjectProperty<Double> xMinProperty;
    private final ObjectProperty<Double> xMaxProperty;
    private final ObjectProperty<Double> yMinProperty;
    private final ObjectProperty<Double> yMaxProperty;
    
    private final IntegerProperty canvasWidthProperty;
    private final IntegerProperty canvasHeightProperty;
    
    private final ObjectProperty<Integer> precisionProperty;
    
    private final ObjectProperty<MandelbrotColorScheme> colorSchemeProperty;
    private final BooleanProperty invertColorSchemeProperty;
    
    private final BooleanProperty pauseAutoRegenerationProperty;
    
    
    private final DoubleBinding widthBinding;
    private final DoubleBinding heightBinding;
    
    private final DoubleBinding xScalingBinding;
    private final DoubleBinding yScalingBinding;
    
    private final DoubleBinding scaledWidthBinding;
    private final DoubleBinding scaledHeightBinding;
    
    private final DoubleBinding scaledXMinBinding;
    private final DoubleBinding scaledXMaxBinding;
    private final DoubleBinding scaledYMinBinding;
    private final DoubleBinding scaledYMaxBinding;
    
    //
    
    private int currentCount;
    private final IntegerBinding changeCounter;
    
    public GameViewContentData(@NotNull Springable springable, double xMin, double yMin, double xMax, double yMax, double width, double height) {
        this.springable = springable.asStrict();
        
        this.xMinProperty = new SimpleObjectProperty<>(xMin);
        this.xMaxProperty = new SimpleObjectProperty<>(xMax);
        this.yMinProperty = new SimpleObjectProperty<>(yMin);
        this.yMaxProperty = new SimpleObjectProperty<>(yMax);
        
        this.canvasWidthProperty = new SimpleIntegerProperty((int) width);
        this.canvasHeightProperty = new SimpleIntegerProperty((int) height);
        
        this.precisionProperty = new SimpleObjectProperty<>(1000);
        
        this.colorSchemeProperty = new SimpleObjectProperty<>(MandelbrotColorScheme.RED);
        this.invertColorSchemeProperty = new SimpleBooleanProperty(false);
        
        this.pauseAutoRegenerationProperty = new SimpleBooleanProperty(false);
        
        
        this.widthBinding = createDoubleBinding(() -> getMaxX() - getMinX());
        this.heightBinding = createDoubleBinding(() -> getMaxY() - getMinY());
        
        this.xScalingBinding = createDoubleBinding(() -> {
            if (((double) getCanvasWidth() / (double) getCanvasHeight()) >= (getWidth() / getHeight()))
                return (getCanvasWidth() * getHeight()) / (getCanvasHeight() * getWidth());
            return 1.0;
        });
        this.yScalingBinding = createDoubleBinding(() -> {
            if (((double) getCanvasWidth() / (double) getCanvasHeight()) <= (getWidth() / getHeight()))
                return (getCanvasHeight() * getWidth()) / (getCanvasWidth() * getHeight());
            return 1.0;
        });
        
        this.scaledWidthBinding = createDoubleBinding(() -> getWidth() * getScalingX());
        this.scaledHeightBinding = createDoubleBinding(() -> getHeight() * getScalingY());
        
        this.scaledXMinBinding = createDoubleBinding(() -> getMinX() - ((getScaledWidth() - getWidth()) / 2));
        this.scaledXMaxBinding = createDoubleBinding(() -> getMaxX() + ((getScaledWidth() - getWidth()) / 2));
        this.scaledYMinBinding = createDoubleBinding(() -> getMinY() - ((getScaledHeight() - getHeight()) / 2));
        this.scaledYMaxBinding = createDoubleBinding(() -> getMaxY() + ((getScaledHeight() - getHeight()) / 2));
        
        
        this.currentCount = 0;
        this.changeCounter = createIntegerBinding(() -> currentCount++);
        this.changeCounter.addListener((observable, oldValue, newValue) -> debugger().print("Counter: " + currentCount));
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final IntegerBinding changeCounter() {
        return changeCounter;
    }
    
    
    public final ObjectProperty<Double> xMinProperty() {
        return xMinProperty;
    }
    
    public final double getMinX() {
        return xMinProperty.get();
    }
    
    public final double setMinX(double newValue) {
        double oldValue = getMinX();
        xMinProperty.set(newValue);
        return oldValue;
    }
    
    
    public final ObjectProperty<Double> xMaxProperty() {
        return xMaxProperty;
    }
    
    public final double getMaxX() {
        return xMaxProperty.get();
    }
    
    public final double setMaxX(double newValue) {
        double oldValue = getMaxX();
        xMaxProperty.set(newValue);
        return oldValue;
    }
    
    
    public final ObjectProperty<Double> yMinProperty() {
        return yMinProperty;
    }
    
    public final double getMinY() {
        return yMinProperty.get();
    }
    
    public final double setMinY(double newValue) {
        double oldValue = getMinY();
        yMinProperty.set(newValue);
        return oldValue;
    }
    
    
    public final ObjectProperty<Double> yMaxProperty() {
        return yMaxProperty;
    }
    
    public final double getMaxY() {
        return yMaxProperty.get();
    }
    
    public final double setMaxY(double newValue) {
        double oldValue = getMaxY();
        yMaxProperty.set(newValue);
        return oldValue;
    }
    
    
    public final IntegerProperty canvasWidthProperty() {
        return canvasWidthProperty;
    }
    
    public final int getCanvasWidth() {
        return canvasWidthProperty.get();
    }
    
    
    public final IntegerProperty canvasHeightProperty() {
        return canvasHeightProperty;
    }
    
    public final int getCanvasHeight() {
        return canvasHeightProperty.get();
    }
    
    
    public final ObjectProperty<Integer> precisionProperty() {
        return precisionProperty;
    }
    
    public final int getPrecision() {
        return precisionProperty.get();
    }
    
    public final int setPrecision(int newValue) {
        int oldValue = getPrecision();
        precisionProperty.set(newValue);
        return oldValue;
    }
    
    
    public final ObjectProperty<MandelbrotColorScheme> colorSchemeProperty() {
        return colorSchemeProperty;
    }
    
    public final MandelbrotColorScheme getColorScheme() {
        return colorSchemeProperty.get();
    }
    
    public final MandelbrotColorScheme setColorScheme(MandelbrotColorScheme newValue) {
        MandelbrotColorScheme oldValue = getColorScheme();
        colorSchemeProperty.set(newValue);
        return oldValue;
    }
    
    
    public final @NotNull Color @NotNull [] getColors() {
        return getColorScheme().getColors(isColorSchemeInverted());
    }
    
    
    public final BooleanProperty invertColorSchemeProperty() {
        return invertColorSchemeProperty;
    }
    
    public final boolean isColorSchemeInverted() {
        return invertColorSchemeProperty.get();
    }
    
    public final boolean setInvertColorScheme(boolean newValue) {
        boolean oldValue = isColorSchemeInverted();
        invertColorSchemeProperty.set(newValue);
        return oldValue;
    }
    
    
    public final BooleanProperty pauseAutoRegenerationProperty() {
        return pauseAutoRegenerationProperty;
    }
    
    public final boolean isAutoRegenerationPaused() {
        return pauseAutoRegenerationProperty.get();
    }
    
    public final boolean setAutoRegenerationPaused(boolean newValue) {
        boolean oldValue = isAutoRegenerationPaused();
        pauseAutoRegenerationProperty.set(newValue);
        return oldValue;
    }
    
    //<editor-fold desc="--- BINDINGS ---">
    
    public final DoubleBinding widthBinding() {
        return widthBinding;
    }
    
    public final double getWidth() {
        return widthBinding.get();
    }
    
    
    public final DoubleBinding heightBinding() {
        return heightBinding;
    }
    
    public final double getHeight() {
        return heightBinding.get();
    }
    
    
    public final DoubleBinding xScalingProperty() {
        return xScalingBinding;
    }
    
    public final double getScalingX() {
        return xScalingBinding.get();
    }
    
    
    public final DoubleBinding yScalingBinding() {
        return yScalingBinding;
    }
    
    public final double getScalingY() {
        return yScalingBinding.get();
    }
    
    
    public final DoubleBinding scaledWidthBinding() {
        return scaledWidthBinding;
    }
    
    public final double getScaledWidth() {
        return scaledWidthBinding.get();
    }
    
    
    public final DoubleBinding scaledHeightBinding() {
        return scaledHeightBinding;
    }
    
    public final double getScaledHeight() {
        return scaledHeightBinding.get();
    }
    
    
    public final DoubleBinding scaledXMinBinding() {
        return scaledXMinBinding;
    }
    
    public final double getScaledMinX() {
        return scaledXMinBinding.get();
    }
    
    
    public final DoubleBinding scaledXMaxBinding() {
        return scaledXMaxBinding;
    }
    
    public final double getScaledMaxX() {
        return scaledXMaxBinding.get();
    }
    
    
    public final DoubleBinding scaledYMinBinding() {
        return scaledYMinBinding;
    }
    
    public final double getScaledMinY() {
        return scaledYMinBinding.get();
    }
    
    
    public final DoubleBinding scaledYMaxBinding() {
        return scaledYMaxBinding;
    }
    
    public final double getScaledMaxY() {
        return scaledYMaxBinding.get();
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    public final @NotNull Point2D convertFromCanvas(Point2D point) {
        ExceptionsSL.nullCheck(point, "Conversion Point");
        return convertFromCanvas(point.getX(), point.getY());
    }
    
    public final @NotNull Point2D convertFromCanvas(double canvasX, double canvasY) {
        final int iCanvasX = (int) canvasX;
        final int iCanvasY = (int) canvasY;
        
        final double percX = canvasX / getCanvasWidth();
        final double percY = canvasY / getCanvasHeight();
        
        final double calcedX = (getScaledWidth() * percX) + getScaledMinX();
        final double calcedY = (getScaledHeight() * percY) + getScaledMinY();
        
        return new Point2D(calcedX, calcedY);
    }
    
    public final void zoomTo(int startX, int startY, int endX, int endY) {
        final Point2D scaledStartPoint = convertFromCanvas(startX, startY);
        final Point2D scaledEndPoint = convertFromCanvas(endX, endY);
        
        xMinProperty.set(scaledStartPoint.getX());
        yMinProperty.set(scaledStartPoint.getY());
        xMaxProperty.set(scaledEndPoint.getX());
        yMaxProperty.set(scaledEndPoint.getY());
    }
    
    public final void resizeTo(double width, double height) {
        canvasWidthProperty.set((int) width);
        canvasHeightProperty.set((int) height);
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull Observable[] properties() {
        return new Observable[]{
                xMinProperty,
                xMaxProperty,
                yMinProperty,
                yMaxProperty,
                canvasWidthProperty,
                canvasHeightProperty,
                precisionProperty
        };
    }
    
    
    @Override
    public @NotNull FxWeaver weaver() {
        return springable.weaver();
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return springable.ctx();
    }
    
    @Override
    public String toString() {
        return "MandelbrotContentData{" +
               "springable=" + springable +
               ", xMin=" + xMinProperty.get() +
               ", xMax=" + xMaxProperty.get() +
               ", yMin=" + yMinProperty.get() +
               ", yMax=" + yMaxProperty.get() +
               ", canvasWidth=" + canvasWidthProperty.get() +
               ", canvasHeight=" + canvasHeightProperty.get() +
               ", precision=" + precisionProperty.get() +
               ", width=" + widthBinding.get() +
               ", height=" + heightBinding.get() +
               ", xScaling=" + xScalingBinding.get() +
               ", yScaling=" + yScalingBinding.get() +
               ", scaledWidth=" + scaledWidthBinding.get() +
               ", scaledHeight=" + scaledHeightBinding.get() +
               ", scaledXMin=" + scaledXMinBinding.get() +
               ", scaledXMax=" + scaledXMaxBinding.get() +
               ", scaledYMin=" + scaledYMinBinding.get() +
               ", scaledYMax=" + scaledYMaxBinding.get() +
               ", colorScheme=" + colorSchemeProperty.get() +
               ", invertColorScheme=" + invertColorSchemeProperty.get() +
               ", pauseAutoRegeneration=" + pauseAutoRegenerationProperty.get() +
               ", currentCount=" + currentCount +
               ", changeCounter=" + changeCounter +
               ", uIDContainer=" + uIDContainer +
               '}';
    }
    
    @Override
    public String getJID() {
        return "test-jid";
    }
    
    @Override
    public JElement[] jFields() {
        return new JElement[]{
                JUtil.create("x-min", getMinX()),
                JUtil.create("y-min", getMinY()),
                JUtil.create("x-max", getMaxX()),
                JUtil.create("y-max", getMaxY()),
                JUtil.create("precision", getPrecision()),
                JUtil.create("color-scheme", getColorScheme().name()),
                JUtil.create("invert-color-scheme", isColorSchemeInverted())
        };
    }
    
    @Override
    public void load(JsonObject parent) {
        setMinX(JUtil.loadDouble(parent, "x-min"));
        setMinY(JUtil.loadDouble(parent, "y-min"));
        setMaxX(JUtil.loadDouble(parent, "x-max"));
        setMaxY(JUtil.loadDouble(parent, "y-max"));
        setPrecision(JUtil.loadInt(parent, "precision"));
        setColorScheme(MandelbrotColorScheme.valueOf((String) parent.get("color-scheme")));
        setInvertColorScheme(JUtil.loadBoolean(parent, "invert-color-scheme"));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC ---">
    
    public static @NotNull GameViewContentData newDefaultInstance(Springable springable) {
        return GameViewContentData.newDefaultInstance(springable, 0.0, 0.0);
    }
    
    public static @NotNull GameViewContentData newDefaultInstance(Springable springable, double width, double height) {
        return new GameViewContentData(springable, -2.2, -1.2, .8, 1.2, width, height);
    }
    
    private UIDProcessor uIDContainer;
    
    @Override
    public UIDProcessor getUIDProcessor() {
        if (uIDContainer == null)
            uIDContainer = new UIDProcessor("group-name");
        return uIDContainer;
    }
    
    //</editor-fold>
}
