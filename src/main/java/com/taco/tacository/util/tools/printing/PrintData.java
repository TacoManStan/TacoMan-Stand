package com.taco.tacository.util.tools.printing;

import com.taco.tacository.util.tools.Props;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.Nullable;

public class PrintData {
    
    private final StringProperty titleProperty;
    private final BooleanProperty enabledProperty;
    private final BooleanProperty printPrefixProperty;
    
    protected PrintData(String title, boolean enabled, boolean printPrefix) {
        this.titleProperty = new SimpleStringProperty(title);
        this.enabledProperty = new SimpleBooleanProperty(enabled);
        this.printPrefixProperty = new SimpleBooleanProperty(printPrefix);
    }
    protected PrintData() { this(null, true, true); }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final StringProperty titleProperty() { return titleProperty; }
    public final String getTitle() { return titleProperty.get(); }
    public final String setTitle(@Nullable String newValue) { return Props.setProperty(titleProperty, newValue); }
    
    public final BooleanProperty enabledProperty() { return enabledProperty; }
    public final boolean isEnabled() { return enabledProperty.get(); }
    public final boolean setEnabled(boolean newValue) { return Props.setProperty(enabledProperty, newValue); }
    
    public final BooleanProperty printPrefixProperty() { return printPrefixProperty; }
    public final boolean isPrintPrefix() { return printPrefixProperty.get(); }
    public final boolean setPrintPrefix(boolean newValue) { return Props.setProperty(printPrefixProperty, newValue); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //<editor-fold desc="> Generic Print Methods">
    
    //<editor-fold desc=">> Standard Print Methods">
    
    public void print(@Nullable Object msg) { print(msg, false); }
    
    //</editor-fold>
    
    //<editor-fold desc=">> Error Print Methods">
    
    public void err(@Nullable Object msg) { print(msg, true); }
    
    //</editor-fold>
    
    //<editor-fold desc=">> Internal: Generic Print Methods">
    
    private void print(@Nullable Object msg, boolean err) {
        if (isEnabled())
            Printer.print(msg, getTitle(), isPrintPrefix(), err);
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
}
