package com.taco.suit_lady.game.galaxy.validators;

import com.taco.suit_lady.game.WrappedGameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.tools.printer.Print;
import com.taco.suit_lady.util.values.ValuePair;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Validator<T extends Validatable<T>>
        implements WrappedGameComponent {
    
    private final T owner;
    
    private final ListProperty<ValidationFilter<T>> validatorsProperty;
    private final ReadOnlyBooleanWrapper validProperty;
    
    public Validator(@NotNull T owner) {
        this.owner = owner;
        
        this.validatorsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.validProperty = new ReadOnlyBooleanWrapper(false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull T getOwner() { return owner; }
    
    protected final @NotNull ReadOnlyBooleanProperty readOnlyValidProperty() { return validProperty.getReadOnlyProperty(); }
    public final boolean isValid() { return validProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    //<editor-fold desc="> Add/Remove Methods">
    
    public final boolean addValidator(@NotNull ValidationFilter<T> validationFilter) { return sync(() -> validatorsProperty.contains(validationFilter) || validatorsProperty.add(validationFilter)); }
    @SafeVarargs public final boolean addValidators(@NotNull ValidationFilter<T>... validationFilters) { return sync(() -> validatorsProperty.addAll(validationFilters)); }
    
    public final boolean removeValidator(@NotNull ValidationFilter<T> validationFilter) { return sync(() -> !validatorsProperty.contains(validationFilter) || validatorsProperty.remove(validationFilter)); }
    @SafeVarargs public final boolean removeValidators(@NotNull ValidationFilter<T>... validationFilters) { return sync(() -> validatorsProperty.removeAll(validationFilters)); }
    @SafeVarargs public final boolean retainValidators(@NotNull ValidationFilter<T>... validationFilters) { return sync(() -> validatorsProperty.retainAll(validationFilters)); }
    
    public final boolean clearValidators() { return sync(() -> retainValidators()); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Validation Methods">
    
    public boolean revalidate(@NotNull Map<String, Object> params) {
        return sync(() -> {
            final boolean valid = validatorsProperty.isEmpty() || validatorsProperty.stream().anyMatch(v -> {
                final boolean valid2 = v.revalidate(params);
                printer().get(getClass()).print("Valid 2: " + valid2);
                return valid2;
            });
            printer().get(getClass()).print("Valid: " + valid);
            validProperty.set(valid);
            return valid;
        });
    }
    @SafeVarargs public final boolean revalidate(@NotNull ValuePair<String, Object>... params) { return revalidate(L.map(params)); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    //</editor-fold>
}

//TODO: Add Locker interface for containing logic pertaining to how a particular class is synchronized, offering mapping to multiple lock objects, lock types, auto-sync strategies, etc.