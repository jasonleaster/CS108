// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean[][] backup;
	private boolean DEBUG = true;
	boolean committed;
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width  = width;
		this.height = height;
		grid   = new boolean[width][height];
		backup = new boolean[width][height];
		committed = true;
		
		// YOUR CODE HERE
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				grid[i][j] = false;
			}
		}
	}

	// A helper function defined by myself
	private void backUp(){
		for(int i = 0; i < this.grid.length; i++){
			System.arraycopy(this.grid[i], 0, this.backup[i], 0, this.backup[i].length);
		}
	}


	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		int maxHeight = 0;
		int height = 0;
		for(int x = 0; x < this.width; x++){
			height = getColumnHeight(x);
			if(height > maxHeight){
				maxHeight = height;
			}
		}
		return maxHeight; // YOUR CODE HERE
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			// YOUR CODE HERE
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int drop_height = 0;
		for(int y = this.height - piece.getHeight() - 1; y >= 0; y--){
			if(getGrid(x, y) == true){
				drop_height = y + 1;
			}
		}
		return drop_height; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		for(int y = this.height-1; y >= 0; y--){
			if(getGrid(x, y) == true){
				return y + 1;
			}
		}
		return 0; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		int start, end;
		for(start = 0; start < this.width; start++){
			if(getGrid(start, y) == true){
				break;
			}
		}
		for(end = this.width - 1; end >= 0; end--){
			if(getGrid(end, y) == true){
				break;
			}
		}
		if(end < start){
			return 0;
		}else {
			return end - start + 1;
		}
		//return 0; // YOUR CODE HERE
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(x < 0 || x >= this.width || y < 0 || y >= this.height){
			return true;
		}else if(this.grid[x][y] == false){
			return false;
		}
		return true; // YOUR CODE HERE
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");

		int result = PLACE_OK;
		
		// YOUR CODE HERE
		this.committed = false;

		TPoint[] body = piece.getBody();
		for(int i = 0; i < body.length; i++){
			if(this.inBoard(body[i].x + x, body[i].y + y) == false){
				result = PLACE_OUT_BOUNDS;
				break;
			}
			if(this.getGrid(body[i].x + x, body[i].y + y) == true){
				result = PLACE_BAD;
				break;
			}
			this.grid[body[i].x + x][body[i].y + y] = true;
		}

		this.commit();
		return result;
	}

	// A helper function defined by me.
	private boolean inBoard(int x, int y){
		if(x < 0 || x > this.width || y < 0 || y > this.height){
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		// YOUR CODE HERE
		this.committed = false;

		for(int j = this.grid[0].length - 1; j >= 0; j--){
			if(fullCol(this.grid, j)){
				for(int i = j; i < this.grid[0].length; i++) {
					moveDown(this.grid, i, 1);
				}
			}
		}

		sanityCheck();
		this.commit();
		this.backUp();
		return rowsCleared;
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

	private boolean fullCol(boolean [][] grid, int col){
		for(int i = 0; i < grid.length - 1; i++){
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

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		// YOUR CODE HERE
		if(this.committed == false){
			return;
		}
		for(int i = 0; i < this.grid.length; i++){
			System.arraycopy(this.backup[i], 0, this.grid[i], 0, this.backup[i].length);
		}
		this.commit();
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}

	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


