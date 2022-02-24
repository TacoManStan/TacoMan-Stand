package com.taco.suit_lady.util.tools;

import org.jetbrains.annotations.Nullable;

public class Print {
    private Print() { } //No Instance
    
    //<editor-fold desc="--- GENERIC ---">
    
    private static void print(@Nullable Object msg, boolean err) {
        final String fullMsg = msg != null ? TasksSL.getCallingPrefix() + ":   " + msg : TasksSL.getCallingPrefix();
        if (err)
            System.err.println(fullMsg);
        else
            System.out.println(fullMsg);
    }
    public static void print(@Nullable Object msg) { print(msg, false); }
    public static void err(@Nullable Object msg) { print(msg, true); }
    
    public static void print() { print(null, false); }
    public static void err() { print(null, true); }
    
    //</editor-fold>
}
