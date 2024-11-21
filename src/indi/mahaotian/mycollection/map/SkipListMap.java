package indi.mahaotian.mycollection.map;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class SkipListMap<K, V> {
    private static final Object BASE_HEADER = new Object();

    private HeadIndex<K, V> head;

    final Comparator<? super K> comparator;

    final Random random = new Random();

    private static class Node<K, V> {
        final K key;
        Object value;
        Node<K, V> next;

        Node(K key, Object value){
            this.key = key;
            this.value = value;
        }
        public void setNext(Node<K, V> next){
            this.next = next;
        }
    }

    private static class Index<K, V>{
        Node<K, V> node;
        Index<K, V> down;
        Index<K, V> right;
        Index(Node<K, V> node, Index<K, V> down, Index<K, V> right){
            this.node = node;
            this.down = down;
            this.right = right;
        }
    }

    private void initialize() {
        head = new SkipListMap.HeadIndex<K, V>(new SkipListMap.Node<K,V>(null, BASE_HEADER),
                null, null, 1);
    }

    private static class  HeadIndex<K, V> extends Index<K, V>{
        int level;
        HeadIndex(Node<K, V> node, Index<K, V> down, Index<K, V> right, int level) {
            super(node, down, right);
            this.level = level;
        }
    }
    public SkipListMap() {
        this.comparator = null;
        initialize();
    }

    public SkipListMap(Comparator<? super K> comparator){
        this.comparator = comparator;
        initialize();
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public Node<K, V> findPreNode(Object key){
        if (key == null)
            throw new NullPointerException();
        Index<K, V> current = head, right = current.right;
        while(true){
            if(right != null){
                if(cpr(key, right.node.key) > 0){
                    current = right;
                    right = right.right;
                    continue;
                }
            }
            if (current.down == null){
                return current.node;
            }
            current = current.down;
            right = current.right;
        }
    }

    public V get(Object key) {
        if (key == null)
            throw new NullPointerException();
        Node<K, V> prev = findPreNode(key), next = prev.next;
        while (next != null){
            int compareResult = cpr(key, next.key);
            if (compareResult == 0)
                return (V) next.value;
            if (compareResult < 0)
                break;
            next = next.next;
        }
        return null;

    }

    public V put(K key, V value) {
        Node<K, V> prev = findPreNode(key), next = prev.next;
        while (next != null){
            int compareResult = cpr(key, next.key);
            if (compareResult > 0){
                prev = next;
                next = next.next;
            }
            if (compareResult == 0){
                next.value = value;
                return value;
            }
        }
        Node<K, V> nodeToInsert = new Node<>(key, value);
        nodeToInsert.next = next;
        prev.next = nodeToInsert;
        int rand = random.nextInt();
        if (((rand & 0x80000001) != 0)){
            return value;
        }
        int level = 1;
        while (((rand >>>= 1) & 1) != 0){
            ++level;
        }
        int headLevel = head.level;
        Index<K, V> tempIndex = null;
        if(level <= headLevel){
            for(int i = 0; i < level; i++){
                tempIndex = new Index<>(nodeToInsert, tempIndex, null);
            }
        }
        else {
            level = headLevel + 1;
            Index<K, V>[] indexes = new Index[level + 1];
            for(int i = 1; i <= level; i++){
                tempIndex = new Index<>(nodeToInsert, tempIndex, null);
                indexes[i] = tempIndex;
            }
            Node<K, V> oldHeadNode = head.node;
            head = new HeadIndex<>(oldHeadNode, head, tempIndex, level);
            tempIndex = indexes[--level];
        }
        int insertionLevel = level;
        int currentLevel = head.level;
        for (Index<K, V> currentIndex = head, rightIndex = currentIndex.right, newIndex = tempIndex;;) {
            if (rightIndex != null) {
                if (cpr(key, rightIndex.node.key) > 0) {
                    currentIndex = rightIndex;
                    rightIndex = rightIndex.right;
                    continue;
                }
            }
            if (currentLevel == insertionLevel) {
                currentIndex.right = newIndex;
                newIndex.right = rightIndex;
                insertionLevel--;
                newIndex = newIndex.down;
                if (insertionLevel == 0) {
                    return value;
                }
            }
            currentLevel--;
            currentIndex = currentIndex.down;
            rightIndex = currentIndex.right;
        }
    }

    public V remove(K key) {
        for (Node<K, V> prev = findPreNode(key), next = prev.next;;) {
            if (next == null) {
                return null;
            }
            Object value = next.value;
            int compare = cpr(key, next.key);
            if (compare < 0) {
                return null;
            }
            if (compare > 0) {
                prev = next;
                next = next.next;
                continue;
            }
            next.value = null;
            prev.next = next.next;
            cleanIndex(key);
            if (head.right == null) {
                reduceLevel();
            }
            return (V) value;
        }
    }

    private void cleanIndex(K key) {
        for (Index<K, V> currentIndex = head, rightIndex = currentIndex.right;;) {
            if (rightIndex != null) {
                Node<K, V> rightNode = rightIndex.node;
                if (rightNode.value == null) {
                    currentIndex.right = rightIndex.right;
                    rightIndex = currentIndex.right;
                    continue;
                }
                if (cpr(key, rightNode.key) > 0) {
                    currentIndex = rightIndex;
                    rightIndex = rightIndex.right;
                    continue;
                }
            }
            if (currentIndex.down == null) {
                return;
            }
            currentIndex = currentIndex.down;
            rightIndex = currentIndex.right;
        }
    }

    private void reduceLevel() {
        HeadIndex<K, V> down;
        HeadIndex<K, V> down2Level;
        if (head.level > 3 &&
                (down = (HeadIndex<K, V>) head.down) != null &&
                (down2Level = (HeadIndex<K, V>) down.down) != null &&
                down2Level.right == null &&
                down.right == null &&
                head.right == null) {
            head = down;
        }
    }

    private int cpr( Object x, Object y){
        return (comparator != null) ? comparator.compare((K) x,(K) y) : ((Comparable) x).compareTo(y);
    }
}
