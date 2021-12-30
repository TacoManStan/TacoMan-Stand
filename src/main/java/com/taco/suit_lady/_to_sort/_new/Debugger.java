package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.tools.ExceptionTools;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public final class Debugger {
    
    //<editor-fold desc="--- STATIC SINGLETON ---">
    
    private static final Debugger debugger;
    
    static {
        debugger = new Debugger(true, false, false, false);
    }
    
    /**
     * <p>Returns the static singleton {@link Debugger} instance for this application runtime.</p>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>Note that objects that implement {@link Springable} should use the <i>{@link Springable#debugger()}</i> method to access the Spring-managed singleton instance whenever possible.</li>
     *     <li>
     *         While the Spring-managed {@link Debugger} singleton instance has only {@link #readOnlyIsWarnEnabledProperty() warn} and {@link #readOnlyIsErrorEnabledProperty() error} printing enabled by default,
     *         the static singleton {@link Debugger} instance has {@link #readOnlyIsWarnEnabledProperty() warn} and {@link #readOnlyIsErrorEnabledProperty() error} as well as {@link #readOnlyIsStatusEnabledProperty() status} printing enabled as well.
     *     </li>
     * </ol>
     *
     * @return The static singleton {@link Debugger} instance for this application runtime.
     */
    public static Debugger get() {
        return debugger;
    }
    
    //</editor-fold>
    
    public static final String STATUS = "status";
    public static final String DEBUG = "debug";
    public static final String WARN = "warn";
    public static final String ERROR = "error";
    
    private final ReadOnlyBooleanWrapper isStatusEnabledProperty;
    private final ReadOnlyBooleanWrapper isDebugEnabledProperty;
    private final ReadOnlyBooleanWrapper isWarnEnabledProperty;
    private final ReadOnlyBooleanWrapper isErrorEnabledProperty;
    
    public Debugger() {
        this(false, true, true, true);
    }
    
    public Debugger(boolean statusEnabled, boolean debugEnabled, boolean warnEnabled, boolean errorEnabled) {
        this.isStatusEnabledProperty = new ReadOnlyBooleanWrapper(statusEnabled);
        this.isDebugEnabledProperty = new ReadOnlyBooleanWrapper(debugEnabled);
        this.isWarnEnabledProperty = new ReadOnlyBooleanWrapper(warnEnabled);
        this.isErrorEnabledProperty = new ReadOnlyBooleanWrapper(errorEnabled);
        
        
        this.isStatusEnabledProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue && !oldValue)
                System.out.println("Status Debug Output is now Enabled");
            else if (!newValue && oldValue)
                System.out.println("Status Debug Output is now Disabled");
        });
        this.isDebugEnabledProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue && !oldValue)
                System.out.println("Debug Debug Output is now Enabled");
            else if (!newValue && oldValue)
                System.out.println("Debug Debug Output is now Disabled");
        });
        this.isWarnEnabledProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue && !oldValue)
                System.out.println("Warning Debug Output is now Enabled");
            else if (!newValue && oldValue)
                System.out.println("Warning Debug Output is now Disabled");
        });
        this.isErrorEnabledProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue && !oldValue)
                System.out.println("Error Debug Output is now Enabled");
            else if (!newValue && oldValue)
                System.out.println("Error Debug Output is now Disabled");
        });
    }
    
    
    public void print(@NotNull String msg) {
        print(STATUS, msg);
    }
    
    public void print(@NotNull String printType, @NotNull String msg) {
        if (isTypeEnabled(printType))
            System.out.println(printType + ": " + msg);
    }
    
    public <E> void printList(@NotNull List<E> list, @Nullable String footer) {
        printList(STATUS, list, footer);
    }
    
    public <E> void printList(@NotNull String printType, @NotNull List<E> list, @Nullable String footer) {
        if (!list.isEmpty())
            printBlock(printType, "list", footer, true, list.toArray());
        else
            printBlock(printType, "list", footer, true, "empty");
    }
    
    
    public void printBlock(@NotNull Runnable printAction, @Nullable String title, @Nullable String footer, boolean box) {
        printBlock(STATUS, printAction, title, footer, box);
    }
    
    @SafeVarargs
    public final <E> void printBlock(@Nullable String title, @Nullable String footer, boolean box, E... prints) {
        printBlock(STATUS, title, footer, box, prints);
    }
    
    @SafeVarargs
    public final <E> void printBlock(@Nullable String printType, @Nullable String title, @Nullable String footer, boolean box, E... prints) {
        printBlock(printType,
                   () -> IntStream.range(0, prints.length).mapToObj(
                           i -> "[" + i + "]: " + prints[i]).forEach(System.out::println),
                   title,
                   footer,
                   box);
    }
    
    
    public void printBlock(@NotNull String printType, @NotNull Runnable prints, @Nullable String title, @Nullable String footer, boolean box) {
        if (isTypeEnabled(printType)) {
            footer = "[ " + printType.toUpperCase() + " ]" + (footer != null ? "  " + footer : "");
            
            if (box) {
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println("------------------------------------------------------------");
            }
            
            if (title != null) {
                if (!box)
                    System.out.println("------------------------------------------------------------");
                System.out.println("::: " + title.toUpperCase() + " :::");
                System.out.println("------------------------------------------------------------");
                System.out.println();
            }
            
            //
            
            prints.run();
            
            //
            
            if (footer != null) {
                if (box && title != null) {
                    System.out.println();
                    System.out.println("" + footer + "");
                } else
                    System.out.println("    > " + footer);
            }
            if (box) {
                if (footer == null)
                    System.out.println();
                System.out.println("------------------------------------------------------------");
                System.out.println();
                System.out.println();
                System.out.println();
            }
        }
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public ReadOnlyBooleanProperty readOnlyIsStatusEnabledProperty() {
        return isStatusEnabledProperty.getReadOnlyProperty();
    }
    
    public boolean isStatusEnabled() {
        return isStatusEnabledProperty.get();
    }
    
    public void setStatusEnabled(boolean printEnabled) {
        isStatusEnabledProperty.set(printEnabled);
    }
    
    
    public ReadOnlyBooleanProperty readOnlyIsDebugEnabledProperty() {
        return isDebugEnabledProperty.getReadOnlyProperty();
    }
    
    public boolean isDebugEnabled() {
        return isDebugEnabledProperty.get();
    }
    
    public void setDebugEnabled(boolean debugEnabled) {
        isDebugEnabledProperty.set(debugEnabled);
    }
    
    
    public ReadOnlyBooleanProperty readOnlyIsWarnEnabledProperty() {
        return isWarnEnabledProperty.getReadOnlyProperty();
    }
    
    public boolean isWarnEnabled() {
        return isWarnEnabledProperty.get();
    }
    
    public void setWarnEnabled(boolean warnEnabled) {
        isWarnEnabledProperty.set(warnEnabled);
    }
    
    
    public ReadOnlyBooleanProperty readOnlyIsErrorEnabledProperty() {
        return isErrorEnabledProperty.getReadOnlyProperty();
    }
    
    public boolean isErrorEnabled() {
        return isErrorEnabledProperty.get();
    }
    
    public void setErrorEnabled(boolean errorEnabled) {
        isErrorEnabledProperty.set(errorEnabled);
    }
    
    
    public boolean isTypeEnabled(@NotNull String printType) {
        ExceptionTools.nullCheck(printType, "Print Type");
        if (printType.equalsIgnoreCase(STATUS))
            return isStatusEnabled();
        else if (printType.equalsIgnoreCase(DEBUG))
            return isDebugEnabled();
        else if (printType.equalsIgnoreCase(WARN))
            return isWarnEnabled();
        else if (printType.equalsIgnoreCase(ERROR))
            return isErrorEnabled();
        throw ExceptionTools.unsupported("Unrecognized Print Type: " + printType);
    }
    
    //</editor-fold>
}
