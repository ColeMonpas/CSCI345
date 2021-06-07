package cs345.deadwood;

/*
USAGE: Each player will be kept as an object, with all of their information stored here.
the model will be accessing this quite a lot.
 */
public class Player {
    private int playerID;
    private int money;
    private int credits;
    private int rank;
    private BaseSet currentLocation;
    private Role currentRole;
    private int practiceChips;
    private int score;

    /*
    CONSTRUCTOR
     */
    public Player(int playerID, int credits, int rank, BaseSet currentLocation) {
        this.playerID = playerID;
        this.money = 0;
        this.credits = credits;
        this.rank = rank;
        this.currentLocation = currentLocation;
        this.currentRole = null;
        this.score = 0;
    }

    // Getters:
    public int getPlayerID() {
        return playerID;
    }
    public int getMoney() {
        return money;
    }
    public int getCredits() {
        return credits;
    }
    public int getRank() {
        return rank;
    }
    public BaseSet getCurrentLocation() {
        return currentLocation;
    }
    public Role getCurrentRole() {
        return currentRole;
    }
    public int getPracticeChips() {
        return practiceChips;
    }
    public int getScore() {
        return score;
    }

    // Setters:
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public void setCurrentLocation(BaseSet currentLocation) {
        this.currentLocation = currentLocation;
    }
    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
    }
    public void setPracticeChips(int practiceChips) {
        this.practiceChips = practiceChips;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
