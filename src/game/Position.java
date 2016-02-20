package game;

public class Position {
	
	private final int rank, file;

	public Position(int r, int f) {
		if(r < 1 || r > 8 || f < 1 || f > 8) {
			throw new IllegalArgumentException("Rank and/or file must be within the bounds of the game");
		} else {
			this.rank = r;
			this.file = f;
		}
	}

	public int getRank() {
		return rank;
	}

	public int getFile() {
		return file;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + file;
		result = prime * result + rank;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (file != other.file)
			return false;
		if (rank != other.rank)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Position [rank=" + rank + ", file=" + file + "]";
	}
	
	public String algebraicPosition() {
		return file + "" + ((char)('a' + (rank - 1)));
	}
}
