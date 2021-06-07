package cs345.deadwood;

import java.util.ArrayList;
import java.util.Queue;

public interface ModelObserver {
    /*
    interface: model
    usage: used in model observer method to connect deadwoodmodel to board view
     */
    void updateBoard(Location location);
    void updatePlayer(Location location, Queue<Player> players);
    void updatePlayerCount(int playerCount);
    void updateMovePanel(int type,String stringComment);
    void updateEndGame(Queue<Player> players);

}
