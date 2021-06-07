package cs345.deadwood;

import java.util.Random;

/*
USAGE: Rolls a random number from high value to low value
 */
public class Dice {
    private int highVal;
    private int lowVal;
    private Random r;

    /*
    CONSTRUCTOR
     */
    public Dice(int highVal, int lowVal){
        this.highVal = highVal;
        this.lowVal = lowVal;
        this.r = new Random();
    }

    /*
    USAGE: will return a random integer between two values.
     */
    public int roll(){
        return r.nextInt(highVal-lowVal) + lowVal;
    }
}
