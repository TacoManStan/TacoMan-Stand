package com.taco.suit_lady.ui.console;

import com.taco.suit_lady.util.UIDProcessor;

import java.time.LocalDateTime;

public class ConsoleMessage<T>
        implements ConsoleMessageable<T>
{
    
    private final T text;
    private final LocalDateTime timestamp;
    private final ConsoleInterpreter<T> interpreter;
    
    private ConsoleMessageable<T>[] children;
    
    public ConsoleMessage(T text)
    {
        this(text, LocalDateTime.now());
    }
    
    public ConsoleMessage(T text, LocalDateTime timestamp)
    {
        this(text, timestamp, defaultInterpreter());
    }
    
    public ConsoleMessage(T text, LocalDateTime timestamp, ConsoleInterpreter<T> interpreter)
    {
        this.text = text;
        this.timestamp = timestamp;
        this.interpreter = interpreter;
    }
    
    //<editor-fold desc="Properties">
    
    @Override
    public final T getText()
    {
        return text;
    }
    
    @Override
    public final LocalDateTime getTimestamp()
    {
        return timestamp;
    }
    
    public final ConsoleInterpreter<T> getInterpreter()
    {
        return interpreter;
    }
    
    //
    
    public final ConsoleMessageable<T>[] getChildren()
    {
        return children;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Implementation">
    
    private UIDProcessor uidProcessor;
    
    @Override
    public UIDProcessor getUIDProcessor()
    {
        if (uidProcessor == null) // Lazy initialization
            uidProcessor = new UIDProcessor("console-message");
        return uidProcessor;
    }
    
    //</editor-fold>
    
    //
    
    public static <T> ConsoleInterpreter<T> defaultInterpreter()
    {
        return Object::toString;
    }
}
