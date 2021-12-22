package com.taco.suit_lady.ui.jfx.lists.treehandler;

import java.io.*;
import java.util.function.Function;

/**
 * A class that contains all of the necessary information for loading an {@link Object} into the GUI.
 *
 * @param <T> The type of object this {@link CreationTreeCellData} is to create.
 */
public class CreationTreeCellData<T> extends TreeCellData<T>
		implements Serializable {

	private final Function<Object[], T> valueProvider;

	private Class<T> wrappedClass;

	public CreationTreeCellData(Function<Object[], T> valueProvider) {
		this("NULL", false, valueProvider);
	}

	/**
	 * Constructs a new TreeCellData with the specified name and folder value.
	 *
	 * @param name     The name.
	 * @param isFolder True if the constructed TreeCellData should be a folder, false otherwise.
	 */
	public CreationTreeCellData(String name, boolean isFolder, Function<Object[], T> valueProvider) {
		this(name, null, isFolder, valueProvider);
	}

	/**
	 * Constructs a new TreeCellData with the specified name and parent name.
	 *
	 * @param name       The name.
	 * @param parentName The parent name.
	 */
	public CreationTreeCellData(String name, String parentName, Function<Object[], T> valueProvider) {
		this(name, parentName, false, valueProvider);
	}

	/**
	 * Constructs a new TreeCellData with the specified name, parent name, and folder value.
	 *
	 * @param name       The name.
	 * @param parentName The parent name.
	 * @param isFolder   True if the constructed TreeCellData should be a folder, false otherwise.
	 */
	public CreationTreeCellData(String name, String parentName, boolean isFolder, Function<Object[], T> valueProvider) {
		super(name, parentName, isFolder);
		this.valueProvider = valueProvider;
	}

	//<editor-fold desc="Implementation">

	/**
	 * Used to define the {@link Object} that this {@link CreationTreeCellData} creates.
	 *
	 * @param objs The {@link Object} parameters being used to create an instance for this {@link CreationTreeCellData}.
	 * @return The newly created {@link TreeCellData}.
	 */
	@Override public T createWrappedInstance(Object... objs) {
		return valueProvider != null ? valueProvider.apply(objs) : null;
	}

	//</editor-fold>

	//

	//<editor-fold desc="Static Serialization">

	/**
	 * Deserializes the specified array of bytes into the {@link CreationTreeCellData} that the array was serialized from.
	 *
	 * @param bytes The array of bytes being deserialized.
	 * @return The {@link CreationTreeCellData} that the array of bytes was serialized from.
	 */
	public static CreationTreeCellData deserialize(final byte[] bytes) {
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(is);
			return (CreationTreeCellData) ois.readObject();
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
		return null;
	}

	/**
	 * Serializes the specified {@link CreationTreeCellData} into a deserializable array bytes.
	 *
	 * @param data The {@link CreationTreeCellData} being deserialized.
	 * @return A deserializable array bytes that represents the specified {@link CreationTreeCellData}.
	 */
	public static byte[] serialize(CreationTreeCellData data) {
		try {
			ObjectOutputStream oos;
			byte[] bytes;
			try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
				oos = new ObjectOutputStream(bytesOut);
				oos.writeObject(data);
				oos.flush();
				bytes = bytesOut.toByteArray();
			}
			oos.close();
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//</editor-fold>

	//<editor-fold desc="Classes">

	/**
	 * Used for the purpose of lambda support.
	 */
	public interface TreeCellable<T> extends Serializable {
		/**
		 * Used to define the {@link Object} that the {@link CreationTreeCellData} creates.
		 *
		 * @param objs The {@link Object} parameters being used to create an instance for the {@link CreationTreeCellData}.
		 * @return The {@link Object} that the {@link CreationTreeCellData} created.
		 */
		T createInstance(Object... objs);
	}

	//</editor-fold>
}

/*
 * TODO LIST:
 * [S] Come up with new prefix other than "Creation"
 *     [S] Also don't forget to apply this to CreationTreeCellData.
 */