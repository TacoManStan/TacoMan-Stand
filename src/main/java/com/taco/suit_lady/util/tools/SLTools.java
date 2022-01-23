package com.taco.suit_lady.util.tools;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Contains a variety of classes that provide utility features.
 */
public class SLTools
{
    public static SLTools get()
    {
        return TB.general();
    }
    
    SLTools() { }
    
    /**
     * Checks if the specified {@link Predicate} is valid for the specified {@code Object},
     * and that the specified {@code Object} is not null.
     *
     * @param obj       The {@code Object} being tested by the specified {@code Predicate}.
     * @param predicate The {@code Predicate} testing the specified {@code Object}.
     * @param <T>       The type of {@code Object} being tested.
     * @return True if the specified {@link Predicate} is valid for the specified {@code Object}
     * and if the specified {@code Object} is not null, false otherwise.
     * @throws NullPointerException if the specified {@code Object} is null.
     */
    public <T> boolean test(T obj, Predicate<T> predicate)
    {
        SLExceptions.nullCheck(predicate, "Predicate");
        return obj != null && predicate.test(obj);
    }
    
    //<editor-fold desc="--- SLEEPING ---">
    
    /**
     * Sleeps for the specified amount of time. In milliseconds.
     *
     * @param millis The amount of time to sleep in milliseconds.
     * @return True if an InterruptedException was thrown while sleeping; false otherwise.
     */
    public boolean sleep(double millis)
    {
        try
        {
            Thread.sleep((long) millis);
        }
        catch (final InterruptedException ignored)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Sleeps for an amount of time appropriate for the inside of a loop.
     *
     * @return True if an InterruptedException was thrown while sleeping; false otherwise.
     */
    public boolean sleepLoop()
    {
        return sleep(15);
    } // TODO
    
    //</editor-fold>
    
    /* *************************************************************************** *
     *                                                                             *
     * Classes/Instances                                                           *
     *                                                                             *
     * *************************************************************************** */
    
    /**
     * Returns the component type {@link Class} for the specified value.
     * <ol>
     * <li>If the specified value <i>is not</i> an array, the specified value's
     * direct {@link Class} is returned.</li>
     * <li>If the specified value <i>is</i> an array, the specified array's
     * component type is returned.</li>
     * <li>This method <i>does not</i> work with any type of {@link List}.
     * <p>
     * In other words, if the specified value is a {@link List} or other data
     * structure type, the {@link Class} of that data structure is returned.
     * </li>
     * </ol>
     *
     * @param param The value. Does not have to be an array.
     * @param <T>   The value type of the {@link Class} being returned.
     * @return The component type {@link Class} for the specified value.
     */
    public <T> Class<? extends T> getClass(T[] param)
    {
        SLExceptions.nullCheck(param, "Param Array");
        return (Class<T>) param.getClass().getComponentType();
    }
    
    /**
     * Returns the array {@link Class} for the specified value.
     * <p>
     * The intended use of this method is to pass a value that is <i>not</i> an
     * array into this method. Because of the nature of varargs, the array
     * {@link Class} of the specified value will be returned.
     *
     * @param param The value.
     * @param <T>   The type of value being returned.
     * @return The array {@link Class} for the specified value.
     */
    @SafeVarargs
    public final <T> Class getArrayClass(T... param)
    {
        if (param != null)
        {
            if (param.length > 0 && param[0] != null && param[0] instanceof Class)
            {
                Class c = (Class) param[0];
                if (c.isArray())
                    return c;
                else
                    try
                    {
                        return Class.forName("[L" + c.getName() + ";");
                    }
                    catch (ClassNotFoundException e)
                    {
                        throw SLExceptions.ex(e);
                    }
            }
            return param.getClass();
        }
        throw new NullPointerException("Param cannot be null");
    }
    
    /**
     * Returns the simple name of the specified {@link Object} using the {@link Class#getSimpleName()} method.
     * <p>
     * Returns the String {@code "Null"} if the specified {@link Object} is null.
     * <br>The returned String will <i>never</i> itself be null.
     *
     * @param obj The {@link Object}.
     * @return The simple name of the specified {@link Object}.
     */
    public String getSimpleName(Object obj)
    {
        if (obj != null)
            return obj.getClass().getSimpleName();
        return null;
    }
    
    /**
     * Returns the "work-around" simple name of the specified {@link Object}.
     * <p>
     * Returns the String {@code "Null"} if the specified {@link Object} is null.
     * <br>The returned String will <i>never</i> itself be null.
     * <p>
     * This method was created as a work-around for inconsistencies present when obfuscation was applied in the original TRiBot client.
     * <br> Should no longer be necessary in TRiBot FX, but is being kept just in case.
     *
     * @param obj The {@link Object}.
     * @return The simple name of the specified {@link Object}.
     * @see #getSimpleName(Object)
     */
    @Deprecated
    public String getWOSimpleName(Object obj)
    {
        if (obj != null)
        {
            Class c = obj instanceof Class ? (Class) obj : obj.getClass();
            int indexOf = c.getName().lastIndexOf(c.getName().contains("$") ? "$" : ".");
            if (indexOf < c.getName().length() - 1)
                indexOf++;
            String name = c.getName().substring(indexOf);
            if (SLStrings.get().isNumber(name))
                name = c.getName().substring(c.getName().lastIndexOf(".") + 1, c.getName().lastIndexOf("$"));
            return name;
        }
        return "Null";
    }
    
    /**
     * Checks if the specified {@link Object} is an instance of any of the
     * specified {@link Class Classes}.
     * <p>
     * If the specified {@link Object} is {@link Class#isAssignableFrom(Class)
     * assignable} from any of the specified classes, this method returns true.
     * In other words, the specified {@link Object} can be an instance of a
     * child class.
     *
     * @param t  The {@link Object} being compared.
     * @param cs The {@link Class Classes} being compared.
     * @return if the specified {@link Object} is an instance of any of the
     * specified {@link Class Classes}, false otherwise.
     */
    public boolean instanceOf(Object t, Class... cs)
    {
        if (t != null && cs != null)
            for (Class c: cs)
                if (c != null)
                    if (t instanceof Enum)
                    {
                        if (c.equals(t.getClass()) || ((Enum) t).getDeclaringClass().isAssignableFrom(c))
                            return true;
                    }
                    else if (t instanceof Class)
                    {
                        if (c.equals(t) || c.isAssignableFrom((Class) t))
                            return true;
                    }
                    else if (c.equals(t.getClass()) || c.isAssignableFrom(t.getClass()))
                        return true;
        return false;
    }
    
    /* *************************************************************************** *
     *                                                                             *
     * Equality                                                                    *
     *                                                                             *
     * *************************************************************************** */
    
    /**
     * Checks the equality of the two specified {@code objects}.
     * <p>
     * Equivalent to {@link Objects#equals(Object, Object) Objects.equals(obj1, obj2)}.
     *
     * @param obj1 The first object being compared. Can be null.
     * @param obj2 The second object being compared. Can be null.
     * @return True if the specified objects are equal, false otherwise.
     * @see #equalsUnsafe(Object, Object)
     */
    public boolean equals(Object obj1, Object obj2)
    {
        return Objects.equals(obj1, obj2);
    }
    
    /**
     * Checks the equality of the two specified {@code objects}.
     *
     * @param obj1 The first object. <i>Cannot</i> be null.
     * @param obj2 The second object. <i>Cannot</i> be null.
     * @return True if the specified objects are equal <i>and</i> non-null, false otherwise.
     * @see #equals(Object, Object)
     */
    public boolean equalsUnsafe(Object obj1, Object obj2)
    {
        return obj1 != null && obj2 != null && Objects.equals(obj1, obj2);
    }
    
    //
    
    /**
     * Checks if the specified object is equal to <u>any</u> of the elements contained within the specified array.
     * Returns false if the array is null or empty.
     *
     * @param obj  The object being compared. Can be null.
     * @param objs The array of objects being compared to the first object. Elements can be null, but the array cannot.
     * @return If the specified object is equal to <u>any</u> of the elements contained within the specified array.
     * Returns false if the array is null or empty.
     */
    public boolean equalsAny(Object obj, Object... objs)
    {
        return equals(obj, false, true, false, objs);
    }
    
    /**
     * Checks if the specified object is equal to <u>any</u> of the elements contained within the specified array.
     * Returns false if the array is null, true if it is empty.
     *
     * @param obj  The object being compared. Can be null.
     * @param objs The array of objects being compared to the first object. Elements can be null, but the array cannot.
     * @return If the specified object is equal to <u>any</u> of the elements contained within the specified array.
     * Returns false if the array is null, true if it is empty.
     */
    public boolean equalsAll(Object obj, Object... objs)
    {
        return equals(obj, true, true, false, objs);
    }
    
    /**
     * TODO
     *
     * @param obj        ...
     * @param requireAll ...
     * @param allowNull  ...
     * @param deep       ...
     * @param objs       ...
     * @return ...
     */
    public boolean equals(Object obj, boolean requireAll, boolean allowNull, boolean deep, Object... objs)
    {
        if (objs == null || (!allowNull && obj == null))
            return false;
        else if (objs.length == 1)
            return equalsImpl(obj, objs[0], allowNull, deep);
        else
        {
            for (Object arrObj: objs)
                if (equalsImpl(obj, arrObj, allowNull, deep))
                {
                    if (requireAll)
                        return false;
                }
                else
                {
                    if (!requireAll)
                        return true;
                }
            return requireAll;
        }
    }
    
    private boolean equalsImpl(Object obj1, Object obj2, boolean allowNull, boolean deep)
    {
        if (allowNull && obj1 == null && obj2 == null)
            return true;
        else
            return deep ? Objects.deepEquals(obj1, obj2) : Objects.equals(obj1, obj2);
    }
    
    public long generateHashID()
    {
        return (long) TB.random().nextDouble(0, Long.MAX_VALUE);
    }
    
    /**
     * Generates a hash id that is useful for saving/loading unique objects.
     *
     * @return A hash id.
     * @see #generateHashID()
     */
    public String generateHashString()
    {
        return "" + generateHashID();
    }
}


/*
 * TODO LIST:
 * [S] Make an enum for compare types, e.g., "ALL", "ANY", etc.
 */