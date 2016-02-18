package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class is responsible for saving the state of one specific subclass of
 * {@code ChessSerializable} between sessions of the application.
 * 
 * @author Declan
 *
 * @param <T>
 *            the type, which implements {@code ChessSerializable}, that the
 *            specific {@code SudokuRegister} will keep track of
 */
public class ChessGameRegister<T extends ChessSerializable> implements Serializable {

	private static final long serialVersionUID = 5580281115018204171L;

	/**
	 * The data structure that is in charge of storing the registered
	 * {@code ChessSerializables}
	 */
	private ArrayList<T> registered = new ArrayList<T>();

	/**
	 * Adds the {@code ChessSerializable} to the register and the underlying
	 * {@code ArrayList}, and checks if the name of the
	 * {@code ChessSerializable} is unique. If not, it modifies it until it is
	 * 
	 * @param g
	 *            the {@code ChessSerializable} to add and check
	 */
	public void register(T g) {
		if (!registered.contains(g)) {
			String newName = g.getName();
			if (!isUnique(g.getName())) {
				int count = 1;
				String orig = newName;
				do {
					newName = orig + " (" + count + ")";
					count++;

				} while (!isUnique(newName));
			}
			g.setName(newName);

			registered.add(g);
		}
	}

	/**
	 * Determines whether or not the {@code String} name is unique among the
	 * other saved {@code ChessSerializable} in the register
	 * 
	 * @param name
	 *            the {@code String} name to check for uniqueness
	 * @return whether or not the name is unique
	 */
	private boolean isUnique(String name) {
		for (T g : registered)
			if (g.getName().equals(name))
				return false;
		return true;
	}

	/**
	 * Removes a {@code ChessSerializable} from the register via the index
	 * 
	 * @param i
	 *            the index of the {@code ChessSerializable} to remove
	 */
	public void deregister(int i) {
		registered.remove(i);
	}

	/**
	 * This method is responsible for saving the state of the
	 * {@code SudokuRegister}
	 * 
	 * @param n
	 *            the {@code String} filename that specifies the location to
	 *            save the {@code SudokuRegister}
	 */
	public void saveState(String n) {
		try {
			FileOutputStream fos = new FileOutputStream(n);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is responsible for the reloading of the
	 * {@code SudokuRegister} after it has been shut down, or serialized.
	 * 
	 * @param n
	 *            the {@code String} filename that specified the serialized
	 *            location of the {@code SudokuRegister}
	 */
	@SuppressWarnings("unchecked")
	public void loadState(String n) {
		ChessGameRegister<T> temp = null;
		try {
			FileInputStream fis = new FileInputStream(n);
			ObjectInputStream ois = new ObjectInputStream(fis);
			temp = (ChessGameRegister<T>) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			temp = new ChessGameRegister<T>();
		}
		this.registered = temp.registered;
	}

	/**
	 * Loads a {@code ChessSerializable} from a specified {@code File} location
	 * 
	 * @param in
	 *            the {@code File} to load the {@code ChessSerializable} from
	 * @return the {@code ChessSerializable} saved at the specified
	 *         {@code File} location
	 */
	public static ChessSerializable load(File in) {
		ChessSerializable re = null;

		try {
			FileInputStream fis = new FileInputStream(in);
			ObjectInputStream ois = new ObjectInputStream(fis);
			re = (ChessSerializable) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return re;

	}

	/**
	 * Saves a specified {@code ChessSerializable}, using the information
	 * accessable through the {@code ChessSerializable} interface
	 * 
	 * @param out
	 *            the {@code ChessSerializable} to save
	 */
	public static void save(ChessSerializable out) {
		try {
			File loc = out.isSaved() ? out.getSave() : new File(out.getName() + out.getSuffix());
			FileOutputStream fos = new FileOutputStream(loc);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(out);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Provides access to underlying methods of the {@code ArrayList} that
	 * provides storage in this class.
	 * 
	 * @return whether or not the underlying {@code ArrayList} is empty
	 * @see ArrayList#isEmpty()
	 */
	public boolean isEmpty() {
		return registered.isEmpty();
	}

	/**
	 * Provides access to the clear() method of the underlying {@code ArrayList}
	 * 
	 * @see ArrayList#clear()
	 */
	public void clear() {
		registered.clear();
	}

	/**
	 * Provides access to the indexOf(Object) method of the underlying
	 * {@code ArrayList}
	 * 
	 * @param obj
	 *            the object to find the index of in the underlying
	 *            {@code ArrayList}
	 * @return the index of the specified object or -1 if not found
	 * @see ArrayList#indexOf(Object)
	 */
	public int indexOf(T obj) {
		return registered.indexOf(obj);
	}

	/**
	 * 
	 * @return the underlying {@code ArrayList} so that it can be iterated over.
	 *         Maybe not best practice
	 */
	public Collection<T> values() {
		return registered;
	}

}
