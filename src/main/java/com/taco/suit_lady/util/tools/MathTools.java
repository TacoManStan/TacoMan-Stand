package com.taco.suit_lady.util.tools;

public class MathTools {
    private MathTools() { } //No Instance
    
    public static int ceil(int val1, int val2) {
        return (int) Math.ceil((double) val1 / (double) val2);
    }
}
