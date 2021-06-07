package cs345.deadwood;

import java.util.ArrayList;

/*
USAGE: Abstract with shared commonalities between trailer casting office and sceneSet
All sets have these same commonalities, we can then use this to extend the trailer and casting office.
 */
public abstract class BaseSet {
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;
    private ArrayList<String> neighbors;
    private String name;
    private ArrayList<BlankArea> blankAreasList;

    /*
    CONSTRUCTOR
     */
    public BaseSet(String name, ArrayList<String> neighbors, ArrayList<BlankArea> blanks, int maxX, int maxY, int minX, int minY) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.minX = minX;
        this.minY = minY;
        this.neighbors = neighbors;
        this.name = name;
        this.blankAreasList = blanks;
    }

    /*
    USAGE: This will add a player to the first found blank area that doesn't have a player on it.
     */
    public void addPlayerToBlanks(int playerId){
        for (BlankArea blank: blankAreasList) {
            if (blank.getPlayerId() == 0) {
                blank.setPlayerId(playerId);
                return;
            }
        }
    }

    /*
    USAGE: This will remove a certain player from the blank area, if that player moved for example.
     */
    public void removePlayerFromBlanks(int playerId){
        for (BlankArea blank: blankAreasList) {
            if (blank.getPlayerId() == playerId) {
                blank.setPlayerId(0);
            }
        }

    }

    /*
    USAGE: This will check if a person is on any of the blanks, will return true if they are.
     */
    public boolean isPersonOnBlank(int playerId){
        for (BlankArea blank: blankAreasList) {
            if (blank.getPlayerId() == playerId) {
                return true;
            }
        }
        return false;
    }

    // Getters:
    public int getMaxX() { return maxX; }
    public int getMaxY() { return maxY; }
    public int getMinX() { return minX; }
    public int getMinY() { return minY; }
    public ArrayList<String> getNeighbors() { return neighbors; }
    public String getName() { return name; }
    public ArrayList<BlankArea> getBlankAreasList() { return blankAreasList; }

    // Setters:
    public void setMaxX(int maxX) { this.maxX = maxX; }
    public void setMaxY(int maxY) { this.maxY = maxY; }
    public void setMinX(int minX) { this.minX = minX; }
    public void setMinY(int minY) { this.minY = minY; }
    public void setNeighbors(ArrayList<String> neighbors) { this.neighbors = neighbors; }
    public void setName(String name) { this.name = name; }
    public void setBlankAreasList(ArrayList<BlankArea> blankAreasList) { this.blankAreasList = blankAreasList; }
}
