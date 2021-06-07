package cs345.deadwood;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/*
USAGE: The location class will be used to parse in x and y values and return what the player clicked
it wil also be used to return neighbors of the sets.
 */
public class Location {
    private HashMap<String, BaseSet> setMap = new HashMap<>(); //<Name of set, SceneSet>
    private ArrayList<SceneCard> allSceneCards;
    private ArrayList<BaseSet> allSetsList;
    private int scenesFinished;

    /*
    CONSTRUCTOR
     */
    public Location(ArrayList<SceneCard> allSceneCards, ArrayList<BaseSet> allSets){
        this.allSceneCards = allSceneCards;
        this.allSetsList = allSets;
        this.scenesFinished = 0;
    }

    /*
    USAGE: Given a name of a set it will return an array list of neighbors from the sets list
     */
    public ArrayList<String> getNeighbors(BaseSet set) {
        return set.getNeighbors();
    }
    public BaseSet getSetFromName(String name){
        return setMap.get(name);
    }

    /*
    USAGE: returns 10 unused scenes to the model and puts them on a set,
     */
    public void initTenScenes(){
        for(BaseSet set: allSetsList){
            if (set instanceof SceneSet){
                setMap.remove(set.getName());
            }
        }
        Collections.shuffle(allSceneCards);
        ArrayList<SceneCard> tenScenes = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            tenScenes.add(allSceneCards.remove(0));
        }
        for (BaseSet set: allSetsList){
            if(set instanceof SceneSet){
                ((SceneSet) set).setSceneCard(tenScenes.remove(0));
            }
            setMap.put(set.getName(), set);
        }
    }

    /*
    USAGE: Given x and y coordinates this function will return the upgrade the player clicked on or null if they didnt click an upgrade
     */
    public Upgrade getUpgrade(int x, int y){
        for (Upgrade upgrade: ((CastingOffice) setMap.get("office")).getCreditList()){
            int maxX = upgrade.getX()+ 19;
            int minX = upgrade.getX() ;
            int maxY = upgrade.getY()+ 19;
            int minY = upgrade.getY() ;
            boolean insideX = x < maxX && x > minX;
            boolean insideY = y < maxY && y > minY;
            if(insideX && insideY){
                return upgrade;
            }
        }
        return null;
    }

    /*
    USAGE: Given x and y coordinates this function will return a set that the player attempts to move to
     */
    public BaseSet getSet(int x, int y){

        for(BaseSet set: allSetsList){
            int maxX = set.getMaxX();
            int minX = set.getMinX();
            int maxY = set.getMaxY();
            int minY = set.getMinY();
            boolean insideX = x < maxX && x > minX;
            boolean insideY = y < maxY && y > minY;
            if(insideX && insideY){
                return set;
            }
        }
        return null;
    }

    /*
    USAGE: Given x and y cords return a role the player clicked on or return null if there is not role at x,y
     */
    public Role getRole(int x, int y){
        for(BaseSet set: allSetsList) {
            if (set instanceof SceneSet) {
                for (Role role : ((SceneSet) set).getExtras()) {
                    int maxX = role.getX() + 40;
                    int minX = role.getX();
                    int maxY = role.getY() + 40;
                    int minY = role.getY();
                    boolean insideX = x < maxX && x > minX;
                    boolean insideY = y < maxY && y > minY;
                    if (insideX && insideY) {
                        return role;
                    }
                }
                for(Role role : ((SceneSet) set).getSceneCard().getRoleList()){
                    int maxX = role.getX() + 40 + set.getMinX();
                    int minX = role.getX() + set.getMinX();
                    int maxY = role.getY() + 40 + set.getMinY();
                    int minY = role.getY() + set.getMinY();
                    boolean insideX = x < maxX && x > minX;
                    boolean insideY = y < maxY && y > minY;
                    if (insideX && insideY) {
                        return role;
                    }
                }
            }
        }
        return null;
    }

    /*
    USAGE: Will return if the day has completed or not.
     */
    public boolean isDayDone(){
        if(scenesFinished == 9){
            return true;
        }
        return false;
    }

    // Getters:
    public HashMap<String, BaseSet> getSets() { return setMap; }
    public ArrayList<SceneCard> getAllSceneCards() { return allSceneCards; }
    public ArrayList<BaseSet> getAllSetsList() { return allSetsList; }
    // Setters:
    public void addFinishedScene(){ scenesFinished++; }
    public void resetFinishedScenes(){ scenesFinished = 0; }
}
