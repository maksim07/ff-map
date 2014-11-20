package ffcollections;

import java.util.*;

/**
 * @author Max Osipov
 */
public class FMap<K, V> implements Map<K,V> {

    /**
     * This factor is used to increase capacity of the collection in case of it is full
     */
    public static final double LOAD_FACTOR = 0.75;

    /**
     * If no capacity provided in constructor
     */
    public static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * Predicted capacity of the collection.
     */
    private int capacity;

    /**
     * Threshold after which resize operation is performed
     */
    private int threshold;

    /**
     * Each row either reference to row in tha linked table or null (zero)
     */
    private int[] baskets;

    /**
     * Table for first keys for each hashcode
     */
    private LinkedTable<K, V> table;

    public FMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public FMap(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity has to be greater than zero");

        this.capacity = capacity;
        this.threshold = (int)(this.capacity * LOAD_FACTOR);
        table = new LinkedTable<>(capacity);
        baskets = new int[capacity];
    }

    @Override
    public int size() {
        return table.size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }


    /**
     * Method resizes map for new capacity
     */
    private void resize(int newCapacity) {
        int[] newBaskets = new int[newCapacity];


        for (int i = 0; i < table.size; i ++) {
            if (table.hasRow(i))
                table.clearNextRowLink(i);

            int hash = table.hash(i);

            int basket = hashIndex(hash, newCapacity);
            if (!isBasketExists(newBaskets, basket)) {
                setBasketLink(newBaskets, basket, i);
            }
            else {
                int row = getLink(newBaskets, basket);

                while (table.hasNextRow(row)) {
                    row = table.getNextRow(row);
                }

                table.setNextNowLink(row, i);
            }
        }

        this.baskets = newBaskets;
        this.capacity = newCapacity;
    }

    private void resizeIfNecessary() {
        int s = size();
        if (s >= threshold) {
            resize(capacity * 2);
            threshold = (int)(capacity * LOAD_FACTOR);
        }
        table = table.increaseIfNecessary(2 - LOAD_FACTOR);
    }

    @Override
    public boolean containsKey(Object key) {
        int hash = hashcode(key);
        int basket = hashIndex(hash, capacity);

        boolean basketExists = isBasketExists(baskets, basket);

        // if it is the first key with such hash
        if (!basketExists) {
            return false;
        }
        // if such hash is already in the map
        else {
            int row = getLink(baskets, basket);

            do {
                if (equals(table.keys[row], key)) {
                    return true;
                }

                if (!table.hasNextRow(row))
                    return false;

                row = table.getNextRow(row);
            } while(true);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        for (Object v : table.values)
            if (v.equals(value))
                return true;

        return false;
    }

    @Override
    public V get(Object key) {
        int hash = hashcode(key);
        int basket = hashIndex(hash, capacity);

        boolean basketExists = isBasketExists(baskets, basket);

        // if it is the first key with such hash
        if (!basketExists) {
            return null;
        }
        // if such hash is already in the map
        else {
            int row = getLink(baskets, basket);

            do {
                if (equals(table.keys[row], key)) {
                    return table.value(row);
                }

                if (!table.hasNextRow(row))
                    return null;

                row = table.getNextRow(row);
            } while(true);
        }
    }

    private static void setBasketLink(int baskets[], int basket, int row) {
        baskets[basket] = (1 << 31) | row;
    }

    @Override
    public V put(K key, V value) {
        resizeIfNecessary();

        int hash = hashcode(key);
        int basket = hashIndex(hash, capacity);

        boolean basketExists = isBasketExists(baskets, basket);

        // if it is the first key with such hash
        if (!basketExists) {
            int row = table.addRow(hash, key, value);
            setBasketLink(baskets, basket, row);
        }
        // if such hash is already in the map
        else {
            int row = getLink(baskets, basket);

            do {
                if (equals(table.keys[row], key)) {
                    table.values[row] = value;
                    return value;
                }

                if (!table.hasNextRow(row))
                    break;

                row = table.getNextRow(row);
            } while(true);


            table.addNextRow(row, hash, key, value);
        }

        return value;
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Currently removing is not supported");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Method is not supported");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Method is not supported");
    }

    @Override
    public Set<K> keySet() {
        return new AbstractSet<K>() {
            public Iterator<K> iterator() {
                return table.keysIterator();
            }
            public int size() {
                return FMap.this.size();
            }
            public boolean contains(Object o) {
                return containsKey(o);
            }
            public boolean remove(Object o) {
                throw new UnsupportedOperationException("Remove method is currently not supported");
            }
            public void clear() {
                FMap.this.clear();
            }
        };
    }

