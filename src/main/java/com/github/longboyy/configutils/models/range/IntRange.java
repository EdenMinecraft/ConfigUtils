package com.github.longboyy.configutils.models.range;

import com.github.longboyy.configutils.models.IRange;

import java.util.Random;

public record IntRange(int minVal, int maxVal) implements IRange<Integer> {

    //private static final Random random = new Random();

    @Override
    public Integer getMin() {
        return this.minVal;
    }

    @Override
    public Integer getMax() {
        return this.maxVal;
    }

    @Override
    public Integer getRandom() {
        return random.nextInt(this.maxVal - this.minVal + 1) + this.minVal;
    }
}
