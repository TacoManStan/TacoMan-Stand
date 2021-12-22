package com.taco.suit_lady.ui.console;

import com.taco.suit_lady.util.UIDProcessor;

import java.time.LocalDateTime;

public class SimpleConsoleMessage
		implements ConsoleMessageable<String> {

	private final String text;
	private final LocalDateTime timestamp;

	public SimpleConsoleMessage(String text) {
		this(text, LocalDateTime.now());
	}

	public SimpleConsoleMessage(String text, LocalDateTime timestamp) {
		this.text = text;
		this.timestamp = timestamp;
	}

	//<editor-fold desc="Properties">

	@Override
	public final String getText() {
		return text;
	}

	@Override
	public final LocalDateTime getTimestamp() {
		return timestamp;
	}

	//</editor-fold>

	//<editor-fold desc="Implementation">

	private UIDProcessor uIDContainer;

	@Override
	public UIDProcessor getUIDProcessor() {
		if (uIDContainer == null) // Lazy initialization
			uIDContainer = new UIDProcessor("console-message");
		return uIDContainer;
	}

	//</editor-fold>
}
