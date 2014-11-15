package org.fstruct.fmap;

import java.util.Iterator;

/**
 * @author Max Osipov
 */
public class CompositeIterator<T> implements Iterator<T> {

    private Iterator<T>[] underlying;
    private int currentUnderlying;


    @SafeVarargs
    public CompositeIterator(Iterator<T>... underlying) {
        currentUnderlying = 0;
        this.underlying = underlying;
    }

    @Override
    public boolean hasNext() {
        if (currentUnderlying >= underlying.length)
            return false;
        else if (currentUnderlying == underlying.length - 1) {
            return underlying[currentUnderlying].hasNext();
        }
        else {
            if (underlying[currentUnderlying].hasNext())
                return true;
            else {
                currentUnderlying ++;
                return underlying[currentUnderlying].hasNext();
            }
        }
    }

    @Override
    public T next() {
        return underlying[currentUnderlying].next();
    }

    @Override
    public void remove() {
        underlying[currentUnderlying].remove();
    }
}
