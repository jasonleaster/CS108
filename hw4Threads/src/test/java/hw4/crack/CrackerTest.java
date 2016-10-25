package hw4.crack;

import org.junit.Test;
import org.junit.Assert;

public class CrackerTest{

    String hashToFind = "adeb6f2a18fe33af368d91b09587b68e3abcb9a7";
    String password   = "fm";
    int pwLenLimit    = 3;
    int threadLimit   = 1;
    String resultWord = "";
    Cracker cracker = new Cracker(hashToFind, pwLenLimit, threadLimit);

    /**
     * Test this search algorithm
     * */
    private void searchPasswords(int maxWordLen, String word) throws Exception{
        if (word.length() > maxWordLen){
            return;
        }

        if (cracker.generateHashVal(word.getBytes()).equals(hashToFind)){
            resultWord = word;
            return;
        }

        for(char c : cracker.CHARS){
            if (resultWord.length() == 0){
                searchPasswords(maxWordLen, word + c);
            }else{
                return;
            }

        }
        return;
    }

    @Test
    public void searchPasswordsTest() throws Exception{
        searchPasswords(pwLenLimit, "");
        Assert.assertTrue(resultWord.equals(password));
    }
}
