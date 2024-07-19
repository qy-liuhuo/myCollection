package indi.mahaotian.mycollection.list;


import indi.mahaotian.mycollection.Collection;
import indi.mahaotian.mycollection.queue.Deque;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;


public class LinkedList<E> implements List<E>, Deque<E>,Cloneable, java.io.Serializable {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private int size;
    private Node<E> virtualFirst;
    private Node<E> virtualLast;

    public LinkedList(){
        virtualFirst = new Node<>(null,null,null);
        virtualLast = new Node<>(virtualFirst,null,virtualFirst);
        virtualLast.next = virtualLast;
        size = 0;
    }

    public LinkedList(Collection<? extends E> c){
        this();
        this.addAll(c);
    }

    @Override
    public int size() {
        return this.size;
    }
    @Override
    public boolean isEmpty() {
        return this.size==0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] ca = c.toArray();
        int caLen = ca.length;
        if(caLen==0)
            return false;
        Node<E> pre = virtualLast.prev;
        for (Object o : ca) {
            @SuppressWarnings("unchecked")
            Node<E> temp = new Node<>(pre, (E) o, null);
            temp.prev.next = temp;
            pre = temp;
        }
        pre.next = virtualLast;
        virtualLast.prev = pre;
        size+=caLen;
        return true;
    }

    @Override
    public void addFirst(E e) {
        @SuppressWarnings("")
        Node<E> newNode = new Node<>(virtualFirst,e,virtualFirst.next);
        newNode.next.prev = newNode;
        size++;
    }

    @Override
    public void addLast(E e) {
        @SuppressWarnings("")
        Node<E> newNode = new Node<>(virtualLast.prev,e,virtualLast);
        newNode.prev.next = newNode;
        size++;
    }
    @Override
    public boolean offer(E e) {
        return add(e);
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
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        Node<E> first = virtualFirst.next;
        if(first == virtualLast)
            throw new NoSuchElementException();
        first.next.prev = virtualFirst;
        virtualFirst.next = first.next;
        E element = first.item;
        first = null; // 垃圾回收
        size--;
        return element;
    }

    @Override
    public E removeLast() {
        Node<E> last = virtualLast.prev;
        if(last == virtualFirst)
            throw new NoSuchElementException();
        last.prev.next = virtualLast;
        virtualFirst.prev = last.prev;
        E element = last.item;
        last = null; //GC
        size--;
        return element;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        Node<E> temp = virtualFirst.next;
        if (o == null) {
            while (temp!=virtualLast){
                if(temp.item==null){
                    temp.prev.next = temp.next;
                    temp.next.prev = temp.prev;
                    temp = null; //gc
                    size--;
                    return true;
                }
                temp = temp.next;
            }
        } else {
            while (temp!=virtualLast){
                if(o.equals(temp.item)){
                    temp.prev.next = temp.next;
                    temp.next.prev = temp.prev;
                    temp = null; //gc
                    size--;
                    return true;
                }
                temp = temp.next;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> temp = virtualLast.prev;
        if (o == null) {
            while (temp!=virtualFirst){
                if(temp.item==null){
                    temp.prev.next = temp.next;
                    temp.next.prev = temp.prev;
                    temp = null; //gc
                    size--;
                    return true;
                }
                temp = temp.prev;
            }
        } else {
            while (temp!=virtualFirst){
                if(o.equals(temp.item)){
                    temp.prev.next = temp.next;
                    temp.next.prev = temp.prev;
                    temp = null; //gc
                    size--;
                    return true;
                }
                temp = temp.prev;
            }
        }
        return false;
    }

    @Override
    public E poll() {
       return pollFirst();
    }

    @Override
    public E pollFirst() {
        return virtualFirst.next == virtualLast?null:removeFirst();
    }

    @Override
    public E pollLast() {
        return virtualLast.prev == virtualFirst?null:removeLast();
    }


    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if(virtualFirst.next == virtualLast)
            throw new NoSuchElementException();
        return virtualFirst.next.item;
    }

    @Override
    public E getLast() {
        if(virtualLast.prev == virtualFirst)
            throw new NoSuchElementException();
        return virtualLast.prev.item;
    }

    @Override
    public E peek() {
        return peekFirst();
    }


    @Override
    public E peekFirst() {
        return virtualFirst.next == virtualLast?null:getFirst();
    }

    @Override
    public E peekLast() {
        return virtualFirst.next == virtualLast?null:getLast();
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
    public void clear() {
        Node<E> temp = virtualFirst.next;
        while (temp!=virtualLast){
            Node<E> next = temp.next;
            temp = null;
            temp = next;
        }
        virtualFirst.next = virtualLast;
        virtualLast.prev = virtualFirst;
        size = 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Node<E> temp = virtualFirst.next;
        while (temp!=virtualLast){
            assert temp != null;
            if (c.contains(temp.item)){
                temp.prev.next = temp.next;
                temp.next.prev = temp.prev;
                temp = null;
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Node<E> temp = virtualFirst.next;
        while (temp!=virtualLast){
            assert temp != null;
            if (!c.contains(temp.item)){
                temp.prev.next = temp.next;
                temp.next.prev = temp.prev;
                temp = null;
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e:c){
            if(contains(e))
                return false;
        }
        return true;
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        Node<E> temp = virtualFirst.next;
        for(int i=0;i<size;i++){
            a[i] = temp.item;
            temp = temp.next;
        }
        return a;
    }

    @Override
    public E[] toArray(E[] a) {
        if(a.length<size){
            return (E[]) toArray();
        }

        Node<E> temp = virtualFirst.next;
        for(int i=0;i<size;i++){
            a[i] = temp.item;
            temp = temp.next;
        }
        if(a.length>size)
            a[size] = null;
        return a;
    }

    @Override
    public E get(int index) {
        if(index < 0 || index >= size)
            throw new ArrayIndexOutOfBoundsException("Index:"+index+"is larger than Size:"+size);
        Node<E> temp = virtualFirst.next;
        for(int i = 0; i<index ;i++){
            temp = temp.next;
        }
        return temp.item;
    }

    @Override
    public E set(int index, E element) {
        if(index < 0 || index >= size)
            throw new ArrayIndexOutOfBoundsException("Index:"+index+"is larger than Size:"+size);
        Node<E> temp = virtualFirst.next;
        for(int i = 0; i<index ;i++){
            temp = temp.next;
        }
        E oldVal = temp.item;
        temp.item = element;
        return oldVal;

    }

    @Override
    public void add(int index, E element) {
        if( index < 0 || index > size )
            throw new ArrayIndexOutOfBoundsException("Index:"+index+"is larger than Size:"+size);
        Node<E> temp = virtualFirst;
        for(int i = 0; i < index; i++){
            temp = temp.next;
        }
        Node<E> newNode = new Node<>(temp,element,temp.next);
        newNode.prev.next = newNode;
        newNode.next.prev = newNode;
    }

    @Override
    public E remove(int index) {
        if(index < 0 || index >= size)
            throw new ArrayIndexOutOfBoundsException("Index:"+index+"is larger than Size:"+size);
        Node<E> temp = virtualFirst;
        for(int i = 0; i<index ;i++){
            temp = temp.next;
        }
        Node<E> oldNode = temp.next;
        E oldVal = oldNode.item;
        temp.next = temp.next.next;
        temp.next.prev = temp;
        oldNode = null; //GC
        return oldVal;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        Node<E> temp = virtualFirst.next;
        if(o == null){
            while (temp!=virtualLast){
                if(temp.item == null)
                    return index;
                index++;
                temp = temp.next;
            }
        }
        else{
            while (temp!=virtualLast){
                if(o.equals(temp.item))
                    return index;
                index++;
                temp = temp.next;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size-1;
        Node<E> temp = virtualLast.prev;
        if(o == null){
            while (temp!=virtualFirst){
                if(temp.item == null)
                    return index;
                index--;
                temp = temp.prev;
            }
        }
        else{
            while (temp!=virtualFirst){
                index--;
                if(o.equals(temp.item))
                    return index;
                temp = temp.prev;
            }
        }
        return -1;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public LinkedList<E> clone() {
        try {
            return (LinkedList) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
