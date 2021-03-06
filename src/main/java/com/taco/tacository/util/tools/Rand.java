package com.taco.tacository.util.tools;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A utility for generating pseudo-random numbers.
 */
public final class Rand {
    
    /**
     * {@link Random} instance unique to the current {@link Thread} as defined by {@link ThreadLocalRandom#current()}.
     */
    public static final String THREAD_SOURCE = "thread";
    /**
     * Singleton {@link Random} instance unique to the current runtime environment.
     */
    public static final String GLOBAL_SOURCE = "global";
    /**
     * Method-local {@link Random} instance constructed upon method call and destroyed upon method completion.
     */
    public static final String METHOD_SOURCE = "method";
    
    private static final Random random;
    private static String source;
    
    static {
        random = new Random();
        source = GLOBAL_SOURCE;
    }
    
    private Rand() { } //No Instance
    
    public static Random getRandom() {
        return random;
    }
    
    public static Random getRandomBySource(String source) {
        switch (source) {
            case Rand.GLOBAL_SOURCE -> {
                return random;
            }
            case Rand.THREAD_SOURCE -> {
                return ThreadLocalRandom.current();
            }
            case Rand.METHOD_SOURCE -> {
                return new Random();
            }
        }
        return null;
    }
    
    public static String getSource() {
        return source;
    }
    
    public static void setSource(String source) {
        Rand.source = source;
    }
    
    //
    
