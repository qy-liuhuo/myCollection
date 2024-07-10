package indi.mahaotian.mycollection;

public interface Collection<T> extends Iterable<T>{
    int size();
    boolean isEmpty();
    boolean contains(Object o);
    boolean add(T t);
    boolean remove(Object o);
    void clear();
    boolean addAll(Collection<? extends T> c);
    boolean removeAll(Collection<?> c);
    boolean retainAll(Collection<?> c);
    boolean containsAll(Collection<?> c);
    Object[] toArray();
    T[] toArray(T[] a);
    boolean equals(Object o);
    int hashCode();

}
