package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.tools.printing.Printer;
import com.taco.suit_lady.util.values.enums.Axis;
import com.taco.suit_lady.util.values.numbers.N;
import com.taco.suit_lady.util.values.numbers.Num;
import com.taco.suit_lady.util.values.numbers.Num2D;
import com.taco.suit_lady.util.values.shapes.Box;
import com.taco.suit_lady.util.values.enums.LocType;
import com.taco.suit_lady.util.values.shapes.Shape;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.timing.Timing;
import com.taco.suit_lady.util.values.numbers.expressions.NumExpr2D;
import com.taco.suit_lady.util.values.Value2D;
import com.taco.suit_lady.util.values.ValueExpr2D;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Contains methods related to doing various calculations.
 */
public class Calc {
    private Calc() { } // No instance
    
    /**
     * Returns the absolute value difference between the two specified integers.
     * <p>
     * This method will evaluate the two values as |a - b|.
     *
     * @param number1 The first integer.
     * @param number2 The second integer.
     *
     * @return The absolute value.
     */
    public static int getAbsoluteDifferent(Number number1, Number number2) {
        Exc.nullCheck(number1);
        Exc.nullCheck(number2);
        return (Math.abs(number1.intValue() - number2.intValue()));
    }
    
    /**
     * Returns the absolute value difference between the two specified integers.
     * <p>
     * This method will evaluate the two values as |a - b|.
     *
     * @param number1 The first integer.
     * @param number2 The second integer.
     *
     * @return The absolute value.
     */
    public static double getAbsoluteDoubleDifferent(Number number1, Number number2) {
        Exc.nullCheck(number1);
        Exc.nullCheck(number2);
        return (Math.abs(number1.doubleValue() - number2.doubleValue()));
    }
    
    /**
     * Returns the per hour value for the specified value.
     *
     * @param value     The value to evaluate.
     * @param startTime The time in milliseconds when the tracking of the value
     *                  started.
     *
     * @return The per hour value.
     */
    public static long getPerHour(int value, long startTime) {
        final long t = Timing.currentTimeMillis();
        
        if (t == startTime)
            return 0;
        
        return (long) (value * 3600000D / (t - startTime));
    }
    
    /**
     * Rounds the specified number to the specified place (non-decimal).
     * <p>
     * <i>See {@link #roundD(double, int, int)} to round decimals.</i>
     *
     * @param number The number being rounded.
     * @param place  The place being rounded to.
     *
     * @return The formatted number.
     *
     * @see #roundD(double, int, int)
     */
    public static int round(int number, int place) {
        place = (int) Math.pow(10, place);
        return number < place ? number : (number / place) * place;
    }
    
    /**
     * Moves the decimal point over {@code decimal shift} places, and then
     * rounds the specified double to the specified {@code decimal place}.
     * <p>
     * For example: {@code roundD(4563.2352345342, 2, 1)} returns {@code 456.32}
     * .
     * <p>
     * <i>See {@link #round(int, int)} to round whole numbers.</i>
     *
     * @param number       The number being rounded.
     * @param decimalShift The number of places the decimal place is being moved over.
     *                     <p>
     *                     Note that this calculation
     * @param decimalPlace The decimal place to round to.
     *
     * @return The specified double to the specified integer place and specified
     * floating place.
     */
    public static double roundD(double number, int decimalShift, int decimalPlace) {
        decimalShift = (int) Math.pow(10, decimalShift); // Turn the decimal
        // shift value into
        // the decimal shift
        // multiplier
        decimalPlace = (int) Math.pow(10, decimalPlace); // Turn the decimal
        // point value into
        // the decimal place
        // multiplier
        double percent = number * ((double) decimalShift * decimalPlace); // Multiply
        // the
        // value
        // by
        // the
        // combined
        // multipliers
        // to
        // get
        // a
        // large
        // number
        // representation
        int rounded = (int) percent; // Round the value to truncate any
        // remaining floating numbers
        return (double) rounded / decimalPlace; // Divide by the decimal place
        // multiplier to get to the
        // desired decimal place value
    }
    
    /**
     * Rounds the specified int ({@code i}) to a value that is a multiple of the
     * second specified int ({@code number}). If i less than number, then 0 is
     * returned.
     *
     * @param i      The number being rounded.
     * @param number The returned value must be a multiple of this number.
     *
     * @return The a value that is a multiple of the second specified int, or 0
     * if i less than number.
     */
    public static int isMultipleOf(int i, int number) {
        return i / number * number;
    }
    
    /**
     * Returns the distance between the points defined by Point(x1, y1) and
     * Point(x2, y2).
     *
     * @param x1 The x coordinate of the first point.
     * @param y1 The y coordinate of the first point.
     * @param x2 The x coordinate of the second point.
     * @param y2 The y coordinate of the second point.
     *
     * @return The distance between the points defined by Point(x1, y1) and
     * Point(x2, y2).
     */
    public static double distanceTo(int x1, int y1, int x2, int y2) {
        throw Exc.nyi();
    } // TODO - TRiLeZ - Have I already created a method for this yet?
    
