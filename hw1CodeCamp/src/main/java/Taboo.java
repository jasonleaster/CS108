/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Taboo<T> {
	private List<T> rules;
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		this.rules = rules;
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		HashSet set = new HashSet();
		for(Iterator iter = rules.listIterator(); iter.hasNext();) {
			T obj = (T) iter.next();
			if(obj.equals(elem) && iter.hasNext()){
				set.add(iter.next());
			}
		}
		return set; // TODO YOUR CODE HERE
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		if(list.size() <= 0){
			return ;
		}

		for(int j = 1; j <  list.size();j++){
			for(int i = 1; i < this.rules.size() && j < list.size(); i++){
				if(list.get(j) == rules.get(i) && list.get(j - 1) == rules.get(i - 1)){
					list.remove(j);
					i = 0;
				}
			}
		}
	}
}
