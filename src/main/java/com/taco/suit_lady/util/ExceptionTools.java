package com.taco.suit_lady.util;

import com.taco.util.quick.ConsoleBB;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ExceptionTools
{

	/* *************************************************************************** *
	 *                                                                             *
	 * Static                                                                      *
	 *                                                                             *
	 * *************************************************************************** */

	/**
	 * The default message that is displayed if an empty-args Exception is thrown.
	 * <p>
	 * It is sometimes a good idea to change this variable to give a more specific default error message.
	 */
	public static String default_message = null;


	/* *************************************************************************** *
	 *                                                                             *
	 * Checks                                                                      *
	 *                                                                             *
	 * *************************************************************************** */

	public static <T> T check(T obj, Predicate<T> condition) {
		ExceptionTools.nullCheck(condition, "Condition");
		return check(obj, condition, () -> new RuntimeException());
	}

	public static <T> T check(T obj, Predicate<T> condition, String message) {
		ExceptionTools.nullCheck(condition, "Condition");
		ExceptionTools.nullCheck(message, "Message");
		return check(obj, condition, () -> new RuntimeException(message));
	}

	public static <T> T check(T obj, Predicate<T> condition, Supplier<RuntimeException> exceptionSupplier) {
		ExceptionTools.nullCheck(condition, "Condition");
		ExceptionTools.nullCheck(exceptionSupplier, "Exception Supplier");
		if (condition.test(obj))
			throw ExceptionTools.ex(exceptionSupplier.get());
		return obj;
	}


	/* *************************************************************************** *
	 *                                                                             *
	 * Null Checks                                                                 *
	 *                                                                             *
	 * *************************************************************************** */

	/**
	 * Checks if the specified {@code Object} is non-null.
	 * <br>
	 * If the specified {@code Object}, a {@link NullPointerException} with no error message (default constructor) is thrown.
	 *
	 * @param obj The {@code Object} being null-checked.
	 * @param <T> The generic type of {@code obj}.
	 * @return The specified {@code Object} (if non-null).
	 *
	 * @see #nullCheck(Object, String)
	 */
	public static <T> T nullCheck(T obj, String name) {
		return nullCheck(obj, name, null);
	}

	/**
	 * Checks if the specified {@code Object} is not null.
	 * <br>
	 * If the specified {@code Object} is null, a {@link NullPointerException} with the specified {@code error message} is thrown.
	 *
	 * @param obj  The {@code Object} being null-checked.
	 * @param name The {@code name} of the {@code Object} being null-checked.
	 *             <ol>
	 *             <li>If {@code name} <i>is</i> null, the {@code NullPointerException}, if thrown, will not have an error message.</li>
	 *             <li>If {@code name} <i>is not</i> null, the {@code NullPointerException} that is thrown will specify the {@code name} in its error message.</li>
	 *             </ol>
	 * @param info Any additional information that would be useful to print in the {@code error message}.
	 *             <br>
	 *             Specify null if no additional information is required.
	 * @param <T>  The generic type of {@code obj}.
	 * @return The specified {@code Object} (if non-null).
	 *
	 * @see #nullCheckMessage(Object, String)
	 */
	public static <T> T nullCheck(T obj, String name, String info) {
		// TODO [S]: Move info to custom ConsoleErrorMessage parameter, and then only show when expanded.
		return nullCheckMessage(obj, "'" + name + "' cannot be null!" + (info == null ? "" : " (" + info + ")"));
	}

	/**
	 * Checks if the specified {@code Object} is not null.
	 * <br>
	 * If the specified {@code Object} is null, a {@link NullPointerException} with the specified {@code error message} is thrown.
	 *
	 * @param obj          The {@code Object} being null-checked.
	 * @param errorMessage The {@code message} to be set as the {@code error message} of the {@code NullPointerException}, if thrown.
	 * @param <T>          The generic type of {@code obj}.
	 * @return The specified {@code Object} (if non-null).
	 *
	 * @see #nullCheck(Object, String)
	 */
	public static <T> T nullCheckMessage(T obj, String errorMessage) {
		if (obj == null)
			if (errorMessage == null)
				throw ex(new NullPointerException());
			else
				throw ex(new NullPointerException(errorMessage));
		return obj;
	}

	//<editor-fold desc="Primitive Wrapper Null Checks">

	/**
	 * Checks if the specified {@link Boolean} value is null.
	 * <br>
	 * If the specified {@code Boolean} is null, a {@link NullPointerException} is thrown.
	 * <p>
	 * Equivalent to {@code ExceptionTools.nullCheck(value, null)}.
	 *
	 * @param value The {@code Boolean} being null-checked.
	 * @return The specified {@code Boolean} (if non-null).
	 *
	 * @see #nullCheck(Object, String)
	 * @see #nullCheck(Boolean, String)
	 */
	public static boolean nullCheck(Boolean value) {
		return nullCheck(value, null);
	}

	/**
	 * Checks if the specified {@link Boolean} value is null.
	 * <br>
	 * If the specified {@code Boolean} is null, a {@link NullPointerException} is thrown.
	 *
	 * @param value The {@code Boolean} being null-checked.
	 * @param name  Any additional information that would be useful to print in the {@code error message}.
	 *              <br>
	 *              Specify null if no additional information is required.
	 * @return The specified {@code Boolean} (if non-null).
	 *
	 * @see #nullCheck(Object, String)
	 */
	public static boolean nullCheck(Boolean value, String name) {
		return nullCheckPrimitiveWrapper(value, name);
	}

	//

	/**
	 * Checks if the specified {@link Character} wrapper is null.
	 * <br>
	 * If the specified {@code Character} is null, a {@link NullPointerException} is thrown.
	 * <p>
	 * Equivalent to {@code ExceptionTools.nullCheck(value, null)}.
	 *
	 * @return The specified {@code Character} (if non-null).
	 *
	 * @see #nullCheck(Object, String)
	 * @see #nullCheck(Character, String)
	 */
	public static char nullCheck(Character value) {
		return nullCheck(value, null);
	}

	/**
	 * Checks if the specified {@link Character} wrapper is null.
	 * <br>
	 * If the specified {@code Character} is null, a {@link NullPointerException} is thrown.
	 *
	 * @param value The {@code Character} being null-checked.
	 * @param name  Any additional information that would be useful to print in the {@code error message}.
	 *              <br>
	 *              Specify null if no additional information is required.
	 * @return The specified {@code Character} (if non-null).
	 *
	 * @see #nullCheck(Object, String)
	 */
	public static char nullCheck(Character value, String name) {
		return nullCheckPrimitiveWrapper(value, name);
	}

	//

	/**
	 * Checks if the specified {@link Number} wrapper is null.
	 * <br>
	 * If the specified {@code Number} is null, a {@link NullPointerException} is thrown.
	 * <p>
	 * Equivalent to {@code ExceptionTools.nullCheck(value, null)}.
	 *
	 * @return The specified {@code Number} (if non-null).
	 *
	 * @see #nullCheck(Object, String)
	 * @see #nullCheck(Number, String)
	 */
	public static <T extends Number> T nullCheck(T value) {
		return nullCheck(value, null);
	}

	/**
	 * Checks if the specified {@link Number} wrapper is null.
	 * <br>
	 * If the specified {@code Number} is null, a {@link NullPointerException} is thrown.
	 *
	 * @param value The {@code Number} being null-checked.
	 * @param name  Any additional information that would be useful to print in the {@code error message}.
	 *              <br>
	 *              Specify null if no additional information is required.
	 * @return The specified {@code Number} (if non-null).
	 *
	 * @see #nullCheck(Object, String)
	 */
	public static <T extends Number> T nullCheck(T value, String name) {
		return nullCheckPrimitiveWrapper(value, name);
	}

	//

	/**
	 * Helper method to assist primitive wrapper null-checks.
	 */
	private static <T> T nullCheckPrimitiveWrapper(T value, String name) {
		return nullCheck(value, "Primitive wrappers cannot be null!", name);
	}

	//</editor-fold>


	/* *************************************************************************** *
	 *                                                                             *
	 * Bounds Checks                                                               *
	 *                                                                             *
	 * *************************************************************************** */

	// NOTE TO DEV: Bounds checks are always INCLUSIVE by default.

	public static <T extends Number> T boundsCheck(T value, double floor, double ceiling, String valueName) {
		return boundsCheck(value, floor, ceiling, true, true, valueName);
	}

	/**
	 * Checks if the specified {@code value} is within the specified {@code bounds} defined by the specified {@code floor} and {@code ceiling}.
	 * <br>
	 * If the specified {@code value} is not within the specified bounds, an {@link IndexOutOfBoundsException} is thrown.
	 * <p>
	 *
	 * <b>Example:</b>
	 * <blockquote><pre>
	 * <code>long myNumber = 1000;
	 * int number = check(myNumber, -50, 750, "My Number");</code></pre></blockquote>
	 *
	 * The example above does the following:
	 * <ol>
	 * <li>Creates a variable of type {@code long} equal to {@code 1000} called {@code myVariable}.</li>
	 * <li>Uses the {@link ExceptionTools#boundsCheck(Number, double, double, String) check} method to check if the bounds of {@code myVariable} are between -50 and 750.</li>
	 * <li>Determines that {@code myVariable} is not within the specified bounds ({@code 750 is less than 1000}</li>
	 * <li>Throws a {@code IndexOutOfBoundsException}</li>
	 * </ol>
	 *
	 * This method will never return null.
	 *
	 * @param value     The {@code value} being bounds-checked.
	 * @param valueName The name of the {@code value}.
	 * @return The specified {@code value} (if the bounds-check is successful).
	 */
	public static <T extends Number> T boundsCheck(T value, double floor, double ceiling, boolean isFloorInclusive, boolean isCeilingInclusive, String valueName) {
		ExceptionTools.nullCheck(value, "Value");
		final String _valueString = valueName != null ? valueName + " (" + value + ")" : "" + value;
		if (isFloorInclusive && value.doubleValue() < floor)
			throw ex(new IndexOutOfBoundsException(_valueString + " must be greater than or equal to " + floor + "."));
		else if (!isFloorInclusive && value.doubleValue() <= floor)
			throw ex(new IndexOutOfBoundsException(_valueString + " must be greater than " + floor + "."));
		else if (isCeilingInclusive && value.doubleValue() > ceiling)
			throw ex(new IndexOutOfBoundsException(_valueString + " must be less than or equal to " + ceiling + "."));
		else if (!isCeilingInclusive && value.doubleValue() >= ceiling)
			throw ex(new IndexOutOfBoundsException(_valueString + " must be less than " + ceiling + "."));
		return value;
	}

	// Bounds Check - Zero

	public static <T extends Number> T boundsCheckZero(T value, String valueName) {
		return boundsCheckZero(value, Double.MAX_VALUE, true, true, valueName);
	}

	public static <T extends Number> T boundsCheckZero(T value, double ceiling, String valueName) {
		return boundsCheckZero(value, ceiling, true, true, valueName);
	}

	public static <T extends Number> T boundsCheckZero(T value, double ceiling, boolean isFloorInclusive, boolean isCeilingInclusive, String valueName) {
		return boundsCheck(value, 0, ceiling, isFloorInclusive, isCeilingInclusive, valueName);
	}


	/* *************************************************************************** *
	 *                                                                             *
	 * Create & Return Exceptions                                                  *
	 *                                                                             *
	 * *************************************************************************** */

	// Unsupported & NYI

	public static RuntimeException unsupported() {
		return unsupported("Unsupported operation. See stack trace for details.");
	}

	public static RuntimeException unsupported(String message) {
		return ex(new UnsupportedOperationException(), message);
	}

	public static RuntimeException nyi() {
		return unsupported("Operation is not yet implemented.");
	}

	// Read-Only

	public static RuntimeException readOnly() {
		return readOnly("This object is read-only.");
	}

	public static RuntimeException readOnly(String name) {
		return unsupported(name + " is read-only.");
	}

	// Abstract Method Error

	public static RuntimeException abstractMethodError(String methodName, String message) {
		return ex(new AbstractMethodError("Abstract method \"" + methodName + "\" requires implementation."), message);
	}


	/* *************************************************************************** *
	 *                                                                             *
	 * Ex & Internal                                                               *
	 *                                                                             *
	 * *************************************************************************** */

	public static RuntimeException ex() {
		return ex(null, null);
	}

	public static RuntimeException ex(String message) {
		return ex(null, message);
	}

	public static RuntimeException ex(Throwable cause) {
		return ex(cause, null);
	}

	public static RuntimeException ex(Throwable cause, String message) {
		return hlp_getException(cause, message, ExceptionType.RUNTIME);
	}

	//

	public static InternalException internal() {
		return internal(null, null);
	}

	public static InternalException internal(String message) {
		return internal(null, message);
	}

	public static InternalException internal(Throwable cause) {
		return internal(cause, null);
	}

	public static InternalException internal(Throwable cause, String message) {
		return (InternalException) hlp_getException(cause, message, ExceptionType.INTERNAL);
	}


	/* *************************************************************************** *
	 *                                                                             *
	 * Print                                                                       *
	 *                                                                             *
	 * *************************************************************************** */

	public static void print(Object message) {
		ConsoleBB.CONSOLE.print(message.toString());
	} //TODO

	public static void print(Object message, Exception exception) {
		ConsoleBB.CONSOLE.print(message + " [ExceptionTools]");
		exception.printStackTrace();
	} //TODO

	//

	//<editor-fold desc="Helpers">

	private static RuntimeException hlp_getException(Throwable cause, String message, ExceptionType type) {
		if (type == null)
			type = ExceptionType.RUNTIME;

		RuntimeException _exception;
		if (message == null && cause == null)
			_exception = type.get(new RuntimeException(default_message));
		else if (cause == null)
			_exception = type.get(new RuntimeException(message));
		else if (message == null)
			if (cause instanceof RuntimeException)
				_exception = type.get((RuntimeException) cause);
			else
				_exception = type.get(new RuntimeException(cause));
		else
			_exception = type.get(new RuntimeException(message, cause));

		ConsoleBB.CONSOLE.print("ExceptionTools Stack Trace " +
						"[Thread | " + Thread.currentThread() + "] " +
						"[Exception | " + _exception + "]");
//		FXDialogTools.showInfoDialog("Exception Thrown", _exception.toString());
		_exception.printStackTrace();

		return _exception;
	}

	private enum ExceptionType {

		RUNTIME() {
			@Override protected RuntimeException get(RuntimeException base) {
				return base != null ? base : new RuntimeException();
			}
		},
		INTERNAL() {
			@Override protected RuntimeException get(RuntimeException base) {
				return new InternalException(base);
			}
		};

		ExceptionType() {}

		protected abstract RuntimeException get(RuntimeException base);
	}

	//</editor-fold>
}

/*
 * TODO LIST:
 * [S] Add a 2nd message parameter to applicable methods for setting the wrapped Exception's message.
 * [S] Make methods for silently reporting exceptions to the scripter (or devs for internal exceptions).
 *     [S] An example would be when an image icon isn't loaded correctly, but instead Missingno is displayed.
 */