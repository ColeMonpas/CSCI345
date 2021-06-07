package cs345.deadwood;

import java.sql.Array;
import java.util.*;

/*
USAGE: The deadwood model function operates as the main hub of action
its called by controller based on users inputs and will be told what actions to perform
this model will either perform operations in itself or call on other classes to perform actions
 */
public class DeadWoodModel implements Model{
    private final List<ModelObserver> observerList = new ArrayList<>();
    private Player currentPlayer;
    private Dice Dice;
    private final ParseXML parser = new ParseXML();
    private Queue<Player> playerOrder = new LinkedList<>();
    private int dayCounter;
    private int maxDays;
    private Location location;
    private boolean playerMovedThisTurn;
    private boolean playerActionDoneThisTurn;



    /*
    USAGE: initializer of the game given a number of players specified
     */
    public void startGame(int numPlayers){

        notifyObserversAboutPlayerCount(numPlayers);
        // init the scenes and sets, parses the xml files
        parser.ParseForTheSets();
        parser.ParseForTheScenes();
        //init location, location will have the scene cards, and sets lists
        location = new Location(parser.getAllSceneCards(), parser.getSetsList());
        location.initTenScenes();

        //initial starting information based on number of players
        int startingCredits = 0;
        int startingRank = 1;
        dayCounter = 1;

        //inits max days
        if (numPlayers <=3){
            maxDays = 3;
        }else {maxDays = 4;}

        //inits starting bonuses
        if(numPlayers == 5){
            startingCredits = 2;
        }
        else if(numPlayers >5 && numPlayers <7){
            startingCredits = 4;
        }
        else if( numPlayers >=7){
            startingCredits = 4;
            startingRank = 2;
        }
        //iterates and generates players and player order queue
        for (int i = 1; i <= numPlayers; i ++){
            Player tmpPlayer =new Player(i,startingCredits, startingRank, location.getSets().get("trailer"));
            playerOrder.add(tmpPlayer);

        }
        //sets cur player
        currentPlayer = playerOrder.peek();

        //inits dice
        Dice = new Dice(6,1);
        notifyObserversAboutBoard(location);
        notifyObserversAboutPlayer(location, playerOrder);
    }

    /*
    USAGE: will check the neighbors of a set name
     */
    public boolean isValidMove(String name){
        ArrayList<String> possibleMoves =  location.getNeighbors(currentPlayer.getCurrentLocation());
        possibleMoves.add(currentPlayer.getCurrentLocation().getName());
        return possibleMoves.contains(name);
    }