    @Override
    public Collection<V> values() {
        return new AbstractCollection<V>() {
            public Iterator<V> iterator() {
                return table.valuesIterator();
            }
            public int size() {
                return FMap.this.size();
            }
            public boolean contains(Object o) {
                return containsValue(o);
            }
            public void clear() {
                FMap.this.clear();
            }
        };
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Method is not supported");
    }

    private static boolean isBasketExists(int[] baskets, int basketNum) {
        return baskets[basketNum] != 0;
    }

    private static int getLink(int[] baskets, int basketNum) {
        return 0x7FFFFFFF & baskets[basketNum];
    }

    /**
     * Calculation of key hashcode
     *
     * @param key key
     * @return corresponding hashcode
     */
    private static int hashcode(Object key) {
        if (key == null) return 0;

        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    /**
     * Returns index in the table corresponds to the hashcode
     *
     * @param h hashcode
     * @param length length of main table
     * @return rownum in the table
     */
    private static int hashIndex(int h, int length) {
        return (h > 0 ? 1 : -1) * (h % length);//h & (length - 1);
    }

    /**
     * Returns true of objects are equal. Method takes in account that keys can be nulls.
     *
     * @param key1 first object
     * @param key2 second object
     * @return true of they are equal
     */
    private static boolean equals(Object key1, Object key2) {
        if (key1 == null) return key2 == null;
        else return key1.equals(key2);
    }



    /**
     * Iterator over linked table keys
     */
    private abstract class LinkedTableIterator<I> implements Iterator<I> {

        private LinkedTable table;

        private int row;

        public LinkedTableIterator(LinkedTable table) {
            this.table = table;
            this.row = -1;

            if (table.size > 0) {
                for (int i = 0; i < table.size; i ++) {
                    if (table.hasRow(i)) {
                        row = i;
                        break;
                    }
                }
            }
        }

        abstract I value(int row);

        @Override
        public boolean hasNext() {
            return row >= 0;
        }

        @Override
        public I next() {
            I ret = value(row);

            int nrow = -1;
            for (int i = row + 1; i < table.size; i ++) {
                if (table.hasRow(i)) {
                    nrow = i;
                    break;
                }
            }
            row = nrow;

            return ret;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported yet");
        }
    }


    /**
     * Table with columns (hashcode, key, value) where each row can reference another row
     */
    private final class LinkedTable<N, L> {
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
            return ((tails[row] & 0x7FFFFFFF) ^ 0x7FFFFFFF) != 0;
        }

        /**
         *  Returns index of next row to the parameter row
         * 
         * @param row row in the table for which next row has to be returned
         */
        private int getNextRow(int row) {
            return tails[row] & 0x7FFFFFFF;
        }

        public N key(int row) {
            return (N)keys[row];
        }

        public L value(int row) {
            return (L)values[row];
        }

        public int hash(int row) {
            return hashes[row];
        }

        /**
         * Adds row to the table
         *
         * @param hash hash value of the key
         * @param key key field
         * @param value value field
         * @return rownum of inserted tuple
         */
        private int addRow(int hash, N key, L value) {
            tails[size] = ~0;
            hashes[size] = hash;
            keys[size] = key;
            values[size] = value;
            return size++;
        }

        /**
         * Method clears nexr row link for the row
         */
        private void clearNextRowLink(int row) {
            tails[row] = ~0;
        }

        private void setNextNowLink(int row, int next) {
            tails[row] = (1 << 31) | next;
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
        private int addNextRow(int current, int hash, N key, L value) {
            tails[current] = (1 << 31) | size;
            tails[size] = ~0;
            hashes[size] = hash;
            keys[size] = key;
            values[size] = value;
            return size++;
        }

        private LinkedTable<N, L> increaseIfNecessary(double factor) {
            if (size < capacity)
                return this;

            int newCapacity = (int)(capacity * factor);
            if (newCapacity == capacity)
                newCapacity ++;

            int[] newTails = Arrays.copyOf(tails, newCapacity);
            int[] newHashes = Arrays.copyOf(hashes, newCapacity);
            Object[] newKeys = Arrays.copyOf(keys, newCapacity);
            Object[] newValues = Arrays.copyOf(values, newCapacity);

            return new LinkedTable<>(size, newCapacity, newTails, newHashes, newKeys, newValues);
        }

        private Iterator<N> keysIterator() {
            return new LinkedTableIterator<N>(this) {
                @Override
                N value(int row) {
                    return LinkedTable.this.key(row);
                }
            };
        }

        private Iterator<L> valuesIterator() {
            return new LinkedTableIterator<L>(this) {
                @Override
                L value(int row) {
                    return LinkedTable.this.value(row);
                }
            };
        }
    }
}
