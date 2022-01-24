package com.taco.suit_lady.util.tools;

public class MathSL {
    private MathSL() { } //No Instance
    
    public static int ceil(int val1, int val2) {
        return (int) Math.ceil((double) val1 / (double) val2);
    }
}