    /*
    USAGE: The move player function will take in x and y inputs and use the location class to
    determine if a player can move somewhere and it will also update the player information based on where they clicked
     */
    public void movePlayer(int x, int y){
        BaseSet moveSet = location.getSet(x, y);
        Role moveRole = location.getRole(x, y);
        //checks if valid move
        if(moveSet == null && moveRole == null){
            //send to a text box later
            notifyObserversAboutMovePanel(2, "Player: "+currentPlayer.getPlayerID() + " did not click a valid space");
            notifyObserversAboutPlayer(location, playerOrder);
            return;
        }
        if(moveSet == null){
            moveSet = location.getSetFromName(moveRole.getSetName());
        }
        if((playerMovedThisTurn && moveRole == null)  ||(playerMovedThisTurn && moveSet.getName() != currentPlayer.getCurrentLocation().getName())){
            notifyObserversAboutMovePanel(2, "Player: "+currentPlayer.getPlayerID() + " already moved this turn");
            notifyObserversAboutPlayer(location, playerOrder);
            return;
        }
        if(moveSet instanceof SceneSet){
            if(((SceneSet) moveSet).getFinished() && moveRole !=null){
                notifyObserversAboutMovePanel(2, "Player: "+currentPlayer.getPlayerID() + " attempted to work on a finished scene");
                notifyObserversAboutPlayer(location, playerOrder);
                return;
            }
        }
        if(isValidMove(moveSet.getName())) {
            //checks if player clicked on a role
            if (moveRole != null) {
                if (currentPlayer.getRank() >= moveRole.getRank() && !moveRole.getTaken()) {
                    moveSet.removePlayerFromBlanks(currentPlayer.getPlayerID());
                    currentPlayer.setCurrentLocation(moveSet);
                    currentPlayer.setCurrentRole(moveRole);
                    if(moveSet instanceof SceneSet){
                        ((SceneSet) moveSet).setActive(true);
                    }
                    moveRole.setTaken(true);
                    notifyObserversAboutMovePanel(2, "Player: "+currentPlayer.getPlayerID() + " took up role " + moveRole.getName() + "as a " + moveRole.getType());
                } else {
                    //player cant do this role perform stuff
                    notifyObserversAboutMovePanel(2, "Player: "+currentPlayer.getPlayerID() + " cannot take this role because rank it too low or someone else is on the role");
                }
            } else {
                moveSet.removePlayerFromBlanks(currentPlayer.getPlayerID());
                currentPlayer.setCurrentLocation(moveSet);
                if(moveSet instanceof SceneSet){
                    ((SceneSet) moveSet).setActive(true);
                }
                notifyObserversAboutMovePanel(2, "Player: "+currentPlayer.getPlayerID() + " moved to " + moveSet.getName());
                playerMovedThisTurn = true;
            }
        }else{
            notifyObserversAboutMovePanel(2, "Player: "+currentPlayer.getPlayerID() + " attempted to move to " + moveSet.getName() + " \nThis is an invalid move");

        }
        notifyObserversAboutPlayer(location, playerOrder);
        notifyObserversAboutBoard(location);
    }


    /*
    USAGE: performs the proper actions if a player chooses to rehearse
     */
    public void playerRehearse(){
        if(playerActionDoneThisTurn){
            notifyObserversAboutMovePanel(1, "Player: "+currentPlayer.getPlayerID() + " already performed their action this turn");
            System.out.println("Got to player did action");
            notifyObserversAboutPlayer(location,playerOrder);
            return;
        }
        currentPlayer.setPracticeChips(currentPlayer.getPracticeChips()+1);
        notifyObserversAboutMovePanel(1, "Player: "+currentPlayer.getPlayerID()+ " rehearsed, your practice chip count is now, " + currentPlayer.getPracticeChips());
        playerActionDoneThisTurn = true;
        notifyObserversAboutPlayer(location,playerOrder);
    }

