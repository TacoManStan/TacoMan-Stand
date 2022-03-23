package com.taco.suit_lady._to_sort._new.interfaces.functional.filter;

import com.taco.suit_lady._to_sort._new.CompareType;
import com.taco.suit_lady.util.enums.FilterType;
import com.taco.suit_lady.util.values.numbers.N;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * <p>Defines a {@link Predicate} implementation combining a group of {@link Predicate} children.</p>
 * <p><b>Details</b></p>
 * <p><i>{@link CompoundFilter} offers a variety of {@link Property Properties} defining how the {@link #filterListProperty() Predicate Filters} assigned to this {@link CompoundFilter} are {@link Predicate#test(Object) Compared}.</i></p>
 * <ol>
 *     <li><b>{@link #filterListProperty()}:</b> The {@link ReadOnlyListProperty} containing all child {@link Predicate Predicate Filters} assigned to this {@link CompoundFilter} instance.</li>
 *     <li>
 *         <b>{@link #checkMidLoopProperty()}</b>
 *         <ul>
 *             <li>If {@code true}, the {@link CompoundFilter} <i>{@link #test(Object)}</i> implementation will {@code return} as soon as the {@code result} is known.</li>
 *             <li>If {@code false}, the {@link CompoundFilter} <i>{@link #test(Object)}</i> implementation will only {@code return} once every {@link Predicate Predicate Filter} in the {@link #filterListProperty() Filter List} has been {@link Predicate#test(Object) tested}.</li>
 *         </ul>
 *     </li>
 *     <li><b>{@link #onEmptyResultProperty()}:</b> The {@link Boolean value} returned by <i>{@link #test(Object)}</i> if the {@link #filterListProperty() Filter List} is {@link List#isEmpty() empty}.</li>
 *     <li>
 *         <b>{@link #filterTypeProperty()}:</b> The {@link FilterType} used by this {@link CompoundFilter} to {@link Predicate#test(Object) process} the {@link Predicate Predicate Filter} members of the {@link #filterListProperty() Filter List}.
 *         <ul>
 *             <li><i>See {@link FilterType} for details.</i></li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @param <T> The {@link Predicate} {@link Predicate#test(Object) Input Parameter} {@link Class Type}.
 */
public class CompoundFilter<T>
        implements Predicate<T> {
    
    private final BooleanProperty checkMidLoopProperty;
    private final BooleanProperty onEmptyResultProperty;
    private final ObjectProperty<CompareType> filterTypeProperty;
    
    private final ReadOnlyListWrapper<Predicate<T>> filterListProperty;
    
    @SafeVarargs
    public CompoundFilter(Predicate<T>... filters) {
        this(null, filters);
    }
    
    public CompoundFilter(List<Predicate<T>> filterList) {
        this(null, filterList);
    }
    
    @SafeVarargs
    public CompoundFilter(CompareType filterType, Predicate<T>... filters) {
        this(filterType, Arrays.asList(filters));
    }
    
    public CompoundFilter(CompareType filterType, List<Predicate<T>> filterList) {
        this(filterType, true, true, filterList);
    }
    
    @SafeVarargs
    public CompoundFilter(CompareType filterType, boolean checkMidLoop, boolean onEmptyResult, Predicate<T>... filters) {
        this(filterType, checkMidLoop, onEmptyResult, Arrays.asList(filters));
    }
    
    /**
     * <p>The fully parameterized {@link CompoundFilter} constructor; all other constructors must either directly or indirectly use this constructor.</p>
     *
     * @param filterType    The {@link CompareType} enum defining how this {@link CompoundFilter} compares its {@link Predicate Sub-Filters}.
     *                      If the specified value is {@code null}, the {@link CompareType} defaults to {@link CompareType#ANY}.
     * @param checkMidLoop  Tells this {@link CompoundFilter} whether it should check if it can deduce a guaranteed return value after each individual {@link Predicate Sub-Filter} is called.
     * @param onEmptyResult Tells this {@link CompoundFilter} what result should be returned if this {@link CompoundFilter} has no {@link Predicate Sub-Filters} added.
     * @param filterList    The list of {@link Predicate Sub-Filters} to be called by this {@link CompoundFilter} given the aforementioned {@link CompareType}.
     */
    public CompoundFilter(CompareType filterType, boolean checkMidLoop, boolean onEmptyResult, List<Predicate<T>> filterList) {
        if (filterList == null)
            throw new NullPointerException("Filters list cannot be null.");
        
        this.checkMidLoopProperty = new SimpleBooleanProperty(checkMidLoop);
        this.onEmptyResultProperty = new SimpleBooleanProperty(onEmptyResult);
        this.filterTypeProperty = new SimpleObjectProperty<>(filterType != null ? filterType : CompareType.ANY);
        
        this.filterListProperty = new ReadOnlyListWrapper<>(FXCollections.observableArrayList(filterList));
    }
    
    //
    
    /**
     * <p>Compares the {@link Predicate Predicate Filters} in the {@link #filterListProperty() Filter List}.</p>
     * <blockquote><i>See {@link CompoundFilter CompoundFilter Class Documentation} for in-depth information/instructions.</i></blockquote>
     *
     * @param t The {@link T object} being {@link #test(Object) tested}.
     *
     * @return The compound {@link Predicate#test(Object) Test Result} compiled by iterating the {@link #filterListProperty() Filter List}.
     */
    @Override public final boolean test(T t) {
        // Lots of room for efficiency improvements here...
        // Also, definitely going to redesign some of this if you intend on using it asynchronously.
        
        if (filterListProperty() == null)
            throw new NullPointerException("Filter array cannot be null.");
        for (Predicate<T> filter: filterListProperty())
            if (filter == null)
                throw new NullPointerException("Sub Filters must be non-null.");
        
        if (filterListProperty().isEmpty())
            return getOnEmptyResult();
        
        final int countTotal = filterListProperty().size();
        int countPassed = 0;
        int countFailed = 0;
        
        for (Predicate<T> subFilter: filterListProperty()) {
            final boolean passed = subFilter.test(t);
            if (passed)
                countPassed++;
            else
                countFailed++;
            
            if (isCheckMidLoop())
                switch (getFilterType()) {
                    // If any sub-filter PASSES, a CF of type 'ANY' is guaranteed to PASS.
                    case ANY -> { if (countPassed >= 1) return true; }
                    
                    // If any sub-filter FAILS, a CF of type 'ALL' is guaranteed to FAIL.
                    case ALL -> { if (countFailed >= 1) return false; }
                    
                    // If more than one sub-filter PASSES, a parent CF of type 'ONE_TRUE' is guaranteed to FAIL.
                    case ONE -> { if (countPassed > 1) return false; }
                    
                    // If more than one sub-filter FAILS, a parent CF of type 'ONE_FALSE' is guaranteed to FAIL.
                    case ALL_BUT_ONE -> { if (countFailed > 1) return false; }
                    
                    // If more than half of the sub-filters have FAILED, a parent CF of type 'LESS_THAN_HALF' is guaranteed to FAIL.
                    case HALF_OR_LESS -> { if (countFailed > countTotal / 2) return false; }
                    
                    // If more than half of the sub-filters have PASSED, a parent CF of type 'MORE_THAN_HALF' is guaranteed to PASS.
                    case HALF_OR_MORE -> { if (countPassed >= countTotal / 2) return true; }
                    
                    case HALF -> {
                        // If the total number of sub-filters is ODD, a parent CF of type 'HALF' is guaranteed to FAIL.
                        if (N.isOdd(countTotal) ||
                            // If more than half of the sub-filters have FAILED, a parent CF of type 'HALF' is guaranteed to FAIL.
                            countFailed > countTotal / 2)
                            return false;
                    }
                }
        }
        
        switch (getFilterType()) {
            case ANY -> { return countPassed >= 1; }
            case ALL -> { return countFailed == 0; }
            case ONE -> { return countPassed == 1; }
            case ALL_BUT_ONE -> { return countFailed == 1; }
            case HALF_OR_LESS -> { return countFailed <= countTotal / 2; }
            case HALF_OR_MORE -> { return countPassed >= countTotal / 2; }
            case HALF -> { return N.isEven(countTotal) && countPassed == countTotal / 2; }
            
            default -> throw new RuntimeException("This... What... Getting here shouldn't be... this shouldn't be possible, what the actual fuck did you do to trigger this error?");
        }
    }
    
    //
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public BooleanProperty checkMidLoopProperty() { return checkMidLoopProperty; }
    public boolean isCheckMidLoop() { return checkMidLoopProperty.get(); }
    public void setCheckMidLoop(boolean checkMidLoop) { checkMidLoopProperty.set(checkMidLoop); }
    
    public BooleanProperty onEmptyResultProperty() { return onEmptyResultProperty; }
    public boolean getOnEmptyResult() { return onEmptyResultProperty.get(); }
    public void setOnEmptyResult(boolean onEmptyResult) { onEmptyResultProperty.set(onEmptyResult); }
    
    public ObjectProperty<CompareType> filterTypeProperty() { return filterTypeProperty; }
    public CompareType getFilterType() { return filterTypeProperty.get(); }
    public void setFilterType(CompareType filterType) { filterTypeProperty.set(filterType); }
    
    
    public ReadOnlyListProperty<Predicate<T>> filterListProperty() { return filterListProperty.getReadOnlyProperty(); }
    
    //</editor-fold>
}
