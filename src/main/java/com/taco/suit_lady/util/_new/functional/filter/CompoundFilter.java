package com.taco.suit_lady.util._new.functional.filter;

import com.taco.util.numbers.Numbers;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.Arrays;
import java.util.List;

public class CompoundFilter<T>
        implements Filter<T>
{
    public static void main(String[] args)
    {
        Filter<String> filter1 = s -> s.length() > 3;
        Filter<String> filter2 = s -> s.contains("s");
        Filter<String> filter3 = s -> !s.isEmpty();
        
        String testString = "superman is here";
        CompoundFilter<String> cf = new CompoundFilter<>(FilterType.ALL, Arrays.asList(filter1, filter2));
        cf.filterListProperty.add(filter3);
        cf.filterListProperty().add(filter3);
        
        System.out.println("Result for String \"" + testString + "\": " + cf.filter(testString));
    }
    
    //
    
    private final BooleanProperty checkMidLoopProperty;
    private final BooleanProperty onEmptyResultProperty;
    private final ObjectProperty<FilterType> filterTypeProperty;
    
    private final ReadOnlyListWrapper<Filter<T>> filterListProperty;
    
    public CompoundFilter(List<Filter<T>> filterList)
    {
        this(null, filterList);
    }
    
    @SafeVarargs
    public CompoundFilter(Filter<T>... filters)
    {
        this(null, filters);
    }
    
    @SafeVarargs
    public CompoundFilter(FilterType filterType, Filter<T>... filters)
    {
        this(filterType, Arrays.asList(filters));
    }
    
    public CompoundFilter(FilterType filterType, List<Filter<T>> filterList)
    {
        this(filterType, true, true, filterList);
    }
    
    @SafeVarargs
    public CompoundFilter(FilterType filterType, boolean checkMidLoop, boolean onEmptyResult, Filter<T>... filters)
    {
        this(filterType, checkMidLoop, onEmptyResult, Arrays.asList(filters));
    }
    
    /**
     * <p>The fully parameterized {@link CompoundFilter} constructor; all other constructors must either directly or indirectly use this constructor.</p>
     *
     * @param filterType    The {@link FilterType} enum defining how this {@link CompoundFilter} compares its {@link Filter Sub-Filters}.
     *                      If the specified value is {@code null}, the {@link FilterType} defaults to {@link FilterType#ANY}.
     * @param checkMidLoop  Tells this {@link CompoundFilter} whether it should check if it can deduce a guaranteed return value after each individual {@link Filter Sub-Filter} is called.
     * @param onEmptyResult Tells this {@link CompoundFilter} what result should be returned if this {@link CompoundFilter} has no {@link Filter Sub-Filters} added.
     * @param filterList    The list of {@link Filter Sub-Filters} to be called by this {@link CompoundFilter} given the aforementioned {@link FilterType}.
     */
    public CompoundFilter(FilterType filterType, boolean checkMidLoop, boolean onEmptyResult, List<Filter<T>> filterList)
    {
        if (filterList == null)
            throw new NullPointerException("Filters list cannot be null.");
        
        this.checkMidLoopProperty = new SimpleBooleanProperty(checkMidLoop);
        this.onEmptyResultProperty = new SimpleBooleanProperty(onEmptyResult);
        this.filterTypeProperty = new SimpleObjectProperty<>(filterType != null ? filterType : FilterType.ANY);
        
        this.filterListProperty = new ReadOnlyListWrapper<>(FXCollections.observableArrayList(filterList));
    }
    
    //
    
    @Override
    public final boolean filter(T t)
    {
        // Lots of room for efficiency improvements here...
        // Also, definitely going to redesign some of this if you intend on using it asynchronously.
        
        if (filterListProperty() == null)
            throw new NullPointerException("Filter array cannot be null.");
        for (Filter<T> filter: filterListProperty())
            if (filter == null)
                throw new NullPointerException("All Sub Filters must be non-null.");
        
        if (filterListProperty().isEmpty())
            return true;
        
        final int countTotal = filterListProperty().size();
        int countPassed = 0;
        int countFailed = 0;
        
        for (Filter<T> subFilter: filterListProperty())
        {
            final boolean passed = subFilter.filter(t);
            if (passed)
                countPassed++;
            else
                countFailed++;
            
            if (isCheckMidLoop())
                switch (getFilterType())
                {
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
                        if (Numbers.isOdd(countTotal) ||
                            // If more than half of the sub-filters have FAILED, a parent CF of type 'HALF' is guaranteed to FAIL.
                            countFailed > countTotal / 2)
                            return false;
                    }
                }
        }
        
        switch (getFilterType())
        {
            case ANY -> { return countPassed >= 1; }
            case ALL -> { return countFailed == 0; }
            case ONE -> { return countPassed == 1; }
            case ALL_BUT_ONE -> { return countFailed == 1; }
            case HALF_OR_LESS -> { return countFailed <= countTotal / 2; }
            case HALF_OR_MORE -> { return countPassed >= countTotal / 2; }
            case HALF -> { return Numbers.isEven(countTotal) && countPassed == countTotal / 2; }
            
            default -> throw new RuntimeException("This... What... Getting here shouldn't be... this shouldn't be possible, what the actual fuck did you do to trigger this error?");
        }
    }
    
    //
    
    //<editor-fold desc="--- PROPERTIES ---">
    
    public BooleanProperty checkMidLoopProperty()
    {
        return checkMidLoopProperty;
    }
    
    public boolean isCheckMidLoop()
    {
        return checkMidLoopProperty.get();
    }
    
    public void setCheckMidLoop(boolean checkMidLoop)
    {
        checkMidLoopProperty.set(checkMidLoop);
    }
    
    //
    
    public BooleanProperty onEmptyResultProperty()
    {
        return onEmptyResultProperty;
    }
    
    public boolean getOnEmptyResult()
    {
        return onEmptyResultProperty.get();
    }
    
    public void setOnEmptyResult(boolean onEmptyResult)
    {
        onEmptyResultProperty.set(onEmptyResult);
    }
    
    //
    
    public ObjectProperty<FilterType> filterTypeProperty()
    {
        return filterTypeProperty;
    }
    
    public FilterType getFilterType()
    {
        return filterTypeProperty.get();
    }
    
    public void setFilterType(FilterType filterType)
    {
        filterTypeProperty.set(filterType);
    }
    
    //
    
    public ReadOnlyListProperty<Filter<T>> filterListProperty()
    {
        return filterListProperty.getReadOnlyProperty();
    }
    
    //</editor-fold>
}
