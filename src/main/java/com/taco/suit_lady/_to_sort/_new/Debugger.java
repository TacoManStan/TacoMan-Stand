package com.taco.suit_lady._to_sort._new;

import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.list_tools.Operation;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Debugger {
    
    private final ReadOnlyBooleanWrapper isGeneralEnabledProperty;
    private final ReadOnlyBooleanWrapper isWarnEnabledProperty;
    private final ReadOnlyBooleanWrapper isErrorEnabledProperty;
    
    public Debugger() {
        this.isGeneralEnabledProperty = new ReadOnlyBooleanWrapper(false);
        this.isWarnEnabledProperty = new ReadOnlyBooleanWrapper(false);
        this.isErrorEnabledProperty = new ReadOnlyBooleanWrapper(false);
        
        
        this.isGeneralEnabledProperty.addListener((observable, oldValue, newValue) -> {
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
    
    private static @NotNull ObservableList<String> initTestList() {
        System.out.println("Creating List...");
        final ObservableList<String> list = FXCollections.observableArrayList();
        
        System.out.println("Populating List...");
        list.addAll("Dinner", "Elephant", "33", "Accelerator", "Zebra", "Eggplant", "Walrus", "Apple", "Tree", "Aardvark");
        
        System.out.println("Setting Listeners...");
        
        return list;
    }
    
    public void printList(@NotNull List<String> list, @Nullable String footer) {
        if (!list.isEmpty())
            doPrint(() -> list.forEach(s -> System.out.println("[" + list.indexOf(s) + "]: " + s)), "list", footer, true);
        else
            doPrint(() -> System.out.println("empty"), "list", footer, true);
    }
    
    public void doPrint(@NotNull Runnable prints, @Nullable String title, @Nullable String footer, boolean box) {
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
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final ReadOnlyBooleanProperty readOnlyIsGeneralEnabledProperty() {
        return isGeneralEnabledProperty.getReadOnlyProperty();
    }
    
    public final boolean isGeneralEnabled() {
        return isGeneralEnabledProperty.get();
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
    
    //</editor-fold>
    
    //<editor-fold desc="--- CLASS BODY ---">
    
    public void print(String msg) {
        if (isGeneralEnabled())
            System.out.println("STATUS:  " + msg);
    }
    
    public void warn(String msg) {
        if (isWarnEnabled())
            System.out.println("WARNING: " + msg);
    }
    
    public void error(String msg) {
        if (isErrorEnabled())
            System.out.println("ERROR:   " + msg);
    }
    
    //</editor-fold>
}
