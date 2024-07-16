package indi.mahaotian.mycollection.list;

import indi.mahaotian.mycollection.Collection;

public interface List<T> extends Collection<T> {
    T get(int index);
    T set(int index, T element);
    void add(int index, T element);
    T remove(int index);
    int indexOf(Object o);
    int lastIndexOf(Object o);
}
