package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import java.util.List;

public class DataConfig {

    private double min = 0;
    private double max = 1;
    private List<Double> from;
    private int count = 8;
    private int decimals = 2;
    private int continuity = 1;

    public DataConfig min(double min) {
        this.min = min;
        return this;
    }

    public DataConfig max(double max) {
        this.max = max;
        return this;
    }

    public DataConfig from(List<Double> from) {
        this.from = from;
        return this;
    }

    public DataConfig count(int count) {
        this.count = count;
        return this;
    }

    public DataConfig decimals(int decimals) {
        this.decimals = decimals;
        return this;
    }

    public DataConfig continuity(int continuity) {
        this.continuity = continuity;
        return this;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public int getCount() {
        return count;
    }

    public int getDecimals() {
        return decimals;
    }

    public int getContinuity() {
        return continuity;
    }

    public List<Double> getFrom() {
        return from;
    }
}
