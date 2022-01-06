package com.taco.suit_lady.logic.game.execution;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Ticker {
    
    private final ScheduledThreadPoolExecutor tickingThreadPoolExecutor;
    
    public Ticker() {
        this.tickingThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }
    
    //<editor-fold desc="--- INITIALIZATION ---">
    
    public final Ticker init() {
        initLoop();
        return this;
    }
    
    private void initLoop() {
        tickingThreadPoolExecutor.scheduleAtFixedRate(() -> tick(), 0, 25, TimeUnit.MILLISECONDS);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- INTERNAL ---">
    
    private void tick() {
    
    }
    
    //</editor-fold>
}
