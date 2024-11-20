package indi.mahaotian.mycollection.map;

import javax.swing.tree.TreeNode;
import java.util.*;

public class HashMap<K, V> implements Map<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; //初始容量为16

    static final int MAXIMUM_CAPACITY = 1 << 30; //最大容量

    static final float DEFAULT_LOAD_FACTOR = 0.75f; //默认负载因子

    static final int TREEIFY_THRESHOLD = 8; //树化阈值

    static final int UNTREEIFY_THRESHOLD = 6;

    static final int MIN_TREEIFY_CAPACITY = 64;

    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private float loadFactor;

    static class Node<K, V> implements Map.Entry<K, V>{
        final int hash; // hash值
        final K key; //key
        V value; //value
        Node<K, V> next; //next node

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return null;
        }

        @Override
        public String toString() {
            return key + "-" + value;
        }

        @Override
        public boolean equals(Object o) {
            if(o == this) //地址相同
                return true;
            if(o instanceof Map.Entry){
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
            }
            return false;
        }
    }

    static int hash(Object key){
        if(key==null)
            return 0;
        int h = key.hashCode();
        return h^(h>>16);
    }

    static int tableSizeFor(int cap){
        int size = cap - 1;
        size |= size >>> 1;
        size |= size >>> 2;
        size |= size >>> 4;
        size |= size >>> 8;
        size |= size >>> 16;
        return (size < 0) ? 1 : (size >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : size + 1;
    }

    public HashMap(int initialCapacity, float loadFactor){
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }


    public HashMap(){
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }


    final Node<K, V> getNode(int hash, Object key){
        if(table == null)
            return null;
        int tableLen = table.length;
        Node<K, V> first;
        if(tableLen > 0 && (first = table[(tableLen - 1) & hash]) != null){
            if(first.hash == hash && first.key == key || key != null && key.equals(first.key))
                return first;
            Node<K, V> temp = first.next;
            if(temp != null){
                do{
                    if (temp.hash == hash && temp.key == key || key != null && key.equals(temp.key))
                        return temp;
                } while ((temp = temp.next) != null);
            }
        }
        return null;
    }

    public HashMap(int initialCapacity){
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }



    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        Node<K, V> temp = getNode(hash(key), key);
        return temp == null ? null : temp.value;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}



