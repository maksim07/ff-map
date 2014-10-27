package org.fstruct.fmap;

import java.util.*;

/**
 * @author Max Osipov
 */
public class FMap<K, V> implements Map<K,V> {

    /**
     * This factor is used to increase capacity of the collection in case of it is full
     */
    public static final double LOAD_FACTOR = 0.25;

    /**
     * The part of the capacity additionally allocated for collision zone
     */
    public static final double COLLIZION_AREA_FACTOR = 0.1;

    /**
     * Current capacity
     */
    private int capacity;

    /**
     * Count of elements in the collection
     */
    private int size;

    /**
     * Size of the collision area
     */
    private int collisionAreaSize;

    /**
     * Cursor in the collision area
     */
    private int tail;

    /**
     * Each element i of the array either:
     * - zero: means that there is no key in the map with hash % capacity == i
     * - positive integer: index in the collision area of next object with such hashcode % capacity
     * - negative integer means that there is some value, but no next value
     */
    private int[] index;

    /**
     * Keys array
     */
    private Object[] keys;

    /**
     * Values array
     */
    private Object[] values;


    public FMap(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity has to be greater than zero");

        this.capacity = capacity;
        this.collisionAreaSize = (int)(capacity * COLLIZION_AREA_FACTOR);
        int arraySize = this.capacity + this.collisionAreaSize;
        index = new int[arraySize];
        keys = new Object[arraySize];
        values = new Object[arraySize];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Object v : values)
            if (v.equals(value))
                return true;

        return false;
    }

    @Override
    public V get(Object key) {
        int i = indexByHash(key);
        int indexValue = this.index[i];

        while (indexValue != 0) {

            if (keys[i] != null && keys[i].equals(key))
                return (V) values[i];

            if (indexValue > 0) {
                indexValue = index[indexValue];
                i = indexValue;
            }
            else
                break;
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        if (size >= keys.length)
            throw new RuntimeException("Resizing of the hashmap is not supported yet");

        // get next free index
        int i = indexByHash(key);

        // if it is zero
        int indexValue = this.index[i];

        while (true) {
            // if there is no value with such hashcode
            if (indexValue == 0) {
                this.keys[i] = key;
                this.values[i] = value;
                this.index[i] = -1;
                size++;
                break;
            }
            // if there is some value with this hashcode
            else {
                if (keys[i].equals(key)) {
                    values[i] = value;
                    break;
                }
                else if (indexValue < 0) {
                    index[i] = tail + capacity;
                    index[tail] = -1;
                    keys[tail] = key;
                    values[tail] = value;
                    tail++;
                    size++;
                    break;
                }
                else {
                    indexValue = index[indexValue];
                    i = indexValue;
                }
            }
        }

        // ned resize
        if (tail + capacity >= keys.length) {
            resize();
        }

        return value;
    }

    @Override
    public V remove(Object key) {
        throw new RuntimeException("Currently removing is not supported");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {
        throw new RuntimeException("Method is not supported");
    }

    @Override
    public Set<K> keySet() {
        return new HashSet<K>(Arrays.<K>asList((K[]) keys));
    }

    @Override
    public Collection<V> values() {
        return Arrays.<V>asList((V[]) values);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new RuntimeException("Method is not supported");
    }

    /**
     * Returns index of the key according to the current capacity
     *
     * @param key key
     * @return index in the index array or -1 if there is not next index
     */
    private int indexByHash(Object key) {
        int hash = key.hashCode();
        hash = hash < 0 ? -hash : hash;

        return hash % capacity;
    }

    private int nextIndex(int index) {
        return (int)(this.index[index]);
    }

    private void resize() {

        System.out.println("Resizing");
    }

}
