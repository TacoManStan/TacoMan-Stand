package com.taco.suit_lady.util;

public interface UIDProcessable extends UID {

	UIDProcessor getUIDProcessor();

	@Override default String getUID(Object... params) {
		return getUIDProcessor().getUID(params);
	}

	@Override default String getGroupID() {
		return getUIDProcessor().getGroupID();
	}
}
