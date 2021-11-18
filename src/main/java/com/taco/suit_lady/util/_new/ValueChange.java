package com.taco.suit_lady.util._new;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class ValueChange<T>
{
    final T[] values;
    final String[] keys;
    
    public ValueChange(T[] values, String[] keys)
    {
        if (values == null || keys == null)
            throw new NullPointerException("Value and Key arrays must both be non-null.");
        if (values.length != keys.length)
            throw new ArrayStoreException("Value and Key arrays must be the same length.");
        
        this.values = values;
        this.keys = keys;
        
        //
        
        
    }
    
    //
    
    public int getCount()
    {
        if (values.length != keys.length) // Currently, this condition will always be false, and is present only to help protect against future changes that might break functionality.
            throw new ArrayStoreException("Value and Key arrays must be the same length.");
        
        return values.length;
    }
    
    /**
     *
     * @return
     */
    public T[] getValues()
    {
        return Arrays.copyOf(values, values.length);
    }
    
    public String[] getKeys()
    {
        return Arrays.copyOf(keys, keys.length);
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
     *     <li>If the specified {@code key} is null, a {@link NullPointerException} is thrown.</li>
     *     <li>If the specified {@code key} does not exist in this {@link ValueChange} object, a {@link NoSuchElementException} is thrown.a</li>
     *     <li>{@link #getValueFor(String) getValueFor(key)} is identical to {@link #getValueAt(int) getValueAt(getIndexOf(key))}.</li>
     * </ol>
     *
     * @param key The {@link String} to be used as the {@code key}. Cannot be null.
     * @return The {@code value} mapped to the specified {@code key} in this {@link ValueChange} object.
     */
    public T getValueFor(String key)
    {
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
}