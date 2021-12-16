package com.taco.suit_lady._to_sort._new;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class Debugger {
    
    private final ReadOnlyBooleanWrapper isGeneralEnabledProperty;
    private final ReadOnlyBooleanWrapper isWarnEnabledProperty;
    private final ReadOnlyBooleanWrapper isErrorEnabledProperty;
    
    protected Debugger() {
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
