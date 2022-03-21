package com.taco.suit_lady.util.values.params;

import com.taco.suit_lady.util.tools.list_tools.L;
import com.taco.suit_lady.util.values.Value2D;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * <p>A static utility class defining utility methods pertaining to a {@link Paramable} implementation.</p>
 *
 * @see Paramable
 */
//TO-EXPAND
@SuppressWarnings("rawtypes")
public class Params {
    private Params() { } //No Instance
    
    //<editor-fold desc="--- VALIDATION METHODS ---">
    
    public static boolean PRINT_ON_FAIL = true;
    
    public static boolean validateParams(@NotNull List<Value2D<?, Class<?>>> paramReqs, @NotNull Map<?, Object> params, boolean printOnFail) {
        return L.validateMap(params, printOnFail, paramReqs);
    }
    public static boolean validateParams(@NotNull List<Value2D<?, Class<?>>> paramReqs, @NotNull Map<?, Object> params) {
        return validateParams(paramReqs, params, PRINT_ON_FAIL);
    }
    @SafeVarargs public static boolean validateParams(@NotNull List<Value2D<?, Class<?>>> paramReqs, @NotNull Value2D<Object, Object>... params) {
        return validateParams(paramReqs, L.map(params), PRINT_ON_FAIL);
    }
    
    public static boolean validateParams(@NotNull Paramable paramable, @NotNull Map<?, Object> params, boolean printOnFail) {
        return validateParams(paramable.requiredParams(), params, printOnFail);
    }
    public static boolean validateParams(@NotNull Paramable paramable, @NotNull Map<?, Object> params) {
        return validateParams(paramable, params, PRINT_ON_FAIL);
    }
    @SafeVarargs public static boolean validateParams(@NotNull Paramable paramable, @NotNull Value2D<Object, Object>... params) {
        return validateParams(paramable, L.map(params), PRINT_ON_FAIL);
    }
    
    //</editor-fold>
}
