package gui;

import java.io.File;
import java.io.Serializable;

/**
 * The {@code ChessSerializable} interface functions as cover any class that
 * the application might need to serialize, only exposing the necessary
 * functions with regards to the name of the file and its save location.
 * 
 * @author Declan
 *
 */
public interface ChessSerializable extends Serializable {

	/**
	 * 
	 * @return whether or not the {@code ChessSerializable} object has a
	 *         non-null save location, stored as a {@code File}
	 */
	boolean isSaved();

	/**
	 * Gets the save {@code File} of the @{code ChessSerializable}
	 * 
	 * @return the save location, stored as a {@code File}
	 */
	File getSave();

	/**
	 * Sets the {@code File} save locations of the {@code ChessSerializable}
	 * 
	 * @param f
	 *            the {@code File} save location to set
	 */
	void setSave(File f);

	/**
	 * Gets the name of the {@code ChessSerializable}
	 * 
	 * @return the {@code String} name of the {@code ChessSerializable}
	 */
	String getName();

	/**
	 * Sets the name of the {@code ChessSerializable} to the specified
	 * {@code String}
	 * 
	 * @param s
	 *            the {@code String} name of the {@code ChessSerializable} to
	 *            set
	 */
	void setName(String s);

	/**
	 * 
	 * @return the file-type suffix for the {@code ChessSerializable}
	 */
	String getSuffix();
}
