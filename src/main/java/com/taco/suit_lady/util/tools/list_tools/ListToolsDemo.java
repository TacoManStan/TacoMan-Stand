package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady._to_sort._new.Debugger;
import com.taco.suit_lady.util.tools.Exc;
import com.taco.suit_lady.util.tools.list_tools.Op.OperationType;
import com.taco.suit_lady.util.tools.list_tools.Op.TriggerType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

public class ListToolsDemo {
    
    public static void main(String[] args) {
        final ReentrantLock lock = new ReentrantLock();
        final ObservableList<String> list = ListToolsDemo.initTestList();
        final Debugger debugger = new Debugger();
        
        //        ListTools.applyListener(lock, list, op -> Demo.doPrint(() -> System.out.println("GENERIC OPERATION:  " + op), "Listener 1", null, false));
        //        ListTools.applyListener(lock, list, (op, opType, triggerType) -> Demo.printListEvent(op, null, opType, triggerType, "Listener 2", false));
        L.applyListener(lock, list, (op1, op2, opType, triggerType)
                -> ListToolsDemo.printListEvent(debugger, op1, op2, opType, triggerType, null, false));
        ListToolsDemo.testPrints(debugger, list, null);
    }
    
    //
    
    private static void testPrints(Debugger debugger, ObservableList<String> list, @Nullable String footer) {
        debugger.printList(list, footer);
        
        
        System.out.println("Sorting...");
        
        A.sort(list);
        
        debugger.printList(list, footer);
        
        
        System.out.println("Adding...");
        
        list.add("Hello!");
        
        debugger.printList(list, footer);
        
        
        System.out.println("Shuffling (Collections)...");
        
        Collections.shuffle(list);
        
        debugger.printList(list, footer);
        
        
        System.out.println("Shuffling (FXCollections)...");
        
        FXCollections.shuffle(list);
        
        debugger.printList(list, footer);
        
        
        System.out.println("Resorting...");
        
        A.sort(list);
        
        debugger.printList(list, footer);
        
        
        System.out.println("Reversing...");
        
        Collections.reverse(list);
        
        debugger.printList(list, footer);
        
        
        System.out.println("Clearing...");
        
        ArrayList<String> copy = new ArrayList<>(list);
        
        list.clear();
        
        debugger.printList(list, footer);
        
        
        System.out.println("Re-Adding...");
        
        list.addAll(copy);
        
        debugger.printList(list, footer);
    }
    
    //
    
    private static @NotNull ObservableList<String> initTestList() {
        System.out.println("Creating List...");
        final ObservableList<String> list = FXCollections.observableArrayList();
        
        System.out.println("Populating List...");
        list.addAll("Dinner", "Elephant", "33", "Accelerator", "Zebra", "Eggplant", "Walrus", "Apple", "Tree", "Aardvark");
        
        System.out.println("Setting Listeners...");
        
        return list;
    }
    
    @SuppressWarnings("DuplicatedCode")
    private static <E> void printListEvent(
            Debugger debugger,
            Op<E> p1, Op<E> p2,
            OperationType operationType, TriggerType triggerType,
            String message, boolean box) {
        
        Runnable printsI;
        String titleI;
        String footerI;
        boolean boxI;
        
        if (triggerType == TriggerType.CHANGE) {
            printsI = () -> {
                final String p1ContentsStr = p1 != null ? "" + p1.contents() : "N/A";
                final String p1FromIndexStr = p1 != null ? "" + p1.movedFromIndex() : "N/A";
                final String p1ToIndexStr = p1 != null ? "" + p1.movedToIndex() : "N/A";
                
                final String p2ContentsStr = p2 != null ? "" + p2.contents() : "N/A";
                final String p2FromIndexStr = p2 != null ? "" + p2.movedFromIndex() : "N/A";
                final String p2ToIndexStr = p2 != null ? "" + p2.movedToIndex() : "N/A";
                
                String prefix = ">>> " + (operationType != null ? operationType : "No Change Type") + ":  ";
                if (operationType == OperationType.PERMUTATION)
                    System.out.println(prefix + "[" + p1ContentsStr + ": " + p1FromIndexStr + " --> " + p1ToIndexStr + "]" + "  |  " +
                                       "[" + p2ContentsStr + ": " + p2FromIndexStr + " --> " + p2ToIndexStr + "]");
                else if (operationType == OperationType.ADDITION)
                    if (p1.movedFromIndex() != -1)
                        System.out.println(prefix + "[" + p1ContentsStr + ": " + p1FromIndexStr + " --> " + p1ToIndexStr + "]");
                    else
                        System.out.println(prefix + "[" + p1ContentsStr + " --> " + p1ToIndexStr + "]");
                else if (operationType == OperationType.REMOVAL)
                    if (p1.movedToIndex() != -1)
                        System.out.println(prefix + "[" + p1ContentsStr + ": " + p1FromIndexStr + " -> " + p1ToIndexStr + "]");
                    else
                        System.out.println(prefix + "[" + p1ContentsStr + " -/- " + p1FromIndexStr + "]");
                else if (operationType == OperationType.UPDATION)
                    System.out.println("Updation (error)");
                
            };
            
            titleI = null;
            footerI = message;
            boxI = box;
        } else if (triggerType == TriggerType.PRE_CHANGE || triggerType == TriggerType.POST_CHANGE) {
            printsI = () -> System.out.println(">>> " + (operationType != null ? operationType : "No Change Type") + " Operation :  " + triggerType);
            
            titleI = null;
            footerI = null;
            boxI = false;
        } else if (triggerType == TriggerType.UPDATE) {
            printsI = () -> System.out.println(">>> Update Operation [" + operationType + "] :  From " + p1.movedFromIndex() + " To " + p1.movedToIndex());
            
            titleI = null;
            footerI = null;
            boxI = false;
        } else
            throw Exc.ex("BLAH BLAH BLAH");
        
        debugger.printBlock(printsI, titleI, footerI, boxI);
    }
}