    /**
     * Checks if the two specified points are within the specified range of each
     * other using their x and y coordinates.
     * <p>
     * This method does NOT use the distance formula.
     *
     * @param p1    The first point.
     * @param p2    The second point.
     * @param range The range.
     *
     * @return True if the two specified points are within the specified range
     * of each other, false otherwise.
     */
    public static boolean isPointWithinRange(Point p1, Point p2, int range) {
        return p1 != null && p2 != null && Math.abs(p1.x - p2.x) <= range && Math.abs(p1.y - p2.y) <= range;
    }
    
    /**
     * Returns the integer representation of the specified kmb String, or 0 if
     * there was an error.
     * <p>
     * Note that the kmb String uses RuneScape rules, therefore no decimals are
     * allowed. IE, if it wouldn't work in RuneScape, it won't work here.
     * <p>
     * This method allows for negative numbers.
     *
     * @param kmbStr         The kmb String.
     * @param throwException True if this method should throw an exception if the
     *                       conversion cannot be completed, false if it should return 0.
     *
     * @return The integer representation of the specified kmb String.
     */
    public static long getLongkmb(String kmbStr, boolean throwException) {
        return (long) getkmb(kmbStr, throwException);
    }
    
    /**
     * Returns the double representation of the specified kmb String, or 0 if
     * there was an error.
     * <p>
     * Note that the kmb String <i>does</i> allow decimals.
     * <p>
     * This method allows for negative numbers.
     *
     * @param kmbStr         The kmb String.
     * @param throwException True if this method should throw an exception if the
     *                       conversion cannot be completed, false if it should return 0.
     *
     * @return The double representation of the specified kmb String.
     */
    public static double getkmb(String kmbStr, boolean throwException) {
        try {
            if (kmbStr.endsWith("k"))
                return Double.parseDouble(kmbStr.replaceAll("k", "")) * 1000;
            else if (kmbStr.endsWith("m"))
                return Double.parseDouble(kmbStr.replaceAll("m", "")) * 1000000;
            else if (kmbStr.endsWith("b"))
                return Double.parseDouble(kmbStr.replaceAll("b", "")) * 1000000000;
            else
                return Double.parseDouble(kmbStr);
        } catch (NumberFormatException e1) {
            if (throwException)
                throw e1;
        }
        return 0.0;
    }
    
    /**
     * Gets what percentage of the specified value is the specified character.
     * The character should be a character that can be contained within a
     * number, or this method will always return 0.
     *
     * @param value The int being checked.
     * @param charr The char.
     *
     * @return What percentage of the specified value is the specified
     * character. The character should be a character that can be
     * contained within a number, or this method will always return 0.
     */
    public static int getPercent(int value, char charr) {
        double percent = (double) countChars(value, charr) / (double) ((String.valueOf(value)).length());
        return (int) (percent * 100);
    }
    
    /**
     * Returns the number of characters matching the specified character in the
     * specified int.
     *
     * @param value The int being checked.
     * @param charr The char.
     *
     * @return The number of characters matching the specified character in the
     * specified int.
     */
    private static int countChars(int value, char charr) {
        String sValue = "" + value;
        char[] cValue = sValue.toCharArray();
        int zeroes = 0;
        for (char aCValue: cValue)
            if (aCValue == charr)
                zeroes++;
        return zeroes;
    }
    
    // <editor-fold desc="Clamp">
    
    /**
     * Returns a value that is greater than or equal to the specified min value.
     * <ul>
     * <li>If the specified value is greater than the min value, the specified
     * value is returned</li>
     * <li>If the specified value is less than the min value, the min value is
     * returned.</li>
     * </ul>
     *
     * @param value The value.
     * @param min   The min value.
     *
     * @return A value that is greater than or equal to the specified min value.
     */
    public static <T extends Number> T clampMin(T value, T min) {
        Exc.nullCheck(value);
        Exc.nullCheck(min);
        
        if (value.doubleValue() < min.doubleValue())
            return min;
        return value;
    }
    
    /**
     * Returns a value that is less than or equal to the specified max value.
     * <ul>
     * <li>If the specified value is less than the max value, the specified
     * value is returned</li>
     * <li>If the specified value is greater than the max value, the max value
     * is returned.</li>
     * </ul>
     *
     * @param value The value.
     * @param max   The min value.
     *
     * @return A value that is less than or equal to the specified max value.
     */
    public static <T extends Number> T clampMax(T value, T max) {
        Exc.nullCheck(value);
        Exc.nullCheck(max);
        
        if (value.doubleValue() > max.doubleValue())
            return max;
        return value;
    }
    
