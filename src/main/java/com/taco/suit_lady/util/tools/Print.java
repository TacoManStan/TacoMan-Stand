package com.taco.suit_lady.util.tools;

import org.jetbrains.annotations.Nullable;

public class Print {
    private Print() { } //No Instance
    
    //<editor-fold desc="--- GENERIC ---">
    
    private static void print(@Nullable Object msg, boolean printPrefix, boolean err) {
        final String prefix = printPrefix ? TasksSL.getCallingPrefix() + ": " : "";
        final String fullMsg = msg != null ? prefix + msg : TasksSL.getCallingPrefix();
        if (err)
            System.err.println(fullMsg);
        else
            System.out.println(fullMsg);
    }
    
    public static void print() { print(null, true, false); }
    public static void print(@Nullable Object msg) { print(msg, true, false); }
    public static void print(@Nullable Object msg, boolean printPrefix) { print(msg, printPrefix, false); }
    
    public static void err() { print(null, true, true); }
    public static void err(@Nullable Object msg) { print(msg, true, true); }
    public static void err(@Nullable Object msg, boolean printPrefix) { print(msg, printPrefix, true); }
    
    //</editor-fold>
}
