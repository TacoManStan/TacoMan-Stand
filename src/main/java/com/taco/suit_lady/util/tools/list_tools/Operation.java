package com.taco.suit_lady.util.tools.list_tools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Operation<E>(int movedFromIndex, int movedToIndex, E contents) {
    
    public Inferred infer() {
        if (movedFromIndex == -1 && movedToIndex == -1 && contents == null)
            return Inferred.DATA_EMPTY;
        else if (movedFromIndex == -1 && movedToIndex == -1 && contents != null)
            return Inferred.DATA_VALUE;
        else if (contents == null)
            return Inferred.DATA_INDEX;
        else if (movedFromIndex == -1 && movedToIndex >= 0)
            return Inferred.EVENT_ADD;
        else if (movedFromIndex >= 0 && movedToIndex == -1)
            return Inferred.EVENT_REMOVE;
        else if (movedFromIndex == movedToIndex && movedFromIndex >= 0 && movedToIndex >= 0)
            return Inferred.EVENT_NO_CHANGE;
        else if (movedFromIndex >= 0 && movedToIndex >= 0)
            return Inferred.EVENT_MOVE;
        return Inferred.UNKNOWN;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Permutation{" +
               "\"" + contents + "\"" +
               ", fromIndex=" + movedFromIndex +
               ", toIndex=" + movedToIndex +
               ", inferred=" + infer() +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operation<?> that)) return false;
        
        if (movedFromIndex != that.movedFromIndex) return false;
        if (movedToIndex != that.movedToIndex) return false;
        return contents.equals(that.contents);
    }
    
    @Override
    public int hashCode() {
        int result = movedFromIndex;
        result = 31 * result + movedToIndex;
        result = 31 * result + contents.hashCode();
        return result;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- STATIC FACTORIES ---">
    
    @Contract("_, _ -> new")
    public static <E> @NotNull Operation<E> empty(int from, int to) {
        return new Operation<>(from, to, null);
    }
    
    @Contract(" -> new")
    public static <E> @NotNull Operation<E> empty() {
        return new Operation<>(-1, -1, null);
    }
    
    public static <E> @NotNull Operation<E> indexOnly(int to, int from) {
        return new Operation<>(to, from, null);
    }
    
    public static <E> @NotNull Operation<E> valueOnly(E value) {
        return new Operation<>(-1, -1, value);
    }
    
    //</editor-fold>
    
    public enum Inferred {
        UNKNOWN, INVALID,
        DATA_EMPTY, DATA_INDEX, DATA_VALUE,
        EVENT_NO_CHANGE, EVENT_ADD, EVENT_REMOVE, EVENT_MOVE
    }
    
    enum OperationType {
        ADDITION, REMOVAL, PERMUTATION, UPDATION
    }
    
    enum TriggerType {
        PRE_CHANGE, POST_CHANGE, CHANGE, UPDATE
    }
}