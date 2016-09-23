package me.Maze;

public class Coordinate {
	private int row;
    private int col;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(Coordinate rac) {
        return (row == rac.getRow() && col == rac.getCol());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
