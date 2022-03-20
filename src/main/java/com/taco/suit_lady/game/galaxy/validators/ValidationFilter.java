package com.taco.suit_lady.game.galaxy.validators;

import com.taco.suit_lady.game.GameComponent;
import com.taco.suit_lady.game.ui.GameViewContent;
import com.taco.suit_lady.logic.triggers.Galaxy;
import com.taco.suit_lady.util.synchronization.Lockable;
import com.taco.suit_lady.util.springable.Springable;
import com.taco.suit_lady.util.springable.SpringableWrapper;
import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.values.Value2D;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * <p>Used to define the {@link Validator#revalidate(Map) validation logic} for a {@link Validator} instance.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>Used to define the {@link Validatable#isValid() validation logic} for a {@link Validatable} instance.</li>
 *     <li>
 *         A {@link ValidationFilter} is not added directly to a {@link Validatable} instance.
 *         <ul>
 *             <li>Rather, {@link ValidationFilter ValidationFilters} are {@link Validator#addValidator(ValidationFilter) added} to the {@link Validator} returned by <i>{@link Validatable#validator()}</i></li>
 *         </ul>
 *     </li>
 *     <li>
 *         {@link ValidationFilter} Validation Process
 *         <ul>
 *             <li>Use the <i>{@link #revalidate(Map)}</i> method to {@code recalculate} the value of <i>{@link #isValid()}</i> and then {@code return} the resulting value.</li>
 *             <li>Use the <i>{@link #isValid()}</i> method to access the {@code validation status} of the {@link ValidationFilter} without {@link #revalidate(Map) recalculating} the {@code value} — i.e., <i>{@link #isValid()}</i> returns the {@code value} most recently {@code cached} by the <i>{@link #revalidate(Map)}</i> method.</li>
 *         </ul>
 *     </li>
 *     <li>
 *         Constructing a {@link ValidationFilter} Instance
 *         <ul>
 *             <li>Use one of the {@link ValidationFilter} {@link ValidationFilter#ValidationFilter(Validatable) Constructor} to define a custom {@link ValidationFilter} implementation.</li>
 *             <li>Alternatively, use one of the {@link ValidationFilter} {@link Galaxy#newValidator(Validatable, Function) Factory Methods} found in the static {@link Galaxy} utility class.</li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <T> The type of {@link Validatable} object this {@link ValidationFilter} is set to {@link #revalidate(Map) validate}.
 */
//TO-EXPAND - Examples
public abstract class ValidationFilter<T extends Validatable<T>>
        implements SpringableWrapper, Lockable, GameComponent {
    
    private final T owner;
    private final ReadOnlyBooleanWrapper validProperty;
    
    public ValidationFilter(@NotNull T owner) {
        this.owner = owner;
        this.validProperty = new ReadOnlyBooleanWrapper(false);
    }
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public final @NotNull T getOwner() { return owner; }
    
    protected final @NotNull ReadOnlyBooleanProperty readOnlyValidProperty() { return validProperty.getReadOnlyProperty(); }
    public final boolean isValid() { return validProperty.get(); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    public final boolean revalidate(@NotNull Map<String, Object> params) {
        return sync(() -> {
            final boolean valid = validate(params);
            validProperty.set(valid);
            return valid;
        });
    }
    @SafeVarargs public final boolean revalidate(@NotNull Value2D<String, Object>... params) { return revalidate(L.map(params)); }
    
    //</editor-fold>
    
    //<editor-fold desc="--- ABSTRACT ---">
    
    protected abstract boolean validate(@NotNull Map<String, Object> params);
    
    //</editor-fold>
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull GameViewContent getGame() { return getOwner().getGame(); }
    
    @Override public @NotNull Springable springable() { return getOwner(); }
    @Override public @Nullable Lock getLock() { return getOwner().getLock(); }
    
    //</editor-fold>
}
