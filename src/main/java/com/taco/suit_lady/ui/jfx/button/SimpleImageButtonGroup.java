package com.taco.suit_lady.ui.jfx.button;

import java.util.concurrent.locks.ReentrantLock;

public class SimpleImageButtonGroup extends ImageButtonGroup
{

	public SimpleImageButtonGroup() {
		this(null);
	}

	public SimpleImageButtonGroup(ReentrantLock lock) {
		super(lock);
	}
}
