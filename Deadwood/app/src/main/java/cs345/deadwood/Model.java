package cs345.deadwood;

import java.util.Queue;

public interface Model {
    /*
    USAGE: used in model observer method to connect deadwoodModel to board view
     */
    void registerObserver(ModelObserver ob);
    void removeObserver(ModelObserver ob);
    void notifyObserversAboutMovePanel(int type,String stringComment);
    void notifyObserversAboutEndGame(Queue<Player> players);
    void notifyObserversAboutBoard(Location location);
    void notifyObserversAboutPlayer(Location location, Queue<Player> players);
    void notifyObserversAboutPlayerCount(int playerCount);

}
