package com.taco.suit_lady.util.enums;

import com.taco.suit_lady.util.tools.list_tools.A;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum FilterType
        implements DefaultableEnum<FilterType> {
    
    ANY, ALL, ONE, NONE;
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Override public @NotNull FilterType defaultValue() { return FilterType.ALL; }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOGIC ---">
    
    @SafeVarargs public final <T> @NotNull ArrayList<T> filter(@NotNull List<T> input, @NotNull Predicate<T>... filters) {
        if (A.isEmpty(filters))
            return new ArrayList<>();
        return input.stream().filter(getFilter(filters)).collect(Collectors.toCollection(ArrayList::new));
    }
    
    @SafeVarargs public final <T> boolean matches(@NotNull T input, @NotNull Predicate<T>... filters) {
        if (A.isEmpty(filters))
            return false;
        for (Predicate<T> filter: filters) {
            final boolean passed = filter.test(input);
            if (passed && (equals(FilterType.ALL) ||
                           equals(FilterType.ANY) ||
                           equals(FilterType.ONE)))
                return true;
            return !passed && equals(FilterType.NONE);
        }
        return false;
    }
    
    @SafeVarargs public final <T> boolean matchesAny(@NotNull List<T> input, @NotNull Predicate<T>... filters) {
        if (A.isEmpty(filters))
            return false;
        return input.stream().anyMatch(getFilter(filters));
    }
    
    @SafeVarargs public final <T> boolean matchesAll(@NotNull List<T> input, @NotNull Predicate<T>... filters) {
        if (A.isEmpty(filters))
            return false;
        return input.stream().allMatch(getFilter(filters));
    }
    
    @SafeVarargs public final <T> boolean matches(boolean all, @NotNull List<T> input, @NotNull Predicate<T>... filters) {
        return all ? matchesAll(input, filters) : matchesAny(input, filters);
    }
    
    @SafeVarargs public final @NotNull <T> Predicate<T> getFilter(@NotNull Predicate<T>... filters) {
        return t -> {
            int passedCount = 0;
            for (Predicate<T> filter: filters) {
                final boolean passed = filter.test(t);
                if (passed)
                    passedCount++;
                
                if (passed && equals(FilterType.ANY))
                    return true;
                if (passed && equals(FilterType.NONE))
                    return false;
                if (!passed && equals(FilterType.ALL))
                    return false;
                if (passedCount > 1 && (equals(FilterType.ONE)))
                    return false;
            }
            return passedCount != 0 || (!equals(FilterType.ALL) &&
                                        !equals(FilterType.ONE) &&
                                        !equals(FilterType.ANY));
        };
    }
    
    //</editor-fold>
}
