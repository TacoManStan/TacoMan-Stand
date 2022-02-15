package com.taco.suit_lady._to_sort._new.initialization;

import com.taco.suit_lady.util.Lockable;
import com.taco.suit_lady.util.tools.ExceptionsSL;
import org.jetbrains.annotations.NotNull;

public interface Initializable<T extends Initializable<T>> {
    
    @NotNull Initializer<T> initializer();
    
    //<editor-fold desc="--- DEFAULT METHODS ---">
    
    default T init(@NotNull Object... params) { return initializer().init(params); }
    default T shutdown(@NotNull Object... params) { return initializer().shutdown(params); }
    
    default void initCheck() {
        if (initializer().isInitialized())
            initializer().throwInitException();
    }
    
    //</editor-fold>
}
