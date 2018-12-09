import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static WeightedQuickUnionUF wquf;
    private int[] status;
    private int count = 0;
    private int N = 0;
    private int index = 0;
    private static final byte OPEN = 1;
    private static final byte CONNECT_TO_TOP = 2;
    private static final byte CONNECT_TO_BOTTOM = 4;
    private static boolean isPercolation = false;

    public Percolation(int n) {
        N = n;
        wquf = new WeightedQuickUnionUF(N * N);
        status = new int[N * N];
        for (int i = 0; i < N; i++) {
            status[i] = CONNECT_TO_TOP;
            status[N * N - 1 - i] = CONNECT_TO_BOTTOM;
        }
    }// create n-by-n grid, with all sites blocked

    private int changeIndexDim(int row, int col) {
        return (row) * N + (col);
    }

    private void check(int row, int col, char dir) {
        switch (dir) {
            case 'd':
                if ((status[index + N] & OPEN) == 1) {
                    updateStatus(row + 1, col);
                    wquf.union(index + N, index);
                }
                break;
            case 'u':
                if ((status[index - N] & OPEN) == 1) {
                    updateStatus(row - 1, col);
                    wquf.union(index - N, index);
                }
                break;
            case 'l':
                if ((status[index - 1] & OPEN) == 1) {
                    updateStatus(row, col - 1);
                    wquf.union(index - 1, index);
                }
                break;
            case 'r':
                if ((status[index + 1] & OPEN) == 1) {
                    updateStatus(row, col + 1);
                    wquf.union(index + 1, index);
                }
                break;
        }
    }

    private void updateStatus(int row, int col) {
        int ind = changeIndexDim(row, col);
        status[wquf.find(ind)] |= status[ind];
        status[ind] |= status[wquf.find(ind)];
        status[index] |= status[wquf.find(ind)];
        // System.out.printf("status[%d] = %d \n",index,status[index]);
    }


    private void checkAround(int row, int col) {
        if (row == 0 && col == 0) {
            check(row, col, 'r');
            check(row, col, 'd');
        } else if (row == 0 && col == (N - 1)) {
            check(row, col, 'l');
            check(row, col, 'd');
        } else if (row == (N - 1) && col == (N - 1)) {
            check(row, col, 'l');
            check(row, col, 'u');
        } else if (row == (N - 1) && col == 0) {
            check(row, col, 'r');
            check(row, col, 'u');
        } else if (row >= 0 && col == 0) {
            check(row, col, 'r');
            check(row, col, 'u');
            check(row, col, 'd');
        } else if (row >= 0 && col == (N - 1)) {
            check(row, col, 'l');
            check(row, col, 'u');
            check(row, col, 'd');
        } else if (row == 0 && col >= 0) {
            check(row, col, 'l');
            check(row, col, 'r');
            check(row, col, 'd');
        } else if (row == (N - 1) && col >= 0) {
            check(row, col, 'l');
            check(row, col, 'r');
            check(row, col, 'u');
        } else {
            check(row, col, 'r');
            check(row, col, 'l');
            check(row, col, 'u');
            check(row, col, 'd');
        }
    }

    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row > N || col > N) {
            throw new java.lang.IllegalArgumentException();
        }
        row--;
        col--;
        index = changeIndexDim(row, col);
        if ((status[index] & OPEN) == 0) {
            status[index] = status[index] | OPEN;
            count++;
            if (N == 1) {
                status[index] = OPEN | CONNECT_TO_BOTTOM | CONNECT_TO_TOP;
                isPercolation = true;
                return;
            }
            checkAround(row, col);
            updateStatus(row, col);
            if ((status[index] & (OPEN | CONNECT_TO_TOP | CONNECT_TO_BOTTOM)) == (OPEN | CONNECT_TO_TOP | CONNECT_TO_BOTTOM)) {
                isPercolation = true;
            }
        }
    }    // open site (row, col) if it is not open already


    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > N || col > N) {
            throw new java.lang.IllegalArgumentException();
        }
        row--;
        col--;
        return ((status[changeIndexDim(row, col)] & OPEN) == OPEN);
    } // is site (row, col) open?

    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > N || col > N) {
            throw new java.lang.IllegalArgumentException();
        }
        row--;
        col--;
        int ind = changeIndexDim(row, col);

        if ((status[wquf.find(ind)] & (OPEN | CONNECT_TO_TOP)) == (OPEN | CONNECT_TO_TOP)) {
            return true;
        } else {
            return false;
        }
    }  // is site (row, col) full?


    public int numberOfOpenSites() {
        return count;
    }      // number of open sites

    public boolean percolates() {
        return isPercolation;
    }             // does the system percolate?

    public static void main(String[] args) {
        Percolation pc = new Percolation(4);
        pc.open(1,4);
        pc.open(4,4);
        pc.open(2,4);
        pc.open(3,4);
        System.out.println(pc.percolates());
    }

}
