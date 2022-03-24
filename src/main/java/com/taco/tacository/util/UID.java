package com.taco.tacository.util;


public interface UID
{
	/**
	 * Returns a string value that is unique to this {@code UID} instance.
	 * @return A string value that is unique to this {@code UID} instance.
	 * @see UIDProcessor
	 */
	String getUID(Object... params);

	/**
	 * Returns a string value that is unique to this {@code UID} class implementation.
	 * <p>
	 * For example, a class {@code Car} might return "car".
	 * @return A string value that is unique to this {@code UID} class implementation.
	 * @see UIDProcessor
	 */
	String getGroupID();

	static String processUID(String uid) {
		return uid.replace(" ", "-").toLowerCase();
	}
}
