package indi.mahaotian.mycollection.queue;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.*;

public class PriorityQueue<E> extends AbstractQueue<E> implements Serializable {

    private Object[] queue;

    private int size = 0;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    private final Comparator<? super E> comparator;


    public PriorityQueue(int initCap, Comparator<? super E> comparator){

        if(initCap < 1 || initCap > MAX_ARRAY_SIZE)
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
        initFromPriorityQueue(c);
    }

    @SuppressWarnings("unchecked")
    public PriorityQueue(SortedSet<? extends E> c) {
        this.comparator = (Comparator<? super E>) c.comparator();
        initElementsFromCollection(c);
    }

    private void initFromCollection(Collection<? extends E> c) {
        initElementsFromCollection(c);
        heapify();
    }

    private void initFromPriorityQueue(PriorityQueue<? extends E> pq) {
        if(pq.getClass() == PriorityQueue.class){
            this.queue = pq.toArray();
            this.size = pq.size();
        }else{
            initFromCollection(pq);
        }
    }
    private void initElementsFromCollection(Collection<? extends E> c){
        Object[] a = c.toArray();
        if(c.getClass() != ArrayList.class)
            a = Arrays.copyOf(a,a.length,Object[].class);
        int len = a.length;
        if (len == 1 || this.comparator != null) //??
            for (Object o : a)
                if (o == null)
                    throw new NullPointerException();
        this.queue = a;
        this.size = a.length;
    }

    @Override
    public int size() {
        return 0;
    }

    private Comparator<? super E> comparator() {
        return this.comparator;
    }

    @Override
    public boolean offer(E e) {
        if(e == null)
            throw new NullPointerException();
        int i = size;
        if(i>=queue.length)
            grow(i+1);
        size++;
        if(i==0)
            queue[0] = e;
        else
            siftUp(i,e); //插入后向上调整
        return true;
    }


    @SuppressWarnings("unchecked")
    @Override
    public E poll() {
        if(size == 0)
            return null;
        size--;
        E result = (E) queue[0];
        E last = (E) queue[size];
        queue[size] = null;
        if(size != 0)
            siftDown(0,last);
        return result;
    }
    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        if(size == 0)
            return null;
        return (E) queue[0];
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    private void grow(int targetCapacity){
        int oldCapacity = queue.length;
        int newCapacity = oldCapacity + ((oldCapacity<64)? (oldCapacity+2):(oldCapacity>>1));
        newCapacity = Math.max(newCapacity,targetCapacity);
        newCapacity = Math.min(newCapacity,MAX_ARRAY_SIZE);
        queue = Arrays.copyOf(queue,newCapacity);
    }

    @SuppressWarnings("unchecked")
    private void heapify() {
        for(int i = (size >>> 1) - 1; i >= 0; i--)
            siftDown(i, (E) this.queue[i]);
    }

    private void siftUp(int i, E e) {
        if (comparator != null)
            siftUpWithComparator(k, x);
        else
            siftUpWithoutComparator(k, x);
    }

    @SuppressWarnings("unchecked")
    private void siftUpWithComparator(int k, E x){
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (comparator.compare(x, (E) e) >= 0)
                break;
            queue[k] = e;
            k = parent;
        }
        queue[k] = x;
    }

    @SuppressWarnings("unchecked")
    private void siftUpWithoutComparator(int k, E x){
        Comparable<? super E> key = (Comparable<? super E>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (key.compareTo((E) e) >= 0)
                break;
            queue[k] = e;
            k = parent;
        }
        queue[k] = key;
    }

    private void siftDown(int k, E x) {
        if(comparator != null)
            siftDownWithComparator(k,x);
        else
            siftDownWithoutComparator(k,x);
    }
    @SuppressWarnings("unchecked")
    private void siftDownWithoutComparator(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>) x; // 获取comparable对象，来使用compareto方法，super说明E是下界，如果E没有实现comparable接口，会向上查找
        int half = size>>>1;
        while(k < half){
            int child = (k<<1) + 1;
            Object c = queue[child];
            int right = child + 1;
            //如果右节点小于左节点则更新child
            if(right < size && ((Comparable<? super E>)c).compareTo((E) queue[right])>0){
                c = queue[child = right];
            }
            if(key.compareTo((E) c) <=0)
                break; //无需调整
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }

    @SuppressWarnings("unchecked")
    private void siftDownWithComparator(int k, E x) {
        int half = size >>> 1;
        while(k < half){
            int child = (k<<1) + 1;
            Object c = queue[child];
            int right = child + 1;
            //如果右节点小于左节点则更新child
            if(right < size && comparator.compare((E) c,(E) queue[right]) > 0){
                c = queue[child = right];
            }
            if(comparator.compare(x,(E) c) <= 0) //根节点小于子节点
                break; //无需调整
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }

}
