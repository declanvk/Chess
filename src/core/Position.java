package core;

public class Position {

    private final int rank;
    private final int file;
    
    public static boolean isValidPosition(int file, int rank) {
    	return !(rank < 1 || rank > 8 || file < 1 || file > 8);
    }

    public Position(int file, int rank) {
        if (rank < 1 || rank > 8 || file < 1 || file > 8) {
            throw new IllegalArgumentException(
                    "Rank and/or file must be within the bounds of the game. Rank: " + rank
                            + ", File: " + file);
        } else {
            this.rank = rank;
            this.file = file;
        }
    }

    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }
    
    public Position flipRank() {
    	return new Position(file, 8 - rank + 1);
    }
    
    public Position flipFile() {
    	return new Position(8 - file + 1, rank);
    }
    
    public boolean isAdjacent(Position other) {
    	int val = Math.max(Math.abs(other.file - this.file), Math.abs(other.rank - this.rank));
    	return 0 < val && val <= 1;
    }
    
    public boolean isDiagonallyAdjacent(Position other) {
    	return Math.abs(this.file - other.file) == 1 && Math.abs(this.rank - other.rank) == 1;
    }
    
    public boolean isDirectlyAdjacent(Position other) {
    	return Math.abs(this.file - other.file) + Math.abs(this.rank - other.rank) == 1;
    }
    
    public boolean isFileAdjacent(Position other) {
    	return Math.abs(this.file - other.file) == 1 && this.rank == other.rank;
    }
    
    public boolean isRankAdjacent(Position other) {
    	return Math.abs(this.rank - other.rank) == 1 && this.file == other.file;
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

    public String toVerboseString() {
        return "Position [rank=" + rank + ", file=" + file + "]";
    }

    @Override
    public String toString() {
        return ((char) ('a' + (file - 1))) + "" + rank;
    }
}
