package com.taco.suit_lady.util.tools;

/**
 * Contains utility methods related to enums.
 */
public final class SLEnums {
    private SLEnums() { } //No Instance
    
    //
    
    /**
     * Adds all of the enums that are part of the specified enum to an array and then returns the result.
     *
     * @param e The enum.
     *
     * @return An array containing all of the enums that are part of the specified enum.
     */
    public static <T extends Enum> T[] list(Enum e) {
        SLExceptions.nullCheck(e, "Enum");
        
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
        return SLExceptions.nullCheck(enumClass, "Enum Class").getEnumConstants();
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
        SLExceptions.nullCheck(enumName, "Enum Name");
        SLExceptions.nullCheck(t, "Enum");
        
        Enum[] enums = list(t);
        for (Enum e: enums)
            if (e == null)
                throw SLExceptions.ex("e is null for enum: " + t.name());
            else if (!SLTools.instanceOf(t, e.getClass()))
                throw SLExceptions.ex("e is not instance of T for enum: " + t.name());
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
        SLExceptions.nullCheck(enumName, "Enum Name");
        SLExceptions.nullCheck(enumClass, "Enum Class");
        
        T[] enums = list(enumClass);
        for (T e: enums)
            if (e == null)
                throw SLExceptions.ex("e is null for enum: " + SLTools.getSimpleName(enumClass));
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
        return SLArrays.getFirstNonNullElement(list(SLExceptions.nullCheck(enumClass, "Enum Class")));
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
        return SLArrays.getFirstNonNullElement(list(SLExceptions.nullCheck(e, "Enum")));
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
        return SLExceptions.nullCheck(anEnum, "Enum").name();
    }
    
    /**
     * Returns the name of the specified enum (<i>not</i> the enum's value name).
     * <p>
     * Equivalent to '{@link SLTools#getSimpleName(Object) GeneralTools.getSimpleName(anEnum)}'.
     *
     * @param anEnum The {@link Enum}.
     *
     * @return The name of the specified enum (<i>not</i> the enum's value name).
     *
     * @see #name(Enum)
     * @see #fullName(Enum)
     */
    public static String enumName(Enum anEnum) {
        return SLTools.getSimpleName(SLExceptions.nullCheck(anEnum, "Enum"));
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
        SLExceptions.nullCheck(anEnum, "Enum");
        return enumName(anEnum) + "." + name(anEnum);
    }
}