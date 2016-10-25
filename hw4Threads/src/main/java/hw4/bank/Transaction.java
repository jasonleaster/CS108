package hw4.bank;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Transaction {
    /**
     * #Transaction will only supply getter methods and make sure that
     * this class is immutable.
     *
     * Immutable object will never be changed.
     * */

    private Account from;
    private Account to;
    private int money;

    public final static Transaction nullTrans = new Transaction(new Account(-1), new Account(0), 0);

    public Transaction(Account from, Account to, int money){
        this.from  = from;
        this.to    = to;
        this.money = money;
    }

    public Account getFrom() {
        return from;
    }

    public Account getTo() {
        return to;
    }

    public int getMoney() {
        return money;
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
