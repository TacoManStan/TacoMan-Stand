package com.taco.tacository.util.tools.printing;

import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.tools.Enu;
import com.taco.tacository.util.tools.Exc;
import com.taco.tacository.util.tools.Exe;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Printer
        implements Lockable {
    
    private final ReentrantLock lock;
    
    private final PrintData globalPrintData;
    private final MapProperty<String, PrintData> keyDataMap;
    private final MapProperty<Class<?>, PrintData> classDataMap;
    private final MapProperty<Object, PrintData> objDataMap;
    
    private Printer() {
        this.lock = new ReentrantLock();
        
        this.globalPrintData = new PrintData("Global", true, true);
        this.keyDataMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
        this.classDataMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
        this.objDataMap = new SimpleMapProperty<>(FXCollections.observableHashMap());
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final PrintData get() { return globalPrintData; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //<editor-fold desc="> PrintData Accessor Methods">
    
    public final PrintData get(@NotNull Object objKey, @Nullable AbsentDef absentDef) {
        return sync(() -> {
            PrintData data = getDataFor(objKey);
            
            if (data == null) {
                switch (Enu.get(AbsentDef.class)) {
                    case DO_NOTHING -> { }
                    case CREATE_NEW -> getMapFor(objKey).put(objKey, data = new PrintData());
                    case USE_GLOBAL -> data = get();
                    case THROW_EXCEPTION -> throw Exc.ex("PrintData for Key [" + objKey + "] cannot be null.");
                }
            }
            
            return data;
        });
    }
    
    public final PrintData get(@NotNull Object objKey) { return get(objKey, AbsentDef.CREATE_NEW); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @Nullable Lock getLock() { return lock; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private <T> MapProperty<T, PrintData> getMapFor(@NotNull Object objKey) {
        if (objKey instanceof String strKey) {
            return (MapProperty<T, PrintData>) keyDataMap;
        } else if (objKey instanceof Class<?> classKey) {
            return (MapProperty<T, PrintData>) classDataMap;
        } else {
            return (MapProperty<T, PrintData>) objDataMap;
        }
    }
    
    private PrintData getDataFor(@NotNull Object objKey) { return getMapFor(objKey).get(objKey); }
    
    //</editor-fold>
    
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
    
    //
    
    public static void print() { print(null, null, true, false); }
    public static void print(@Nullable Object msg) { print(msg, null, true, false); }
    public static void print(@Nullable Object msg, boolean printPrefix) { print(msg, null, printPrefix, false); }
    public static void print(@Nullable Object msg, @Nullable String title, boolean printPrefix) { print(msg, title, printPrefix, false); }
    
    public static void printLite(@Nullable Object msg) { print(msg, Thread.currentThread().getName(), false); }
    
    //
    
    public static void err() { print(null, null, true, true); }
    public static void err(@Nullable Object msg) { print(msg, null, true, true); }
    public static void err(@Nullable Object msg, boolean printPrefix) { print(msg, null, printPrefix, true); }
    public static void err(@Nullable Object msg, @Nullable String title, boolean printPrefix) { print(msg, title, printPrefix, true); }
    
    public static void errLite(@Nullable Object msg) { err(msg, Thread.currentThread().getName(), false); }
    
    //</editor-fold>
}