    /*
    USAGE: performs the proper actions if a player chooses to work on a scene.
    it will also call end scene if necessary
     */
    public void playerWork(){
        if(playerActionDoneThisTurn){
            notifyObserversAboutMovePanel(1, "Player: "+currentPlayer.getPlayerID() + " already performed their action this turn");
            notifyObserversAboutPlayer(location,playerOrder);
            notifyObserversAboutBoard(location);
            return;
        }
        int roll = Dice.roll();
        String text = "";
        SceneSet playerScene =(SceneSet) location.getSets().get(currentPlayer.getCurrentLocation().getName());
        if(currentPlayer.getCurrentRole().getType() == "Extra"){
            //if they rolled above or equal as an extra
            if (roll + currentPlayer.getPracticeChips() >= currentPlayer.getCurrentRole().getRank()){
                currentPlayer.setCredits(currentPlayer.getCredits()+1);
                currentPlayer.setMoney(currentPlayer.getMoney()+1);
                text += "Player: "+currentPlayer.getPlayerID() + " Rolled a " + roll + " which was above " + currentPlayer.getCurrentRole().getRank() + "\n Player successfully acted as an extra.";
                //removes a take
                subtractTake();
                //if all takes are flipped it calls endScene
                if(playerScene.getFinished()){
                    text += "\nScene has finished";
                    endScene(playerScene);
                    location.addFinishedScene();
                    if(location.isDayDone()){
                        newDay();
                        System.out.println("This day should be finished");
                    }
                }
            }//if an extra fails the roll still add 1 dollar
            else{
                currentPlayer.setMoney(currentPlayer.getMoney()+1);
                text += "Player: "+currentPlayer.getPlayerID() + " Rolled a " + roll + " which was below " + currentPlayer.getCurrentRole().getRank() + "\n Player failed their act as an extra.";
            }

        }else{
            //if they rolled above or equal as an actor
            if (roll + currentPlayer.getPracticeChips() >= currentPlayer.getCurrentRole().getRank()){
                currentPlayer.setCredits(currentPlayer.getCredits()+2);
                text += "Player: "+currentPlayer.getPlayerID() + " Rolled a " + roll + " which was above " + currentPlayer.getCurrentRole().getRank() + "\n Player successfully acted as an actor.";
                //removes a take
                subtractTake();
                //if all takes are flipped it calls endScene
                if(playerScene.getFinished()){
                    endScene(playerScene);
                    text += "\nScene has finished";
                    location.addFinishedScene();
                    if(location.isDayDone()){
                        newDay();
                        System.out.println("This day should be finished");
                    }
                }
            }else{
                text += "Player: "+currentPlayer.getPlayerID() + " Rolled a " + roll + " which was below " + currentPlayer.getCurrentRole().getRank() + "\n Player failed their act as an actor.";
            }
        }
        notifyObserversAboutMovePanel(1, text);
        notifyObserversAboutPlayer(location,playerOrder);
        notifyObserversAboutBoard(location);
        playerActionDoneThisTurn = true;
    }

    /*
    USAGE: when a user completes a take, it will remove one.
     */
    public void subtractTake(){
        SceneSet playerScene =(SceneSet) location.getSets().get(currentPlayer.getCurrentLocation().getName());
        for (Take take: playerScene.getTakes()){
            if(!take.isFlipped()){
                take.setFlipped(true);
                //checks if the take just flipped is the last take if it is then it tells the set its finished
                if(take == playerScene.getTakes().get(
                        playerScene.getTakes().size()-1)){
                    playerScene.setFinished(true);
                }
                return;
            }
        }
    }

    /*
    USAGE: called in player rehearse and will calculate and update money
    as well as end the scene functions
     */
    public void endScene(SceneSet set){
        //rolls dice equal to the budge
        int[] nums =  new int[set.getSceneCard().getBudget()];
        for (int i = 0; i < set.getSceneCard().getBudget(); i++){
            nums[i] = Dice.roll();
        }

        Arrays.sort(nums);
        int[] rolls = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            rolls[i] = nums[nums.length - 1 - i];
        }

        ArrayList<Integer> peopleOnCard = new ArrayList<>();
        for(Role role: set.getSceneCard().getRoleList()) {
            if(role.getTaken()){
                peopleOnCard.add(role.getRank());
            }
        }

        Collections.sort(peopleOnCard);
        int[] earnings = new int[7];
        int i = 0;
        for (int rank: peopleOnCard){
            for (int j = i; j < nums.length; j+=peopleOnCard.size()){
                earnings[rank] += rolls[j];
            }
            i++;
        }


        //iterates through on scene players
        for (Player player:playerOrder){
            if(player.getCurrentLocation() == set){
                //checks if player is on a role in the scene
                if(set.getSceneCard().getRoleList().contains(player.getCurrentRole())){
                    player.setPracticeChips(0);
                    player.setMoney(player.getMoney() + earnings[player.getCurrentRole().getRank()]);
                    player.setCurrentRole(null);
                }
            }
        }

        //checks if there where players on the scene before handing out bonuses
        for (Player player:playerOrder) {
            if (player.getCurrentLocation() == set) {
                //checks if player is on a role on extras
                if (set.getExtras().contains(player.getCurrentRole())) {
                    if(!peopleOnCard.isEmpty()) {
                        player.setMoney(player.getMoney() + player.getCurrentRole().getRank());
                    }
                    player.setCurrentRole(null);
                    player.setPracticeChips(0);
                }
            }
        }

