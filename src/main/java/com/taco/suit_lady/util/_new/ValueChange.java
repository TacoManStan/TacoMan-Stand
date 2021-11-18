package com.taco.suit_lady.util._new;

import com.taco.suit_lady.util._new.functional.SelfValidatable;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class ValueChange<T>
        implements SelfValidatable
{
    final T[] values;
    final String[] keys;
    
    final Supplier<T> defaultValueSupplier;
    
    public ValueChange(T[] values, String[] keys, Supplier<T> defaultValueSupplier)
    {
        if (values == null || keys == null)
            throw new NullPointerException("Value and Key arrays must both be non-null.");
        if (values.length != keys.length)
            throw new ArrayStoreException("Value and Key arrays must be the same length.");
        
        this.values = values;
        this.keys = keys;
        
        this.defaultValueSupplier = defaultValueSupplier != null ? defaultValueSupplier : () -> getValueAt(0);
        
        //
        
        
    }
    
    //
    
    /**
     * <p>Returns the number of {@code values} stored within this {@link ValueChange} object.</p>
     *
     * @return The number of {@code values} stored within this {@link ValueChange} object.
     */
    public int getSize()
    {
        return values.length;
    }
    
    /**
     * <p>Returns an array of all {@code values} stored within this {@link ValueChange} object.</p>
     * <ol>
     *     <li>
     *          The {@code array} returned by this method is a <i>shallow copy</i>.
     *          <br>
     *          As a shallow copy, changes made to the {@code array} itself will have no impact on this {@link ValueChange} object, but changes made to the actual {@code contents} <i>will</i>.
     *     </li>
     *     <li>
     *          A new {@code array} is constructed each time this method is called, which is a resource-intensive operation when called frequently.
     *          <br>
     *          Therefore, caching the returned {@code value} is recommended in the vast majority of cases.
     *     </li>
     *     <li>The order of {@code values} will always be maintained in the {@code array} returned by this method.</li>
     * </ol>
     *
     * @return An array containing all {@code values} stored within this {@link ValueChange} object, in order.
     * @see Arrays#copyOf(Object[], int)
     * @see #getKeys()
     */
    public T[] getValues()
    {
        return Arrays.copyOf(values, values.length);
    }
    
    /**
     * <p>Returns an array of all {@code keys} stored within this {@link ValueChange} object.</p>
     * <ol>
     *     <li>
     *          The {@code array} returned by this method is a <i>shallow copy</i>.
     *          <br>
     *          As a shallow copy, changes made to the {@code array} itself will have no impact on this {@link ValueChange} object, but changes made to the actual {@code contents} <i>will</i>.
     *     </li>
     *     <li>
     *          A new {@code array} is constructed each time this method is called, which is a resource-intensive operation when called frequently.
     *          <br>
     *          Therefore, caching the returned {@code key} is recommended in the vast majority of cases.
     *     </li>
     *     <li>The order of {@code keys} will always be maintained in the {@code array} returned by this method.</li>
     * </ol>
     *
     * @return An array containing all {@code keys} stored within this {@link ValueChange} object, in order.
     * @see Arrays#copyOf(Object[], int)
     * @see #getValues()
     */
    public String[] getKeys()
    {
        return Arrays.copyOf(keys, keys.length);
    }
    
    /**
     * <p>Returns the {@code default value} for this {@link ValueChange} object.</p>
     * <p>If the {@code default value} assigned to this {@link ValueChange} is null, <i>{@link #getValueAt(int) getValueAt(0)}</i> is returned instead.</p>
     * <br>
     * <p>Note that the above logic is located in the {@link ValueChange} constructor.</p>
     *
     * @return The {@code default value} for this {@link ValueChange} object.
     */
    public T getDefaultValue()
    {
        return defaultValueSupplier.get();
    }
    
    //
    
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
        indexCheck(index);
        return values[index];
    }
    
    /**
     * <p>Returns the {@code value} mapped to the specified {@code key}.</p>
     * <ol>
     *     <li>If the specified {@code key} is {@code null}, the {@link #getDefaultValue() default} value is returned.</li>
     *     <li>If the specified {@code key} is <i>default</i>, the {@link #getDefaultValue() default} value is returned.</li>
     *     <li>If the specified {@code key} does not exist in this {@link ValueChange} object, a {@link NoSuchElementException} is thrown.</li>
     *     <li>For all non-default {@code keys}, <i>{@link #getValueFor(String) getValueFor(key)}</i> is identical to <i>{@link #getValueAt(int) getValueAt(getIndexOf(key))}</i>.</li>
     * </ol>
     *
     * @param key The {@link String} to be used as the {@code key}.
     *            <br>
     *            A {@code null} or <i>default</i> {@code key} will return the result of <i>{@link #getDefaultValue()}</i>.
     * @return The {@code value} mapped to the specified {@code key} in this {@link ValueChange} object.
     */
    public T getValueFor(String key)
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
        indexCheck(index);
        return keys[index];
    }
    
    /**
     * <p>Returns the index at which the specified {@code key} is located.</p>
     * <ol>
     * <li>If the specified {@code key} is null, a {@link NullPointerException} is thrown.</li>
     * <li>If the specified {@code key} does not exist in this {@link ValueChange} object, {@code -1} is returned.</li>
     * </ol>
     *
     * @param key The {@link String} to be used as the {@code key}. Cannot be null.
     * @return The index at which the specified {@code key} is located, or -1 if the specified key does not exist in this {@link ValueChange} object.
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
    
    //
    
    private void indexCheck(int index)
    {
        if (index < 0) // Ensure the index is non-negative
            throw new IndexOutOfBoundsException("Index must be non-negative.");
        
        if (index >= values.length) // Ensure the index exists in the values array
            throw new IndexOutOfBoundsException("Index " + index + " does not exist.");
    }
    
    //
    
    //<editor-fold desc="--- VALIDATION ---">
    
    @Override
    public boolean validate(boolean throwException)
    {
        if (!validateArrayNullity(throwException))
            return false;
        
        if (!validateArrayLength(throwException))
            return false;
        
        if (!validateDefaultValueSupplierNullity(throwException))
            return false;
        
        if (!validateValueArrayContents(throwException))
            return false;
        if (!validateKeyArrayContents(throwException))
            return false;
        
        return true;
    }
    
    // --- HELPER METHODS --- //
    
    private boolean validateArrayNullity(boolean throwException)
    {
        if (values == null || keys == null)
            if (throwException)
                throw new NullPointerException("Value and Key arrays must both be non-null.");
            else
                return false;
        return true;
    }
    
    private boolean validateArrayLength(boolean throwException)
    {
        if (values.length != keys.length)
            if (throwException)
                throw new ArrayStoreException("Value and Key arrays must be the same length.");
            else
                return false;
        return true;
    }
    
    private boolean validateDefaultValueSupplierNullity(boolean throwException)
    {
        if (defaultValueSupplier == null)
            if (throwException)
                throw new NullPointerException(
                        "Default supplier cannot be null." +
                        " Note that this should never happen; if the provided supplier is null, one should be automatically created upon construction."
                );
            else
                return false;
        return true;
    }
    
    private boolean validateValueArrayContents(boolean throwException)
    {
        for (int i = 0; i < getSize(); i++)
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
    
    private boolean validateKeyArrayContents(boolean throwException)
    {
        for (int i = 0; i < getSize(); i++)
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
    
    //</editor-fold>
}