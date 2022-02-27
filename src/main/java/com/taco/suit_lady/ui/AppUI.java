package com.taco.suit_lady.ui;

import com.taco.suit_lady.ui.jfx.util.Dimensions;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.Print;
import com.taco.suit_lady.util.tools.PropertiesSL;
import com.taco.suit_lady.util.tools.fx_tools.ToolsFX;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.scene.robot.Robot;
import net.rgielen.fxweaver.core.FxWeaver;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
// TO-DOC
public class AppUI
        implements Springable {
    
    private final FxWeaver weaver;
    private final ConfigurableApplicationContext ctx;
    
    private final ReadOnlyObjectWrapper<AppController> controllerProperty;
    private final ReadOnlyObjectWrapper<Sidebar> sidebarProperty;
    
    private ContentManager contentManager;
    
    //
    
    private Robot robot;
    
    private final ReadOnlyObjectWrapper<Point2D> mouseOnScreenProperty;
    private final ListProperty<Region> trackedRegions;
    private final MapProperty<Region, Point2D> mouseMap;
    
    /**
     * <p>Constructs a new {@link AppUI} instance.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>The {@link AppUI} is constructed as an injected {@code singleton} {@link Springable Spring} {@link Component}.</li>
     *     <li>Both {@link FxWeaver} and {@link ConfigurableApplicationContext ApplicationContext} parameters are {@link Autowired}.</li>
     * </ol>
     *
     * @param weaver The {@link Autowired autowired} {@link FxWeaver} variable for this {@link Springable} object.
     * @param ctx    The {@link Autowired autowired} {@link ConfigurableApplicationContext ApplicationContext} variable for this {@link Springable} object.
     */
    public AppUI(FxWeaver weaver, ConfigurableApplicationContext ctx) {
        this.weaver = weaver;
        this.ctx = ctx;
        
        this.controllerProperty = new ReadOnlyObjectWrapper<>();
        this.sidebarProperty = new ReadOnlyObjectWrapper<>();
        
        //
        
        this.mouseOnScreenProperty = new ReadOnlyObjectWrapper<>();
        this.trackedRegions = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.mouseMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    protected void init() {
        //        console().initialize();
        this.contentManager = new ContentManager(this);
        initMouseTracking();
    }
    
    private void initMouseTracking() {
        robot = new Robot();
        mouseOnScreenProperty.addListener((observable, oldValue, newValue) -> ToolsFX.requireFX(() -> refreshRegionTracking(oldValue, newValue)));
    }
    
    //</editor-fold>
    
    // <editor-fold desc="--- PROPERTIES ---">
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the {@link AppController} in charge of the {@code FXML UI} of this {@link AppUI}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the {@link AppController} in charge of the {@code FXML UI} of this {@link AppUI}.
     */
    public final ReadOnlyObjectProperty<AppController> controllerProperty() { return controllerProperty.getReadOnlyProperty(); }
    public final AppController getController() { return controllerProperty.get(); }
    protected final AppController setController(AppController newValue) { return PropertiesSL.setProperty(controllerProperty, newValue); }
    
    /**
     * <p>Returns the {@link ReadOnlyObjectProperty} containing the {@link Sidebar} of this {@link AppUI}.</p>
     *
     * @return The {@link ReadOnlyObjectProperty} containing the {@link Sidebar} of this {@link AppUI}.
     */
    // TO-EXPAND
    public final ReadOnlyObjectProperty<Sidebar> sidebarProperty() { return sidebarProperty.getReadOnlyProperty(); }
    public final @NotNull Sidebar getSidebar() { return sidebarProperty.get(); }
    protected final Sidebar setSidebar(Sidebar newValue) { return PropertiesSL.setProperty(sidebarProperty, newValue); }
    
    /**
     * <p>Returns the {@link ContentManager Content Manager} in charge of managing the {@link Content} of this {@link AppUI} instance.</p>
     *
     * @return The {@link ContentManager Content Manager} in charge of managing the {@link Content} of this {@link AppUI} instance.
     */
    // TO-EXPAND
    public final ContentManager getContentManager() { return contentManager; }
    
    //<editor-fold desc="> Mouse Tracking">
    
    private Robot robot() { return ToolsFX.requireFX(() -> robot); }
    
    private ReadOnlyObjectWrapper<Point2D> mouseOnScreenProperty() { return ToolsFX.requireFX(() -> mouseOnScreenProperty); }
    public final ReadOnlyObjectProperty<Point2D> readOnlyMouseOnScreenProperty() { return mouseOnScreenProperty().getReadOnlyProperty(); }
    public final Point2D getMouseOnScreen() { return mouseOnScreenProperty().get(); }
    private Point2D setMouseOnScreen(@NotNull Point2D newValue) { return PropertiesSL.setProperty(mouseOnScreenProperty(), newValue); }
    
    private ListProperty<Region> trackedRegions() { return ToolsFX.requireFX(() -> trackedRegions); }
    private MapProperty<Region, Point2D> mouseMap() { return ToolsFX.requireFX(() -> mouseMap); }
    
    public final boolean trackRegion(@NotNull Region region) {
        Print.print("Starting Tracking for \"" + region + "\"");
        return trackedRegions().contains(region) || trackedRegions.add(region);
    }
    public final boolean stopTrackingRegion(@NotNull Region region) {
        Print.print("Stopping Tracking for \"" + region + "\"");
        return !trackedRegions().contains(region) || trackedRegions.remove(region);
    }
    
    public final void refreshMouseTracking() {
        ToolsFX.requireFX(() -> { setMouseOnScreen(robot.getMousePosition()); });
    }
    
    public final @NotNull Point2D getMouseOnRegion(@NotNull Region region) {
        final Point2D regionLocation = mouseMap().get(region);
        if (regionLocation == null)
            Print.err("WARNING: Attempting to retrieve mouse location for untracked Region [" + region + "]");
        return regionLocation;
    }
    public final @NotNull Point2D getMouseOnRegionSafe(@NotNull Region region) { return getSafe(getMouseOnRegion(region), region); }
    
    //</editor-fold>
    
    // </editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override
    public @NotNull FxWeaver weaver() {
        return weaver;
    }
    
    @Override
    public @NotNull ConfigurableApplicationContext ctx() {
        return ctx;
    }
    
    @Override
    public final @NotNull AppUI ui() {
        return this;
    }
    
    @Override
    public final @NotNull Sidebar sidebar() {
        return getSidebar();
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private @NotNull Point2D getMouseLocationFor(@NotNull Region region) { return region.screenToLocal(getMouseOnScreen()); }
    
    private void refreshRegionTracking(@Nullable Point2D oldMouseOnScreen, @Nullable Point2D newMouseOnScreen) {
        ToolsFX.requireFX(() -> {
            if (newMouseOnScreen == null || (newMouseOnScreen != null && !Objects.equals(oldMouseOnScreen, newMouseOnScreen))) {
                mouseMap.clear();
                trackedRegions.forEach(region -> {
                    final Point2D screenToLocalOld = oldMouseOnScreen != null ? region.screenToLocal(oldMouseOnScreen) : null;
                    final Point2D screenToLocalNew = region.screenToLocal(newMouseOnScreen);
                    Print.print("Updating Mouse Tracking for Region \"" + region + "\":  " + "["
                                + screenToLocalOld + "::" + getSafe(screenToLocalOld, region)
                                + " --> " +
                                screenToLocalNew + "::" + getSafe(screenToLocalNew, region)
                                + "]", false);
                    mouseMap.put(region, region.screenToLocal(newMouseOnScreen));
                    //                mouseMap.put(region, region.screenToLocal(getMouseOnScreen()));
                });
            }
        });
    }
    
    private @NotNull Point2D getSafe(@NotNull Point2D source, @NotNull Dimensions dimensions) {
        return ToolsFX.requireFX(() -> {
            double safeX = source.getX() >= 0 ? source.getX() : 0;
            double safeY = source.getY() >= 0 ? source.getY() : 0;
            safeX = safeX <= dimensions.width() ? safeX : dimensions.width();
            safeY = safeY <= dimensions.height() ? safeY : dimensions.height();
            return new Point2D(safeX, safeY);
        });
    }
    private @Nullable Point2D getSafe(@Nullable Region region) {
        if (region != null)
            return getSafe(region.screenToLocal(getMouseOnScreen()), ToolsFX.getDimensions(region));
        return null;
    }
    private @Nullable Point2D getSafe(@Nullable Point2D source, @Nullable Region region) {
        if (source != null && region != null)
            return getSafe(source, ToolsFX.getDimensions(region));
        return null;
    }
    
    //</editor-fold>
}
