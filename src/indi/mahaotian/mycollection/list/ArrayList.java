package indi.mahaotian.mycollection.list;

import indi.mahaotian.mycollection.Collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.RandomAccess;

public class ArrayList<E> implements List<E> , RandomAccess, Cloneable, java.io.Serializable{

    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private static final int MAX_SIZE = Integer.MAX_VALUE - 8 ; //一些jvm会存储一些额外信息导致不能达到MAX_VALUE容量
    transient Object[] elementData;

    public ArrayList(int initialCapacity) {
        if (initialCapacity>0){
            this.elementData = new Object[initialCapacity];
        }
        else if(initialCapacity==0){
            this.elementData = new Object[]{};
        }
        if(initialCapacity<0){
            throw new IllegalArgumentException("Array size needs to be greater than zero");
        }
    }
    public ArrayList(){
        this.elementData = new Object[]{};
    }

    public ArrayList(Collection<? extends E> c){
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
        return this.indexOf(o) == -1;
    }

    private void grow(int targetCapacity){
        int oldCapacity = this.elementData.length;
        int newCapacity;
        // 未初始化状态
        if(oldCapacity==0){
            newCapacity = targetCapacity;
        }
        else{
            newCapacity = oldCapacity + (oldCapacity>>1); //1.5倍扩容
            newCapacity = Math.max(targetCapacity,newCapacity); //还不够就直接分够
        }
        newCapacity = Math.min(newCapacity, MAX_SIZE);
        this.elementData = Arrays.copyOf(this.elementData,newCapacity);
    }
    @Override
    public E remove(int index) {
        if(index>=size)
            throw new IndexOutOfBoundsException("Index:"+index+"is larger than Size:"+size);
        E value = (E)elementData[index];
        fastRemove(index);
        return value;
    }
    @Override
    public boolean remove(Object o) {
        if(o==null){
            for(int i = 0;i<size;i++){
                if(elementData[i] == null){
                    fastRemove(i);
                    return true;
                }
            }
        }
        else{
            for(int i = 0;i<size;i++){
                if(o.equals(elementData[i])) {
                    fastRemove(i);
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c); // 防止c未null
        return batchRemove(c,false);
    }

    //仅保留c中元素
    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c); // 防止c未null
        return batchRemove(c,true);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    public void fastRemove(int index){
        int numMoved = size - index - 1;// 需向前移动的数量
        if(numMoved!=0){
           System.arraycopy(elementData,index+1,elementData,index,numMoved);
        }
        elementData[--size] = null ; //删除对对象的引用，防止不能被回收
    }

    private boolean batchRemove(Collection<?>c, boolean complement){
        final Object[] elementData_final = this.elementData;
        boolean modified = false;
        int r = 0, w = 0;
        try{
            for(;r < size; r++){
                if(c.contains(elementData_final[r]) == complement)
                    this.elementData[w++] = elementData_final[r];
            }
        } finally {
            if(r!=size)
            {
                System.arraycopy(elementData_final,r,this.elementData,w,size-1);
                w += size - r;
            }
            if(w!=size){
                for(int i = 2; i<size ; i++)
                    elementData[i] = null;
                size = w;
                modified = true;
            }
        }
        return modified;

    }

    @Override
    public void clear() {
        for(int i = 0;i<size;i++){
            elementData[i] = null;
        }
        size = 0;
    }


    @Override
    public boolean add(E e) {
        if(this.size == elementData.length)
            this.grow(this.size+1);
        elementData[this.size++] = e;
        return true;
    }

    @Override
    public void add(int index, E element) {
        if(index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index:"+index+"is larger than Size:"+size);
        if(this.size == elementData.length)
            this.grow(this.size+1);
        System.arraycopy(elementData,index,elementData,index+1,size-index);
        elementData[index] = element;
        size++;

    }


    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] cl = c.toArray();
        int newSize = c.size();
        if( this.size+newSize > elementData.length){
            this.grow( this.size+newSize);
        }
        System.arraycopy(cl,0, elementData, size, c.size());
        size += newSize;
        return true;
    }


    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData,size);
    }

    @Override
    public E[] toArray(E[] a) {
        if(a.length<size)
            return (E[]) Arrays.copyOf(elementData,size,a.getClass());
        System.arraycopy(elementData,0,a,0,size);
        if(a.length>size)
            a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public E get(int index) {
        if(index < 0 ||index>=size)
            throw new ArrayIndexOutOfBoundsException("Index:"+index+"is larger than Size:"+size);
        return (E) elementData[index];
    }

    @Override
    public E set(int index, E element) {
        if(index < 0 ||index>=size)
            throw new IndexOutOfBoundsException("Index:"+index+"is larger than Size:"+size);
        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
    }


    @Override
    public int indexOf(Object o) {
        if(o==null){
            for(int i = 0;i<size;i++){
                if(elementData[i] == null)
                    return i;
            }
        }
        else{
            for(int i = 0;i<size;i++){
                if(o.equals(elementData[i]))
                    return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o==null){
            for(int i = size-1; i >= 0; i--){
                if(elementData[i] == null)
                    return i;
            }
        }
        else{
            for(int i = size-1; i >= 0;i--){
                if(o.equals(elementData[i]))
                    return i;
            }
        }
        return -1;
    }
    @Override
    public ArrayList<E> clone() {
        try {
            ArrayList clone = (ArrayList) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
