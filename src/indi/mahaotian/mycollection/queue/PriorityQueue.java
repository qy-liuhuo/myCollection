package indi.mahaotian.mycollection.queue;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.*;

public class PriorityQueue<E> extends AbstractQueue<E> implements Serializable {

    private Object[] queue;

    private int size = 0;

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    private final Comparator<? super E> comparator;


    public PriorityQueue(int initCap, Comparator<? super E> comparator){

        if(initCap < 1)
            throw new IllegalArgumentException();
        this.queue = new Object[initCap];
        this.comparator = comparator;

    }

    public PriorityQueue(int initCap){
        this(initCap,null);
    }

    public PriorityQueue(Comparator<? super E> comparator){
        this(DEFAULT_INITIAL_CAPACITY,comparator);
    }

    public PriorityQueue(){
        this(DEFAULT_INITIAL_CAPACITY,null);
    }

    @SuppressWarnings("unchecked")
    public PriorityQueue(Collection<? extends E> c){
        if(c instanceof SortedSet<?>){
            SortedSet<? extends E> ss = (SortedSet<? extends E>) c;
            this.comparator = ( Comparator<? super E>) ss.comparator();
            initElementsFromCollection(ss);
        }
        else if (c instanceof PriorityQueue<?>) {
            PriorityQueue<? extends E> pq = (PriorityQueue<? extends E>) c;
            this.comparator = (Comparator<? super E>) pq.comparator();
            initFromPriorityQueue(pq);
        }
        else {
            this.comparator = null;
            initFromCollection(c);
        }
    }
    @SuppressWarnings("unchecked")
    public PriorityQueue(PriorityQueue<? extends E> c) {
        this.comparator = (Comparator<? super E>) c.comparator();
    }

    @SuppressWarnings("unchecked")
    public PriorityQueue(SortedSet<? extends E> c) {
        this.comparator = (Comparator<? super E>) c.comparator();
        initElementsFromCollection(c);
    }

    private void initFromCollection(Collection<? extends E> c) {
    }

    private void initFromPriorityQueue(PriorityQueue<? extends E> pq) {
    }

    private Comparator<? super E> comparator() {
        return this.comparator();
    }

    private void initElementsFromCollection(SortedSet<? extends E> ss) {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
