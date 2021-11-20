package com.taco.suit_lady.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A utility for generating pseudo-random numbers.
 */
public class RandomTools
{
    public static RandomTools get()
    {
        return TB.random();
    }
    
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
    
    private final Random random;
    private String source;
    
    RandomTools()
    {
        this.random = new Random();
        this.source = GLOBAL_SOURCE;
    }
    
    public Random getRandom()
    {
        return this.random;
    }
    
    public Random getRandomBySource(String source)
    {
        switch(source)
        {
            case RandomTools.GLOBAL_SOURCE -> {
                return this.random;
            }
            case RandomTools.THREAD_SOURCE -> {
                return ThreadLocalRandom.current();
            }
            case RandomTools.METHOD_SOURCE -> {
                return new Random();
            }
        }
        return null;
    }
    
    public String getSource()
    {
        return this.source;
    }
    
    public void setSource(String source)
    {
        this.source = source;
    }
    
    //
    
    // <editor-fold desc="Boolean">
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code boolean} value.
     *
     * @return {@code boolean}
     * @see Random#nextBoolean()
     */
    public boolean nextBoolean()
    {
        return getRandom().nextBoolean();
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Integer">
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code int} in the range
     * [-2^<sup>31</sup>, 2<sup>31</sup>).
     *
     * @return {@code int}
     * @see Random#nextInt()
     */
    public int nextInt()
    {
        return getRandom().nextInt();
    }
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code int} in the range
     * [0, {@code bound}) if {@code bound} is non-negative, or ({@code bound},
     * 0] if {@code bound} is negative.
     *
     * @param bound The lower or upper bound (exclusive).
     * @return {@code int}
     * @see Random#nextInt()
     */
    public int nextInt(final int bound)
    {
        return getRandom().nextInt(bound >= 0 ? bound : -bound);
    }
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code int} in the range
     * [{@code min}, {@code max}).
     *
     * @param min The lower bound (inclusive).
     * @param max The upper bound (exclusive).
     * @return {@code int}
     * @throws IllegalArgumentException When {@code min} > {@code max}.
     * @see Random#nextInt(int)
     */
    public int nextInt(final int min, final int max)
    {
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
     * @see Random#nextDouble()
     */
    public double nextDouble()
    {
        return getRandom().nextDouble();
    }
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code double} in the
     * range [0.0, {@code bound}) if {@code bound} is non-negative, or (
     * {@code bound}, 0.0] if {@code bound} is negative.
     *
     * @param bound The lower or upper bound (exclusive).
     * @return {@code double}
     * @see Random#nextDouble()
     */
    public double nextDouble(final double bound)
    {
        return getRandom().nextDouble() * bound;
    }
    
    /**
     * Generates a pseudo-random, uniformly distributed {@code double} in the
     * range [{@code min}, {@code max}).
     *
     * @param min The lower bound (inclusive).
     * @param max The upper bound (exclusive).
     * @return {@code double}
     * @throws IllegalArgumentException When {@code min} > {@code max}.
     * @see Random#nextDouble()
     */
    public double nextDouble(final double min, final double max)
    {
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
     * @return The random integer ({@code >=} min and {@code <=} max).
     */
    public double nextGaussian(final double min, final double max, final double mean, final double sd)
    {
        if (!(mean >= min && mean <= max))
            throw new IllegalArgumentException(
                    "Invalid range (Min: " + min + ", Mean: " + mean + ", Max: " + max + ").");
        
        double rand;
        
        final Random random = getRandom();
        
        do
        {
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
     * @return The random integer ({@code >=} min and {@code <=} max).
     */
    public double nextGaussian(final double min, final double max, final double sd)
    {
        final double mean = min + (max - min) / 2;
        
        return nextGaussian(min, max, mean, sd);
    }
    
    /**
     * Generates a pseudo-random, normally distributed {@code double} about the
     * specified {@code mean} with the specified standard deviation, {@code sd}.
     *
     * @param mean The mean ({@code <=} min and {@code <=} max).
     * @param sd   The standard deviation.
     * @return The random integer ({@code >=} min and {@code <=} max).
     */
    public double nextGaussian(final double mean, final double sd)
    {
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
     * @return {@code double}
     */
    public double nextSkewedBoundedDouble(
            final double min, final double max, final double skew,
            final double bias)
    {
        final double range = max - min;
        final double mid = min + range / 2.0;
        final double bias_factor = Math.exp(bias);
        
        return mid + (range * (bias_factor / (bias_factor + Math.exp(-getRandom().nextGaussian() / skew)) - 0.5));
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Any Number">
    
    public double next(final Number bound)
    {
        if (bound.longValue() <= Integer.MAX_VALUE)
        { // Ensure bound is not a
            // long
            double decimal = bound.doubleValue() - bound.intValue();
            if (decimal == 0) // Ensure bound is not a double
                return nextInt(bound.intValue(), bound.intValue());
        }
        return nextDouble(bound.doubleValue(), bound.doubleValue());
    }
    
    public double next(final Number min, final Number max)
    {
        if (min.longValue() <= Integer.MAX_VALUE && max.longValue() <= Integer.MAX_VALUE)
        {
            double min_decimal = min.doubleValue() - min.intValue();
            double max_decimal = max.doubleValue() - max.intValue();
            if (min_decimal == 0 && max_decimal == 0)
                return nextInt(min.intValue(), max.intValue());
        }
        return nextDouble(min.doubleValue(), max.doubleValue());
    }
    
    // </editor-fold>
}