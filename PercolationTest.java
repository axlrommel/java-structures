import static org.junit.Assert.*;

import org.junit.Test;

public class PercolationTest {
    @Test
    public void Test() {
        Percolation perc = new Percolation(5);
        perc.open(4, 1);
        perc.open(3, 2);
        perc.open(3, 1);
        perc.open(3, 5);
        perc.open(4, 4);
        perc.open(5, 4);
        perc.open(5, 5);
        perc.open(3, 4);
        perc.open(2, 3);
        perc.open(2, 1);
        perc.open(3, 3);
        perc.open(1, 1);
        assertEquals(true, perc.percolates());
    }
}
