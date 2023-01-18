import edu.princeton.cs.algs4.StdRandom;

public class Percolation {

    private int[] id;
    private int sideLength;

    private int getIndex(int row, int col) {
        int index = (row - 1) * sideLength + (col - 1) + 1;
        return index;
    }

    private int[] getAdjacents(int row, int col) {
        int index = 0;
        int[] adjacents = new int[4];
        if (row == 1) {
            adjacents[index++] = -1;
            adjacents[index++] = getIndex(2, col);
        } else if (row == sideLength) {
            adjacents[index++] = getIndex(sideLength - 1, col);
            adjacents[index++] = -1;
        } else {
            adjacents[index++] = getIndex(row - 1, col);
            adjacents[index++] = getIndex(row + 1, col);
        }

        if (col == 1) {
            adjacents[index++] = -1;
            adjacents[index++] = getIndex(row, 2);
        } else if (col == sideLength) {
            adjacents[index++] = getIndex(row, sideLength - 1);
            adjacents[index++] = -1;
        } else {
            adjacents[index++] = getIndex(row, col - 1);
            adjacents[index++] = getIndex(row, col + 1);
        }

        return adjacents;
    }

    private static void printAdjacents(int[] points) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < points.length; i++) {
            buff.append(points[i]);
            buff.append(" ,");
        }
        System.out.println(buff.toString());
    }

    private void printValues() {
        StringBuffer mainBuff = new StringBuffer();
        for (int i = 0; i < sideLength * sideLength; i++) {
            mainBuff.append(id[i + 1]);
            if ((i + 1) % sideLength == 0) {
                mainBuff.append('\n');
            }
        }
        System.out.println(mainBuff);
    }

    private void printGrid() {
        StringBuffer mainBuff = new StringBuffer();
        for (int i = 0; i < sideLength * sideLength; i++) {
            if (id[i + 1] == -1) {
                mainBuff.append('X');
            } else {
                mainBuff.append(' ');
            }
            if ((i + 1) % sideLength == 0) {
                mainBuff.append('\n');
            }
        }
        System.out.println(mainBuff);
    }

    private void printRoots() {
        StringBuffer mainBuff = new StringBuffer();
        for (int i = 0; i < sideLength * sideLength; i++) {
            mainBuff.append(root(i + 1));
            if ((i + 1) % sideLength == 0) {
                mainBuff.append('\n');
            }
        }
        System.out.println(mainBuff);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < sideLength * sideLength; i++) {
            if (id[i + 1] > -1)
                count++;
        }
        return count;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return id[getIndex(row, col)] != -1;
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        int totalSites = n * n + 2;
        id = new int[totalSites];
        for (int i = 0; i < totalSites; i++) {
            id[i] = -1;
        }
        id[0] = 0;
        id[totalSites - 1] = totalSites - 1;
        sideLength = n;
    }

    // make every other node in path point to its grandparent
    private int root(int i) {
        if (id[i] == -1) {
            return -1;
        }
        while (i != id[i]) {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    private boolean adjacentIsZeroRoot(int[] adjacents) {
        return ((adjacents[0] != -1 && root(adjacents[0]) == 0) || (adjacents[1] != -1 && root(adjacents[1]) == 0)
                || (adjacents[2] != -1 && root(adjacents[2]) == 0) || (adjacents[3] != -1 && root(adjacents[3]) == 0));
    }

    private boolean adjacentIsLastRoot(int[] adjacents) {
        int lastRoot = sideLength * sideLength + 1;
        return ((adjacents[0] != -1 && root(adjacents[0]) == lastRoot)
                || (adjacents[1] != -1 && root(adjacents[1]) == lastRoot)
                || (adjacents[2] != -1 && root(adjacents[2]) == lastRoot)
                || (adjacents[3] != -1 && root(adjacents[3]) == lastRoot));
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int pos = getIndex(row, col);
        if (id[pos] > -1) {
            return;
        }
        int[] adjacents = getAdjacents(row, col);
        int newRoot = 0;
        if (adjacentIsZeroRoot(adjacents) && adjacentIsLastRoot(adjacents)) { // percolates
            id[sideLength * sideLength + 1] = 0;
        }
        if (row == 1 || adjacentIsZeroRoot(adjacents)) {
            id[pos] = 0;
        } else if (row == sideLength || adjacentIsLastRoot(adjacents)) {
            id[pos] = sideLength * sideLength + 1;
            newRoot = sideLength * sideLength + 1;
        } else {
            id[pos] = pos;
            newRoot = pos;
        }

        // update all sites with the adjacent keys to match newRoot
        for (int i = 0; i < 4; i++) {
            if (adjacents[i] != -1 && id[adjacents[i]] != -1 && id[adjacents[i]] != 0
                    && id[adjacents[i]] != sideLength * sideLength + 1) {
                for (int j = 1; j < sideLength * sideLength + 1; j++) {
                    if (id[j] == root(adjacents[i])) {
                        id[j] = newRoot;
                    }
                }
            }
        }
    }

    // A full site is an open site that can be connected to an open site in the top
    // row via a chain of neighboring (left,right, up, down) open sites.
    public boolean isFull(int row, int col) {
        int index = getIndex(row, col);
        if (id[index] == -1) {
            return false;
        }
        return root(index) == 0;
    }

    // does the system percolate?
    public boolean percolates() {
        return root(0) == root(sideLength * sideLength + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        int numberOfRows = 5;
        Percolation perc = new Percolation(numberOfRows);
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
        perc.printRoots();
        perc.open(3, 3);
        perc.printRoots();
        // perc.printValues();

        perc.open(1, 1);
        perc.printRoots();
        // perc.printValues();

        // perc.printGrid();
        // perc.printRoots();

        // boolean found = false;
        // while (!found) {
        // int randomOne = StdRandom.uniformInt(1, numberOfRows + 1);
        // int randomTwo = StdRandom.uniformInt(1, numberOfRows + 1);
        // perc.open(randomOne, randomTwo);
        // System.out.println(randomOne + ", " + randomTwo);
        // found = perc.percolates();
        // }
        // System.out.println(perc.numberOfOpenSites());
        System.out.println(perc.percolates());
        // perc.printRoots();
        // perc.printGrid();
    }
}
