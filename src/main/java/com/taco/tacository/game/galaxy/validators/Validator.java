package com.taco.tacository.game.galaxy.validators;

import com.taco.tacository.game.GameComponent;
import com.taco.tacository.game.galaxy.abilities.Ability;
import com.taco.tacository.game.ui.GameViewContent;
import com.taco.tacository.util.synchronization.Lockable;
import com.taco.tacository.util.springable.Springable;
import com.taco.tacository.util.springable.SpringableWrapper;
import com.taco.tacository.util.tools.list_tools.L;
import com.taco.tacository.util.values.Value2D;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * <p>Defines the {@link Validator} for a {@link Validatable} implementation.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>{@link Validator Validators} contain a {@link ListProperty List} of {@link ValidationFilter Validation Filters} that together define the {@link #revalidate(Map) validation logic} for the {@link Validatable} this {@link Validator} is assigned to.</li>
 *     <p><i>See {@link Ability} for a {@link Validatable} implementation example.</i></p>
 * </ol>
 *
 * @param <T> The type of {@link Validatable} implementation this {@link Validator} is assigned to.
 *
 * @see ValidationFilter
 * @see Validatable
 * @see Ability
 */
//TO-EXPAND - Examples
public class Validator<T extends Validatable<T>>
        implements SpringableWrapper, Lockable, GameComponent {
    
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
    @SafeVarargs public final boolean revalidate(@NotNull Value2D<String, Object>... params) { return revalidate(L.map(params)); }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public final @NotNull GameViewContent getContent() { return getOwner().getGame(); }
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    @Override public @Nullable Lock getLock() { return getOwner().getLock(); }
    
    //</editor-fold>
}

//TODO: Add Locker interface for containing logic pertaining to how a particular class is synchronized, offering mapping to multiple lock objects, lock types, auto-sync strategies, etc.