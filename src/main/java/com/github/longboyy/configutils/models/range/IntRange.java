package com.github.longboyy.configutils.models.range;

import com.github.longboyy.configutils.models.IRange;

public record IntRange(int minVal, int maxVal) implements IRange<Integer> {
    @Override
    public Integer getMin() {
        return this.minVal;
    }

    @Override
    public Integer getMax() {
        return this.maxVal;
    }
}
