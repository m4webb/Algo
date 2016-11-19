package com.bamwebb.data;

import java.util.List;

public class ReadOnlyList<T> {

    final private List<T> list;
    
    public ReadOnlyList(List<T> list) {
        this.list = list;
    }
    
    public T get(int index) {
        return list.get(index);
    }
}
