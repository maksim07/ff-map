package org.fstruct.fmap;

import java.util.*;

/**
 * @author Max Osipov
 */
public class FMap<K, V> implements Map<K,V> {

    /**
     * This factor is used to increase capacity of the collection in case of it is full
     */
    public static final double LOAD_FACTOR = 1.25;

    /**
     * The part of the capacity additionally allocated for collision zone
     */
    public static final double COLLISION_AREA_FACTOR = 0.1;

    private int capacity;

    private LinkedTable main;

    private LinkedTable tail;


    public FMap(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity has to be greater than zero");

        this.capacity = capacity;
        main = new LinkedTable(capacity);
        tail = new LinkedTable((int)(capacity * COLLISION_AREA_FACTOR));
    }

    @Override
    public int size() {
        return main.size + tail.size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Object v : main.values)
            if (v.equals(value))
                return true;

        for (Object v : tail.values)
            if (v.equals(value))
                return true;

        return false;
    }

    @Override
    public V get(Object key) {
        int hash = hashcode(key);
        int basket = hashIndex(hash, capacity);

        boolean basketExists = main.hasRow(basket);

        // if it is the first key with such hash
        if (!basketExists) {
            return null;
        }
        // if such hash is already in the map
        else {
            int row = basket;
            LinkedTable table = main;

            do {
                if (equals(table.keys[row], key)) {
                    return (V) table.values[row];
                }

                if (!table.hasNextRow(row))
                    return null;

                row = table.getNextRow(row);
                table = tail;

            } while(true);
        }
    }


    @Override
    public V put(K key, V value) {
        int hash = hashcode(key);
        int basket = hashIndex(hash, capacity);

        boolean basketExists = main.hasRow(basket);

        // if it is the first key with such hash
        if (!basketExists) {
            main.setRowAndIncreaseSize(basket, hash, key, value);
        }
        // if such hash is already in the map
        else {
            int row = basket;
            LinkedTable table = main;

            do {
                if (equals(table.keys[row], key)) {
                    table.values[row] = value;
                    return value;
                }

                if (!table.hasNextRow(row))
                    break;

                row = table.getNextRow(row);
                table = tail;

            } while(true);

            tail = tail.increaseIfNeeded(LOAD_FACTOR);

            if (table == main) {
                int link = tail.addRow(hash, key, value);
                main.setLinkTo(row, link);
            }
            else {
                tail.addNextRow(row, hash, key, value);
            }
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
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new RuntimeException("Method is not supported");
    }


    private int hashcode(Object key) {
        if (key == null) return 0;

        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private int hashIndex(int h, int length) {
        return (h > 0 ? 1 : -1) * (h % length);//h & (length - 1);
    }

    private boolean equals(Object key1, Object key2) {
        if (key1 == null) return key2 == null;
        else return key1.equals(key2);
    }

    private static class LinkedTable {
        private int size;
        private int capacity;
        private int[] tails;
        private final int[] hashes;
        private final Object[] keys;
        private final Object[] values;

        LinkedTable(int capacity) {
            this.capacity = capacity;
            this.size = 0;
            this.hashes = new int[capacity];
            this.tails = new int[capacity];
            this.keys = new Object[capacity];
            this.values = new Object[capacity];
        }

        private LinkedTable(int size, int capacity, int[] tails, int[] hashes, Object[] keys, Object[] values) {
            this.size = size;
            this.capacity = capacity;
            this.hashes = hashes;
            this.tails = tails;
            this.keys = keys;
            this.values = values;
        }

        /**
         * If the row is not null
         *
         * @param row row index
         * @return true if it is not null
         */
        private boolean hasRow(int row) {
            return (tails[row] >>> 31) == 1;
        }

        /**
         * Returns true if next to <code>row</code> is not null
         *
         * @param row row index
         * @return true or false according to existence of next row
         */
        private boolean hasNextRow(int row) {
            return hasRow(row) && ((tails[row] & 0x7FFFFFFF) ^ 0x7FFFFFFF) != 0;
        }

        private int getNextRow(int row) {
            return tails[row] & 0x7FFFFFFF;
        }

        /**
         * Adds row to the table
         *
         * @param hash hash value of the key
         * @param key key field
         * @param value value field
         * @return rownum of inserted tuple
         */
        private int addRow(int hash, Object key, Object value) {
            tails[size] = ~0;
            hashes[size] = hash;
            keys[size] = key;
            values[size] = value;
            return size++;
        }

        private void setRowAndIncreaseSize(int row, int hash, Object key, Object value) {
            tails[row] = ~0;
            hashes[row] = hash;
            keys[row] = key;
            values[row] = value;
            size ++;
        }

        private void setLinkTo(int row, int link) {
            tails[row] = (1 << 31) | link;
        }

        /**
         * Inserts row to the table which is next row for <code>current</code> row.
         * Method does not check if there is already some value has been written to current row.
         *
         * @param current current row
         * @param hash hash to save in the row
         * @param key key to save in the row
         * @param value value to save in the row
         * @return rownum
         */
        private int addNextRow(int current, int hash, Object key, Object value) {
            tails[current] = (1 << 31) | size;
            tails[size] = ~0;
            hashes[size] = hash;
            keys[size] = key;
            values[size] = value;
            return size++;
        }

        private LinkedTable increaseIfNeeded(double factor) {
            if (size < capacity)
                return this;

            int newCapacity = (int)(capacity * factor);

            int[] newTails = Arrays.copyOf(tails, newCapacity);
            int[] newHashes = Arrays.copyOf(hashes, newCapacity);
            Object[] newKeys = Arrays.copyOf(keys, newCapacity);
            Object[] newValues = Arrays.copyOf(values, newCapacity);

            return new LinkedTable(size, newCapacity, newTails, newHashes, newKeys, newValues);
        }
    }
}
