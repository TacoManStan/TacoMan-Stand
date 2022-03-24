package com.taco.tacository.util.tools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Obj {
    private Obj() { } //No Instance
    
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
    
    //<editor-fold desc="--- EXECUTION METHODS">
    
    //<editor-fold desc="> Runnable Execution">
    
    public static void run(@Nullable Runnable runnable, @NotNull Predicate<Runnable> filter, @NotNull Runnable fallbackOperation) {
        if (runnable != null && filter.test(runnable))
            runnable.run();
        else
            fallbackOperation.run();
    }
    
    //
    
    public static void run(@Nullable Runnable runnable, @NotNull Predicate<Runnable> filter) { run(runnable, filter, () -> { }); }
    public static void run(@Nullable Runnable runnable, @NotNull Runnable fallbackOperation) { run(runnable, run -> true, fallbackOperation); }
    public static void run(@Nullable Runnable runnable) { run(runnable, run -> true, () -> { }); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Callable Execution">
    
    public static <V> @Nullable V call(@Nullable Callable<V> callable, @NotNull Predicate<Callable<V>> filter, @NotNull Supplier<V> fallbackSupplier, @NotNull Function<Throwable, V> exceptionHandler) {
        if (callable != null && filter.test(callable)) {
            try {
                return callable.call();
            } catch (Exception e) {
                return exceptionHandler.apply(e);
            }
        } else
            return fallbackSupplier.get();
    }
    
    //
    
    public static <V> @Nullable V call(@Nullable Callable<V> callable, @NotNull Predicate<Callable<V>> filter, @NotNull Function<Throwable, V> exceptionHandler) { return call(callable, filter, () -> null, exceptionHandler); }
    public static <V> @Nullable V call(@Nullable Callable<V> callable, @NotNull Supplier<V> fallbackSupplier, @NotNull Function<Throwable, V> exceptionHandler) { return call(callable, o -> true, fallbackSupplier, exceptionHandler); }
    public static <V> @Nullable V call(@Nullable Callable<V> callable, @NotNull Predicate<Callable<V>> filter, @NotNull Supplier<V> fallbackSupplier) { return call(callable, filter, fallbackSupplier, t -> Exc.printStackTrace(t, () -> null)); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Supplier Execution">
    
    /**
     * <p>Note: This method was added much later than {@link #getIf(Supplier, Predicate, Function) getIf} methods and is entirely independent of them, despite similar or identical functionality.</p>
     */
    public static <T> @Nullable T get(@Nullable Supplier<T> supplier, @NotNull Predicate<Supplier<T>> filter, @NotNull Supplier<T> fallbackSupplier) {
        if (supplier != null && filter.test(supplier))
            return supplier.get();
        else
            return fallbackSupplier.get();
    }
    
    //
    
    public static <T> @Nullable T get(@Nullable Supplier<T> supplier, @NotNull Predicate<Supplier<T>> filter) { return get(supplier, filter, () -> null); }
    public static <T> @Nullable T get(@Nullable Supplier<T> supplier, @NotNull Supplier<T> fallbackSupplier) { return get(supplier, sup -> true, fallbackSupplier); }
    public static <T> @Nullable T get(@Nullable Supplier<T> supplier) { return get(supplier, sup -> true, () -> null); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Consumer Execution">
    
    public static <T> void consume(@Nullable Consumer<T> consumer, @Nullable T input, @NotNull Predicate<Consumer<T>> filter, @NotNull Runnable fallbackOperation) {
        if (consumer != null && filter.test(consumer))
            consumer.accept(input);
        else
            fallbackOperation.run();
    }
    
    //
    
    public static <T> void consume(@Nullable Consumer<T> consumer, @Nullable T input, @NotNull Predicate<Consumer<T>> filter) { consume(consumer, input, filter, () -> { }); }
    public static <T> void consume(@Nullable Consumer<T> consumer, @Nullable T input, @NotNull Runnable fallbackOperation) { consume(consumer, input, cons -> true, fallbackOperation); }
    public static <T> void consume(@Nullable Consumer<T> consumer, @Nullable T input) { consume(consumer, input, cons -> true, () -> { }); }
    
    //</editor-fold>
    
    //<editor-fold desc="> Function Execution">
    
    public static <T, R> @Nullable R apply(@Nullable Function<T, R> function, @Nullable T input, @NotNull Predicate<Function<T, R>> filter, @NotNull Supplier<R> fallbackSupplier) {
        if (function != null && filter.test(function))
            return function.apply(input);
        else
            return fallbackSupplier.get();
    }
    
    //
    
    public static <T, R> @Nullable R apply(@Nullable Function<T, R> function, @Nullable T input, @NotNull Predicate<Function<T, R>> filter) { return apply(function, input, filter, () -> null); }
    public static <T, R> @Nullable R apply(@Nullable Function<T, R> function, @Nullable T input, @NotNull Supplier<R> fallbackSupplier) { return apply(function, input, func -> true, fallbackSupplier); }
    public static <T, R> @Nullable R apply(@Nullable Function<T, R> function, @Nullable T input) { return apply(function, input, func -> true, () -> null); }
    
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