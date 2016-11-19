package com.bamwebb.data;

public class ReadOnlyArray<T> {

    final private T[] array;
    
    public ReadOnlyArray(T[] array) {
        this.array = array;
    }
    
    public T getIndex(int i) {
        return array[i];
    }
}
