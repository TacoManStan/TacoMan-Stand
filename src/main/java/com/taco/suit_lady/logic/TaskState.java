package com.taco.suit_lady.logic;

import com.taco.suit_lady.util.enums.Enumable;

//TODO: nyi
public enum TaskState
        implements Enumable<TaskState> {
    PRE_EXECUTION,
    RUNNING,
    PAUSED,
    CANCELLED,
    COMPLETED
}
