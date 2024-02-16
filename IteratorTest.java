import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.util.Iterator;

public class IteratorTest {
    private List<String> list;
    private Iterator<String> itr;

    @Before
    public void setUp()         // set up test fixture
    {
        list = new ArrayList<String>();
        list.add ("apple");
        list.add ("banana");
        itr = list.iterator();
    }
    /* hasNext() tests*/
    @Test
    public void testHasNextMoreAndConcurrent(){
        assertTrue(itr.hasNext());
    }

    @Test
    public void testHasNextNoMoreAndConcurrent(){
        itr.next();
        itr.next();
        assertFalse(itr.hasNext()); //now empty
    }

    /* this test fails */
    @Test(expected = ConcurrentModificationException.class)
    public void testHasNextMoreAndNotConcurrent(){
        list.add("orange");
        itr.hasNext();
    }

    /* next() tests*/
    @Test
    public void testNextMoreAndNonNullAndConcurrent(){
        assertEquals("apple", itr.next());
    }

    @Test(expected=NoSuchElementException.class)
    public void testNextNoMoreAndNoNonNullAndConcurrent(){
        itr.next();
        itr.next();
        itr.next(); //now empty
    }

    @Test
    public void testNextMoreAndNoNonNullAndConcurrent(){
        list = new ArrayList<String>();
        list.add(null);
        itr = list.iterator();
        assertNull(itr.next());
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testNextMoreAndNoNonNullAndNotConcurrent(){
        list.add("orange");
        itr.next();
    }

    /* remove() tests*/
    @Test
    public void testRemoveMoreAndNonNullAndSupportedAndSatisfiedAndConcurrent(){
        itr.next();
        itr.remove();
        assertFalse(list.contains("apple"));
    }

    @Test
    public void testRemoveNoMoreAndNoNonNullAndSupportedAndSatisfiedAndConcurrent(){
        itr.next(); //consume apple
        itr.next(); //consume banana
        itr.remove(); //remove banana from list
        assertFalse(list.contains("banana"));
    }

    @Test
    public void testRemoveMoreAndNoNonNullAndSupportedAndSatisfiedAndConcurrent(){
        list.add(null);
        list.add("orange");
        itr = list.iterator();
        itr.next(); //consume apple
        itr.next(); // consume banana
        itr.next(); // consume null; iterator not empty
        itr.remove(); // remove null from list
        assertFalse (list.contains(null));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testRemoveMoreAndNonNullAndNotSupportedAndSatisfiedAndConcurrent(){
        list = Collections.unmodifiableList(list);
        itr = list.iterator();
        itr.next();   // consume first element to make C4 true
        itr.remove();
    }

    @Test(expected=IllegalStateException.class)
    public void testRemoveMoreAndNonNullAndSupportedAndNotSatisfiedAndConcurrent(){
        itr.remove();
    }

    @Test(expected=ConcurrentModificationException.class)
    public void testRemoveMoreAndNonNullAndSupportedAndSatisfiedAndNotConcurrent(){
        itr.next();
        list.add("orange");
        itr.remove();
    }
}