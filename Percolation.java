/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int openSitesCount;
    private boolean[][] openSites;
    private WeightedQuickUnionUF grid, gridWithoutBottom;

    private int n, top, bottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("ERROR");

        this.n = n;
        this.openSites = new boolean[n][n];
        this.openSitesCount = 0;
        this.grid = new WeightedQuickUnionUF(n * (n + 2));
        this.gridWithoutBottom = new WeightedQuickUnionUF(n * (n + 1));

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                openSites[i][j] = false;
            }
        }

        top = 0;
        bottom = n * (n + 1);

        connectFirstRowToTop();
        connectLastRowToBottom();
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateCoordinate(row);
        validateCoordinate(col);

        openSites[row - 1][col - 1] = true;
        openSitesCount++;

        connectToNeighbors(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateCoordinate(row);
        validateCoordinate(col);

        return openSites[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateCoordinate(row);
        validateCoordinate(col);

        return isOpen(row, col) && isConnectedToTop(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.connected(top, bottom);
    }

    /* HELPER FUNCTIONS */

    // loops over first row nodes and connects them to top
    private void connectFirstRowToTop() {
        for (int col = 1; col <= n; col++)
            connectToTop(1, col);   // coordinates of node to connect to top
    }

    // called in connectFirstRowToTop()
    // connects passed node to top
    private void connectToTop(int row, int col) {
        grid.union(top, convertTo1d(row, col));
        gridWithoutBottom.union(top, convertTo1d(row, col));
    }

    // loops over last row nodes and connects them to bottom
    private void connectLastRowToBottom() {
        for (int col = 1; col <= n; col++)
            connectToBottom(n, col);   // coordinates of node to connect to bottom
    }

    // called in connectLastRowToBottom()
    // connects passed node to bottom
    private void connectToBottom(int row, int col) {
        grid.union(bottom, convertTo1d(row, col));
    }

    // called in open() to connect opened site to open neighbors
    private void connectToNeighbors(int row, int col) {
        // right side
        connect(row, col, row, col + 1);
        // left side
        connect(row, col, row, col - 1);
        // top side
        connect(row, col, row - 1, col);
        // bottom side
        connect(row, col, row + 1, col);
    }

    // helper function called in connectToNeighbors
    // validates passed neighbor and connects to og node if neighbor is open
    private void connect(int row1, int col1, int row2, int col2) {
        // go back if neighbor out of bounds
        if (row2 < 1 || row2 > n || col2 < 1 || col2 > n)
            return;

        // go back if neighbor closed
        if (!isOpen(row2, col2))
            return;

        // now make connection
        grid.union(convertTo1d(row1, col1), convertTo1d(row2, col2));
        gridWithoutBottom.union(convertTo1d(row1, col1), convertTo1d(row2, col2));
    }

    private boolean isConnectedToTop(int row, int col) {
        return gridWithoutBottom.connected(top, convertTo1d(row, col));
    }

    // converts 2d grid coordinate to 1d coordinate and returns
    private int convertTo1d(int row, int col) {
        return ((row - 1) * n + col);
    }

    private void validateCoordinate(int coord) {
        if (coord < 1 || coord > n)
            throw new IllegalArgumentException("ERROR");
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}
