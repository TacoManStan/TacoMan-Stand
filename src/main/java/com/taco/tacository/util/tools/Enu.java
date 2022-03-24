package com.taco.tacository.util.tools;

import com.taco.tacository.util.enums.DefaultableEnum;
import com.taco.tacository.util.tools.list_tools.A;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains utility methods related to enums.
 */
public final class Enu {
    private Enu() { } //No Instance
    
    //<editor-fold desc="--- ACCESSORS (GET) ---">
    
    /**
     * <p>Returns the Default {@link Class#getEnumConstants() Default Enum Constant} for the {@link Enum} defined by the specified {@link Class}.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If the {@link Enum} represented by the specified {@link Class} implements {@link DefaultableEnum}, the value returned by <i>{@link DefaultableEnum#defaultValue()}</i> is returned.</li>
     *     <li>If the {@link Enum} represented by the specified {@link Class} does <u><i>not</i></u> implement {@link DefaultableEnum}, the {@link Class#getEnumConstants() Enum Constant} at {@code Index 0} is returned.</li>
     *     <li>If the {@link Enum} represented by the specified {@link Class} does not contain any <i>{@link Class#getEnumConstants() Enum Constants}, an {@link UnsupportedOperationException} is thrown.</i></li>
     *     <li>If the {@code array} of {@link Class#getEnumConstants() Enum Constants} for the specified {@link Class} is {@code null}, a {@link NullPointerException} is thrown.</li>
     * </ol>
     *
     * @param clazz The {@link Class} object defining the {@link Enum} type to be looked-up.
     * @param <E>   The exact type of {@link Enum} being accessed.
     *
     * @return The default {@link Class#getEnumConstants() Enum Constant} for the {@link Enum} defined by the specified {@link Class}.
     */
    public static <E extends Enum<E>> @NotNull E get(@NotNull Class<E> clazz) {
        final String name = "[" + Exc.nullCheck(clazz, "Enum Class Input").getSimpleName() + "]";
        if (!clazz.isEnum())
            throw Exc.typeMismatch("Class must be an enum: " + name);
        else {
            final E[] values = clazz.getEnumConstants();
            if (A.isEmpty(Exc.nullCheck(values, "Enum Values " + name)))
                throw Exc.unsupported("Enum [" + clazz.getSimpleName() + "] does not have any values.");
            else {
                if (DefaultableEnum.class.isAssignableFrom(clazz))
                    return Exc.nullCheck(((DefaultableEnum<E>) values[0]).defaultValue(), "DefaultableEnum Value: " + name);
                else
                    return Exc.nullCheck(values[0], "Enum & Index 0: " + name);
            }
        }
    }
    
    /**
     * <p>Returns a guaranteed {@code non-null} {@link Class#getEnumConstants() Enum Constant} matching the specified {@link Class} definition.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If {@code non-null}, no additional operations are performed and the specified {@link Enum Enum Constant} is {@code returned}.</li>
     *     <li>If {@code null}, the specified {@link Enum Enum Constant} is ignored and the value of <i>{@link Enu#get(Class)}</i> is {@code returned} instead.</li>
     * </ol>
     *
     * @param enumConst If {@code non-null}, no additional operations are performed and the {@link Enum Enum Constant} defined by this {@code parameter} is {@code returned}.
     *                  If {@code null}, this {@code parameter} is ignored.
     * @param clazz     The {@link Class Class} object defining the {@link Enum} to be accessed by {@link #get(Enum, Class) this method}.
     * @param <E>       The exact type of {@link Enum} being accessed as defined by {@code Class<E>}.
     *
     * @return A guaranteed {@code non-null} {@link Class#getEnumConstants() Enum Constant} matching the specified {@link Class} definition.
     */
    public static <E extends Enum<E>> @NotNull E get(@Nullable E enumConst, @NotNull Class<E> clazz) { return enumConst != null ? enumConst : get(clazz); }
    
    
    //</editor-fold>
    
    //
    
    //<editor-fold desc="--- LEGACY ---">
    
    /**
     * Adds all of the enums that are part of the specified enum to an array and then returns the result.
     *
     * @param e The enum.
     *
     * @return An array containing all of the enums that are part of the specified enum.
     */
    public static <T extends Enum> T[] list(Enum e) {
        Exc.nullCheck(e, "Enum");
        
        Enum[] declaring;
        declaring = e.getClass().getEnumConstants();
        if (declaring == null && e.getDeclaringClass() != null)
            declaring = (Enum[]) e.getDeclaringClass().getEnumConstants();
        return (T[]) declaring;
    }
    
