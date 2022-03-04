package com.taco.suit_lady.util.tools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Objs {
    private Objs() { } //No Instance
    
    //<editor-fold desc="--- EQUALITY CHECKS ---">
    
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
        doIf(valueFactory, Objects::nonNull, filterPassedOperation, filterFailedOperation);
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
        return getIf(valueFactory, Objects::nonNull, filterPassedOperation, filterFailedOperation);
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
    
    //<editor-fold desc="--- FUNCTIONAL INTERFACE METHODS ---">
    
    //<editor-fold desc="> Conversion Methods">
    
    public static <V> @NotNull Runnable asRunnable(@NotNull Callable<V> callable, @Nullable Consumer<Throwable> exceptionHandler) {
        return () -> {
            try {
                callable.call();
            } catch (Exception e) {
                exConsumer(exceptionHandler).accept(e);
            }
        };
    }
    
    public static <V> @NotNull Runnable asRunnable(@NotNull Callable<V> callable) { return asRunnable(callable, null); }
    
    public static <T> @NotNull Consumer<T> asConsumer(@NotNull Runnable runnable) { return ignored -> runnable.run(); }
    
    //
    
    public static <V> @NotNull Supplier<V> asSupplier(@NotNull Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }
    
    public static <V> @NotNull Supplier<V> asSupplier(@NotNull Callable<V> callable, @Nullable Function<Throwable, V> exceptionHandler) {
        return () -> {
            try {
                return callable.call();
            } catch (Exception e) {
                return exFunction(exceptionHandler).apply(e);
            }
        };
    }
    
    public static <V> @NotNull Supplier<V> asSupplier(@NotNull Callable<V> callable) { return asSupplier(callable, null); }
    
    //
    
    @Contract(pure = true)
    public static <V> @NotNull Callable<V> asCallable(@NotNull Runnable runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }
    
    public static <V> @NotNull Callable<V> asCallable(@NotNull Supplier<V> supplier) { return supplier::get; }
    
    //<editor-fold desc=">> Conversion Methods: Internal">
    
    private static @NotNull Consumer<Throwable> exConsumer(@Nullable Consumer<Throwable> exceptionHandler) { return exceptionHandler != null ? exceptionHandler : Throwable::printStackTrace; }
    private static @NotNull <V> Function<Throwable, V> exFunction(@Nullable Function<Throwable, V> exceptionHandler) {
        return exceptionHandler != null ? exceptionHandler : t -> {
            t.printStackTrace();
            return null;
        };
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
//    public static <T> @NotNull Supplier<T> getSupplier(@Nullable Supplier<T> input, @Nullable Supplier<Supplier<T>> onFailSupplier) { return getSupplier()}
//    public static <T> @NotNull Supplier<T> getSupplier(@Nullable Supplier<T> input, @Nullable Predicate<Supplier<T>> filter, @Nullable Supplier<Supplier<T>> onFailSupplier) {
//        if (getPredicate(filter).test(input))
//            return input;
//        else {
//
//        }
//    }
//
//    public static <T> @NotNull Predicate<T> getPredicate(@Nullable Predicate<T> input) { return getPredicate(input, getPredicate(), Objs::getPredicate); }
//    public static <T> @NotNull Predicate<T> getPredicate(@Nullable Predicate<T> input, @Nullable Supplier<Predicate<T>> onFailSupplier) { return getPredicate(input, getPredicate(), onFailSupplier); }
//    public static <T> @NotNull Predicate<T> getPredicate(@Nullable Predicate<T> input, @Nullable Predicate<Predicate<T>> filter, @Nullable Supplier<Predicate<T>> onFailSupplier) {
//        if (getPredicate(filter).test(input)) {
//            return input;
//        } else {
//            onFailSupplier = onFailSupplier != null ? onFailSupplier : Objs::getPredicate;
//            return onFailSupplier.get();
//        }
//    }
//    public static <T> @NotNull Predicate<T> getPredicate() { return Objects::nonNull; }
//
//    public static @NotNull Runnable getRunnable(@Nullable Runnable input, @Nullable Predicate<Runnable> filter, @Nullable Supplier<Runnable> onFailSupplier) {
//        if (input != null)
//            return input;
//        else {
//
//        }
//    }
//
//    //</editor-fold>
}