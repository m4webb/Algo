package com.bamwebb.algo.data;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class ReadOnlyList<T> {

    final private List<T> list;
   
    public ReadOnlyList(List<T> list) {
        this.list = list;
    }
    
    public T get(int index) {
        return list.get(index);
    }
    
    public Enumeration<T> enumerate() {
        return Collections.enumeration(list);
    }
    
    public int size() {
        return list.size();
    }
}
