package hw4.crack;

/**
 * File name  : hw4.crack.Cracker.java
 * Programmer : EOF (Jason Leaster)
 * Date       : 2016/10/22
 * Email      : jasonleaster@gmail.com
 * Descirption: Reverse a hash value into the original password string.
 * Usage:
 *      Eg: java -classpath "path of your classes" hw4.crack.Cracker 66b27417d37e024c46526c2f6d358a754fc552f3 4 2
 */

import java.security.MessageDigest;
import java.util.concurrent.CountDownLatch;

public class Cracker {

    // Array of chars used to produce strings
    public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();

    private Thread[] threads;
    private String hashToFind;
    private String resultWord;
    private int    maxWordLen;

    public Cracker(){}

    /**
     * Methods Parameter
     * @param hashToFind : The hash value which wait to be cracked
     * @param maxWordLen : The max limit of the length of the password
     * @param threadLimit: The limit number of threads to be used.
     * */
    public Cracker(String hashToFind, int maxWordLen, int threadLimit){
        this.threads    = new Thread[threadLimit];
        this.hashToFind = hashToFind;
        this.maxWordLen = maxWordLen;
        this.resultWord = "";
    }

    /**
     * @return A boolean value.
     *      If #resultWord == null or it's length is zero(empty string),
     *      which means that the password has not been found.
     *
     *      If the #resultWord (password) has been found, return true.
     *      Otherwise, return false.
     * */
    public boolean resultFound(){
        if (resultWord == null || resultWord.length() == 0){
            return false;
        }else{
            return true;
        }
    }

    public String getResultWord() {
        return resultWord;
    }

    /*
     Given a byte[] array, produces a hex String,
     such as "234a6f". with 2 chars for each byte in the array.
     (provided code)
     */
    public static String hexToString(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (int i=0; i<bytes.length; i++) {
            int val = bytes[i];
            val = val & 0xff;  // remove higher bits, sign
            if (val<16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }

    /*
     Given a string of hex byte values such as "24a26f", creates
     a byte[] array of those values, one byte value -128..127
     for each 2 chars.
     (provided code)
    */
    public static byte[] hexToArray(String hex) {
        byte[] result = new byte[hex.length()/2];
        for (int i=0; i<hex.length(); i+=2) {
            result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
        }
        return result;
    }

    // possible test values:
    // a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
    // fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
    // a! 34800e15707fae815d7c90d49de44aca97e2d759
    // xyz 66b27417d37e024c46526c2f6d358a754fc552f3

    /*
    * Generate the hash value with the help #MessageDigiest.
    * */
    public static String generateHashVal(byte[] inputs) throws Exception{
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        byte[] hashArr = messageDigest.digest(inputs);
        String hashStr = hexToString(hashArr);
        return hashStr;
    }

    private class Worker extends Thread{

        private int from;
        private int to;
        private int maxWordLen;
        private CountDownLatch foundWord;

        public Worker(){}

        /**
         * Method Parameter
         * @param from  : The index of #Cracker.CHARS
         * @param to    : The index of #Cracker.CHARS
         * @param maxWordLen : The max length of the password
         * @param foundWord  : The concurrent lock. A instance of #CountDownLatch
         *
         * Every #Worker object will search the password start from (#from ~ #to)
         * Every possible combination of password will be translate into the hash value
         * and compare it with the answer #hashToFind (hashValue input by user.)
         **/
        public Worker(int from, int to, int maxWordLen, CountDownLatch foundWord){
            this.to         = to;
            this.from       = from;
            this.maxWordLen = maxWordLen;
            this.foundWord  = foundWord; // The lock outside of the worker threads.
        }

        /**
         * Method Parameter
         * @param word : the password
         **/
        public void searchPasswords(String word) throws Exception{
            if (word.length() > this.maxWordLen){
                return;
            }

            if (generateHashVal(word.getBytes()).equals(hashToFind)){
                resultWord = word;
                foundWord.countDown();
                // Release the signal that we have found the result answer to others threads.
                // Tell the main thread to stop others threads.
                return;
            }

            for(char c : CHARS){
                if (resultFound() == false) {
                    searchPasswords(word + c);
                }else{
                    // Cut off the search branch and return
                    return;
                }
            }
            return;
        }

        @Override
        public void run(){
            try {
                for (int i = from; i < to; i++){
                    if (resultFound() == false){
                        searchPasswords("" + CHARS[i]);
                    }else{
                        return;
                    }
                }
            }catch (Exception ignored){}
        }
    }


    /**
     * map different scope of start char of password string into different thread.
     * */
    public void map(int maxWordLen, CountDownLatch foundWord){

        int step = CHARS.length / threads.length;

        for (int i = 0; i < threads.length; i++){
            threads[i] = new Worker( i * step, (i + 1) * step, maxWordLen, foundWord);
            threads[i].start();
        }

    }

    public void reduce(CountDownLatch foundWord){
        try {
            foundWord.await();

            for (Thread t: threads){
                t.interrupt();
                t.join();
            }
        }catch (InterruptedException ignored){}
    }

    public static void main(String [] args)throws Exception{

        if(args.length == 1){
            /* The input is a string of  password.
             * Just convert it into a hash value with MessageDigest
             * */
            String password = args[0];
            String hashStr  = generateHashVal(password.getBytes());

            System.out.println(hashStr);

        }else if (args.length == 3){

            String hashToFind = args[0];
            int pwLenLimit    = Integer.parseInt(args[1]);
            int threadLimit   = Integer.parseInt(args[2]);

            Cracker cracker   = new Cracker(hashToFind, pwLenLimit, threadLimit);

            CountDownLatch foundWord = new CountDownLatch(1);// Initialize a outside concurrent locker.

            final long startTime = System.currentTimeMillis();
            cracker.map(pwLenLimit, foundWord);

            cracker.reduce(foundWord);

            final long endTime = System.currentTimeMillis();

            System.out.println("Total execution time: " + (endTime - startTime));

            System.out.println(cracker.getResultWord());
        }else{
            throw new Exception("Unhandled input model");
        }
    }

}
