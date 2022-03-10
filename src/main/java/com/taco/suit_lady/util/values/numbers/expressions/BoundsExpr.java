package com.taco.suit_lady.util.values.numbers.expressions;

import com.taco.suit_lady.util.tools.Enu;
import com.taco.suit_lady.util.values.enums.Axis;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.tools.Calc;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.numbers.Bounds;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BoundsExpr {
    
    @NotNull Number x();
    @NotNull Number y();
    
    @NotNull Number w();
    @NotNull Number h();
    
    @NotNull LocType locType();
    
    //<editor-fold desc="--- DEFAULT ---">
    
    default @NotNull Bounds getBounds() { return new Bounds(x(), y(), w(), h(), locType()); }
    default @NotNull Num2D getDimensions() { return new Num2D(w(), h()); }
    default @NotNull Number getDimension(@NotNull Axis axis) { return axis.getFor(w(), h()); }
    
    
    default @NotNull Num2D getLocation() { return getLocation(locType()); }
    default @NotNull Num2D getLocation(@NotNull LocType locType) { return LocType.translate(x(), y(), w(), h(), locType(), locType); }
    default @NotNull Number getLocation(@NotNull Axis axis) { return axis.getFor(x(), y()); }
    default @NotNull Number getLocation(@NotNull Axis axis, @NotNull LocType locType) {
        final Num2D loc = getLocation(locType);
        return axis.getFor(loc.a(), loc.b());
    }
    
    //<editor-fold desc="> Min/Max/Mid Accessor Methods">
    
    default @NotNull Number xMin() { return getLocation(Axis.X_AXIS, LocType.MIN); }
    default @NotNull Number xMax() { return getLocation(Axis.X_AXIS, LocType.MAX); }
    default @NotNull Number xMid() { return getLocation(Axis.X_AXIS, LocType.CENTER); }
    
    default @NotNull Number yMin() { return getLocation(Axis.Y_AXIS, LocType.MIN); }
    default @NotNull Number yMax() { return getLocation(Axis.Y_AXIS, LocType.MAX); }
    default @NotNull Number yMid() { return getLocation(Axis.Y_AXIS, LocType.CENTER); }
    
    //<editor-fold desc=">> Min/Max/Mid X-Axis Primitive Accessor Methods">
    
    default int xMinI() { return xMin().intValue(); }
    default int xMaxI() { return xMax().intValue(); }
    default int xMidI() { return xMid().intValue(); }
    
    default long xMinL() { return xMin().longValue(); }
    default long xMaxL() { return xMax().longValue(); }
    default long xMidL() { return xMid().longValue(); }
    
    default float xMinF() { return xMin().floatValue(); }
    default float xMaxF() { return xMax().floatValue(); }
    default float xMidF() { return xMid().floatValue(); }
    
    default double xMinD() { return xMin().doubleValue(); }
    default double xMaxD() { return xMax().doubleValue(); }
    default double xMidD() { return xMid().doubleValue(); }
    
    //</editor-fold>
    
    //<editor-fold desc=">> Min/Max/Mid Y-Axis Primitive Accessor Methods">
    
    default int yMinI() { return yMin().intValue(); }
    default int yMaxI() { return yMax().intValue(); }
    default int yMidI() { return yMid().intValue(); }
    
    default long yMinL() { return yMin().longValue(); }
    default long yMaxL() { return yMax().longValue(); }
    default long yMidL() { return yMid().longValue(); }
    
    default float yMinF() { return yMin().floatValue(); }
    default float yMaxF() { return yMax().floatValue(); }
    default float yMidF() { return yMid().floatValue(); }
    
    default double yMinD() { return yMin().doubleValue(); }
    default double yMaxD() { return yMax().doubleValue(); }
    default double yMidD() { return yMid().doubleValue(); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="> Bounded Accessors">
    
    default @NotNull Number x(@NotNull Number min, @NotNull Number max) { return Calc.bounded(x(), min, max); }
    default @NotNull Number x(@NotNull NumExpr2D<?> restraints) { return x(restraints.a(), restraints.b()); }
    
    default @NotNull Number y(@NotNull Number min, @NotNull Number max) { return Calc.bounded(y(), min, max); }
    default @NotNull Number y(@NotNull NumExpr2D<?> restraints) { return y(restraints.a(), restraints.b()); }
    
    
    default @NotNull Number w(@NotNull Number min, @NotNull Number max) { return Calc.bounded(w(), min, max); }
    default @NotNull Number w(@NotNull NumExpr2D<?> restraints) { return w(restraints.a(), restraints.b()); }
    
    default @NotNull Number h(@NotNull Number min, @NotNull Number max) { return Calc.bounded(h(), min, max); }
    default @NotNull Number h(@NotNull NumExpr2D<?> restraints) { return h(restraints.a(), restraints.b()); }
    
    //
    
    default @NotNull Bounds bounds(@NotNull Number minX, @NotNull Number minY, @NotNull Number minW, @NotNull Number minH,
                                   @NotNull Number maxX, @NotNull Number maxY, @NotNull Number maxW, @NotNull Number maxH,
                                   @Nullable LocType locType) {
        return new Bounds(x(minX, maxX), y(minY, maxY),
                          w(minW, maxW), h(minH, maxH),
                          locType != null ? locType : Enu.get(LocType.class));
    }
    
    //
    
    default @NotNull Bounds bounds(@NotNull NumExpr2D<?> minLoc, @NotNull NumExpr2D<?> maxLoc,
                                   @NotNull NumExpr2D<?> minDim, @NotNull NumExpr2D<?> maxDim,
                                   @Nullable LocType locType) {
        return bounds(minLoc.a(), minLoc.b(), minDim.a(), minDim.b(),
                      maxLoc.a(), maxLoc.b(), maxDim.a(), maxDim.b(),
                      locType);
    }
    default @NotNull Bounds bounds(@NotNull NumExpr2D<?> minLoc, @NotNull NumExpr2D<?> maxLoc,
                                   @NotNull NumExpr2D<?> minDim, @NotNull NumExpr2D<?> maxDim) {
        return bounds(minLoc, maxLoc, minDim, maxDim, null);
    }
    
    default @NotNull Bounds bounds(@NotNull BoundsExpr min, @NotNull BoundsExpr max, @Nullable LocType locType) {
        return bounds(min.x(), min.y(), min.w(), min.h(),
                      max.x(), max.y(), max.w(), max.h(),
                      locType);
    }
    default @NotNull Bounds bounds(@NotNull BoundsExpr min, @NotNull BoundsExpr max) { return bounds(min, max, null); }
    
    //<editor-fold desc="> Floor/Ceil">
    
    //<editor-fold desc=">> Floor">
    
    default @NotNull Bounds boundsFloor(@NotNull Number minX, @NotNull Number minY,
                                        @NotNull Number minW, @NotNull Number minH,
                                        @Nullable LocType locType) {
        return bounds(minX, minY, minW, minH, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, locType);
    }
    
    default @NotNull Bounds boundsFloor(@NotNull Number minX, @NotNull Number minY,
                                        @NotNull Number minW, @NotNull Number minH) {
        return boundsFloor(minX, minY, minW, minH, null);
    }
    
    //<editor-fold desc=">>> Floor Loc/Dim">
    
    default @NotNull Bounds boundsFloorLoc(@NotNull Number minX, @NotNull Number minY, @Nullable LocType locType) {
        return boundsFloor(minX, minY, Integer.MIN_VALUE, Integer.MIN_VALUE, locType);
    }
    default @NotNull Bounds boundsFloorLoc(@NotNull Number minX, @NotNull Number minY) { return boundsFloorLoc(minX, minY, null); }
    default @NotNull Bounds boundsFloorLoc(@NotNull Number min) { return boundsFloorLoc(min, min, null); }
    
    default @NotNull Bounds boundsFloorLoc() { return boundsFloorLoc(Double.MIN_VALUE); }
    default @NotNull Bounds boundsFloorLocI() { return boundsFloorLoc(1); }
    
    //
    
    default @NotNull Bounds boundsFloorDim(@NotNull Number minW, @NotNull Number minH, @Nullable LocType locType) {
        return boundsFloor(Integer.MIN_VALUE, Integer.MIN_VALUE, minW, minH, locType);
    }
    default @NotNull Bounds boundsFloorDim(@NotNull Number minW, @NotNull Number minH) { return boundsFloorDim(minW, minH, null); }
    default @NotNull Bounds boundsFloorDim(@NotNull Number min) { return boundsFloorDim(min, min, null); }
    
    default @NotNull Bounds boundsFloorDim() { return boundsFloorDim(Double.MIN_VALUE); }
    default @NotNull Bounds boundsFloorDimI() { return boundsFloorLoc(1); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc=">> Ceil">
    
    default @NotNull Bounds boundsCeil(@NotNull Number maxX, @NotNull Number maxY, @NotNull Number maxW, @NotNull Number maxH, @Nullable LocType locType) {
        return bounds(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, maxX, maxY, maxW, maxH, locType);
    }
    default @NotNull Bounds boundsCeil(@NotNull Number maxX, @NotNull Number maxY, @NotNull Number maxW, @NotNull Number maxH) {
        return boundsCeil(maxX, maxY, maxW, maxH, null);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="> Preset Bounds Factory Methods">
    
    //<editor-fold desc=">> Preset POSITIVE Bounds Factory Methods">
    
    default @NotNull Bounds boundsPos(boolean constrainLocation, boolean constrainDimensions, @Nullable LocType locType) {
        return boundsFloor(constrainLocation ? Double.MIN_VALUE : Integer.MIN_VALUE,
                           constrainLocation ? Double.MIN_VALUE : Integer.MIN_VALUE,
                           constrainDimensions ? Double.MIN_VALUE : Integer.MIN_VALUE,
                           constrainDimensions ? Double.MIN_VALUE : Integer.MIN_VALUE,
                           locType);
    }
    default @NotNull Bounds boundsPos(boolean constrainLocation, boolean constrainDimensions) {
        return boundsPos(constrainLocation, constrainDimensions, null);
    }
    default @NotNull Bounds boundsPos(@Nullable LocType locType) { return boundsPos(true, true, locType); }
    default @NotNull Bounds boundsPos() { return boundsPos(null); }
    
    default @NotNull Bounds boundsPosLoc(@Nullable LocType locType) { return boundsPos(true, false, locType); }
    default @NotNull Bounds boundsPosLoc() { return boundsPosLoc(null); }
    default @NotNull Bounds boundsPosDim(@Nullable LocType locType) { return boundsPos(false, true, locType); }
    default @NotNull Bounds boundsPosDim() { return boundsPosDim(null); }
    
    //</editor-fold>
    
    //<editor-fold desc=">> Preset NEGATIVE Bounds Factory Methods">
    
    default @NotNull Bounds boundsNeg(boolean constrainLocation, boolean constrainDimensions, @Nullable LocType locType) {
        return boundsCeil(constrainLocation ? -Double.MIN_VALUE : Integer.MIN_VALUE,
                          constrainLocation ? -Double.MIN_VALUE : Integer.MIN_VALUE,
                          constrainDimensions ? -Double.MIN_VALUE : Integer.MIN_VALUE,
                          constrainDimensions ? -Double.MIN_VALUE : Integer.MIN_VALUE,
                          locType);
    }
    default @NotNull Bounds boundsNeg(boolean constrainLocation, boolean constrainDimensions) {
        return boundsNeg(constrainLocation, constrainDimensions, null);
    }
    default @NotNull Bounds boundsNeg(@Nullable LocType locType) { return boundsNeg(true, true, locType); }
    default @NotNull Bounds boundsNeg() { return boundsNeg(null); }
    
    default @NotNull Bounds boundsNegLoc(@Nullable LocType locType) { return boundsNeg(true, false, locType); }
    default @NotNull Bounds boundsNegLoc() { return boundsNegLoc(null); }
    default @NotNull Bounds boundsNegDim(@Nullable LocType locType) { return boundsNeg(false, true, locType); }
    default @NotNull Bounds boundsNegDim() { return boundsNegDim(null); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="> Primitive Accessors">
    
    default int xI() { return x().intValue(); }
    default int yI() { return y().intValue(); }
    default int wI() { return w().intValue(); }
    default int hI() { return h().intValue(); }
    
    default long xL() { return x().longValue(); }
    default long yL() { return y().longValue(); }
    default long wL() { return w().longValue(); }
    default long hL() { return h().longValue(); }
    
    default float xF() { return x().floatValue(); }
    default float yF() { return y().floatValue(); }
    default float wF() { return w().floatValue(); }
    default float hF() { return h().floatValue(); }
    
    default double xD() { return x().doubleValue(); }
    default double yD() { return y().doubleValue(); }
    default double wD() { return w().doubleValue(); }
    default double hD() { return h().doubleValue(); }
    
    //
    
    default int locI(@NotNull Axis axis) { return getLocation(axis).intValue(); }
    default int locI(@NotNull Axis axis, @NotNull LocType locType) { return getLocation(axis, locType).intValue(); }
    
    default long locL(@NotNull Axis axis) { return getLocation(axis).longValue(); }
    default long locL(@NotNull Axis axis, @NotNull LocType locType) { return getLocation(axis, locType).longValue(); }
    
    default float locF(@NotNull Axis axis) { return getLocation(axis).floatValue(); }
    default float locF(@NotNull Axis axis, @NotNull LocType locType) { return getLocation(axis, locType).floatValue(); }
    
    default double locD(@NotNull Axis axis) { return getLocation(axis).doubleValue(); }
    default double locD(@NotNull Axis axis, @NotNull LocType locType) { return getLocation(axis, locType).doubleValue(); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Calculated Accessors">
    
    default double area() { return wD() * hD(); }
    default double hypot() { return Math.hypot(wD(), hD()); }
    
    //</editor-fold>
    
    //</editor-fold>
}
