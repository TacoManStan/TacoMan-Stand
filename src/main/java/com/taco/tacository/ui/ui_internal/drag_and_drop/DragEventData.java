package com.taco.tacository.ui.ui_internal.drag_and_drop;

import javafx.scene.input.InputEvent;

import java.io.Serializable;

public record DragEventData<T extends Serializable, E extends InputEvent>(E event, DragAndDropHandler<T> owner, DragEventType eventType) { }
