package com.taco.suit_lady.util._new;

public class ValuePair<T1, T2>
{
    private final T1 value1;
    private final T2 value2;
    
    public ValuePair(T1 value1, T2 value2)
    {
        this.value1 = value1;
        this.value2 = value2;
    }
    
    //
    
    public T1 getValue1()
    {
        return this.value1;
    }

    public T2 getValue2()
    {
        return this.value2;
    }
}
