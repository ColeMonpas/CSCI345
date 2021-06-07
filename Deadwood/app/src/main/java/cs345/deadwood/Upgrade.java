package cs345.deadwood;

/*
USAGE: This object class just stores information regarding to certain upgrades in the casting office.
 */
public class Upgrade {
    private int x;
    private int y;
    private int amt;
    private String currency;
    private int rank;

    /*
    CONSTRUCTOR
     */
    public Upgrade(int x, int y, int amt, String currency, int rank) {
        this.x = x;
        this.y = y;
        this.amt = amt;
        this.currency = currency;
        this.rank = rank;
    }

    // For printing the upgrade information:
    @Override
    public String toString(){
        return "R:" + rank + " Amt:" + amt + " Cur:" + currency;
    }

    // Getters:
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getAmt() {
        return amt;
    }
    public String getCurrency() {
        return currency;
    }
    public int getRank() {
        return rank;
    }
}
