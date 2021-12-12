package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TaskTools;
import com.taco.suit_lady.util.tools.list_tools.Operation.OperationType;
import com.taco.suit_lady.util.tools.list_tools.Operation.TriggerType;
import com.taco.util.obj_traits.common.Nameable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>A static utility class containing methods and interfaces</p>
 */
public final class ListTools {
    
    private ListTools() { } // No Instance
    
    //<editor-fold desc="--- LIST LISTENING ---">
    
    //<editor-fold desc="--- OPERATION HANDLER APPLY METHODS ---">
    
    public static <E> OperationHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull OperationListener<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OperationHandler<E> applyListener(@Nullable ReentrantLock lock, @NotNull ObservableList<E> list, @NotNull OperationListener<E> listener) {
        return OperationHandler.wrap(lock, list, listener).apply();
    }
    
    //
    
    public static <E> OperationHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull SimpleOperationListener<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OperationHandler<E> applyListener(@Nullable ReentrantLock lock, @NotNull ObservableList<E> list, @NotNull SimpleOperationListener<E> listener) {
        return applyListener(lock, list, (OperationListener<E>) listener);
    }
    
    
    public static <E> OperationHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull OperationResponder<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OperationHandler<E> applyListener(@Nullable ReentrantLock lock, @NotNull ObservableList<E> list, @NotNull OperationResponder<E> listener) {
        return applyListener(lock, list, (OperationListener<E>) listener);
    }
    
    
    public static <E> OperationHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull UnlinkedOperationResponder<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OperationHandler<E> applyListener(@Nullable ReentrantLock lock, @NotNull ObservableList<E> list, @NotNull UnlinkedOperationResponder<E> listener) {
        return applyListener(lock, list, (OperationListener<E>) listener);
    }
    
    
    public static <E> OperationHandler<E> applyListener(@NotNull ObservableList<E> list, @NotNull SimpleOperationResponder<E> listener) {
        return applyListener(null, list, listener);
    }
    
    public static <E> OperationHandler<E> applyListener(@Nullable ReentrantLock lock, @NotNull ObservableList<E> list, @NotNull SimpleOperationResponder<E> listener) {
        return applyListener(lock, list, (OperationListener<E>) listener);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- CLASSES ---">
    
    //<editor-fold desc="--- INTERFACES ---">
    
    public interface SimpleOperationListener<E> extends OperationListener<E> {
        
        @Override
        default void onPermutateBefore() { }
        
        @Override
        default void onPermutateAfter() { }
        
        
        @Override
        default void onAddBefore() { }
        
        @Override
        default void onAddAfter() { }
        
        
        @Override
        default void onRemoveBefore() { }
        
        @Override
        default void onRemoveAfter() { }
    }
    
    //
    
    @FunctionalInterface
    public interface OperationResponder<E> extends OperationListener<E> {
        
        void respond(@Nullable Operation<E> op1, @Nullable Operation<E> op2, @NotNull OperationType opType, @NotNull TriggerType triggerType);
        
        //<editor-fold desc="--- DEFAULT METHODS ---">
        
        //<editor-fold desc="--- CHANGE ---">
        
        @Override
        default void onPermutate(Operation<E> op, Operation<E> op2) {
            respond(op, op2, OperationType.PERMUTATION, TriggerType.CHANGE);
        }
        
        @Override
        default void onAdd(Operation<E> op) {
            respond(op, null, OperationType.ADDITION, TriggerType.CHANGE);
        }
        
        @Override
        default void onRemove(Operation<E> op) {
            respond(op, null, OperationType.REMOVAL, TriggerType.CHANGE);
        }
        
        @Override
        default void onUpdate(int from, int to) {
            respond(Operation.empty(from, to), null, OperationType.UPDATION, TriggerType.CHANGE);
        }
        
        //</editor-fold>
        
        //<editor-fold desc="--- BEFORE/AFTER ---">
        
        @Override
        default void onPermutateBefore() {
            respond(null, null, OperationType.PERMUTATION, TriggerType.PRE_CHANGE);
        }
        
        @Override
        default void onPermutateAfter() {
            respond(null, null, OperationType.PERMUTATION, TriggerType.POST_CHANGE);
        }
        
        @Override
        default void onAddBefore() {
            respond(null, null, OperationType.ADDITION, TriggerType.PRE_CHANGE);
        }
        
        @Override
        default void onAddAfter() {
            respond(null, null, OperationType.ADDITION, TriggerType.POST_CHANGE);
        }
        
        @Override
        default void onRemoveBefore() {
            respond(null, null, OperationType.REMOVAL, TriggerType.PRE_CHANGE);
        }
        
        @Override
        default void onRemoveAfter() {
            respond(null, null, OperationType.REMOVAL, TriggerType.POST_CHANGE);
        }
        
        //</editor-fold>
        
        //</editor-fold>
    }
    
    public interface UnlinkedOperationResponder<E> extends OperationResponder<E> {
        
        void respond(@Nullable Operation<E> op, @NotNull OperationType opType, @NotNull TriggerType triggerType);
        
        @Override
        default void respond(@Nullable Operation<E> op1, @Nullable Operation<E> op2, @NotNull OperationType opType, @NotNull TriggerType triggerType) {
            respond(op1, opType, triggerType);
        }
    }
    
    public interface SimpleOperationResponder<E> extends OperationResponder<E> {
        
        void respond(@Nullable Operation<E> op);
        
        @Override
        default void respond(@Nullable Operation<E> op1, @Nullable Operation<E> op2, @NotNull OperationType opType, @NotNull TriggerType triggerType) {
            if (triggerType == TriggerType.CHANGE)
                respond(op1);
        }
    }
    
    //</editor-fold>
    
    //</editor-fold>
    
    //</editor-fold>
}
