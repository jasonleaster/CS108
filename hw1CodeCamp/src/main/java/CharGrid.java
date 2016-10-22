// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}

	private int absolute(int x, int y){
		return x * x + y * y;
	}

	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		/********************************************************************
		 * Code Refactoring 1:
		 * 			I used an array @location to store all axis information
		 * 	about element which equals to @ch. Then, I select the two points which
		 * 	are the most far from the original point(0, 0) and the most close to
		 * 	the original point(0, 0) as the boundary point of the rectangle.
		 * 	It's my solution at the first time but not efficient and elegant.
		 *
		 * 	We don't need to store all points but the boundary point is enough.
		 *
		int row = grid.length;
		int col = grid[0].length;

		int counter = 0;
		int location[][] = new int[row*col][2];
		for(int i = 0; i < row; i ++){
			for(int j = 0; j < col; j++){
				if (this.grid[i][j] == ch){
					location[counter][0] = i;
					location[counter][1] = j;
					counter++;
				}
			}
		}

		int maxIdx = 0;
		int minIdx = 0;
		for(int i = 0; i < counter; i++){
			if(this.absolute(location[maxIdx][0], location[maxIdx][1]) <
					this.absolute(location[i][0], location[i][1])){
				maxIdx = i;
			}
			if(this.absolute(location[minIdx][0], location[minIdx][1]) >
					this.absolute(location[i][0], location[i][1])){
				minIdx = i;
			}
		}

		int S = (location[maxIdx][0] - location[minIdx][0] + 1) * (location[maxIdx][1] - location[minIdx][1] + 1);
		if(S < 0){
			S *= -1;
		}

		return S; // TODO ADD YOUR CODE HERE
		 *****************************************************************************/
		// Code Refactoring 1:
		int maxRow = 0, maxCol = 0;
		int minRow = grid.length;
		int minCol = grid[0].length;
		for(int row = 0; row < grid.length; row++){
			for(int col = 0; col < grid[0].length; col++){
				if(grid[row][col] == ch){
					if(minCol > col) minCol = col;
					if(minRow > row) minRow = row;
					if(maxCol < col) maxCol = col;
					if(maxRow < row) maxRow = row;
				}
			}
		}
		return (maxCol - minCol + 1) * (maxRow - minRow + 1);
	}

	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {

		/****
		 * Code Refactoring 2: This implementation  ## may ##  looks like lengthy.
		 * Github User @varren implement this method with the thought of recursion.
		 * https://github.com/varren/cs108/blob/master/hw1/CharGrid.java

		 * My Implementation.

		* 1.I find a continues area in a row with the same character. The length of that is @lengthX.
		* 2.Calculate the center point of that area (centerRow, centerCol)
		* 3.From the center of the area, search and check if there have the same character in
		* the vertical direction. The length of same character is @lengthY in vertical direction.
		* 4.If @lengthX == @lengthY, it's a "Plus" pattern.
		*

		char pattern = 0;
		int counter  = 0; // the number of "Plus"
		int startIdx = 0;
		int lengthX  = 1;
		int lengthY  = 1;
		int centerRow = 0;
		int centerCol = 0;
		for(int i = 0; i < this.grid.length; i++){
			for(int j = 0; j < this.grid[i].length - 1; j ++) {
				if (this.grid[i][j] != ' ' && this.grid[i][j] == this.grid[i][j + 1]) {
					lengthX = 1;
					lengthY = 1;
					pattern = this.grid[i][j];
					startIdx = j;

					for (int idx = startIdx; idx < this.grid[i].length - 1; idx++) {
						if (this.grid[i][idx] == this.grid[i][idx + 1]) {
							lengthX += 1;
						}else {
							break;
						}
					}
					if (lengthX > 1 && lengthX % 2 == 1) {
						centerRow = i;
						centerCol = startIdx + lengthX / 2;
						for (int x = centerRow + 1; x < this.grid.length; x++) {
							if (this.grid[x][centerCol] == pattern) {
								lengthY += 1;
							}else {
								break;
							}
						}
						for (int x = centerRow - 1; x >= 0; x--) {
							if (this.grid[x][centerCol] == pattern) {
								lengthY += 1;
							}else {
								break;
							}
						}
					}
					if(lengthX == lengthY){
						counter++;
					}
					j += lengthX - 1;
				}
			}
		}
		return counter; // TODO ADD YOUR CODE HERE
		* */

		// Code Refactoring 2
		int numOfPlus = 0;
		for (int row = 0; row < grid.length; row++)
			for (int col = 0; col < grid[0].length; col++)
				if (isValidPlus(row, col))
					numOfPlus++;
		return numOfPlus;
	}

	private boolean isValidPlus(int row, int col){
		int left  = nCharsInDirection(row, col, 0, -1);
		int right = nCharsInDirection(row, col, 0, +1);
		int up    = nCharsInDirection(row, col, -1, 0);
		int down  = nCharsInDirection(row, col, +1, 0);

		return left != 0 && left == right && left == up && left == down;
	}

	private int nCharsInDirection(int row, int col,int shiftRow, int shiftCol){
		int nextRow = row + shiftRow;
		int nextCol = col + shiftCol;

		if(isValidCell(nextRow, nextCol) && grid[row][col] == grid[nextRow][nextCol]){
			return 1 + nCharsInDirection(nextRow, nextCol, shiftRow, shiftCol);
		}else {
			return 0;
		}
	}

	private boolean isValidCell(int row, int col){
		return row < grid.length && grid.length > 0 && col < grid[0].length && col >= 0 && row >= 0;
	}
}
