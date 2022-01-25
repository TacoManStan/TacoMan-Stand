package com.taco.suit_lady.util.tools;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ObjectsSL {
    private ObjectsSL() { } //No Instance
    
    //<editor-fold desc="--- EQUALS ---">
    
    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }
    
    public static boolean equalsExcludeNull(Object obj1, Object obj2) {
        return obj1 != null && obj2 != null && Objects.equals(obj1, obj2);
    }
    
    //
    
    public static boolean equalsAny(Object obj, Object... objs) {
        return equals(obj, false, true, false, objs);
    }
    
    public static boolean equalsAll(Object obj, Object... objs) {
        return equals(obj, true, true, false, objs);
    }
    
    public static boolean equals(Object obj, boolean requireAll, boolean allowNull, boolean deep, Object... objs) {
        if (objs == null || (!allowNull && obj == null))
            return false;
        else if (objs.length == 1)
            return equalsImpl(obj, objs[0], allowNull, deep);
        else {
            for (Object arrObj: objs)
                if (equalsImpl(obj, arrObj, allowNull, deep)) {
                    if (requireAll)
                        return false;
                } else {
                    if (!requireAll)
                        return true;
                }
            return requireAll;
        }
    }
    
    private static boolean equalsImpl(Object obj1, Object obj2, boolean allowNull, boolean deep) {
        if (allowNull && obj1 == null && obj2 == null)
            return true;
        else
            return deep ? Objects.deepEquals(obj1, obj2) : Objects.equals(obj1, obj2);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CONDITIONAL OPERATIONS ---">
    
    public static <T> void doIfNonNull(@NotNull Supplier<T> valueFactory, @NotNull Consumer<T> filterPassedOperation) {
        doIfNonNull(valueFactory, filterPassedOperation, null);
    }
    
    public static <T> void doIfNonNull(@NotNull Supplier<T> valueFactory, @NotNull Consumer<T> filterPassedOperation, @Nullable Consumer<T> filterFailedOperation) {
        doIf(valueFactory, value -> value != null, filterPassedOperation, filterFailedOperation);
    }
    
    
    public static <T> void doIf(@NotNull Supplier<T> valueFactory, @NotNull Predicate<T> filter, @NotNull Consumer<T> filterPassedOperation) {
        doIf(valueFactory, filter, filterPassedOperation, null);
    }
    
    public static <T> void doIf(@NotNull Supplier<T> valueFactory, @NotNull Predicate<T> filter, @NotNull Consumer<T> filterPassedOperation, @Nullable Consumer<T> filterFailedOperation) {
        T input = valueFactory.get();
        if (filter.test(input))
            filterPassedOperation.accept(input);
        else if (filterFailedOperation != null)
            filterFailedOperation.accept(input);
    }
    
    
    public static <T, R> @Nullable R getIfNonNull(@NotNull Supplier<T> valueFactory, @NotNull Function<T, R> filterPassedOperation) {
        return getIfNonNull(valueFactory, filterPassedOperation, null);
    }
    
    public static <T, R> @Nullable R getIfNonNull(@NotNull Supplier<T> valueFactory, @NotNull Function<T, R> filterPassedOperation, @Nullable Function<T, R> filterFailedOperation) {
        return getIf(valueFactory, value -> value != null, filterPassedOperation, filterFailedOperation);
    }
    
    
    public static <T, R> @Nullable R getIf(@NotNull Supplier<T> valueFactory, @NotNull Predicate<T> filter, @NotNull Function<T, R> filterPassedOperation) {
        return getIf(valueFactory, filter, filterPassedOperation, null);
    }
    
    public static <T, R> @Nullable R getIf(@NotNull Supplier<T> valueFactory, @NotNull Predicate<T> filter, @NotNull Function<T, R> filterPassedOperation, @Nullable Function<T, R> filterFailedOperation) {
        T input = valueFactory.get();
        if (filter.test(input))
            return filterPassedOperation.apply(input);
        else if (filterFailedOperation != null)
            return filterFailedOperation.apply(input);
        return null;
    }
    
    //</editor-fold>
}