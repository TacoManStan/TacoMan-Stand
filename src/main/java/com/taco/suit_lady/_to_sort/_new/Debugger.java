package com.taco.suit_lady._to_sort._new;

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
    
    public static final String PRINT = "print";
    public static final String WARN = "warn";
    public static final String ERROR = "error";
    
    private final ReadOnlyBooleanWrapper isPrintEnabledProperty;
    private final ReadOnlyBooleanWrapper isWarnEnabledProperty;
    private final ReadOnlyBooleanWrapper isErrorEnabledProperty;
    
    public Debugger() {
        this.isPrintEnabledProperty = new ReadOnlyBooleanWrapper(true);
        this.isWarnEnabledProperty = new ReadOnlyBooleanWrapper(false);
        this.isErrorEnabledProperty = new ReadOnlyBooleanWrapper(false);
        
        
        this.isPrintEnabledProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue && !oldValue)
                System.out.println("General Debug Output is now Enabled");
            else if (!newValue && oldValue)
                System.out.println("General Debug Output is now Disabled");
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
        print(PRINT, msg);
    }
    
    public void print(@NotNull String printType, @NotNull String msg) {
        if (isTypeEnabled(printType))
            System.out.println(printType + ": " + msg);
    }
    
    
    public <E> void printList(@NotNull List<E> list, @Nullable String footer) {
        printList(PRINT, list, footer);
    }
    
    public <E> void printList(@NotNull String printType, @NotNull List<E> list, @Nullable String footer) {
        if (!list.isEmpty())
            printBlock(printType, "list", footer, true, list.toArray());
        else
            printBlock(printType, "list", footer, true, "empty");
    }
    
    
    public void printBlock(@NotNull Runnable printAction, @Nullable String title, @Nullable String footer, boolean box) {
        printBlock(PRINT, printAction, title, footer, box);
    }
    
    @SafeVarargs
    public final <E> void printBlock(@Nullable String title, @Nullable String footer, boolean box, E... prints) {
        printBlock(PRINT, title, footer, box, prints);
    }
    
    @SafeVarargs
    public final <E> void printBlock(@Nullable String printType, @Nullable String title, @Nullable String footer, boolean box, E... prints) {
        printBlock(printType, () -> IntStream.range(0, prints.length).mapToObj(i -> "[" + i + "]: " + prints[i]).forEach(System.out::println),
                   title, footer, box);
    }
    
    
    public void printBlock(@NotNull String printType, @NotNull Runnable prints, @Nullable String title, @Nullable String footer, boolean box) {
        if (isTypeEnabled(printType)) {
            footer = "[ " + printType.toUpperCase()  + " ]" + (footer != null ? "  " + footer : "");
            
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
    
    public final ReadOnlyBooleanProperty readOnlyIsPrintEnabledProperty() {
        return isPrintEnabledProperty.getReadOnlyProperty();
    }
    
    public final boolean isPrintEnabled() {
        return isPrintEnabledProperty.get();
    }
    
    public final ReadOnlyBooleanProperty readOnlyIsWarnEnabledProperty() {
        return isWarnEnabledProperty.getReadOnlyProperty();
    }
    
    public final boolean isWarnEnabled() {
        return isWarnEnabledProperty.get();
    }
    
    public final ReadOnlyBooleanProperty readOnlyIsErrorEnabledProperty() {
        return isErrorEnabledProperty.getReadOnlyProperty();
    }
    
    public final boolean isErrorEnabled() {
        return isErrorEnabledProperty.get();
    }
    
    public final boolean isTypeEnabled(@NotNull String printType) {
        ExceptionTools.nullCheck(printType, "Print Type");
        if (printType.equalsIgnoreCase(PRINT))
            return isPrintEnabled();
        else if (printType.equalsIgnoreCase(WARN))
            return isWarnEnabled();
        else if (printType.equalsIgnoreCase(ERROR))
            return isErrorEnabled();
        else
            throw ExceptionTools.unsupported("Unrecognized Print Type: " + printType);
    }
    
    //</editor-fold>
}