    /**
     * Returns a value that is between the specified min and max values.
     * <ul>
     * <li>If the specified value is between the specified min and max values,
     * the specified value is returned</li>
     * <li>If the specified value is less than the min value, the min value is
     * returned.</li>
     * <li>If the specified value is greater than the max value, the max value
     * is returned.</li>
     * </ul>
     *
     * @param value The value.
     * @param min   The min value.
     * @param max   The max value.
     *
     * @return A value that is between the specified min and max values.
     */
    public static <T extends Number> T clamp(T value, T min, T max) {
        Exc.nullCheck(value);
        Exc.nullCheck(min);
        Exc.nullCheck(max);
        
        if (value.doubleValue() < min.doubleValue())
            value = min;
        if (value.doubleValue() > max.doubleValue())
            value = max;
        return value;
    }
    
    // </editor-fold>
    
    public static void generateStraightPath(final Point start, final Point end, final Consumer<Point> point_consumer) {
        Calc.generateStraightPath(start, end, point_consumer, true);
    }
    
    public static void generateStraightPath(final Point start, final Point end, final Consumer<Point> point_consumer,
                                            final boolean add_start) {
        final double distance = start.distance(end);
        final double points = Math.floor(distance + 1);
        
        if (distance <= 1.4 || points <= 1) {
            if (add_start)
                point_consumer.accept((Point) start.clone());
            
            point_consumer.accept((Point) end.clone());
            
            return;
        }
        
        final double start_x = start.getX();
        final double start_y = start.getY();
        
        final double dx = end.getX() - start_x;
        final double dy = end.getY() - start_y;
        
        final double x_step = dx / points;
        final double y_step = dy / points;
        
        int last_x = 0;
        int last_y = 0;
        
        for (int i = 0; i <= points; i++) {
            final int x = (int) Math.round(start.getX() + (x_step * i));
            final int y = (int) Math.round(start.getY() + (y_step * i));
            
            if (i == 0 || !(last_x == x && last_y == y)) {
                if ((i == 0 && add_start) || i > 0)
                    point_consumer.accept(new Point(x, y));
                
                last_x = x;
                last_y = y;
            }
        }
        
        if (!(last_x == end.getX() && last_y == end.getY()))
            point_consumer.accept((Point) end.clone());
    }
    
    public static List<Point> generateStraightPath(final Point start, final Point end) {
        return Calc.generateStraightPath(start, end, true);
    }
    
    public static List<Point> generateStraightPath(final Point start, final Point end,
                                                   final boolean add_start) {
        final double distance = start.distance(end);
        final double points = Math.floor(distance + 1);
        
        if (distance <= 1.4 || points <= 1) {
            final List<Point> arr = new ArrayList<>(2);
            
            if (add_start)
                arr.add((Point) start.clone());
            
            arr.add((Point) end.clone());
            
            return arr;
        }
        
        final List<Point> arr = new ArrayList<>((int) points + 2);
        
        Calc.generateStraightPath(start, end, p -> arr.add(p), add_start);
        
        return arr;
    }
    
    public static double[] getRotatedPoint(final double x1, final double y1, final double x2, final double y2,
                                           final double angle) {
        final double[] pt = new double[]{x2, y2};
        
        AffineTransform.getRotateInstance(Math.toRadians(angle), x1, y1).transform(pt, 0, pt, 0, 1);
        
        return pt;
    }
    
    public static double getAngle(final double x1, final double y1, final double x2, final double y2, final double x3,
                                  final double y3) {
        final double angle1 = Math.atan2(y1 - y2, x1 - x2);
        final double angle2 = Math.atan2(y1 - y3, x1 - x3);
        
        return angle1 - angle2;
    }
    
    /**
     * Calculates the amount of time a mouse movement should take based on
     * Fitts' Law.
     *
     * @param delay The time taken for the user to realize the mouse needs to be
     *              moved.
     * @param dist  The distance from the current position to the center of the
     *              target.
     * @param size  The width of the target.
     * @param speed The speed of the mouse movement.
     *
     * @return The amount of time (in ms) the movement should take
     *
     * @see <a href="https://en.wikipedia.org/wiki/Fitts%27s_law">https://en.
     * wikipedia.org/wiki/Fitts%27s_law</a>
     */
    public static double fittsLaw(final double delay, final double dist, final double size, final double speed) {
        return (delay + speed * Math.log10(dist / size + 1) / Math.log10(2));
    }
    
    /**
     * Satisfies Integral[gaussian(t),t,0,1] == 1D. Therefore can distribute a
     * value as a bell curve over the interval 0 to 1.
     *
     * @param t A value, 0 to 1, representing a percent along the curve.
     *
     * @return The value of the gaussian curve at this position.
     */
    public static double gaussian(double t) {
        t = 10D * t - 5D;
        
        return 1D / (Math.sqrt(5D) * Math.sqrt(2D * Math.PI)) * Math.exp(-t * t / 20D);
    }
    
