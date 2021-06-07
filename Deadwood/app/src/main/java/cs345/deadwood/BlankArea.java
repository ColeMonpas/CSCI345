package cs345.deadwood;


/*
USAGE: class used to hold information related to placement of dice for players on a baseSet
 */
public class BlankArea {

    private int X;
    private int Y;
    private int playerId;
    private int number;

    /*
    CONSTRUCTOR
     */
    public BlankArea(int x, int y, int number) {
        X = x;
        Y = y;
        this.number = number;
        this.playerId= 0;
    }

    // used for printing out blank area information
    @Override
    public String toString(){
        return "ID:" + number + " X:" + X + " Y:" + Y;
    }

    // Getters:
    public int getPlayerId() { return playerId; }
    public int getX() { return X; }
    public int getY() { return Y; }

    // Setters:
    public void setPlayerId(int playerId) { this.playerId = playerId; }
}
