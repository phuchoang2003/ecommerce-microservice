package com.hdp.core.usecase;

public interface Usecase<I, O> {
    O execute(I input);
}