    /**
     * Returns an array of gaussian values that add up to 1 for the number of
     * steps Solves the problem of having using an integral to distribute
     * values.
     *
     * @param steps Number of steps in the distribution.
     *
     * @return An array of values that contains the percents of the
     * distribution.
     */
    public static double[] gaussTable(final int steps) {
        final double[] table = new double[steps];
        final double step = 1D / steps;
        
        double sum = 0;
        
        for (int i = 0; i < steps; i++)
            sum -= (table[i] = -Calc.gaussian(i * step));
        
        for (int i = 0; i < steps; i++)
            table[i] /= sum;
        
        double min = +0.0;
        double max = -1.0;
        
        for (int i = 0; i < steps; i++) {
            if (table[i] < min)
                min = table[i];
            
            if (table[i] > max)
                max = table[i];
        }
        
        final double d = min + max;
        
        for (int i = 0; i < steps; i++)
            table[i] -= d;
        
        sum = 0;
        
        for (int i = 0; i < steps; i++)
            sum += table[i];
        
        for (int i = 0; i < steps; i++)
            table[i] /= sum;
        
        return table;
    }
    
    /**
     * Calculates the distance in degrees between two angles, always in the
     * range of [0, 180].
     *
     * @param deg1 The degrees of the first angle.
     * @param deg2 The degrees of the second angle.
     *
     * @return {@code double}
     */
    public static double degDistance(final double deg1, final double deg2) {
        final double phi = Math.abs(deg2 - deg1) % 360;
        final double distance = phi > 180 ? 360 - phi : phi;
        
        return distance;
    }
    
    static final double RAD_TO_DEG_NEG = -180.0 / Math.PI;
    
    /**
     * Reflects all of the points in the specified list over the straight line
     * between the first and the last points.
     *
     * @param line The line to flip.
     *
     * @return {@code line}
     */
    public static List<? extends Point> flipLine(final List<? extends Point> line) {
        if (line.size() <= 2)
            return line;
        
        final Point start = line.get(0);
        final Point end = line.get(line.size() - 1);
        
        final double start_x = start.getX();
        final double start_y = start.getY();
        
        final double end_x = end.getX();
        final double end_y = end.getY();
        
        line.stream().limit(line.size() - 1).skip(1).forEach(p -> {
            final double angle = Calc.getAngle(start_x, start_y, p.getX(), p.getY(), end_x, end_y)
                                 * Calc.RAD_TO_DEG_NEG;
            if (angle == 0)
                return;
            
            final double[] rt = Calc.getRotatedPoint(start_x, start_y, p.getX(), p.getY(), angle * 2.0);
            
            p.setLocation(rt[0], rt[1]);
        });
        
        return line;
    }
    
    public static Point flipLinePoint(final List<? extends Point> line, final Point p) {
        if (line.size() < 2)
            return p;
        
        return Calc.flipLinePoint(line.get(0), line.get(line.size() - 1), p);
    }
    
    public static Point flipLinePoint(final Point start, final Point end, final Point p) {
        final double start_x = start.getX();
        final double start_y = start.getY();
        
        final double angle = Calc.getAngle(start_x, start_y, p.getX(), p.getY(), end.getX(), end.getY())
                             * Calc.RAD_TO_DEG_NEG;
        if (angle == 0)
            return p;
        
        final double[] rt = Calc.getRotatedPoint(start_x, start_y, p.x, p.y, angle * 2.0);
        
        p.setLocation(rt[0], rt[1]);
        
        return p;
    }
    
    
    public static @NotNull Point2D getPointInBounds(
            @NotNull Point2D origin,
            @Nullable ValueExpr2D<? extends Number, ? extends Number> minBounds,
            @NotNull ValueExpr2D<? extends Number, ? extends Number> maxBounds) {
        minBounds = minBounds != null ? minBounds : new Value2D<>(0, 0);
        
        if (minBounds.a().doubleValue() >= maxBounds.a().doubleValue())
            throw Exc.unsupported("Min X Bounds (" + minBounds.a().doubleValue() + ") must be less than Max X Bounds (" + maxBounds.a().doubleValue());
        if (minBounds.b().doubleValue() >= maxBounds.b().doubleValue())
            throw Exc.unsupported("Min Y Bounds (" + minBounds.b().doubleValue() + ") must be less than Max Y Bounds (" + maxBounds.b().doubleValue());
        
        double x = origin.getX();
        double y = origin.getY();
        
        if (x >= maxBounds.a().doubleValue())
            x = maxBounds.a().doubleValue();
        if (x <= minBounds.a().doubleValue())
            x = minBounds.a().doubleValue();
        
        if (y >= maxBounds.b().doubleValue())
            y = maxBounds.b().doubleValue();
        if (y <= minBounds.b().doubleValue())
            y = minBounds.b().doubleValue();
        
        return new Point2D(x, y);
    }
    public static @NotNull Point2D getPointInBounds(@NotNull Point2D origin, @NotNull ValueExpr2D<? extends Number, ? extends Number> maxBounds) { return getPointInBounds(origin, null, maxBounds); }
    
