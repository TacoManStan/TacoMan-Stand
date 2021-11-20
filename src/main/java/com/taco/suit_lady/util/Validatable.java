package com.taco.suit_lady.util;

import java.io.Serializable;

public interface Validatable<T>
		extends Serializable {

	/**
	 * Checks if the specified value is valid.
	 * <br><br>
	 * This method is oftentimes executed inside of a loop.
	 * It is therefore recommended to avoid the following when using this method (when possible):
	 * <ol>
	 * <li>Lengthy operations</li>
	 * <li>CPU intensive operations</li>
	 * <li>The modification of any external values based on this method's result</li>
	 * </ol>
	 *
	 * @param t The object that is being validated.
	 * @return True if the specified object is valid based on this {@link Validatable}, false otherwise.
	 */
	boolean validate(T t);

	/**
	 * Executes any post-validation cleanup operations.
	 * <p>
	 * This method does <i>not</i> always run directly after the {@link #validate(Object) validate(T)} method is finished executing.
	 * However, it can be assumed that this method will be executed on a <i>single</i> {@link Validatable} object after an operation is completed.
	 * <p>
	 * The default implementation of this method always returns true.
	 *
	 * @param t        The object that was validated by {@link #validate(Object) validate(T)}.
	 * @param wasValid True if the result returned by {@link #validate(Object) validate(T)} was true, false otherwise.
	 * @return True if the post validation process was successful, false otherwise.
	 * <p>
	 * {@code True} by default.
	 */
	default boolean postValidation(T t, boolean wasValid) {
		return true;
	}
}

/*
 * TODO:
 * [S]. Clarify documentation.
 */