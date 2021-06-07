package cs345.deadwood;

import java.util.ArrayList;

/*
USAGE: The set class contains the information for a set including the location,
the scene currently on it, the number of shots, the extras on it, the neighbors
and the current number of shots
 */
public class SceneSet extends BaseSet{
    private SceneCard sceneCard;
    private ArrayList<Role> extras;
    private ArrayList<Take> takes;
    private boolean isFinished;
    private boolean isActive;

    /*
    CONSTRUCTOR
     */
    public SceneSet(String name, int maxX, int maxY, int minX, int minY, ArrayList<Role> extras,
                    ArrayList<String> neighbors, ArrayList<Take> takes, ArrayList<BlankArea> blankAreasList  ) {
        super(name, neighbors,blankAreasList, maxX, maxY, minX, minY);
        this.sceneCard = null;
        this.extras = extras;
        this.takes = takes;
        this.isFinished = false;
        this.isActive = false;
    }

    // Getters:
    public ArrayList<Role> getExtras() {
        return extras;
    }
    public ArrayList<Take> getTakes() { return takes; }
    public boolean getFinished() {
        return isFinished;
    }
    public SceneCard getSceneCard() {
        return sceneCard;
    }
    public boolean isActive() {
        return isActive;
    }

    // Setters:
    public void setFinished(boolean finished) {
        isFinished = finished;
    }
    public void setSceneCard(SceneCard sceneCard) {
        this.sceneCard = sceneCard;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
