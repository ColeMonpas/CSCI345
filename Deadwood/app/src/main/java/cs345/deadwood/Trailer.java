package cs345.deadwood;

import java.util.ArrayList;

/*
USAGE: This trailer set extends BaseSet, because the trailer is a special case of the sets.
No further data is stored here as it only needs it's blanks and neighbors.
 */
public class Trailer extends BaseSet{

    /*
    CONSTRUCTOR
     */
    public Trailer(String name, ArrayList<String> neighbors, ArrayList<BlankArea> blanks, int maxX, int maxY, int minX, int minY) {
        super(name, neighbors, blanks, maxX, maxY, minX, minY);
    }
}
