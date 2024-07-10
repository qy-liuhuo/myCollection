package indi.mahaotian.mycollection.list;

import indi.mahaotian.mycollection.Iterator;
public interface ListIterator<T> extends Iterator<T> {
    boolean hasNext();
    T next();
    boolean hasPrevious();
    T previous();
    int nextIndex();
    int previousIndex();
    void remove();
    void set(T e);
    void add(T e);
}
