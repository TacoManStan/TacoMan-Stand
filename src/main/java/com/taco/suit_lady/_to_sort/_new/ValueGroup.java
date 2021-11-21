package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady._to_sort._new.functional.SelfValidatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class ValueGroup<T>
        implements SelfValidatable
{
    private final T[] values;
    private final String[] keys;
    
    private final Supplier<T> defaultValueSupplier;
    
    @SafeVarargs
    public ValueGroup(String[] keys, T... values)
    {
        this((T) null, keys, values);
    }
    
    @SafeVarargs
    public ValueGroup(T defaultValue, String[] keys, T... values)
    {
        this(() -> defaultValue, keys, values);
    }
    
    /**
     * <p>
     * Constructs a new {@link ValueGroup} instance containing the specified {@link #getKeys() keys} and corresponding {@link #getValues() values}
     * and a {@link #getDefaultValue() default value} {@link Supplier supplier}.</p>
     * <ol>
     *     <li>Both arrays must be {@code non-null}.</li>
     *     <li>The contents of both arrays must be {@code non-null}.</li>
     *     <li>The {@code key array} and {@code value array} must be the same size.</li>
     *     <li>Neither array can be empty.</li>
     *     <li>The arrays stored within a {@link ValueGroup} are <i>shallow copies</i>.</li>
     * </ol>
     * <p><b>Default Supplier Details</b></p>
     * <ol>
     *     <li>If the specified {@link Supplier} is {@code null} or <i>default</i>, a fallback {@link Supplier} is automatically created instead.</li>
     *     <li>The fallback {@link Supplier} will cause the {@link #getDefaultValue() default value} to always be the first {@code value} in this {@link ValueGroup}.</li>
     *     <li>More specifically, the fallback {@link Supplier} is {@code () -> getValueAt(0)}.</li>
     * </ol>
     *
     * @param defaultValueSupplier The {@link Supplier} that returns the {@link #getDefaultValue() default value} for this {@link ValueGroup} instance.
     *                             <br>
     * @param keys                 The array of {@link #getKeys() keys} mapped to the specified {@link #getValues() values}.
     * @param values               The array of {@link #getValues() values} mapped by the specified {@link #getKeys() keys}.
     */
    @SafeVarargs
    public ValueGroup(Supplier<T> defaultValueSupplier, String[] keys, T... values)
    {
        this.values = Arrays.copyOf(values, values.length);
        this.keys = Arrays.copyOf(keys, keys.length);
        
        this.defaultValueSupplier = defaultValueSupplier != null ? defaultValueSupplier : () -> getValueAt(0);
    }
    
    /**
     * <p>Returns the number of {@code values} stored within this {@link ValueGroup} object.</p>
     *
     * @return The number of {@code values} stored within this {@link ValueGroup} object.
     */
    public int size()
    {
        return values.length;
    }
    
    /**
     * <p>Returns an array of all {@code values} stored within this {@link ValueGroup} object.</p>
     * <ol>
     *     <li>
     *          The {@code array} returned by this method is a <i>shallow copy</i>.
     *          <br>
     *          As a shallow copy, changes made to the {@code array} itself will have no impact on this {@link ValueGroup} object, but changes made to the actual {@code contents} <i>will</i>.
     *     </li>
     *     <li>
     *          A new {@code array} is constructed each time this method is called, which is a resource-intensive operation when called frequently.
     *          <br>
     *          Therefore, caching the returned {@code value} is recommended in the vast majority of cases.
     *     </li>
     *     <li>The order of {@code values} will always be maintained in the {@code array} returned by this method.</li>
     * </ol>
     *
     * @return An array containing all {@code values} stored within this {@link ValueGroup} object, in order.
     * @see Arrays#copyOf(Object[], int)
     * @see #getKeys()
     */
    public T[] getValues()
    {
        return Arrays.copyOf(values, size());
    }
    
    /**
     * <p>Returns an array of all {@code keys} stored within this {@link ValueGroup} object.</p>
     * <ol>
     *     <li>
     *          The {@code array} returned by this method is a <i>shallow copy</i>.
     *          <br>
     *          As a shallow copy, changes made to the {@code array} itself will have no impact on this {@link ValueGroup} object, but changes made to the actual {@code contents} <i>will</i>.
     *     </li>
     *     <li>
     *          A new {@code array} is constructed each time this method is called, which is a resource-intensive operation when called frequently.
     *          <br>
     *          Therefore, caching the returned {@code key} is recommended in the vast majority of cases.
     *     </li>
     *     <li>The order of {@code keys} will always be maintained in the {@code array} returned by this method.</li>
     * </ol>
     *
     * @return An array containing all {@code keys} stored within this {@link ValueGroup} object, in order.
     * @see Arrays#copyOf(Object[], int)
     * @see #getValues()
     */
    public String[] getKeys()
    {
        return Arrays.copyOf(keys, size());
    }
    
    /**
     * <p>Returns the default {@code value} for this {@link ValueGroup} object by calling the default value {@link Supplier supplier} for this {@link ValueGroup} instance.</p>
     * <ol>
     *     <li>If the default value {@link Supplier supplier} assigned to this {@link ValueGroup} is null, then <i>{@link #getValueAt(int) getValueAt(0)}</i> is returned instead.</li>
     *     <li>
     *         The {@code value} returned by the default value {@link Supplier supplier} is <i>not</i> cached,
     *         therefore, the default value {@link Supplier supplier} is executed every time <i>{@link #getDefaultValue()}</i> is called.
     *     </li>
     * </ol>
     * <p><b>Note:</b> The logic detailed above is located/handled in/by the {@link #ValueGroup(Supplier, String[], Object[]) constructor}, <i>not</i> in/by this or any other method.</p>
     *
     * @return The {@code default value} for this {@link ValueGroup} object.
     */
    public T getDefaultValue()
    {
        return defaultValueSupplier.get();
    }
    
    /**
     * <p>Returns the {@code value} at the specified {@code index}.</p>
     * <ol>
     *     <li>If the specified {@code index} is a negative value, an {@link IndexOutOfBoundsException} is thrown.</li>
     *     <li>If no {@code value} exists at the specified {@code index}, an {@link IndexOutOfBoundsException} is thrown.</li>
     * </ol>
     *
     * @param index The {@code index} of the {@code value} being retrieved.
     * @return The {@code value} located at the specified {@code index}.
     * <br>
     * If the {@code index} is invalid, an {@link IndexOutOfBoundsException} is thrown.
     */
    public T getValueAt(int index)
    {
        validateIndex(index);
        return values[index];
    }
    
    /**
     * <p>Returns the {@code value} mapped to the specified {@code key}.</p>
     * <ol>
     *     <li>If the specified {@code key} is {@code null}, the {@link #getDefaultValue() default} value is returned.</li>
     *     <li>If the specified {@code key} is <i>default</i>, the {@link #getDefaultValue() default} value is returned.</li>
     *     <li>If the specified {@code key} does not exist in this {@link ValueGroup} object, a {@link NoSuchElementException} is thrown.</li>
     *     <li>For all non-default {@code keys}, <i>{@link #get(String) getValueFor(key)}</i> is identical to <i>{@link #getValueAt(int) getValueAt(getIndexOf(key))}</i>.</li>
     * </ol>
     *
     * @param key The {@link String} to be used as the {@code key}.
     *            <br>
     *            A {@code null} or <i>default</i> {@code key} will return the result of <i>{@link #getDefaultValue()}</i>.
     * @return The {@code value} mapped to the specified {@code key} in this {@link ValueGroup} object.
     */
    public T get(String key)
    {
        if (key == null || key.equalsIgnoreCase("default"))
            return getDefaultValue();
        return getValueAt(getIndexOf(key));
    }
    
    /**
     * <p>Returns the {@code key} at the specified {@code index}.</p>
     * <ol>
     *     <li>If the specified {@code index} is a negative value, an {@link IndexOutOfBoundsException} is thrown.</li>
     *     <li>If no {@code key} exists at the specified {@code index}, an {@link IndexOutOfBoundsException} is thrown.</li>
     * </ol>
     *
     * @param index The {@code index} of the {@code key} being retrieved.
     * @return The {@code key} located at the specified {@code index}.
     * <br>
     * If the {@code index} is invalid, an {@link IndexOutOfBoundsException} is thrown.
     */
    public String getKeyAt(int index)
    {
        validateIndex(index);
        return keys[index];
    }
    
    /**
     * <p>Returns the index at which the specified {@code key} is located.</p>
     * <ol>
     * <li>If the specified {@code key} is null, a {@link NullPointerException} is thrown.</li>
     * <li>If the specified {@code key} does not exist in this {@link ValueGroup} object, {@code -1} is returned.</li>
     * </ol>
     *
     * @param key The {@link String} to be used as the {@code key}. Cannot be null.
     * @return The index at which the specified {@code key} is located, or -1 if the specified key does not exist in this {@link ValueGroup} object.
     */
    public int getIndexOf(String key)
    {
        if (key == null)
            throw new NullPointerException("Key cannot be null.");
        
        for (int i = 0; i < keys.length; i++)
            if (key.equalsIgnoreCase(keys[i]))
                return i;
        
        return -1;
    }
    
    /**
     * <p>Constructs a new {@link ValuePair} containing the {@link #getKeyAt(int) key} and {@link #getValueAt(int) value} located at the specified {@code index}.</p>
     *
     * @param index The {@code index} of the {@link ValuePair} being retrieved.
     * @return The newly created {@link ValuePair} containing the {@code key} and {@code value} located at the specified {@code index}.
     * @see #getKeyAt(int)
     * @see #getValueAt(int)
     * @see #getValuePairs()
     */
    public ValuePair<String, T> getValuePairAt(int index)
    {
        validateIndex(index);
        return new ValuePair<>(getKeyAt(index), getValueAt(index));
    }
    
    /**
     * <p>Constructs a new {@link ArrayList} and then populates it with all {@link ValuePair ValuePairs} represented by this {@link ValueGroup}.</p>
     *
     * @return The newly constructed {@link ArrayList} containing all {@link ValuePair ValuePairs} represented by this {@link ValueGroup}.
     * @see #getValuePairAt(int)
     */
    public ArrayList<ValuePair<String, T>> getValuePairs()
    {
        final ArrayList<ValuePair<String, T>> valuePairs = new ArrayList<>();
        for (int i = 0; i < size(); i++)
            valuePairs.add(getValuePairAt(i));
        return valuePairs;
    }
    
    //<editor-fold desc="--- VALIDATION ---">
    
    /**
     * <p>Checks if this {@link ValueGroup} is valid. Details below.</p>
     * <ol>
     *     <li>{@link #validateArrayNullity(boolean)}</li>
     *     <li>{@link #validateArrayLength(boolean)}</li>
     *     <li>{@link #validateDefaultValueSupplierNullity(boolean)}</li>
     *     <li>{@link #validateValueArrayContents(boolean)}</li>
     *     <li>{@link #validateKeyArrayContents(boolean)}</li>
     * </ol>
     *
     * @param throwException True if a detailed {@link RuntimeException Exception} should be thrown if this {@link SelfValidatable} is {@code invalid},
     *                       false if it should follow through with a {@code return value} depicting the validity instead.
     * @return {@code True} if none of the aforementioned methods indicate an invalid state, {@code false} otherwise.
     * <br>
     * Note that if {@code throwException} is set to {@code true}, this method can only ever return {@code true} or throw an {@link NullPointerException exception}.
     * @see #validateArrayNullity(boolean)
     * @see #validateArrayLength(boolean)
     * @see #validateDefaultValueSupplierNullity(boolean)
     * @see #validateValueArrayContents(boolean)
     * @see #validateKeyArrayContents(boolean)
     */
    @Override
    public boolean isValid(boolean throwException)
    {
        if (!validateArrayNullity(throwException))
            return false;
        
        if (!validateArrayLength(throwException))
            return false;
        
        if (!validateDefaultValueSupplierNullity(throwException))
            return false;
        
        if (!validateValueArrayContents(throwException)
            || !validateKeyArrayContents(throwException))
            return false;
        
        return true;
    }
    
    // --- HELPER METHODS --- //
    
    /**
     * <p>Checks if the {@code values} and {@code keys} arrays are non-null.</p>
     *
     * @param throwException {@code True} if an {@link NullPointerException exception} should be thrown if the state is invalid, {@code false} if the method should {@code return} instead.
     * @return {@code True} if both {@code values} and {@code keys} arrays are non-null, {@code false} otherwise.
     * <br>
     * Note that if {@code throwException} is set to {@code true}, this method can only ever return {@code true} or throw an {@link NullPointerException exception}.
     */
    private boolean validateArrayNullity(boolean throwException)
    {
        if (values == null || keys == null)
            if (throwException)
                throw new NullPointerException("Value and Key arrays must both be non-null.");
            else
                return false;
        return true;
    }
    
    /**
     * <p>Checks if the {@code values} and {@code keys} arrays are of the same length.</p>
     *
     * @param throwException {@code True} if an {@link NullPointerException exception} should be thrown if the state is invalid, {@code false} if the method should {@code return} instead.
     * @return {@code True} if {@code values} and {@code keys} arrays are the same size, {@code false} otherwise.
     * <br>
     * Note that if {@code throwException} is set to {@code true}, this method can only ever return {@code true} or throw an {@link NullPointerException exception}.
     */
    private boolean validateArrayLength(boolean throwException)
    {
        if (values.length != keys.length)
            if (throwException)
                throw new ArrayStoreException("Value and Key arrays must be the same length.");
            else
                return false;
        return true;
    }
    
    /**
     * <p>Checks if the {@link #getDefaultValue() default value} {@code supplier} is null.</p>
     *
     * @param throwException {@code True} if an {@link NullPointerException exception} should be thrown if the state is invalid, {@code false} if the method should return instead.
     * @return {@code True} if the {@code default value supplier} is non-null, {@code false} otherwise.
     * <br>
     * Note that if {@code throwException} is set to {@code true}, this method can only ever return {@code true} or throw an {@link NullPointerException exception}.
     */
    private boolean validateDefaultValueSupplierNullity(boolean throwException)
    {
        if (defaultValueSupplier == null)
            if (throwException)
                throw new NullPointerException(
                        "Default supplier cannot be null." +
                        " Note that this should never happen; if the provided supplier is null, one should be automatically created upon construction.");
            else
                return false;
        return true;
    }
    
    /**
     * <p>Checks if there are any null elements in the {@code values} array.</p>
     *
     * @param throwException {@code True} if an {@link NullPointerException exception} should be thrown if the state is invalid, {@code false} if the method should return instead.
     * @return {@code True} if all elements in the {@code values} array are non-null, {@code false} otherwise.
     * <br>
     * Note that if {@code throwException} is set to {@code true}, this method can only ever return {@code true} or throw an {@link NullPointerException exception}.
     */
    private boolean validateValueArrayContents(boolean throwException)
    {
        for (int i = 0; i < size(); i++)
        {
            final T value = getValueAt(i);
            if (value == null)
                if (throwException)
                    throw new NullPointerException("Null value at index [" + i + "]");
                else
                    return false;
        }
        return true;
    }
    
    /**
     * <p>Checks if there are any null elements in the {@code keys} array.</p>
     *
     * @param throwException {@code True} if an {@link NullPointerException exception} should be thrown if the state is invalid, {@code false} if the method should return instead.
     * @return {@code True} if all elements in the {@code keys} array are non-null, {@code false} otherwise.
     * <br>
     * Note that if {@code throwException} is set to {@code true}, this method can only ever return {@code true} or throw an {@link NullPointerException exception}.
     */
    private boolean validateKeyArrayContents(boolean throwException)
    {
        for (int i = 0; i < size(); i++)
        {
            final String key = getKeyAt(i);
            if (key == null)
                if (throwException)
                    throw new NullPointerException("Null key at index [" + i + "]");
                else
                    return false;
        }
        return true;
    }
    
    //
    
    /**
     * <p>Checks of the specified {@code index} is valid for this {@link ValueGroup} object.</p>
     * <br>
     * <p>
     * <b>Note:</b> Despite the similar name, this method is <i>not</i> called by the <i>{@link #isValid(boolean)}</i> method.
     * <br>
     * This is because this method is checking if an {@code index} is valid, whereas <i>{@link #isValid(boolean)}</i> checks if the {@link ValueGroup} object itself is valid.
     * </p>
     * <br>
     *
     * @param index The {@code index} being validated.
     */
    public void validateIndex(int index)
    {
        if (index < 0) // Ensure the index is non-negative
            throw new IndexOutOfBoundsException("Index must be non-negative.");
        
        if (index >= values.length) // Ensure the index exists in the values array
            throw new IndexOutOfBoundsException("Index " + index + " does not exist.");
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC FACTORY METHODS ---">
    
    /**
     * <p>Constructs a new {@link ValueGroup} object containing the specified {@code values}.</p>
     * <ol>
     *      <li>The {@code keys} assigned to the specified {@code values} are <i>old</i> and <i>new</i>, respectively.</li>
     *      <li>A {@code default value} is <i>not</i> provided.</li>
     *      <li>
     *          The {@link ValueGroup} created by this factory method is designed to be used with {@code keys} to improve readability when used.
     *          <br><br>
     *          All of the following examples are valid, but readability is obviously improved when using {@code keys}.
     *          <ol>
     *              <li><i>{@code get("old")}</i> - Correct</li>
     *              <li><i>{@code get("new")}</i> - Correct</li>
     *              <li><i>{@code getValueAt(0)}</i> - Incorrect</li>
     *              <li><i>{@code getValueAt(1)}</i> - Incorrect</li>
     *              <li><i>{@code getDefaultValue()}</i> - Incorrect</li>
     *          </ol>
     *      </li>
     * </ol>
     *
     * @param oldValue The {@code value} to be stored as the {@code old value}.
     * @param newValue The {@code value} to be stored as the {@code new value}.
     * @param <T>      The type of {@code values} to be stored in the newly constructed {@link ValueGroup}.
     * @return The newly constructed {@link ValueGroup} containing the specified {@code values}.
     */
    public static <T> ValueGroup<T> newValueChangeGroup(T oldValue, T newValue)
    {
        return new ValueGroup<>(new String[]{"old", "new"}, oldValue, newValue);
    }
    
    //</editor-fold>
}