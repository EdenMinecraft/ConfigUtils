package com.github.longboyy.configutils.models;

import java.util.Random;

public interface IRange<T extends Number> {
    Random random = new Random();

    T getMin();
    T getMax();
    T getRandom();
}