    // <editor-fold desc="Boolean">
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code boolean} value.
     *
     * @return {@code boolean}
     *
     * @see Random#nextBoolean()
     */
    public static boolean nextBoolean() {
        return getRandom().nextBoolean();
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Integer">
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code int} in the range
     * [-2^<sup>31</sup>, 2<sup>31</sup>).
     *
     * @return {@code int}
     *
     * @see Random#nextInt()
     */
    public static int nextInt() {
        return getRandom().nextInt();
    }
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code int} in the range
     * [0, {@code bound}) if {@code bound} is non-negative, or ({@code bound},
     * 0] if {@code bound} is negative.
     *
     * @param bound The lower or upper bound (exclusive).
     *
     * @return {@code int}
     *
     * @see Random#nextInt()
     */
    public static int nextInt(final int bound) {
        return getRandom().nextInt(bound >= 0 ? bound : -bound);
    }
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code int} in the range
     * [{@code min}, {@code max}).
     *
     * @param min The lower bound (inclusive).
     * @param max The upper bound (exclusive).
     *
     * @return {@code int}
     *
     * @throws IllegalArgumentException When {@code min} > {@code max}.
     * @see Random#nextInt(int)
     */
    public static int nextInt(final int min, final int max) {
        final int n = max - min;
        if (n < 0)
            throw new IllegalArgumentException(
                    "Max must be greater than or equal to min (Min: " + min + ", Max: " + max + ").");
        
        return min + (n == 0 ? 0 : getRandom().nextInt(n));
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Double">
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code double} in the
     * range [0.0, 1.0).
     *
     * @return {@code double}
     *
     * @see Random#nextDouble()
     */
    public static double nextDouble() {
        return getRandom().nextDouble();
    }
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code double} in the
     * range [0.0, {@code bound}) if {@code bound} is non-negative, or (
     * {@code bound}, 0.0] if {@code bound} is negative.
     *
     * @param bound The lower or upper bound (exclusive).
     *
     * @return {@code double}
     *
     * @see Random#nextDouble()
     */
    public static double nextDouble(final double bound) {
        return getRandom().nextDouble() * bound;
    }
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code double} in the
     * range [{@code min}, {@code max}).
     *
     * @param min The lower bound (inclusive).
     * @param max The upper bound (exclusive).
     *
     * @return {@code double}
     *
     * @throws IllegalArgumentException When {@code min} > {@code max}.
     * @see Random#nextDouble()
     */
    public static double nextDouble(final double min, final double max) {
        final double n = max - min;
        if (n < 0)
            throw new IllegalArgumentException(
                    "Max must be greater than or equal to min (Min: " + min + ", Max: " + max + ").");
        
        return min + (n == 0 ? 0 : getRandom().nextDouble() * n);
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Gaussian">
    
    /**
     * Generates a pseudo-random, normally distributed {@code double} about the
     * specified {@code mean} with the specified standard deviation, {@code sd},
     * in the range [{@code min}, {@code max}].
     *
     * @param min  The minimum value (inclusive).
     * @param max  The maximum value (inclusive).
     * @param mean The mean ({@code >=} min and {@code <=} max).
     * @param sd   The standard deviation.
     *
     * @return The random integer ({@code >=} min and {@code <=} max).
     */
    public static double nextGaussian(final double min, final double max, final double mean, final double sd) {
        if (!(mean >= min && mean <= max))
            throw new IllegalArgumentException(
                    "Invalid range (Min: " + min + ", Mean: " + mean + ", Max: " + max + ").");
        
        double rand;
        
        final Random random = getRandom();
        
        do {
            rand = random.nextGaussian() * sd + mean;
        }
        while (rand < min || rand > max);
        
        return rand;
    }
    
    /**
     * Generates a pseudo-random, normally distributed {@code double} about a
     * mean of betwen {@code min} and {@code max} with the specified standard
     * deviation, {@code sd}, in the range [{@code min}, {@code max}].
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (inclusive).
     * @param sd  The standard deviation.
     *
     * @return The random integer ({@code >=} min and {@code <=} max).
     */
    public static double nextGaussian(final double min, final double max, final double sd) {
        final double mean = min + (max - min) / 2;
        
        return nextGaussian(min, max, mean, sd);
    }
    
    /**
     * Generates a pseudo-random, normally distributed {@code double} about the
     * specified {@code mean} with the specified standard deviation, {@code sd}.
     *
     * @param mean The mean ({@code <=} min and {@code <=} max).
     * @param sd   The standard deviation.
     *
     * @return The random integer ({@code >=} min and {@code <=} max).
     */
    public static double nextGaussian(final double mean, final double sd) {
        return getRandom().nextGaussian() * sd + mean;
    }
    
    /**
     * Generates a bounded pseudo-random skewed {@code double}.
     *
     * @param min  The minimum bound (inclusive).
     * @param max  The maximum bound (exclusive).
     * @param skew The degree to which the values cluster around the mode of the
     *             distribution; higher values mean tighter clustering.
     * @param bias The tendency of the mode to approach the {@code min},
     *             {@code max} or midpoint value; positive values bias toward
     *             {@code max}, negative values toward {@code min}.
     *
     * @return {@code double}
     */
    public static double nextSkewedBoundedDouble(
            final double min, final double max, final double skew,
            final double bias) {
        final double range = max - min;
        final double mid = min + range / 2.0;
        final double bias_factor = Math.exp(bias);
        
        return mid + (range * (bias_factor / (bias_factor + Math.exp(-getRandom().nextGaussian() / skew)) - 0.5));
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Any Number">
    
    public static double next(final Number bound) {
        if (bound.longValue() <= Integer.MAX_VALUE) { // Ensure bound is not a
            // long
            double decimal = bound.doubleValue() - bound.intValue();
            if (decimal == 0) // Ensure bound is not a double
                return nextInt(bound.intValue(), bound.intValue());
        }
        return nextDouble(bound.doubleValue(), bound.doubleValue());
    }
    
    public static double next(final Number min, final Number max) {
        if (min.longValue() <= Integer.MAX_VALUE && max.longValue() <= Integer.MAX_VALUE) {
            double min_decimal = min.doubleValue() - min.intValue();
            double max_decimal = max.doubleValue() - max.intValue();
            if (min_decimal == 0 && max_decimal == 0)
                return nextInt(min.intValue(), max.intValue());
        }
        return nextDouble(min.doubleValue(), max.doubleValue());
    }
    
    // </editor-fold>
    
    /**
     * <p>Returns a random element from the specified array.</p>
     *
     * @param arr The input array.
     * @param <T> The type of elements in the array.
     *
     * @return A random element from the specified array, or null if the array is empty.
     *
     * @throws NullPointerException If the specified array is {@code null}.
     */
    public static <T> @Nullable T getRandomElement(@NotNull T[] arr) {
        if (Exc.nullCheck(arr, "Input Array").length == 0)
            return null;
        return arr[nextInt(arr.length - 1)];
    }
}