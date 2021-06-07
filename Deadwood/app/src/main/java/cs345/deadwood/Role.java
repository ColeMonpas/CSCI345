package cs345.deadwood;

/*
USAGE: This class object is used to store all of the required role information for each set.
placing roles, ending scenes, practicing, working, rehearsing
 */
public class Role {

    private String name;
    private Boolean taken;
    private int rank;
    private String type;
    private int X;
    private int Y;
    private String text;
    private String setName;

    /*
    CONSTRUCTOR
     */
    public Role(String name,String setName, int rank, String type, String text, int X, int Y ) {
        this.name = name;
        this.taken = false;
        this.rank = rank;
        this.type = type;
        this.text = text;
        this.setName = setName;
        this.X = X;
        this.Y = Y;
    }

    /*
    CONSTRUCTOR #2 this one just doesn't take a setName
     */
    public Role(String name, int rank, String type, String text, int X, int Y ) {
        this.name = name;
        this.taken = false;
        this.rank = rank;
        this.type = type;
        this.text = text;
        this.X = X;
        this.Y = Y;
    }

    // Used for printing some of the role information
    @Override
    public String toString(){ return name + " " + rank + " " + X + " " + Y + " Text: " + text; }

    // Getters:
    public String getText() { return text; }
    public String getName() { return name; }
    public Boolean getTaken() { return taken; }
    public int getRank() { return rank; }
    public String getType() { return type; }
    public int getX() { return X; }
    public int getY() { return Y; }
    public String getSetName() { return setName; }
    // Setters:
    public void setTaken(Boolean taken) { this.taken = taken; }
}
