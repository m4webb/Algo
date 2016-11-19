package com.bamwebb.data;

import java.util.ArrayList;
import java.util.Map;

public class ReadOnlyMap<A, B> {

    final private Map<A, B> map;
    
    public ReadOnlyMap(Map<A, B> map) {
        this.map = map;
    }
    
    public boolean containsKey(A a) {
        return map.containsKey(a);
    }
    
    public B get(A a) {
        return map.get(a);
    }
    
    public ReadOnlyList<B> valuesList() {
        return new ReadOnlyList<B>(new ArrayList<B>(map.values()));
    }
}
