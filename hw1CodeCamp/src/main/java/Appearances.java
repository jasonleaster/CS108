import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Appearances {

    /**
     * Returns the number of elements that appear the same number
     * of times in both collections. Static method. (see handout).
     *
     * @return number of same-appearance elements
     */
    public static <T> int sameCount(Collection<T> a, Collection<T> b) {
        int counter = 0;

        HashMap<T, Integer> map_a = _statistic(a);
        HashMap<T, Integer> map_b = _statistic(b);

        for (Iterator iter = map_a.keySet().iterator(); iter.hasNext(); ) {
            T key = (T) iter.next();
            if (map_b.containsKey(key)) {
                if (map_a.get(key) == map_b.get(key)) {
                    counter++;
                }
            }

        }
        return counter; // TODO ADD CODE HERE
    }

    private static <T> HashMap<T, Integer> _statistic(Collection collection) {
        HashMap<T, Integer> map = new HashMap<T, Integer>();
        for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
            T obj = (T) iter.next();
            if (map.containsKey(obj)) {
                map.put(obj, map.get(obj) + 1);
            } else {
                map.put(obj, +1);
            }
        }
        return map;
    }

}
