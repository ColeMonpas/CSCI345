package cs345.deadwood;

import java.util.ArrayList;

/*
USAGE: The controller class takes in the user x and y inputs and calls the proper functions in model
 */
public class Controller {
    DeadWoodModel dwModel;
    /*
    CONSTRUCTOR
    */
    public Controller(DeadWoodModel model){
        this.dwModel = model;
    }

    /*
    The action sub-class is used to hold necessary information generated from user x and y values
     */
    private class Action{
        public String name;
        public int Xpos;
        public int Ypos;

        /*
        SUB CONSTRUCTOR
        */
        public Action(String name, int xpos, int ypos) {
            this.name = name;
            this.Xpos = xpos;
            this.Ypos = ypos;

        }
    }

    /*
    USAGE: Initializes the game
     */
    public void startGame(int numPlayers){
        dwModel.startGame(numPlayers);
    }

    /*
    USAGE: Determines what action the player wants to do
     */
    public void playerClickedButton(String name){
        if(name.equals("Rehearse")){
            dwModel.playerRehearse();
        }else if(name.equals("Act")){
            dwModel.playerWork();
        }else if(name.equals("End turn")){
            nextTurn();
        }
    }

    /*
    USAGE: The first function used when a user clicks at x and y values,
    it will call the proper helper functions before sending off the action to model
     */
    public void playerClicked(int x, int y){
        String nameLocation = dwModel.parseAction(x,y);
        Action action = new Action(nameLocation, x, y);
        System.out.println(nameLocation);
        if(isValidAction(action)){
            //calls send action to then call the right methods in deadwoodModel
            sendAction(action);
        }
    }

    /*
    USAGE: When a users actions determine an end of turn action this function will be called and
    will use the rotate players function in model
     */
    public void nextTurn(){
        dwModel.rotatePlayers();
    }

    /*
    USAGE: When an action has been parsed from the users input this function is called which will
    determine the appropriate methods in in model that should be called
     */
    public void sendAction(Action action){
        // based on the name of the action that is generated in ____clicked
        // calls the appropriate method in DeadWoodModel
        if(action.name == "Move"){
            dwModel.movePlayer(action.Xpos, action.Ypos);
        }else if(action.name == "Take up role"){
            dwModel.movePlayer(action.Xpos, action.Ypos);
        }else if(action.name == "Upgrade Rank"){
            dwModel.upgradeRank(action.Xpos, action.Ypos);
        }
    }

    /*
    USAGE: This function is a helper function that checks if a given action based on player input is valid
     */
    public Boolean isValidAction (Action action){
        ArrayList<String> actions = dwModel.getPossibleActions();
        if(actions.contains(action.name)){
            return true;
        }
        return false;
    }
}
