package hw4.bank;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private static final int INIT_BLANCE = 1000;
    private int id;
    private int blance = INIT_BLANCE;
    private int transactionTimes;
    private ReentrantLock lock;

    public Account(int id){
        this.id = id;
        this.transactionTimes = 0;
        this.lock = new ReentrantLock();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlance() {
        return blance;
    }

    public void setBlance(int blance) {
        this.blance = blance;
    }

    public int getTransactionTimes() {
        return transactionTimes;
    }

    public void setTransactionTimes(int transactionTimes) {
        this.transactionTimes = transactionTimes;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