    /**
     * Adds all of the enums that are part of the specified enum {@link Class} to an array and then returns the result.
     *
     * @param enumClass The enum {@link Class}.
     *
     * @return An array containing all of the enums that are part of the specified enum {@link Class}.
     */
    public static <T extends Enum> T[] list(Class<T> enumClass) {
        return Exc.nullCheck(enumClass, "Enum Class").getEnumConstants();
    }
    
    //
    
    /**
     * Returns the {@link Enum} that equals the specified enum name.
     * <br>
     * Returns null if the specified enum name or {@link Enum} is null.
     *
     * @param <T>      The type of enum.
     * @param enumName The name of the enum.
     * @param t        The {@link Enum}.
     *
     * @return The {@link Enum} that equals the specified enum name.
     */
    public static <T extends Enum> T valueOf(String enumName, T t) {
        Exc.nullCheck(enumName, "Enum Name");
        Exc.nullCheck(t, "Enum");
        
        Enum[] enums = list(t);
        for (Enum e: enums)
            if (e == null)
                throw Exc.ex("e is null for enum: " + t.name());
            else if (!TB.instanceOf(t, e.getClass()))
                throw Exc.ex("e is not instance of T for enum: " + t.name());
            else if (e.name().equals(enumName))
                return (T) e;
        return null;
    }
    
    /**
     * Returns the {@link Enum} that equals the specified enum name.
     * <br>
     * Returns null if the specified enum name or enum {@link Class} is null.
     *
     * @param <T>       The type of enum.
     * @param enumName  The name of the enum.
     * @param enumClass The enum {@link Class}.
     *
     * @return The {@link Enum} that equals the specified enum name.
     */
    public static <T extends Enum> T valueOf(String enumName, Class<T> enumClass) {
        Exc.nullCheck(enumName, "Enum Name");
        Exc.nullCheck(enumClass, "Enum Class");
        
        T[] enums = list(enumClass);
        for (T e: enums)
            if (e == null)
                throw Exc.ex("e is null for enum: " + TB.getSimpleName(enumClass));
            else if (e.name().equals(enumName))
                return e;
        return null;
    }
    
    //
    
    /**
     * Returns the first {@code enum value} for the specified {@code enum class}.
     *
     * @param enumClass The {@code enum class}.
     * @param <T>       The type of {@code enum}.
     *
     * @return The first {@code enum value} for the specified {@code enum class}.
     */
    public static <T extends Enum> T first(Class<T> enumClass) {
        return A.getFirstNonNullElement(list(Exc.nullCheck(enumClass, "Enum Class")));
    }
    
    /**
     * Returns the first {@code enum value} for the specified {@code enum}.
     *
     * @param e   The {@code enum}.
     * @param <T> The type of {@code enum}.
     *
     * @return The first {@code enum value} for the specified {@code enum}.
     */
    public static <T extends Enum> T first(T e) {
        return A.getFirstNonNullElement(list(Exc.nullCheck(e, "Enum")));
    }
    
    //
    
    /**
     * Returns the <i>value</i> name of the specified enum (<i>not</i> the enum's name).
     * <p>
     * Equivalent to '{@link Enum#name() anEnum.name()}'.
     *
     * @param anEnum The {@link Enum}.
     *
     * @return The <i>value</i> name of the specified enum (<i>not</i> the enum's name).
     *
     * @see #enumName(Enum)
     * @see #fullName(Enum)
     */
    public static String name(Enum anEnum) {
        return Exc.nullCheck(anEnum, "Enum").name();
    }
    
    /**
     * Returns the name of the specified enum (<i>not</i> the enum's value name).
     * <p>
     * Equivalent to '{@link TB#getSimpleName(Object) GeneralTools.getSimpleName(anEnum)}'.
     *
     * @param anEnum The {@link Enum}.
     *
     * @return The name of the specified enum (<i>not</i> the enum's value name).
     *
     * @see #name(Enum)
     * @see #fullName(Enum)
     */
    public static String enumName(Enum anEnum) {
        return TB.getSimpleName(Exc.nullCheck(anEnum, "Enum"));
    }
    
    /**
     * Returns the full name of the specified enum.
     * <p>
     * Equivalent to '{@link #enumName(Enum) getEnumName(anEnum} + "." + {@link #name(Enum) getValueName(anEnum}'.
     *
     * @param anEnum The {@link Enum}.
     *
     * @return The full name of the specified enum.
     *
     * @see #enumName(Enum)
     * @see #name(Enum)
     */
    public static String fullName(Enum anEnum) {
        Exc.nullCheck(anEnum, "Enum");
        return enumName(anEnum) + "." + name(anEnum);
    }
    
    //</editor-fold>
}