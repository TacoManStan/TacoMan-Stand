package com.taco.suit_lady.util.tools.printer;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.tools.Enums;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.Exe;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Print
        implements Lockable {
    
    private final ReentrantLock lock;
    
    private final PrintData globalPrintData;
    private final MapProperty<String, PrintData> keyDataMap;
    private final MapProperty<Class<?>, PrintData> classDataMap;
    private final MapProperty<Object, PrintData> objDataMap;
    
    private Print() {
        this.lock = new ReentrantLock();
        
        this.globalPrintData = new PrintData("Global", true, true);
        this.keyDataMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
        this.classDataMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
        this.objDataMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final PrintData get() { return globalPrintData; }
    
    public final PrintData get(@NotNull Object objKey, @Nullable AbsentDef onAbsent) {
        return sync(() -> {
            PrintData data = getDataFor(objKey);
            
            if (data == null) {
                switch (Enums.get(AbsentDef.class)) {
                    case DO_NOTHING -> { }
                    case CREATE_NEW -> getDataMapFor(objKey).put(objKey, data = new PrintData());
                    case USE_GLOBAL -> data = get();
                    case THROW_EXCEPTION -> throw Exc.ex("PrintData for Key [" + objKey + "] cannot be null.");
                }
            }
            
            return data;
        });
    }
    public final PrintData get(@NotNull Object objKey) { return get(objKey, AbsentDef.CREATE_NEW); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @Nullable Lock getLock() { return lock; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    
    //</editor-fold>
    
    private <T> MapProperty<T, PrintData> getDataMapFor(@NotNull Object objKey) {
        if (objKey instanceof String strKey) {
            return (MapProperty<T, PrintData>) keyDataMap;
        } else if (objKey instanceof Class<?> classKey) {
            return (MapProperty<T, PrintData>) classDataMap;
        } else {
            return (MapProperty<T, PrintData>) objDataMap;
        }
    }
    private PrintData getDataFor(@NotNull Object objKey) { return getDataMapFor(objKey).get(objKey); }
    
    //<editor-fold desc="--- GENERIC STATIC ---">
    
    protected static void print(@Nullable Object msg, @Nullable String title, boolean printPrefix, boolean err) {
        title = title != null ? " - [" + title + "]" : "";
        final String prefix = printPrefix ? Exe.getCallingPrefix(1) + title + ": " : "";
        final String fullMsg = msg != null ? prefix + msg : Exe.getCallingPrefix(1);
        if (err)
            System.err.println(fullMsg);
        else
            System.out.println(fullMsg);
    }
    
    public static void print() { print(null, null, true, false); }
    public static void print(@Nullable Object msg) { print(msg, null, true, false); }
    public static void print(@Nullable Object msg, boolean printPrefix) { print(msg, null, printPrefix, false); }
    public static void print(@Nullable Object msg, @Nullable String title, boolean printPrefix) { print(msg, title, printPrefix, false); }
    
    public static void err() { print(null, null, true, true); }
    public static void err(@Nullable Object msg) { print(msg, null, true, true); }
    public static void err(@Nullable Object msg, boolean printPrefix) { print(msg, null, printPrefix, true); }
    public static void err(@Nullable Object msg, @Nullable String title, boolean printPrefix) { print(msg, title, printPrefix, true); }
    
    //</editor-fold>
}