    public static @NotNull ObjectBinding<Point2D> getPointInBoundsBinding(
            @NotNull ObservableValue<Point2D> originProperty,
            @Nullable ValueExpr2D<? extends Number, ? extends Number> minBounds,
            @NotNull ValueExpr2D<? extends Number, ? extends Number> maxBounds) {
        return Bind.objBinding(() -> getPointInBounds(originProperty.getValue(), minBounds, maxBounds), originProperty);
    }
    public static @NotNull ObjectBinding<Point2D> getPointInBoundsBinding(
            @NotNull ObservableValue<Point2D> originProperty,
            @NotNull ValueExpr2D<? extends Number, ? extends Number> maxBounds) {
        return getPointInBoundsBinding(originProperty, null, maxBounds);
    }
    
    //
    
    public static <N extends Number> @NotNull N bounded(@NotNull N value, @NotNull N min, @NotNull N max) {
        if (min.doubleValue() >= max.doubleValue())
            throw Exc.inputMismatch("Min must be less than max.");
        else if (value.doubleValue() < min.doubleValue())
            return min;
        else if (value.doubleValue() > max.doubleValue())
            return max;
        return value;
    }
    
    //
    
    public static @NotNull ArrayList<Num2D> formCircle(@NotNull Num2D center, @NotNull Number radius, @NotNull Number precision) {
        return IntStream.iterate(0, i -> i < 360, i -> i + precision.intValue())
                        .mapToObj(i -> center.interpolateTowards(i, radius))
                        .collect(Collectors.toCollection(ArrayList::new));
    }
    public static @NotNull ArrayList<Num2D> formCircle(@NotNull Num2D center, @NotNull Number radius) {
        return formCircle(center, radius, 9);
    }
    
    //
    
    public static @Nullable Num2D nearestMatching(@NotNull Num2D pos, @NotNull Num2D dims,
                                                  @NotNull LocType inputLocType, @NotNull LocType outputLocType, @NotNull LocType testLocType,
                                                  @NotNull Number step, @NotNull Number maxRange,
                                                  @NotNull Number targetAngle,
                                                  @NotNull Predicate<Num2D> filter) {
        final Num2D centerPos = LocType.translate(pos, dims, inputLocType, LocType.CENTER);
        final double targetAngleD = normalizeAngle(targetAngle);
        //        final ArrayList<Num2D> testPoints = formCircle(centerPos, maxRange);
        Num2D closestPoint = null;
        for (int i = 0; i < maxRange.intValue(); i += step.intValue()) {
            final ArrayList<Num2D> testPoints = formCircle(centerPos, i);
            Num2D bestPoint = null;
            for (Num2D testPointCenter: testPoints)
                if (filter.test(LocType.translate(testPointCenter, dims, LocType.CENTER, testLocType)))
                    bestPoint = bestPoint == null ? testPointCenter : closestAngleTo(centerPos, targetAngle, bestPoint, testPointCenter);
            if (bestPoint != null)
                return LocType.translate(bestPoint, dims, LocType.CENTER, outputLocType); ;
        }
        throw Exc.ex("No valid pathing found.");
    }
    
    public static @Nullable Num2D nearestMatching(@NotNull Num2D pos, @NotNull Num2D dims,
                                                  @NotNull LocType locType, @NotNull LocType testLocType,
                                                  @NotNull Number maxRange, @NotNull Number targetAngle,
                                                  @NotNull Predicate<Num2D> filter) {
        return nearestMatching(pos, dims, locType, locType, testLocType, 1, maxRange, targetAngle, filter);
    }
    
