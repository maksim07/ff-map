package ffcollections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class CompositeIteratorTest {

    @Test
    public void test() {
        List<Integer> i1 = new ArrayList<>();
        List<Integer> i2 = new ArrayList<>();

        for (int i = 0; i < 100; i ++) {
            i1.add(i);
        }

        for (int i = 100; i < 200; i ++) {
            i2.add(i);
        }

        Iterator<Integer> composite = new CompositeIterator<>(i1.iterator(), i2.iterator());
        for (int i = 0; composite.hasNext(); i ++) {
            assertEquals(composite.next(), new Integer(i));
        }
    }
}