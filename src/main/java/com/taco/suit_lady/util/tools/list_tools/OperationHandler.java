package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.UIDProcessable;
import com.taco.suit_lady.util.UIDProcessor;
import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.TaskTools;
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

public abstract class OperationHandler<E>
    implements OperationListener<E>, ListChangeListener<E>, Nameable, UIDProcessable {
        
        private final ReentrantLock lock;
        private final String name;
        
        private final ObservableList<E> list;
        private final ReadOnlyObjectWrapper<List<E>> backingListProperty;
        
        //<editor-fold desc="--- CONSTRUCTORS ---">
        
        protected OperationHandler(@NotNull ObservableList < E > list) {
            this(null, null, list);
        }
        
        protected OperationHandler(@Nullable String name, @NotNull ObservableList < E > list) {
            this(null, name, list);
        }
        
        protected OperationHandler(@Nullable ReentrantLock lock, @NotNull ObservableList<E> list) {
            this(lock, null, list);
        }
        
        protected OperationHandler(@Nullable ReentrantLock lock, @Nullable String name, @NotNull ObservableList<E> list) {
            this.lock = lock;
            this.name = name;
            
            this.list = list;
            this.backingListProperty = new ReadOnlyObjectWrapper<>();
        }
        
        //</editor-fold>
        
        @SafeVarargs
        public final @Nullable Operation<E> getByIndex(int index, boolean to, List<Operation<E>> @NotNull ... lists) {
            for (List<Operation<E>> list: lists) {
                Operation<E> p = list.stream().filter(
                        operation -> (!to && operation.movedFromIndex() == index) || (to && operation.movedToIndex() == index)).findFirst().orElse(null);
                if (p != null)
                    return p;
            }
            return null;
        }
        
        public final OperationHandler<E> apply() {
            refresh();
            ExceptionTools.nullCheck(list, "Observable List").addListener(ExceptionTools.nullCheck(this, "List Listener"));
            return this;
        }
        
        
        //<editor-fold desc="--- IMPLEMENTATIONS ---">
        
        @Override
        public final void onChanged(Change<? extends E> change) {
            TaskTools.sync(lock, () -> {
                while (change.next())
                    if (change.wasPermutated()) {
                        onPermutateOperationInternal(true);
                        
                        IntStream.range(change.getFrom(), change.getTo()).forEach(i -> {
                            final E newElement = list.get(i);
                            final int oldIndex = backingListProperty.get().indexOf(newElement);
                            @SuppressWarnings("UnnecessaryLocalVariable") final int newIndex = i;
                            
                            final E oldElement = list.get(change.getPermutation(newIndex));
                            final int oldElementOldIndex = backingListProperty.get().indexOf(oldElement);
                            final int oldElementNewIndex = change.getPermutation(newIndex);
                            
                            onPermutateInternal(
                                    new Operation<>(oldIndex, newIndex, newElement),
                                    new Operation<>(oldElementOldIndex, oldElementNewIndex, oldElement));
                        });
                        
                        onPermutateOperationInternal(false);
                    } else if (change.wasUpdated())
                        onUpdateInternal(change.getFrom(), change.getTo());
                    else {
                        ArrayList<Operation<E>> allOperations = list.stream().map(element -> new Operation<>(
                                backingListProperty.get().indexOf(element),
                                list.indexOf(element),
                                element)).collect(Collectors.toCollection(ArrayList::new));
                        
                        ArrayList<Operation<E>> removedOperations = change.getRemoved().stream().<Operation<E>>map(element -> new Operation<>(
                                backingListProperty.get().indexOf(element),
                                list.indexOf(element),
                                element)).collect(Collectors.toCollection(ArrayList::new));
                        ArrayList<Operation<E>> addedOperations = change.getAddedSubList().stream().<Operation<E>>map(element -> new Operation<>(
                                backingListProperty.get().indexOf(element),
                                list.indexOf(element),
                                element)).collect(Collectors.toCollection(ArrayList::new));
                        
                        ArrayList<Operation<E>> operations = allOperations.stream().filter(
                                o -> addedOperations.contains(o) && removedOperations.contains(o)).collect(
                                Collectors.toCollection(ArrayList::new));
                        
                        operations.forEach(p -> {
                            removedOperations.remove(p);
                            addedOperations.remove(p);
                        });
                        
                        
                        if (!operations.isEmpty()) {
                            onPermutateOperationInternal(true);
                            operations.forEach(operation -> onPermutateInternal(operation, getByIndex(operation.movedToIndex(), false, allOperations)));
                            onPermutateOperationInternal(false);
                        }
                        
                        
                        if (!removedOperations.isEmpty()) {
                            onAddOrRemoveOperationInternal(true, false);
                            removedOperations.forEach(operation -> onRemoveInternal(operation));
                            onAddOrRemoveOperationInternal(false, false);
                        }
                        
                        if (!addedOperations.isEmpty()) {
                            onAddOrRemoveOperationInternal(true, true);
                            addedOperations.forEach(operation -> onAddInternal(operation));
                            onAddOrRemoveOperationInternal(false, true);
                        }
                    }
                
                refresh();
            }, true);
        }
        
        //<editor-fold desc="--- EVENT RESPONSE ---">
        
        @Override
        public void onPermutateBefore() { }
        
        @Override
        public void onPermutateAfter() { }
        
        //
        
        @Override
        public void onAddBefore() { }
        
        @Override
        public void onAddAfter() { }
        
        //
        
        @Override
        public void onRemoveBefore() { }
        
        @Override
        public void onRemoveAfter() { }
        
        //
        
        @Override
        public void onUpdate(int from, int to) { }
        
        //</editor-fold>
        
        @Override
        public String getName() {
            return name;
        }
        
        private UIDProcessor uIDContainer;
        
        @Override
        public UIDProcessor getUIDProcessor() {
            if (uIDContainer == null) // Lazy Initialization
                uIDContainer = new UIDProcessor("group-name");
            return uIDContainer;
        }
        
        //</editor-fold>
        
        //<editor-fold desc="--- INTERNAL ---">
        
        private void onPermutateInternal(Operation<E> op1, Operation<E> op2) {
            TaskTools.sync(lock, () -> onPermutate(op1, op2), true);
        }
        
        private void onUpdateInternal(int from, int to) {
            TaskTools.sync(lock, () -> onUpdate(from, to), true);
        }
        
        private void onAddInternal(Operation<E> op) {
            TaskTools.sync(lock, () -> onAdd(op), true);
        }
        
        private void onRemoveInternal(Operation<E> op) {
            TaskTools.sync(lock, () -> onRemove(op), true);
        }
        
        //
        
        private void onPermutateOperationInternal(boolean before) {
            TaskTools.sync(lock, () -> {
                if (before) onPermutateBefore();
                else onPermutateAfter();
            }, true);
        }
        
        private void onAddOrRemoveOperationInternal(boolean before, boolean add) {
            TaskTools.sync(lock, () -> {
                if (add)
                    if (before)
                        onAddBefore();
                    else
                        onAddAfter();
                if (!add)
                    if (before)
                        onRemoveBefore();
                    else
                        onRemoveAfter();
            }, true);
        }
        
        //
        
        private void refresh() {
            TaskTools.sync(lock, () -> backingListProperty.set(ArrayTools.copy(list)), true);
        }
        
        //</editor-fold>
        
        //<editor-fold desc="--- STATIC ---">
        
        @Contract("_, _, _ -> new")
        public static <E> @NotNull OperationHandler<E> wrap(@Nullable ReentrantLock lock, @NotNull ObservableList<E> list, OperationListener<E> listener) {
            return new OperationHandler<>(lock, list) {
                @Override
                public void onPermutate(Operation<E> op, Operation<E> op2) {
                    listener.onPermutate(op, op2);
                }
                
                @Override
                public void onAdd(Operation<E> op) {
                    listener.onAdd(op);
                }
                
                @Override
                public void onRemove(Operation<E> op) {
                    listener.onRemove(op);
                }
                
                //
                
                @Override
                public void onPermutateBefore() {
                    listener.onPermutateBefore();
                }
                
                @Override
                public void onPermutateAfter() {
                    listener.onPermutateAfter();
                }
                
                @Override
                public void onAddBefore() {
                    listener.onAddBefore();
                }
                
                @Override
                public void onAddAfter() {
                    listener.onAddAfter();
                }
                
                @Override
                public void onRemoveBefore() {
                    listener.onRemoveBefore();
                }
                
                @Override
                public void onRemoveAfter() {
                    listener.onRemoveAfter();
                }
                
                @Override
                public void onUpdate(int from, int to) {
                    listener.onUpdate(from, to);
                }
            };
        }
        
        //</editor-fold>
    }