    //<editor-fold desc="> Point Calculations">
    
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * <u>origin</u><b>.</b>{@link Point2D#distance(Point2D) distance}<b>(</b><u>other</u><b>)</b>
     * </code></i></blockquote>
     */
    public static double distance(@NotNull Point2D origin, @NotNull Point2D other) { return origin.distance(other); }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #distance(Point2D, Point2D) distance}<b>(</b>{@link Calc}<b>.</b>{@link #point2D(Number, Number) point2D}<b>(</b><u>x</u>, <u>y</u><b>)</b>, {@link Calc}<b>.</b>{@link #point2D(Number, Number) point2D}<b>(</b><u>oX</u>, <u>oY</u><b>))</b>
     * </code></i></blockquote>
     */
    public static double distance(@NotNull Number x, @NotNull Number y, @NotNull Number oX, @NotNull Number oY) { return distance(Calc.point2D(x, y), Calc.point2D(oX, oY)); }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i><code>
     * {@link #distance(Point2D, Point2D) distance}<b>(</b><u>origin</u><b>.</b>{@link NumExpr2D#asPoint() asPoint}<b>()</b>, <u>other</u><b>.</b>{@link NumExpr2D#asPoint() asPoint}<b>())</b>
     * </code></i></blockquote>
     */
    public static double distance(@NotNull NumExpr2D<?> origin, @NotNull NumExpr2D<?> other) { return distance(origin.asPoint(), other.asPoint()); }
    
    //
    
    /**
     * <p>Returns the arc distance as an angle between the two points represented by the specified x and y point values.</p>
     *
     * @param p1X The x value of the first point.
     * @param p1Y The y value of the first point.
     * @param p2X The x value of the second point.
     * @param p2Y The y value of the second point.
     *
     * @return The arc distance as an angle between the two points represented by the specified x and y point values.
     */
    public static double angle(@NotNull Number p1X, @NotNull Number p1Y, @NotNull Number p2X, @NotNull Number p2Y) {
        return normalizeAngle(Calc.radsToDegrees(Math.atan2(p1Y.doubleValue() - p2Y.doubleValue(), p1X.doubleValue() - p2X.doubleValue()), true) - 90);
    }
    
    /**
     * <p><i>See {@link #angle(Number, Number, Number, Number)}</i></p>
     */
    public static double angle(@NotNull NumExpr2D<?> p1, @NotNull NumExpr2D<?> p2) { return angle(p1.a(), p1.b(), p2.a(), p2.b()); }
    /**
     * <p><i>See {@link #angle(Number, Number, Number, Number)}</i></p>
     */
    public static double angle(@NotNull Point2D p1, @NotNull Point2D p2) { return angle(N.num2D(p1), N.num2D(p2)); }
    /**
     * <p><i>See {@link #angle(Number, Number, Number, Number)}</i></p>
     */
    public static double angle(@NotNull NumExpr2D<?> p1, @NotNull Point2D p2) { return angle(p1, N.num2D(p2)); }
    /**
     * <p><i>See {@link #angle(Number, Number, Number, Number)}</i></p>
     */
    public static double angle(@NotNull Point2D p1, @NotNull NumExpr2D<?> p2) { return angle(N.num2D(p1), p2); }
    
    /**
     * <p><i>See {@link #angle(Number, Number, Number, Number)}</i></p>
     */
    public static double angle(@NotNull NumExpr2D<?> p1, @NotNull Number p2X, @NotNull Number p2Y) { return angle(p1, new Num2D(p2X, p2Y)); }
    /**
     * <p><i>See {@link #angle(Number, Number, Number, Number)}</i></p>
     */
    public static double angle(@NotNull Point2D p1, @NotNull Number p2X, @NotNull Number p2Y) { return angle(p1, new Num2D(p2X, p2Y)); }
    /**
     * <p><i>See {@link #angle(Number, Number, Number, Number)}</i></p>
     */
    public static double angle(@NotNull Number p1X, @NotNull Number p1Y, @NotNull NumExpr2D<?> p2) { return angle(new Num2D(p1X, p1Y), p2); }
    /**
     * <p><i>See {@link #angle(Number, Number, Number, Number)}</i></p>
     */
    public static double angle(@NotNull Number p1X, @NotNull Number p1Y, @NotNull Point2D p2) { return angle(new Num2D(p1X, p1Y), p2); }
    
    //
    
    public static @NotNull Num2D interpolate(@NotNull Point2D origin, @NotNull Point2D other, @NotNull Number percentage) { return N.num2D(origin.interpolate(other, percentage.doubleValue())); }
    public static @NotNull Num2D interpolate(@NotNull Number x, @NotNull Number y, @NotNull Number oX, @NotNull Number oY, @NotNull Number percentage) { return interpolate(point2D(x, y), point2D(oX, oY), percentage); }
    public static @NotNull Num2D interpolate(@NotNull NumExpr2D<?> origin, @NotNull NumExpr2D<?> other, @NotNull Number percentage) { return interpolate(origin.asPoint(), other.asPoint(), percentage); }
    
    //
    
    public static @NotNull Num2D interpolateTowards(@NotNull Point2D origin, @NotNull Point2D other, @NotNull Number distance) { return interpolateTowards(origin, Calc.degreesToRads(Calc.angle(origin, other)), distance); }
    public static @NotNull Num2D interpolateTowards(@NotNull Number x, @NotNull Number y, @NotNull Number oX, @NotNull Number oY, @NotNull Number distance) { return interpolateTowards(Calc.point2D(x, y), Calc.point2D(oX, oY), distance); }
    public static @NotNull Num2D interpolateTowards(@NotNull NumExpr2D<?> origin, @NotNull NumExpr2D<?> other, @NotNull Number distance) { return interpolateTowards(origin.asPoint(), other.asPoint(), distance); }
    
    public static @NotNull Num2D interpolateTowards(@NotNull Point2D origin, @NotNull Number angle, @NotNull Number distance) {
        return new Num2D((distance.doubleValue() * Math.cos(angle.doubleValue())) + origin.getX(), (distance.doubleValue() * Math.sin(angle.doubleValue())) + origin.getY());
    }
    public static @NotNull Num2D interpolateTowards(@NotNull NumExpr2D<?> origin, @NotNull Number angle, @NotNull Number distance) { return interpolateTowards(origin.asPoint(), angle, distance); }
    
    //
    
    public static boolean minMaxAngleTest = false;
    
    public static @Nullable Num2D closestAngleTo(@NotNull NumExpr2D<?> center, @NotNull Number targetAngle, @NotNull NumExpr2D<?> @NotNull ... testPoints) {
        Num2D bestPoint = null;
        double bestAngle = Double.NaN;
        for (NumExpr2D<?> testPoint: testPoints) {
            final double testAngle = angle(center, testPoint);
            
            if (bestPoint == null || Double.isNaN(bestAngle) || isCloserTo(bestAngle, testAngle, targetAngle)) {
                bestPoint = testPoint.asNum2D();
                bestAngle = testAngle;
            }
            
            if (minMaxAngleTest && isCloserTo(bestAngle, testAngle - 360, targetAngle)) {
                bestPoint = testPoint.asNum2D();
                bestAngle = testAngle - 360;
            }
        }
        
        return bestPoint;
    }
    
    public static boolean isCloserTo(@NotNull Number baseNum, @NotNull Number testNum, @NotNull Number target) {
        final double distBase = Math.abs(baseNum.doubleValue() - target.doubleValue());
        final double distTest = Math.abs(testNum.doubleValue() - target.doubleValue());
        return distTest < distBase;
    }
    
    public static double closestTo(@NotNull Number num1, @NotNull Number num2, @NotNull Number target) {
        final double dist1 = Math.abs(num1.doubleValue() - target.doubleValue());
        final double dist2 = Math.abs(num2.doubleValue() - target.doubleValue());
        return dist1 < dist2 ? num1.doubleValue() : num2.doubleValue();
    }
    
    //
    
    public static boolean isInCone(@NotNull NumExpr2D<?> center, @NotNull NumExpr2D<?> testPoint, @NotNull Number radius, @NotNull Number minAngle, @NotNull Number maxAngle) {
        if (center.distance(testPoint) > radius.doubleValue())
            return false;
        
        final Num2D angleBounds = normalizeAngleBounds(minAngle, maxAngle);
        final double minAng = angleBounds.aD();
        final double maxAng = angleBounds.bD();
        
        final double testAng = angle(center, testPoint);
        
//        Printer.print("Test Angle: " + testAng);
        
        return testAng >= minAng && testAng <= maxAng;
    }
    
    public static boolean isInCone(@NotNull NumExpr2D<?> center, @NotNull NumExpr2D<?> testPoint, @NotNull NumExpr2D<?> limitPoint, @NotNull Number coneSize) {
        if (coneSize.doubleValue() <= 0)
            throw Exc.unsupported("Cone Size must be greater than 0.");
        
        final double limitRadius = center.distance(limitPoint);
        final double limitAngle = center.angle(limitPoint);
        
//        Printer.print("Limit Radius: " + limitRadius);
//        Printer.print("Limit Angle: " + limitAngle);
        
        return isInCone(center, testPoint, limitRadius, limitAngle - (coneSize.doubleValue() / 2), limitAngle + (coneSize.doubleValue() / 2));
    }
    
    public static boolean isInCone(@NotNull NumExpr2D<?> center, @NotNull NumExpr2D<?> testPoint, @NotNull Number radius, @NotNull NumExpr2D<?> targetPoint, @NotNull Number coneSize) {
//        Printer.print("Checking Cone:");
//        Printer.print("Center: " + center);
//        Printer.print("Test Point: " + testPoint);
//        Printer.print("Radius: " + radius);
//        Printer.print("Target Point: " + targetPoint);
//        Printer.print("Cone Size: " + coneSize);
        
        final double targetAngle = Calc.angle(center, targetPoint);
        final double minAng = normalizeAngle(targetAngle - (coneSize.doubleValue() / 2));
        final double maxAng = normalizeAngle(targetAngle + (coneSize.doubleValue() / 2));
        
//        Printer.print("Target Angle (Calc): " + targetAngle);
//        Printer.print("Min Ang (Calc): " + minAng);
//        Printer.print("Max Ang (Calc): " + maxAng);
        
        return isInCone(center, testPoint, radius, minAng, maxAng);
    }
    
    //
    
    public enum AngleType {
        ACTUAL,
        INVERSE,
        MIN_ARC,
        MAX_ARC;
        
        AngleType() { }
    }
    
    //</editor-fold>
    
    /**
     * <p>Returns a new {@link Num2D} instance representing {@link #normalizeAngle(Number) normalized} {@code min} and {@code max} angle values.</p>
     *
     * @param minAngle The raw minimum angle value.
     * @param maxAngle The raw maximum angle value.
     *
     * @return A new {@link Num2D} instance representing {@link #normalizeAngle(Number) normalized} {@code min} and {@code max} angle values.
     */
    public static @NotNull Num2D normalizeAngleBounds(@NotNull Number minAngle, @NotNull Number maxAngle) {
        double minAng = normalizeAngle(minAngle);
        double maxAng = normalizeAngle(maxAngle);
        
        if (minAng >= maxAng && minAng > 0)
            minAng -= 360;
        
        return new Num2D(minAng, maxAng);
        
//        if (minAng > maxAng)
//            return new Num2D(maxAng, minAng);
//        else if (minAng < maxAng)
//            return new Num2D(minAng, maxAng);
//        else
//            throw Exc.unsupported("Normalized Min & Max Angles cannot be equal: " + new Num2D(minAng, maxAng));
    }
    
    /**
     * <p>Converts the specified {@code angle} into an equivalent {@code angle} between 0 and 360 degrees.</p>
     *
     * @param angle The {@code angle} to be normalized.
     *
     * @return The converted representation of the specified {@code angle}.
     */
    public static double normalizeAngle(@NotNull Number angle) {
        //        Printer.print("Normalizing Angle: " + angle);
        final double ang = angle.doubleValue();
        double retAng = ang;
        if (isNormalAngle(retAng))
            return ang;
        else if (ang >= 360) {
            while (!isNormalAngle(retAng))
                retAng -= 360;
        } else if (ang < 0) {
            while (!isNormalAngle(retAng))
                retAng += 360;
        } else {
            throw Exc.ex("Wut.");
        }
        return retAng;
    }
    
    public static boolean isNormalAngle(@NotNull Number angle) {
        return angle.doubleValue() >= 0 && angle.doubleValue() < 360;
    }
    
    
    //
    
    //<editor-fold desc="--- Maths Ported Content ---">
    
    //<editor-fold desc="--- BASIC ---">
    
    public static int ceil(@NotNull Number val1, @NotNull Number val2) {
        return (int) Math.ceil(val1.doubleValue() / val2.doubleValue());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- TRIGONOMETRY ---">
    
    public static double degreesToRads(@NotNull Number angle) { return angle.doubleValue() * (Math.PI / 180); }
    
    public static double radsToDegrees(@NotNull Number angle, boolean normalize) {
        final double ang = angle.doubleValue() * (180 / Math.PI);
        return normalize ? normalizeAngle(ang) : ang;
    }
    public static double radsToDegrees(@NotNull Number angle) { return radsToDegrees(angle, false); }
    
    //<editor-fold desc="> Circle Methods">
    
    //    public static @NotNull NumberValuePair pointOnCircle(@NotNull Number x, @NotNull Number y, @NotNull Number radius, @NotNull Number degrees) {
    //        final double pX = (Math.cos(degreesToRads(degrees)) * radius.doubleValue()) + x.doubleValue();
    //        final double pY = (Math.sin(degreesToRads(degrees)) * radius.doubleValue()) + y.doubleValue();
    //        return new NumberValuePair(pX, pY);
    //    }
    //    public static @NotNull NumberValuePair pointOnCircle(@NotNull NumberValuePairable<?> offset, @NotNull Number radius, @NotNull Number degrees) { return pointOnCircle(offset.a(), offset.b(), radius, degrees); }
    //    public static @NotNull NumberValuePair pointOnCircle(@NotNull Number radius, @NotNull Number degrees) { return pointOnCircle(0, 0, radius, degrees); }
    
    //
    
    public static @NotNull Point2D point2D(@NotNull Number pX, @NotNull Number pY) { return new Point2D(pX.doubleValue(), pY.doubleValue()); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- SHAPES ---">
    
    public static @NotNull Box boundsBox(@NotNull Springable springable, @Nullable Lock lock,
                                         @Nullable BiFunction<NumExpr2D<?>, NumExpr2D<?>, javafx.scene.paint.Color> pixelGenerator,
                                         @NotNull List<Shape> inputs) {
        final ArrayList<Shape> shapes = new ArrayList<>(inputs);
        
        if (inputs.isEmpty())
            return new Box(springable);
        
        double minLocX = Integer.MAX_VALUE;
        double maxLocX = Integer.MIN_VALUE;
        
        double minLocY = Integer.MAX_VALUE;
        double maxLocY = Integer.MIN_VALUE;
        
        for (Shape s: shapes) {
            final double minPointX = s.getLocation(Axis.X_AXIS, LocType.MIN);
            final double maxPointX = s.getLocation(Axis.X_AXIS, LocType.MAX);
            
            final double minPointY = s.getLocation(Axis.Y_AXIS, LocType.MIN);
            final double maxPointY = s.getLocation(Axis.Y_AXIS, LocType.MAX);
            
            if (minPointX < minLocX)
                minLocX = minPointX;
            if (maxPointX > maxLocX)
                maxLocX = maxPointX;
            
            if (minPointY < minLocY)
                minLocY = minPointY;
            if (maxPointY > maxLocY)
                maxLocY = maxPointY;
        }
        
        final double width = maxLocX - minLocX;
        final double height = maxLocY - minLocY;
        
        return new Box(springable, lock, minLocX, minLocY, width, height, LocType.MIN, pixelGenerator);
    }
    
    //</editor-fold>
    
    //</editor-fold>
}