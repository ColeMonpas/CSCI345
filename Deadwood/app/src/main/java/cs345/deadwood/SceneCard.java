package cs345.deadwood;

import java.util.ArrayList;

/*
USAGE: Used for accessing roles, moving players onto roles and is used by the model for working/rehearsing
Each scene card stores its roles in a list.
 */
public class SceneCard {

    private int budget;
    private String name;
    private ArrayList<Role> roleList;
    private String imageName;
    private String text;

    /*
    CONSTRUCTOR
     */
    public SceneCard(String name, int budget, String imageName, String text, ArrayList<Role> roleList) {
        this.name = name;
        this.budget = budget;
        this.roleList = roleList;
        this.imageName = imageName;
        this.text = text;
    }

    // Getters:
    public int getBudget() { return budget; }
    public ArrayList<Role> getRoleList() { return roleList; }
    public String getName() { return name; }
    public String getText() { return text; }
    public String getImageName() { return imageName; }

    // Setters:
    public void setName(String name) { this.name = name; }
    public void setText(String text) { this.text = text; }


}