        //updates roles in the set so no player is on them
        for (Role role: set.getSceneCard().getRoleList()){
            role.setTaken(false);
        }
        for (Role role: set.getExtras()){
            role.setTaken(false);
        }
        notifyObserversAboutBoard(location);
    }


    /*
    USAGE: This function will be used when a player clicks the rank they want to upgrade,
    it will utilize the CastingOffice class to get rank formation adn from there will update the player information
     */
    public void upgradeRank(int x,int y){
        //gets the rank cost and type as an int array as such
        Upgrade upgrade = location.getUpgrade(x,y);
        System.out.println(upgrade);

        if(upgrade == null){
            notifyObserversAboutMovePanel(2, "Player " + currentPlayer.getPlayerID() + " failed to click an upgrade choice on the casting office " );
            notifyObserversAboutPlayer(location,playerOrder);
            return;
        }

        int rank = upgrade.getRank();
        int cost = upgrade.getAmt();
        String type = upgrade.getCurrency();

        if(rank <= currentPlayer.getRank()){
            notifyObserversAboutMovePanel(2, "Player " + currentPlayer.getPlayerID() + " is below rank " + rank);
        }

        //if player clicked on the upgrade option with credits
        if(type.equals("credit")){
            if(currentPlayer.getCredits() < cost){
                notifyObserversAboutMovePanel(2, "Player " + currentPlayer.getPlayerID() + " does not have enough credits for rank " + rank);
            }
            else{
                currentPlayer.setCredits(currentPlayer.getCredits()-cost);
                notifyObserversAboutMovePanel(2, "Player " + currentPlayer.getPlayerID() + " upgraded to rank " + rank + " from rank " + currentPlayer.getRank());
                currentPlayer.setRank(rank);
            }
        }
        //if player clicked upgrade option with dollars
        else{
            if(currentPlayer.getMoney() < cost){
                notifyObserversAboutMovePanel(2, "Player " + currentPlayer.getPlayerID() + " does not have enough dollars for rank " + rank);
            }
            else{
                currentPlayer.setMoney(currentPlayer.getMoney()-cost);
                notifyObserversAboutMovePanel(2, "Player " + currentPlayer.getPlayerID() + " upgraded to rank " + rank + " from rank " + currentPlayer.getRank());
                currentPlayer.setRank(rank);
            }
        }
    }




    /*
    USAGE: When next turn is called then it will push and pop from queue and check if a new day needs to be implemented
     */
    public void rotatePlayers(){
        playerMovedThisTurn = false;
        playerActionDoneThisTurn = false;
        Player prevPlayer = playerOrder.remove();
        playerOrder.add(prevPlayer);

        //updates current player
        currentPlayer = playerOrder.peek();

        ArrayList<String> actionsList = getPossibleActions();
        String actions = "\n";
        for (int i =0; i < actionsList.size(); i++){
            actions += actionsList.get(i);
            actions += "\n";
        }

        if(currentPlayer.getCurrentRole() == null){
            notifyObserversAboutMovePanel(2, "Player " + currentPlayer.getPlayerID() + ": can do any of these actions " + actions);
        }else{
            notifyObserversAboutMovePanel(1, "Player " + currentPlayer.getPlayerID() + ": can do any of these actions " + actions);
        }

        notifyObserversAboutPlayer(location, playerOrder);
    }

    /*
    USAGE: called from within the model to determine if a new day is present and will perform further steps
     */
    private void newDay(){
        dayCounter++;
        location.resetFinishedScenes();
        if(dayCounter == maxDays){
            lastDay();
            return;
        }else{
            location.initTenScenes();
            for (Player player: playerOrder){
                player.setPracticeChips(0);
                player.setCurrentLocation(location.getSetFromName("trailer"));
                player.setCurrentRole(null);
            }

            for(BaseSet set : location.getSets().values()){
                if(set instanceof SceneSet){
                    ((SceneSet) set).setActive(false);
                    ((SceneSet) set).setFinished(false);
                    for(BlankArea blank : set.getBlankAreasList()){
                        blank.setPlayerId(0);
                    }
                    for(Take take : ((SceneSet) set).getTakes()){
                        take.setFlipped(false);
                    }
                }
            }
            notifyObserversAboutMovePanel(2, "New day started");
            notifyObserversAboutPlayer(location,playerOrder);
            notifyObserversAboutBoard(location);
        }
    }



    /*
    USAGE: when a new day is called and it is the last day it will call this function to do the final wrap up to end the game
     */
    private void lastDay(){
        calculatePlayerScore();
        notifyObserversAboutEndGame(playerOrder);
    }


    /*
    USAGE: used by controller to return possible actions of a player based on location and controller then uses that list
    to check for validity of a action
     */
    public ArrayList<String> getPossibleActions(){
        ArrayList<String> actions = new ArrayList<>();
        //checks if working
        if (currentPlayer.getCurrentRole() != null){
            actions.add("Rehearse");
            actions.add("Act");
        }else{
            //checks if player cant take a role without moving or not
            if(currentPlayer.getCurrentLocation().getName() == "office"){
                actions.add("Move");
                actions.add("Upgrade rank");
            }
            else{
                actions.add("Move");
                actions.add("Take up role");
            }
        }
        return actions;
    }


    /*
    USAGE: used with last day function to calculate final score
     */
    private void calculatePlayerScore(){
        for (Player curPlayer: playerOrder){
            // math to calc score and update a class that contains it
            curPlayer.setScore(curPlayer.getCredits() + curPlayer.getMoney() + (5* curPlayer.getRank()));
        }
    }


    /*
    USAGE: given a set of click points the model will use the location object to determine what the player clicked
    it will send that info back as a string to the controller
     */
    public String parseAction(int x, int y) {
        Role role = location.getRole(x, y);
        BaseSet set = location.getSet(x, y);

        if(role!=null){
            return "Take up role";
        //if player clicks on a new set
        }else if(currentPlayer.getCurrentLocation() != set){
            return "Move";
        //if player clicks on the same set they are on this will check if they clicked on one of the different rank options
        }else if(set.getName() == "office") {
            return "Upgrade Rank";
        }
        return "Invalid Action";
    }

    /*
    USAGE: Adds an observer to the observer list
     */
    @Override
    public void registerObserver(ModelObserver ob) { observerList.add(ob); }
    /*
    USAGE: Removes an observer to the observer list
     */
    @Override
    public void removeObserver(ModelObserver ob) {
        observerList.remove(ob);
    }

    /*
    USAGE: called to update the view for the board
     */
    @Override
    public void notifyObserversAboutBoard(Location location) {
        for (ModelObserver ob: observerList){
            ob.updateBoard(location);
        }
    }

    /*
    USAGE: called to update the view for the players
     */
    @Override
    public void notifyObserversAboutPlayer(Location location, Queue<Player> players) {
        for (ModelObserver ob: observerList){
            ob.updatePlayer(location, players);
        }
    }

    /*
    USAGE: called to update the view for the player count
     */
    @Override
    public void notifyObserversAboutPlayerCount(int playerCount){
        for (ModelObserver ob: observerList){
            ob.updatePlayerCount(playerCount);
        }
    }

    /*
    USAGE: called to update the view for the move panel
     */
    @Override
    public void notifyObserversAboutMovePanel(int type,String stringComment){
        for (ModelObserver ob: observerList){
            ob.updateMovePanel(type,stringComment);
        }
    }

    /*
    USAGE: called to update the view for ending the game
     */
    @Override
    public void notifyObserversAboutEndGame(Queue<Player> players){
        for (ModelObserver ob: observerList){
            ob.updateEndGame(players);
        }
    }
}
