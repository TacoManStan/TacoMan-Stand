package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.tools.ArraysSL;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Operation<E>(int movedFromIndex, int movedToIndex, E contents) {
    
    public InferredType infer() {
        if (movedFromIndex == -1 && movedToIndex == -1 && contents == null)
            return InferredType.DATA_EMPTY;
        else if (movedFromIndex == -1 && movedToIndex == -1 && contents != null)
            return InferredType.DATA_VALUE;
        else if (contents == null)
            return InferredType.DATA_INDEX;
        else if (movedFromIndex == -1 && movedToIndex >= 0)
            return InferredType.EVENT_ADD;
        else if (movedFromIndex >= 0 && movedToIndex == -1)
            return InferredType.EVENT_REMOVE;
        else if (movedFromIndex == movedToIndex && movedFromIndex >= 0 && movedToIndex >= 0)
            return InferredType.EVENT_NO_CHANGE;
        else if (movedFromIndex >= 0 && movedToIndex >= 0)
            return InferredType.EVENT_MOVE;
        return InferredType.UNKNOWN;
    }
    
    //<editor-fold desc="--- IMPLEMENTATIONS ---">
    
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Operation{" +
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
    
    public enum InferredType {
        UNKNOWN, INVALID,
        DATA_EMPTY, DATA_INDEX, DATA_VALUE,
        EVENT_NO_CHANGE, EVENT_ADD, EVENT_REMOVE, EVENT_MOVE
    }
    
    public enum OperationType {
        ADDITION, REMOVAL, PERMUTATION, UPDATION
    }
    
    public enum TriggerType {
        PRE_CHANGE, POST_CHANGE, CHANGE, UPDATE
    }
    
    
    public enum OpReturnType {
        
        /**
         * Indicates that a reference to the specified list object should be returned.
         */
        SELF(OperationType.ADDITION, OperationType.PERMUTATION),
        
        /**
         * Indicates that a reference to the previous list object occupying the element changed by the operation should be returned.
         */
        PRIOR(OperationType.ADDITION, OperationType.REMOVAL, OperationType.PERMUTATION),
        
        /**
         * Indicates that a reference to the default value for the list should be returned.
         */
        DEFAULT(OperationType.values()),
        
        /**
         * Indicates that null should always be returned.
         */
        NONE(OperationType.values());
        
        //
        
        private final OperationType[] validOperations;
        
        OpReturnType(OperationType... validOperations) {
            this.validOperations = validOperations;
        }
        
        public boolean isOpValid(OperationType opType) {
            return ArraysSL.contains(opType, validOperations);
        }
    }
}