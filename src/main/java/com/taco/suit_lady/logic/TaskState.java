package com.taco.suit_lady.logic;

import com.taco.suit_lady.util.enums.Enumable;

/**
 * <p><b><i>Not Yet Implemented</i></b></p>
 * <br><hr><br>
 * <p>Defines the {@link TaskState} of a {@link GameTask} instance.</p>
 */
//TO-EXPAND
public enum TaskState
        implements Enumable<TaskState> {
    PRE_EXECUTION,
    RUNNING,
    PAUSED,
    CANCELLED,
    COMPLETED
}
