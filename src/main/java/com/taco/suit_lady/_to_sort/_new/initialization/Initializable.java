package com.taco.suit_lady._to_sort._new.initialization;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Defines implementing {@link Object Objects} as containing an {@link Initializer} that is responsible for {@link #init(Object...) Initializing} this {@link Initializable} implementation.</p>
 * <p><b>Details</b></p>
 * <ol>
 *     <li>Each {@link Initializable} implementation is {@link #init(Object...) initialized} by the {@link Initializer} returned by the abstract <i>{@link #initializer()}</i> method.</li>
 *     <li>{@link Initializable} also offers a variety of {@code default passthrough methods} to the {@link #initializer() Initializer}.</li>
 * </ol>
 *
 * @param <T> The {@link Initializable} {@link Class Type} of this {@link Initializable} implementation.
 *
 * @see Initializer
 */
//TO-EXPAND: Examples
public interface Initializable<T extends Initializable<T>> {
    
    /**
     * <p>Returns the {@link Initializer} instance responsible for {@link #init(Object...) initializing} this {@link Initializable} object.</p>
     *
     * @return The {@link Initializer} instance responsible for {@link #init(Object...) initializing} this {@link Initializable} object.
     */
    @NotNull Initializer<T> initializer();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i>{@link #initializer()}<b>.</b>{@link Initializer#init(Object...) init}<b>(</b>params<b>)</b></i></blockquote>
     *
     * @param params The {@code array} of {@link Object Objects} providing any {@code parameter values} required to {@code initialize} this {@link Initializable} object.
     *
     * @return This {@link Initializable} object.
     */
    default T init(@NotNull Object... params) { return initializer().init(params); }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i>{@link #initializer()}<b>.</b>{@link Initializer#shutdown(Object...) shutdown}<b>(</b>params<b>)</b></i></blockquote>
     *
     * @param params The {@code array} of {@link Object Objects} providing any {@code parameter values} required to {@code shutdown} this {@link Initializable} object.
     *
     * @return This {@link Initializable} object.
     */
    default T shutdown(@NotNull Object... params) { return initializer().shutdown(params); }
    
    /**
     * <p><b>Passthrough Definition</b></p>
     * <blockquote><i>{@link #initializer()}<b>.</b>{@link Initializer#isInitialized() isInitialized()}</i></blockquote>
     * <p><b>Details</b></p>
     * <ol>
     *     <li>If this {@link Initializable} has already been {@link #init(Object...) initialized}, throw an {@link Initializer#throwInitException() Exception}.</li>
     * </ol>
     */
    default void initCheck() {
        if (initializer().isInitialized())
            initializer().throwInitException();
    }
    
    //</editor-fold>
}
