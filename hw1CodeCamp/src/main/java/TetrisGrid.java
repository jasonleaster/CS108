//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {

	boolean[][] grid;
	int row = 0;
	int col = 0;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.row = grid.length;
		this.col = grid[0].length;
		this.grid = new boolean[row][col];
		for(int i = 0; i < this.row; i++){
			for(int j = 0; j < this.col; j++){
				this.grid[i][j] = grid[i][j];
			}
		}
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */

	private boolean fullCol(boolean [][] grid, int col){
		for(int i = 0; i < this.row - 1; i++){
			if (grid[i][col] != this.grid[i+1][col]){
				return false;
			}
		}
		if(grid[0][col]  == false){
			return false;
		}else {
			return true;
		}
	}

	private void moveDown(boolean [][] grid, int col, int step){
		if(col < 0){
			return;
		}
		if(col == grid[0].length - 1){
			for(int i = 0; i < grid.length; i++){
				grid[i][col] = false;
			}
		}else {
			for(int i = 0; i < grid.length; i++){
				grid[i][col] = grid[i][col+step];
			}
		}
	}

	public void clearRows() {
		for(int j = this.col - 1; j >= 0; j--){
			if(fullCol(this.grid, j)){
				for(int i = j; i < this.col; i++) {
					moveDown(this.grid, i, 1);
				}
			}
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return this.grid; // TODO YOUR CODE HERE
	}
}
