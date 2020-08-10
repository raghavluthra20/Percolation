/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double mean, stddev;
    private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("ERROR");

        this.trials = trials;

        double[] proportionOfOpenSitesPerTrial = new double[trials];
        double totalSites = n * n;

        for (int trial = 0; trial < trials; trial++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int randomRow = StdRandom.uniform(1, n + 1);
                int randomCol = StdRandom.uniform(1, n + 1);
                if (!percolation.isOpen(randomRow, randomCol))
                    percolation.open(randomRow, randomCol);
            }

            proportionOfOpenSitesPerTrial[trial] = percolation.numberOfOpenSites() / totalSites;
        }
        this.mean = StdStats.mean(proportionOfOpenSitesPerTrial);
        this.stddev = StdStats.stddev(proportionOfOpenSitesPerTrial);
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.print("mean = " + stats.mean() + "\n");
        StdOut.print("stddev = " + stats.stddev() + "\n");
        StdOut.print(
                "95% Confidence Interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi()
                        + "]\n");
    }
}
