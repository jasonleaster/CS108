import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2016/6/11.
 */
public class SudokuTest {

    static final int [][] arrays        = {{1,0,3},{3,1,0},{0,3,1}};
    static final int [][] answer_skd_1  = {{1,2,3},{3,1,2},{1,3,1}};
    private Sudoku skd_1;
    private Sudoku skd_2;
    @Before
    public void setUp(){
        this.skd_1 = new Sudoku(arrays);
        this.skd_2 = new Sudoku(Sudoku.hardGrid);
    }

    @Test
    public void testSolver(){

        int ret = this.skd_2.solve();
        System.out.print(this.skd_2.getSolutionText());
        System.out.print("Cost time in nano seconds: " + this.skd_2.getElapsed());
        assertEquals(0, ret);
    }

}
