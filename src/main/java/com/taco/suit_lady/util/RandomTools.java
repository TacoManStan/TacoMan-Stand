package com.taco.suit_lady.util;

import com.taco.suit_lady.util.timing.Timer;
import com.taco.suit_lady.util.timing.Timers;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A utility for generating pseudo-random numbers.
 */
public final class RandomTools
{
	private RandomTools() {
	} // No instance

	// public static final java.util.Random Random.random() = new
	// java.util.Random();

	public static Random random() {
		return ThreadLocalRandom.current();
	}

	//

	// <editor-fold desc="Boolean">

	/**
	 * Generates a pseudo-random, uniformly distributed {@code boolean} value.
	 *
	 * @return {@code boolean}
	 *
	 * @see {@link Random#nextBoolean()}
	 */
	public static boolean nextBoolean() {
		return RandomTools.random().nextBoolean();
	}

	// </editor-fold>

	// <editor-fold desc="Integer">

	/**
	 * Generates a pseudo-random, uniformly distributed {@code int} in the range
	 * [-2^<sup>31</sup>, 2<sup>31</sup>).
	 *
	 * @return {@code int}
	 *
	 * @see {@link Random#nextInt()}
	 */
	public static int nextInt() {
		return RandomTools.random().nextInt();
	}

	/**
	 * Generates a pseudo-random, uniformly distributed {@code int} in the range
	 * [0, {@code bound}) if {@code bound} is non-negative, or ({@code bound},
	 * 0] if {@code bound} is negative.
	 *
	 * @param bound
	 *            The lower or upper bound (exclusive).
	 * @return {@code int}
	 *
	 * @see {@link Random#nextInt()}
	 */
	public static int nextInt(final int bound) {
		return RandomTools.random().nextInt(bound >= 0 ? bound : -bound);
	}

	/**
	 * Generates a pseudo-random, uniformly distributed {@code int} in the range
	 * [{@code min}, {@code max}).
	 *
	 * @param min
	 *            The lower bound (inclusive).
	 * @param max
	 *            The upper bound (exclusive).
	 * @return {@code int}
	 *
	 * @throws IllegalArgumentException
	 *             When {@code min} > {@code max}.
	 * @see {@link Random#nextInt(int)}
	 */
	public static int nextInt(final int min, final int max) {
		final int n = max - min;
		if (n < 0)
			throw new IllegalArgumentException(
					"Max must be greater than or equal to min (Min: " + min + ", Max: " + max + ").");

		return min + (n == 0 ? 0 : RandomTools.random().nextInt(n));
	}

	// </editor-fold>

	// <editor-fold desc="Double">

	/**
	 * Generates a pseudo-random, uniformly distributed {@code double} in the
	 * range [0.0, 1.0).
	 *
	 * @return {@code double}
	 *
	 * @see {@link Random#nextDouble()}
	 */
	public static double nextDouble() {
		return RandomTools.random().nextDouble();
	}

	/**
	 * Generates a pseudo-random, uniformly distributed {@code double} in the
	 * range [0.0, {@code bound}) if {@code bound} is non-negative, or (
	 * {@code bound}, 0.0] if {@code bound} is negative.
	 *
	 * @param bound
	 *            The lower or upper bound (exclusive).
	 * @return {@code double}
	 *
	 * @see {@link Random#nextDouble()}
	 */
	public static double nextDouble(final double bound) {
		return RandomTools.random().nextDouble() * bound;
	}

	/**
	 * Generates a pseudo-random, uniformly distributed {@code double} in the
	 * range [{@code min}, {@code max}).
	 *
	 * @param min
	 *            The lower bound (inclusive).
	 * @param max
	 *            The upper bound (exclusive).
	 * @return {@code double}
	 *
	 * @throws IllegalArgumentException
	 *             When {@code min} > {@code max}.
	 * @see {@link Random#nextDouble()}
	 */
	public static double nextDouble(final double min, final double max) {
		final double n = max - min;
		if (n < 0)
			throw new IllegalArgumentException(
					"Max must be greater than or equal to min (Min: " + min + ", Max: " + max + ").");

		return min + (n == 0 ? 0 : RandomTools.random().nextDouble() * n);
	}

	// </editor-fold>

	// <editor-fold desc="Gaussian">

