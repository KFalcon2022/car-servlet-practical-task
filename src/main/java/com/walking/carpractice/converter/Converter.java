package com.walking.carpractice.converter;

public interface Converter<S, T> {
    T convert(S source);
}
