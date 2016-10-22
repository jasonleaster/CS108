// TabooTest.java
// Taboo class tests -- nothing provided.

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class TabooTest {

    public ArrayList<String> generateRules(){
        ArrayList<String> str = new ArrayList<String>();
        str.add("a");
        str.add("c");
        str.add("a");
        str.add("b");
        return str;
    }

	// TODO ADD TESTS
    @Test
    public void test_noFollow(){
        ArrayList<String> rules = generateRules();
        Taboo<String> tb = new Taboo(rules);
        HashSet<String> set =  new HashSet<String>();
        set.add("c");
        set.add("b");
        assertEquals(set, tb.noFollow("a"));
    }

    @Test
    public void test_reduce(){
        ArrayList<String> rules = generateRules();
        Taboo<String> tb = new Taboo(rules);
        LinkedList<String> after =  new LinkedList<String>();
        after.add("a");
        after.add("x");
        after.add("c");

        LinkedList ll = new LinkedList<String>();
        ll.add("a");
        ll.add("c");
        ll.add("b");
        ll.add("x");
        ll.add("c");
        ll.add("a");
        tb.reduce(ll);
        assertEquals(after, ll);
    }
}