	/**
	 * Generates a pseudo-random, normally distributed {@code double} about the
	 * specified {@code mean} with the specified standard deviation, {@code sd},
	 * in the range [{@code min}, {@code max}].
	 *
	 * @param min
	 *            The minimum value (inclusive).
	 * @param max
	 *            The maximum value (inclusive).
	 * @param mean
	 *            The mean (>= min and <= max).
	 * @param sd
	 *            The standard deviation.
	 * @return The random integer (>= min and <= max).
	 */
	public static double nextGaussian(final double min, final double max, final double mean, final double sd) {
		if (!(mean >= min && mean <= max))
			throw new IllegalArgumentException(
					"Invalid range (Min: " + min + ", Mean: " + mean + ", Max: " + max + ").");

		double rand;
		
		final Random random = RandomTools.random();

		do {
			rand = random.nextGaussian() * sd + mean;
		} while (rand < min || rand > max);

		return rand;
	}

	/**
	 * Generates a pseudo-random, normally distributed {@code double} about a
	 * mean of betwen {@code min} and {@code max} with the specified standard
	 * deviation, {@code sd}, in the range [{@code min}, {@code max}].
	 *
	 * @param min
	 *            The minimum value (inclusive).
	 * @param max
	 *            The maximum value (inclusive).
	 * @param sd
	 *            The standard deviation.
	 * @return The random integer (>= min and <= max).
	 */
	public static double nextGaussian(final double min, final double max, final double sd) {
		final double mean = min + (max - min) / 2;

		return RandomTools.nextGaussian(min, max, mean, sd);
	}

	/**
	 * Generates a pseudo-random, normally distributed {@code double} about the
	 * specified {@code mean} with the specified standard deviation, {@code sd}.
	 *
	 * @param mean
	 *            The mean (>= min and <= max).
	 * @param sd
	 *            The standard deviation.
	 * @return The random integer (>= min and <= max).
	 */
	public static double nextGaussian(final double mean, final double sd) {
		return RandomTools.random().nextGaussian() * sd + mean;
	}

	/**
	 * Generates a bounded pseudo-random skewed {@code double}.
	 * 
	 * @param min
	 *            The minimum bound (inclusive).
	 * @param max
	 *            The maximum bound (exclusive).
	 * @param skew
	 *            The degree to which the values cluster around the mode of the
	 *            distribution; higher values mean tighter clustering.
	 * @param bias
	 *            The tendency of the mode to approach the {@code min},
	 *            {@code max} or midpoint value; positive values bias toward
	 *            {@code max}, negative values toward {@code min}.
	 * 
	 * @return {@code double}
	 */
	public static double nextSkewedBoundedDouble(final double min, final double max, final double skew,
			final double bias) {
		final double range = max - min;
		final double mid = min + range / 2.0;
		final double bias_factor = Math.exp(bias);

		return mid + (range * (bias_factor / (bias_factor + Math.exp(-RandomTools.random().nextGaussian() / skew)) - 0.5));
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
		if (min.longValue() <= Integer.MAX_VALUE && max.longValue() <= Integer.MAX_VALUE) { // Ensure
																							// both
																							// numbers
																							// are
																							// not
																							// longs
			double min_decimal = min.doubleValue() - min.intValue();
			double max_decimal = max.doubleValue() - max.intValue();
			if (min_decimal == 0 && max_decimal == 0) // Ensure both numbers are
														// not doubles
				return nextInt(min.intValue(), max.intValue());
		}
		return nextDouble(min.doubleValue(), max.doubleValue());
	}

	// </editor-fold>

	public static void main(String[] args) {
		final int iterations = 100000000; // 100 million iterations
		final int min = 0;
		final int max = 10000;

		//

		// Random.next test
		Timer timer = Timers.newStopwatchTimer().startAndGet();
		for (int i = 0; i < iterations; i++)
			RandomTools.next(min, max);
		timer.stop();
//		ConsoleBB.CONSOLE.dev("next(min, max): " + timer.getElapsedTime() + "ms");

		// Random.nextInt test
		timer = Timers.newStopwatchTimer().startAndGet();
		for (int i = 0; i < iterations; i++)
			RandomTools.nextInt(min, max);
		timer.stop();
//		ConsoleBB.CONSOLE.dev("nextInt(min, max): " + timer.getElapsedTime() + "ms");

		// Random.nextDouble test
		timer = Timers.newStopwatchTimer().startAndGet();
		for (int i = 0; i < iterations; i++)
			RandomTools.nextDouble(0, max);
		timer.stop();
//		ConsoleBB.CONSOLE.dev("nextDouble(0, 1000): " + timer.getElapsedTime() + "ms");
	}
}