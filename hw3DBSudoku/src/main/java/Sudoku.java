/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {

	private class Point{
		public int x;
		public int y;

		public Point(int x, int y){
			this.x = x;
			this.y = y;
		}

		public Point(){
			this.x = -1;
			this.y = -1;
		}
	}

	private class Grid{
		public int [][] grid;
		public int 		width;
		public int 		height;

		public Grid(int [][] arrays){
			if(arrays == null){
				return;
			}

			height = arrays.length;
			width  = arrays[0].length;
			this.grid = new int[height][width];
			for(int i = 0; i < height; i++) {
				System.arraycopy(arrays[i], 0, this.grid[i], 0, arrays[i].length);
			}
		}

		public boolean findUnAssignedLocation(Point emptySlot){
			for(int i  = 0; i < height; i++){
				for(int j = 0; j < width; j++){
					if(grid[i][j] == 0){
						emptySlot.x  = j;
						emptySlot.y  = i;
						return true;
					}
				}
			}
			return false;
		}

		public boolean noConflict(int num, int row, int col){
			for(int i = 0; i < height; i++){
				if(grid[i][col] == num){
					return false;
				}
			}
			for(int i = 0; i < width; i++){
				if(grid[row][i] == num){
					return false;
				}
			}
			return true;
		}

		@Override
		public String toString() {
			String string = new String();

			for(int i = 0; i < height; i++){
				for(int j = 0; j < width; j++){
					string += grid[i][j] + "\t";
				}
				string += "\n";
			}
			return string;
		}
	}

	private Grid grid;
	private Grid answer;
	private long startTime;
	private long endTime;


	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		// YOUR CODE HERE
		this.grid = new Grid(ints);
		this.answer = grid;
	}
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		startTime = System.currentTimeMillis();
		answer = new Grid(this.grid.grid);
		boolean solved = _solver(this.grid, answer);
		endTime = System.currentTimeMillis();
		if(solved){
			return  0;
		}
		return -1; // YOUR CODE HERE
	}

	private boolean _solver(Grid grid, Grid answer){
		Point emptySlot = new Point();

		if(grid.findUnAssignedLocation(emptySlot) == false){
			if(checkSolution(grid)) {
			//if(true) {
				answer.grid = grid.grid;
				return true;
			}else {
				return false;
			}
		}

		for(int num = 1; num <= 9; num++){
			//Constraint
			if(grid.noConflict(num, emptySlot.y,  emptySlot.x)){
				Grid new_grid = new Grid(grid.grid);
				new_grid.grid[emptySlot.y][emptySlot.x] = num;
				if(_solver(new_grid, answer)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkSolution(Grid answer){
		boolean correct = false;
		for(int i = 0; i < 9; i+=3){
			for(int j = 0; j < 9; j+=3){
				for(int num = 1; num <= 9; num++){
					correct = false;
					for(int x = i; x < i + 3; x++){
						for(int y = j; y < j + 3; y++){
							if(answer.grid[x][y] == num){
								correct = true;
								break;
							}
						}
					}
					if(correct == false){
						//System.out.print(answer.toString());
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public String getSolutionText() {
		return answer.toString(); // YOUR CODE HERE
	}
	
	public long getElapsed() {
		return endTime - startTime; // YOUR CODE HERE
	}

}
