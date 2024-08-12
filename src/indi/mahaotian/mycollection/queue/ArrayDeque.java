package indi.mahaotian.mycollection.queue;

import indi.mahaotian.mycollection.Collection;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class ArrayDeque<E>  implements Deque<E> {

    transient Object[] elements;

    transient int head;

    transient int tail;

    private static final int MIN_INITIAL_CAPACITY = 8;

    private static int calculateSize(int numElements){
        int size = MIN_INITIAL_CAPACITY;
        if(numElements >= size){
            size = numElements;
            size |= (size >>> 1);
            size |= (size >>> 2);
            size |= (size >>> 4);
            size |= (size >>> 8);
            size |= (size >>> 16);
            size++;
        }
        if (size < 0){
            size = size >>> 1;
        }
        return size;
    }

    private void doubleCapacity(){
        assert head == tail;
        int p = head;
        int n = elements.length;
        int r = n - p;
        int newCapacity = n << 1; // double
        if (newCapacity < 0)
            throw new IllegalStateException("capacity out of the max value");
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a,0, r); //分两段copy
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }

    private void checkInvariants() {
        assert elements[tail] == null;
        assert head == tail ? elements[head] == null :
                (elements[head] != null &&
                        elements[(tail - 1) & (elements.length - 1)] != null);
        assert elements[(head - 1) & (elements.length - 1)] == null;
    }

    private boolean delete(int i){
        checkInvariants();
        final Object[] elements = this.elements;
        final int mask = elements.length - 1;
        final int h = head;
        final int t = tail;
        final int front = (i - h) & mask; // 前面的元素个数
        final int back  = (t - i) & mask; // 后面的元素个数

        // Invariant: head <= i < tail mod circularity
        if (front >= ((t - h) & mask))
            throw new ConcurrentModificationException();

        // Optimize for least element motion
        if (front < back) {
            if (h <= i) {
                System.arraycopy(elements, h, elements, h + 1, front);
            } else { // Wrap around
                System.arraycopy(elements, 0, elements, 1, i);
                elements[0] = elements[mask];
                System.arraycopy(elements, h, elements, h + 1, mask - h);
            }
            elements[h] = null;
            head = (h + 1) & mask;
            return false;
        } else {
            if (i < t) { // Copy the null tail as well
                System.arraycopy(elements, i + 1, elements, i, back);
                tail = t - 1;
            } else { // Wrap around
                System.arraycopy(elements, i + 1, elements, i, mask - i);
                elements[mask] = elements[0];
                System.arraycopy(elements, 1, elements, 0, t);
                tail = (t - 1) & mask;
            }
            return true;
        }
    }

    public ArrayDeque() {
        elements = new Object[16];
    }

    public ArrayDeque(int numElements){
        elements = new Object[calculateSize(numElements)];
    }

    public ArrayDeque(Collection<? extends E> c){
        this(c.size());
        addAll(c);
    }


    @Override
    public void addFirst(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[head = (head - 1) & (elements.length - 1)] = e;
        if (head == tail)
            doubleCapacity();

    }

    @Override
    public void addLast(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[tail] = e;
        if ((tail = (tail + 1) & (elements.length - 1)) == head ){
            doubleCapacity();
        }
    }

    public boolean addAll(Collection<? extends E> c){
        boolean modified = false;
        for(E e : c){
            if(add(e))
                modified = true;
        }
        return modified;
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        E x = pollFirst();
        if (x == null)
            throw new NoSuchElementException();
        return x;
    }

    @Override
    public E removeLast() {
        E x = pollLast();
        if (x == null)
            throw new NoSuchElementException();
        return x;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E pollFirst() {
        E result = (E) elements[head];
        if (result == null)
            return null;
        elements[head] = null;
        head = (head + 1) & (elements.length - 1);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E pollLast() {
        int temp = (tail - 1) & (elements.length - 1);
        E result = (E) elements[temp];
        if (result == null)
            return null;
        elements[temp] = null;
        tail = temp;
        return result;

    }

    @Override
    @SuppressWarnings("unchecked")
    public E getFirst() {
        E result = (E) elements[head];
        if(result == null)
            throw new NoSuchElementException();
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E getLast() {
        int temp = (tail - 1) & (elements.length - 1);
        E result = (E) elements[temp];
        if (result == null)
            throw new NoSuchElementException();
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peekFirst() {
        return (E) elements[head];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peekLast() {
        return (E) elements[(tail - 1) & (elements.length - 1)];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o == null)
            return false;
        int len = elements.length;
        int i = head;
        Object x;
        while ((x = elements[i]) != null){
            if (o.equals(x)){
                delete(i);
                return true;
            }
            i = (i + 1) & (len - 1);
        }

        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o == null)
            return false;
        int len = elements.length;
        int i = (tail - 1) & (len - 1);
        Object x;
        while ((x = elements[i]) != null){
            if(o.equals(x)){
                delete(i);
                return true;
            }
            i = (i - 1) & (len - 1);
        }
        return false;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        removeFirstOccurrence(o);
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if(o == null)
            return false;
        int mask = elements.length - 1;
        int i = head;
        Object x;
        while ( (x = elements[i]) != null) {
            if (o.equals(x))
                return true;
            i = (i + 1) & mask;
        }
        return false;
    }

    @Override
    public int size() {
        return (tail - head) & (elements.length - 1);
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }
}
