package com.github.longboyy.configutils.models.range;

import com.github.longboyy.configutils.models.IRange;

public record DoubleRange(double minVal, double maxVal) implements IRange<Double> {
    @Override
    public Double getMin() {
        return this.minVal;
    }

    @Override
    public Double getMax() {
        return this.maxVal;
    }
}
