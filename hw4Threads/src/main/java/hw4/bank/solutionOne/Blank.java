package hw4.bank.solutionOne;

import hw4.bank.Account;
import hw4.bank.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Blank {

    private static final int ACCOUNTNUM = 100;
    private static final int THREAD_NUM = 2;
    private ArrayList<Account> accounts         = new ArrayList<Account>();
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private Thread[] workers                    = new Thread[THREAD_NUM];

    public Blank() throws IOException {

        for(int id = 0; id < ACCOUNTNUM; id++){
            accounts.add(new Account(id));
        }

        BufferedReader in = new BufferedReader(new FileReader("E:/CS108/hw4Threads/src/main/resources/small.txt"));
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
            transactions.add(trans);

            line = in.readLine();
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

        private Queue<Transaction> transactions;

        public Worker(Queue<Transaction> transactions){
            this.transactions = transactions;
        }

        @Override
        public void run(){
            System.out.println("running thread: " + this.getId());
            Account from;
            Account to;
            int money;
            while(! transactions.isEmpty()){

                Transaction transaction = transactions.remove();

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

                    transactions.add(transaction);
                    continue;
                }

                from.setBlance( from.getBlance() - money);
                  to.setBlance(   to.getBlance() + money);

                from.setTransactionTimes(from.getTransactionTimes() + 1);
                  to.setTransactionTimes(   to.getTransactionTimes() + 1);

                from.getLock().unlock();
                  to.getLock().unlock();


                //System.out.println("Transaction finished! Thread: " + this.getId() +" " + transaction.toString());
            }
        }
    }

    public void map(){
        int step = this.transactions.size() / this.workers.length;

        for (int i = 0; i < workers.length; i++){
            workers[i] = new Worker(new ConcurrentLinkedQueue<Transaction>(transactions.subList(i * step, (i + 1) * step)));
            workers[i].start();
        }
    }

    public void reduce() throws Exception{
        for (int i = 0; i < workers.length; i++){
            workers[i].join();
        }
    }

    public static void main(String [] args) throws Exception{
        Blank blank = new Blank();

        final long startTime = System.currentTimeMillis();

        blank.map();
        blank.reduce();

        final long endTime = System.currentTimeMillis();

        System.out.println("Total execution time: " + (endTime - startTime));

        for(Account account: blank.accounts){
            System.out.println(account);
        }
    }

}
