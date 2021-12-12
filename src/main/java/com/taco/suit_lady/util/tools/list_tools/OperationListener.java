package com.taco.suit_lady.util.tools.list_tools;

public interface OperationListener<E> {
    
    void onPermutate(Operation<E> op, Operation<E> op2);
    
    void onPermutateBefore();
    
    void onPermutateAfter();
    
    
    void onAdd(Operation<E> op);
    
    void onAddBefore();
    
    void onAddAfter();
    
    
    void onRemove(Operation<E> op);
    
    void onRemoveBefore();
    
    void onRemoveAfter();
    
    
    void onUpdate(int from, int to);
}