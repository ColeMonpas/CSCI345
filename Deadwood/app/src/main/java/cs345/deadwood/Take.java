package cs345.deadwood;

/*
USAGE: The takes object/class is used to just store takes information, such as each takes position, and number
It also stores if the take has been flipped or not.
*/
public class Take {
    private int takeNumber;
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private boolean flipped;

    /*
    CONSTRUCTOR
     */
    public Take(int takeNumber, int maxX, int maxY, int minX, int minY) {
        this.takeNumber = takeNumber;
        this.maxX = maxX;
        this.maxY = maxY;
        this.minX = minX;
        this.minY = minY;
        this.flipped = false;
    }

    // Used for printing out some of the Takes information
    @Override
    public String toString(){
        return "#:" + takeNumber + " xMin:" + minX + " yMin:" + minY;
    }

    // Getters:
    public int getMaxX() {
        return maxX;
    }
    public int getMaxY() {
        return maxY;
    }
    public int getMinX() {
        return minX;
    }
    public int getMinY() {
        return minY;
    }
    public int getTakeNumber() {
        return takeNumber;
    }
    public boolean isFlipped() {
        return flipped;
    }

    // Setters:
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
}
