package com.taco.suit_lady.util.tools.list_tools;

import com.taco.suit_lady.util.tools.ArrayTools;
import com.taco.suit_lady.util.tools.ExceptionTools;
import com.taco.suit_lady.util.tools.list_tools.Operation.OperationType;
import com.taco.suit_lady.util.tools.list_tools.Operation.TriggerType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ListToolsDemo {
    
    public static void main(String[] args) {
        final ReentrantLock lock = new ReentrantLock();
        final ObservableList<String> list = ListToolsDemo.initTestList();
        
        //        ListTools.applyListener(lock, list, op -> Demo.doPrint(() -> System.out.println("GENERIC OPERATION:  " + op), "Listener 1", null, false));
        //        ListTools.applyListener(lock, list, (op, opType, triggerType) -> Demo.printListEvent(op, null, opType, triggerType, "Listener 2", false));
        ListTools.applyListener(lock, list, (op1, op2, opType, triggerType) -> ListToolsDemo.printListEvent(op1, op2, opType, triggerType, null, false));
        
        ListToolsDemo.testPrints(list, null);
    }
    
    //
    
    private static void testPrints(ObservableList<String> list, @Nullable String footer) {
        printList(list, footer);
        
        
        System.out.println("Sorting...");
        
        ArrayTools.sort(list);
        
        printList(list, footer);
        
        
        System.out.println("Adding...");
        
        list.add("Hello!");
        
        printList(list, footer);
        
        
        System.out.println("Shuffling...");
        
        FXCollections.shuffle(list);
        
        printList(list, footer);
        
        
        System.out.println("Resorting...");
        
        ArrayTools.sort(list);
        
        printList(list, footer);
        
        
        System.out.println("Reversing...");
        
        Collections.reverse(list);
        
        printList(list, footer);
        
        
        System.out.println("Clearing...");
        
        ArrayList<String> copy = new ArrayList<>(list);
        
        list.clear();
        
        printList(list, footer);
        
        
        System.out.println("Re-Adding...");
        
        list.addAll(copy);
        
        printList(list, footer);
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
    
    private static void printList(@NotNull List<String> list, @Nullable String footer) {
        if (!list.isEmpty())
            doPrint(() -> list.forEach(s -> System.out.println("[" + list.indexOf(s) + "]: " + s)), "list", footer, true);
        else
            doPrint(() -> System.out.println("empty"), "list", footer, true);
    }
    
    @SuppressWarnings("DuplicatedCode")
    private static <E> void printListEvent(
            Operation<E> p1, Operation<E> p2,
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
            throw ExceptionTools.ex("BLAH BLAH BLAH");
        
        doPrint(printsI, titleI, footerI, boxI);
    }
    
    private static void doPrint(@NotNull Runnable prints, @Nullable String title, @Nullable String footer, boolean box) {
        if (box) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("------------------------------------------------------------");
        }
        
        if (title != null) {
            if (!box)
                System.out.println("------------------------------------------------------------");
            System.out.println("::: " + title.toUpperCase() + " :::");
            System.out.println("------------------------------------------------------------");
            System.out.println();
        }
        
        //
        
        prints.run();
        
        //
        
        if (footer != null) {
            if (box && title != null) {
                System.out.println();
                System.out.println("" + footer + "");
            } else
                System.out.println("    > " + footer);
        }
        if (box) {
            if (footer == null)
                System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }
}