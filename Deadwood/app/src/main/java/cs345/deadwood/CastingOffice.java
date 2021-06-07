package cs345.deadwood;

import java.util.ArrayList;

/*
USAGE: The casting office extends base set and holds all the information related to upgrading
 */
public class CastingOffice extends  BaseSet{
    private ArrayList<Upgrade> creditList;

    /*
    CONSTRUCTOR
     */
    public CastingOffice(String name, ArrayList<String> neighbors, ArrayList<BlankArea> blanks, int maxX, int maxY, int minX, int minY, ArrayList<Upgrade> creditList) {
        super(name, neighbors, blanks, maxX, maxY, minX, minY);
        this.creditList = creditList;
    }


    // Getters:
    public ArrayList<Upgrade> getCreditList() {
        return creditList;
    }
}
