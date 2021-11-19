package com.taco.suit_lady.view.ui.jfx.button;

import java.util.concurrent.locks.ReentrantLock;

public class SimpleButtonViewGroup extends ButtonViewGroup {

	public SimpleButtonViewGroup() {
		this(null);
	}

	public SimpleButtonViewGroup(ReentrantLock lock) {
		super(lock);
	}
}
