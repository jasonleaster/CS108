import java.util.HashSet;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str a instance of String
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int mrl = 0;// Max Run Length
		char array[] = str.toCharArray();
		for(int i = 0, counter = 1; i < str.length();){
			counter = 1;
			for(int j = i; j < str.length()-1;j++){
				if (array[j] != array[j + 1]){
					break;
				}else {
					counter += 1;
				}
			}
			i += counter;
			if (counter > mrl){
				mrl = counter;
			}
		}
		return mrl; // YOUR CODE HERE
	}


	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		String ret = new String();
		char repeatChar  = 0;
		char currentChar = 0;
		for (int i  = 0; i < str.length(); i++){
			currentChar = str.charAt(i);
			if('0' <= currentChar && currentChar <= '9'){
				if(i+1 < str.length()){
					repeatChar = str.charAt(i+1);
					for(int k = currentChar - '0'; k > 0; k--){
						ret += repeatChar;
					}
				}
			}else {
				ret += currentChar;
			}
		}
		return ret; // YOUR CODE HERE
	}

	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		HashSet<String> a_set = new HashSet<String>();
		for(int i = 0; i < a.length() - len + 1; i++){
			a_set.add(a.substring(i, i + len));
		}
		for(int i = 0; i < b.length() - len + 1; i++){
			if(a_set.contains(b.substring(i, i + len))) {
				return true;
			}
		}
		return false; // YOUR CODE HERE
	}
}
