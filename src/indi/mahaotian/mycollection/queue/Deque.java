package indi.mahaotian.mycollection.queue;

public interface Deque<E> extends Queue<E>{
    void addFirst(E e);
    void addLast(E e);
    boolean offerFirst(E e);
    boolean offerLast(E e);
    E removeFirst();
    E removeLast();
    E pollFirst();
    E pollLast();
    E getFirst();
    E getLast();
    E peekFirst();
    E peekLast();
    boolean removeFirstOccurrence(Object o);
    boolean removeLastOccurrence(Object o);
    void push(E e);
    E pop();
    boolean remove(Object o);
    boolean contains(Object o);
    public int size();

}
