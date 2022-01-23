package com.taco.suit_lady.util;

import com.taco.suit_lady.util.tools.SLExceptions;
import com.taco.suit_lady.util.tools.SLTools;

/**
 * A utility class designed to wrap the functionality of the {@link UID} interface into its most common form, so that...
 * <ol>
 * <li>{@link UID#getUID(Object...)} returns a unique value for every new instance of {@code UIDProcessor} created.</li>
 * <li>All objects passed into {@link UID#getUID(Object...)} as parameters are appended to the UID (see method docs for details).</li>
 * <li>{@link UID#getGroupID()} returns the value passed into the {@code UIDProcessor} constructor.</li>
 * </ol>
 */
public class UIDProcessor
		implements UID {

	private final String hashID;
	private final String globalUID;

	/**
	 * Constructs a new {@code UIDProcessor} with the specified group ID.
	 *
	 * @param groupID A String that defines what type of object this {@code UIDProcessor} is identifying.
	 */
	public UIDProcessor(String groupID) {
		this.globalUID = SLExceptions.nullCheck(groupID, "Global UID");
		this.hashID = "" + SLTools.generateHashID();
	}

	/**
	 * Returns a unique identification String for this {@link UIDProcessor}.
	 * <p>
	 * All parameters passed into this method are appended to the UID returned by this method.
	 * <ol>
	 * <li>If a parameter implements {@link UID}, its {@link UID#getUID(Object...) getUID(Object...)} method is called with no parameters.</li>
	 * <li>Otherwise, the parameter's {@code toString()} method is called.</li>
	 * <li>A {@code NullPointerException} is thrown if any parameters are null.</li>
	 * </ol>
	 *
	 * @param params Any additional values to be appended to the UID.
	 *               Leave empty or null for no additional UID parameters.
	 * @return A unique identification String for this {@link UIDProcessor}.
	 */
	@Override
	public String getUID(Object... params) {
		StringBuilder string_builder = new StringBuilder();

		string_builder.append(globalUID);
		string_builder.append('-');
		string_builder.append(hashID);

		if (params != null && params.length > 0)
			for (Object param : params) {
				SLExceptions.nullCheck(param, "UID Parameter");
				string_builder.append('-');
				if (param instanceof UID)
					string_builder.append(((UID) param).getUID());
				else
					string_builder.append(param.toString());
			}

		return UID.processUID(string_builder.toString());
	}

	@Override
	public String getGroupID() {
		return UID.processUID(globalUID);
	}
}
