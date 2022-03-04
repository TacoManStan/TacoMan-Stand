package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.InternalException;
import com.taco.suit_lady.util.exceptions.NYIException;
import com.taco.suit_lady.util.exceptions.ReadOnlyViolationException;
import com.taco.suit_lady.util.tools.printer.Printer;
import com.taco.tacository.quick.ConsoleBB;
import org.hibernate.TypeMismatchException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.InputMismatchException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Exceptions {
    
    //<editor-fold desc="--- STACK TRACE METHODS ---">
    
    @Contract("_ -> param1")
    public static <T extends Throwable> @NotNull T printStackTrace(@NotNull T throwable) {
        throwable.printStackTrace();
        return throwable;
    }
    
    public static <V, T extends Throwable> @Nullable V printStackTrace(@NotNull T throwable, @NotNull Function<T, V> valueSupplier) { return valueSupplier.apply(printStackTrace(throwable)); }
    public static <V, T extends Throwable> @Nullable V printStackTrace(@NotNull T throwable, @NotNull Supplier<V> valueSupplier) { return printStackTrace(throwable, t -> valueSupplier.get()); }
    public static <V, T extends Throwable> @Nullable V printStackTrace(@NotNull T throwable, @Nullable V value) { return printStackTrace(throwable, t -> value); }
    
    //</editor-fold>
    
    //
    
    //<editor-fold desc="--- LEGACY ---">
    
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
        Exceptions.nullCheck(condition, "Condition");
        return check(obj, condition, RuntimeException::new);
    }
    
    public static <T> T check(T obj, Predicate<T> condition, String message) {
        Exceptions.nullCheck(condition, "Condition");
        Exceptions.nullCheck(message, "Message");
        return check(obj, condition, () -> new RuntimeException(message));
    }
    
    public static <T> T check(T obj, Predicate<T> condition, Supplier<RuntimeException> exceptionSupplier) {
        Exceptions.nullCheck(condition, "Condition");
        Exceptions.nullCheck(exceptionSupplier, "Exception Supplier");
        if (condition.test(obj))
            throw Exceptions.ex(exceptionSupplier.get());
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     * <li>Uses the {@link Exceptions#boundsCheck(Number, double, double, String) check} method to check if the bounds of {@code myVariable} are between -50 and 750.</li>
     * <li>Determines that {@code myVariable} is not within the specified bounds ({@code 750 is less than 1000}</li>
     * <li>Throws a {@code IndexOutOfBoundsException}</li>
     * </ol>
     *
     * This method will never return null.
     *
     * @param value     The {@code value} being bounds-checked.
     * @param valueName The name of the {@code value}.
     *
     * @return The specified {@code value} (if the bounds-check is successful).
     */
    public static <T extends Number> T boundsCheck(T value, double floor, double ceiling, boolean isFloorInclusive, boolean isCeilingInclusive, String valueName) {
        Exceptions.nullCheck(value, "Value");
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
    
    //<editor-fold desc="--- MISMATCHED ---">
    
    public static @NotNull RuntimeException inputMismatch() { return inputMismatch(null, null); }
    public static @NotNull RuntimeException inputMismatch(@Nullable String msg) { return inputMismatch("Input Mismatch", msg); }
    public static @NotNull RuntimeException inputMismatch(@Nullable String prefixMsg, @Nullable String msg) {
        return ex(new InputMismatchException(), refine(prefixMsg, msg));
    }
    
    public static @NotNull RuntimeException typeMismatch() { return typeMismatch(null, null); }
    public static @NotNull RuntimeException typeMismatch(@Nullable Object msg) { return typeMismatch("Type Mismatch", msg); }
    public static @NotNull RuntimeException typeMismatch(@Nullable String prefixMsg, @Nullable Object msg) {
        return ex(new TypeMismatchException("Type Mismatch - See Below"), refine(prefixMsg, msg));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- UNSUPPORTED / NYI ---">
    
    //<editor-fold desc="> Unsupported">
    
    public static @NotNull RuntimeException unsupported(@Nullable String prefixMsg, @Nullable Object msg) {
        return ex(new UnsupportedOperationException(), refine(prefixMsg, msg));
    }
    public static @NotNull RuntimeException unsupported() { return unsupported("Unsupported operation. See stack trace for details."); }
    public static @NotNull RuntimeException unsupported(String msg) { return ex(new UnsupportedOperationException(), msg); }
    
    //</editor-fold>
    
    //<editor-fold desc="> NYI">
    
    public static @NotNull RuntimeException nyi(@Nullable String prefixMsg, @Nullable Object msg, @Nullable Throwable cause) {
        final String refined = refine(prefixMsg, msg);
        
        if (msg != null && cause != null)
            return ex(new NYIException(refined, cause));
        else if (msg != null && cause == null)
            return ex(new NYIException(refined));
        else if (msg == null && cause != null)
            return ex(new NYIException(cause));
        else
            return ex(new NYIException());
    }
    
    public static @NotNull RuntimeException nyi(@Nullable String prefixMsg, @Nullable Object msg) { return nyi(prefixMsg, msg, null); }
    public static @NotNull RuntimeException nyi(@Nullable String msg, @Nullable Throwable cause) { return nyi(null, msg, cause); }
    public static @NotNull RuntimeException nyi(@Nullable String msg) { return nyi(null, msg, null); }
    public static @NotNull RuntimeException nyi(@Nullable Throwable cause) { return nyi(null, null, cause); }
    public static @NotNull RuntimeException nyi() { return nyi(null, null, null); }
    
    public static void throwNYI() { throw nyi(); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    // Read-Only
    
    public static RuntimeException readOnly() {
        return readOnly(null, null);
    }
    
    public static RuntimeException readOnly(String message, Throwable cause) {
        if (message != null && cause != null)
            return ex(new ReadOnlyViolationException(message, cause));
        else if (message != null && cause == null)
            return ex(new ReadOnlyViolationException(message));
        else if (message == null && cause != null)
            return ex(new ReadOnlyViolationException(cause));
        else
            return ex(new ReadOnlyViolationException());
    }
    
    // Abstract Method Error
    
    public static RuntimeException abstractMethodError(String methodName, String message) {
        return ex(new AbstractMethodError("Abstract method \"" + methodName + "\" requires implementation."), message);
    }
    
    private static String refine(@Nullable String prefixMsg, @Nullable Object msg) {
        final String suffix = ". See Stack Trace for details.";
        final String message = msg != null ? msg.toString() : null;
        
        if (prefixMsg != null && message == null) {
            return prefixMsg + suffix;
        } else if (prefixMsg == null && message != null) {
            return message;
        } else if (prefixMsg != null && message != null) {
            return refine(prefixMsg + ". [" + message + "]", null);
        } else {
            return refine("Undefined Exception thrown", null);
        }
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
        
        Printer.err("ExceptionTools Stack Trace " +
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
        
        ExceptionType() { }
        
        protected abstract RuntimeException get(RuntimeException base);
    }
    
    //</editor-fold>
    
    //</editor-fold>
}
