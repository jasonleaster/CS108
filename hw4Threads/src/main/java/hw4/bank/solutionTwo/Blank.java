package hw4.bank.solutionTwo;

import hw4.bank.Account;
import hw4.bank.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Notes: make sure you start up all the worker threads before reading the
 * transaction from the file.
 */
public class Blank {

    private static final int ACCOUNTNUM = 100;
    private static final int THREAD_NUM = 2;
    private ArrayList<Account> accounts   = new ArrayList<Account>();
    private BlockingDeque<Transaction> bq = new LinkedBlockingDeque<Transaction>();
    private Thread[] workers              = new Thread[THREAD_NUM];

    public Blank() throws IOException {

        for(int id = 0; id < ACCOUNTNUM; id++){
            accounts.add(new Account(id));
        }
    }

    private void readTransactionsFromFile(FileReader reader) throws Exception{
        BufferedReader in = new BufferedReader(reader);
        String line = in.readLine();
        String ints[];
        Transaction trans;
        int from, to, money;
        while( line != null){
            ints = line.split(" ");

            from  = Integer.parseInt(ints[0]);
            to    = Integer.parseInt(ints[1]);
            money = Integer.parseInt(ints[2]);

            trans = new Transaction(findAccount(from), findAccount(to), money);
            // bq.add(trans);   Don't use add method of BlockingQueue according to the guide of hw4
            bq.put(trans);

            line = in.readLine();
        }

        for(int i = 0; i < this.workers.length; i++){
            bq.put(Transaction.nullTrans);
        }
    }

    private Account findAccount(int id){
        for(int i = 0; i < accounts.size(); i++){
            if(accounts.get(i).getId() == id){
                return accounts.get(i);
            }
        }
        return null;
    }

    // This class will access @hw4.bank.Account
    private class Worker extends Thread{

        public Worker(){}

        @Override
        public void run(){
            System.out.println("running thread: " + this.getId());
            Account from;
            Account to;
            int money;
            Transaction transaction = null;
            while (transaction != Transaction.nullTrans){
                try{
                    /*
                     *  Retrieves and removes the head of the queue represented by this deque
                     * (in other words, the first element of this deque), waiting if
                     * necessary until an element becomes available.
                     */
                    transaction =  bq.take();  //Don't user remove method. bq.removeFirst();
                    if (transaction != null && transaction.equals( Transaction.nullTrans )){
                        return;
                    }else if (transaction == null){
                        System.out.println("Transaction is null!!!!");
                        continue;
                    }
                }catch (Exception ignore){
                    ignore.printStackTrace();
                    continue;
                }

                System.out.println("Thread " + this.getId() + " get the transaction "+ transaction.toString());

                from  = transaction.getFrom();
                to    = transaction.getTo();
                money = transaction.getMoney();

                if(from.equals( to )){
                    continue;
                }

                if(! (from.getLock().tryLock()  &&  to.getLock().tryLock()) ){
                    try{
                        from.getLock().unlock();
                    }catch (Exception ignore){}

                    try{
                        to.getLock().unlock();
                    }catch (Exception ignore){}

                    try{
                        bq.put(transaction);
                    }catch (Exception ignore){}

                    continue;
                }

                from.setBlance( from.getBlance() - money );
                to.setBlance(   to.getBlance()   + money);

                from.setTransactionTimes( from.getTransactionTimes() + 1);
                  to.setTransactionTimes(   to.getTransactionTimes() + 1);

                from.getLock().unlock();
                  to.getLock().unlock();
            }
        }
    }

    public void map() throws Exception{
        int step = this.bq.size() / this.workers.length;

        for (int i = 0; i < workers.length; i++){
            workers[i] = new Worker();
            workers[i].start();
        }

        this.readTransactionsFromFile(new FileReader("E:/CS108/hw4Threads/src/main/resources/5k.txt"));
    }

    public void reduce() throws Exception{
        for (int i = 0; i < workers.length; i++){
            workers[i].join();
        }
    }

    public static void main(String [] args) throws Exception{
        Blank blank = new Blank();

        blank.map();
        blank.reduce();

        for(Account account: blank.accounts){
            System.out.println(account);
        }
    }

}
