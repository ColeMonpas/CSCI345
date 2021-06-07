package cs345.deadwood;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Queue;

/*
the board view class is the gui implementation, it uses a model observer patter with the deadwood model to update itself
 */
public class BoardView implements MouseListener, ModelObserver{

    private JFrame frame;
    private final int VERTICAL_PADDING = 5;
    private final int HORIZONTAL_PADDING = 5;
    private final Model model;
    private final Controller controller;
    private final String shotImagePath = "img/shot.png";
    public int playerCount;
    private final String[] diceToPlayer = {"b","c","g","o","p","r","v","y"};
    private JPanel movePanel;
    private JPanel controlPanel;
    private JButton endTurnButton;

    private ArrayList<Component> cardList = new ArrayList<>();
    private ArrayList<Component> playerBoardList = new ArrayList<>();



    public BoardView(Model model, Controller controller){
        this.model = model;
        this.controller = controller;
        this.model.registerObserver(this);
    }

    /*
    USAGE: initializes all the information and frames
     */
    public void init(int numberOfPlayers) {
        playerCount = numberOfPlayers;
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1500, 930));
        // board img is 1200 x 900. The control panel is 300 x 900, so we want the frame to be 1500 x 900
        // The top bar on the frame is about 30 pixels in height. To account for that, we increase frame height by 30, so 930.

        // Set layout to null, so we can place widgets based on x-y coordinates.
        frame.setLayout(null);

        //init end turn button
        endTurnButton = new JButton("End turn");
        endTurnButton.setActionCommand("End turn");
        endTurnButton.setSize(new Dimension(300, 100));
        endTurnButton.addActionListener(new clickButtonListener());
        endTurnButton.setLocation(1200,700);

        URL boardImg = getClass().getClassLoader().getResource("img/board.png");
        JLabel board = new JLabel(new ImageIcon(boardImg.getPath()));
        board.setLocation(0, 0);
        board.setSize(1200, 900);
        frame.add(board);

        JPanel controlPanel = createControlPanel();



        frame.addMouseListener(this);

        frame.pack();
        frame.setVisible(true);
    }

    /*
    USAGE: inits the control panel initially
     */
    private JPanel createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(300, 900));
        // Set height same as the board image. board image dimensions are 1200 x 900

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add padding around edges

        JLabel team = new JLabel("Team Name");
        team.setFont(new Font("TimesRoman", Font.BOLD, 20));
        controlPanel.add(team);
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        controlPanel.add(new JSeparator());
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        JLabel playerInfoLabel = new JLabel("Player List");
        playerInfoLabel.setFont(new Font("TimesRoman", Font.BOLD, 18));
        controlPanel.add(playerInfoLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

//         Show players
        for(int i = 0; i < playerCount; i++){
            controlPanel.add(showPlayerInfo(i+1, "Trailer", 0, 0, "dice_" + diceToPlayer[i] + 1 + ".png"));
        }
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding


        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding
        controlPanel.add(new JSeparator());
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding
        updateMovePanel(2,"Game started it is player 1's turn \nPlayer 1 options: Move, Take up role");
        controlPanel.add(movePanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding
        controlPanel.add(new JSeparator());


        return controlPanel;
    }

    /*
    USAGE: works to init all player info to a jpanel given the proper inputs
     */
    private JPanel showPlayerInfo(int i, String area, int cash, int credit, String dice) {
        
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400 - HORIZONTAL_PADDING*2, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        panel.add(new JLabel("Player " + i + ": "));
        panel.add(Box.createRigidArea(new Dimension(HORIZONTAL_PADDING,0))); // Add padding

        JLabel playerDice= new JLabel(new ImageIcon(getClass().getClassLoader().getResource("img/" + dice).getPath()));
        panel.add(playerDice);
        panel.add(Box.createRigidArea(new Dimension(HORIZONTAL_PADDING,0))); // Add padding

        JLabel playerLocation = new JLabel(area);
        panel.add(playerLocation);
        panel.add(Box.createRigidArea(new Dimension(HORIZONTAL_PADDING,0))); // Add padding

        JLabel money = new JLabel("$" + cash + " C" + credit); // 2 dollars and 3 credits.
        panel.add(money);
        panel.add(Box.createRigidArea(new Dimension(HORIZONTAL_PADDING,0))); // Add padding

        return panel;
    }


    private class clickButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // send to controller?
            String command = e.getActionCommand();
            controller.playerClickedButton(command);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // The top bar of the frame is about 30 pixels in height. So to get the x,y values on the board, subtract 30 from the y value.
        System.out.println("Mouse clicked at X = " + e.getX() + ", Y = " + (e.getY() - 30));
        controller.playerClicked(e.getX(), (e.getY()-30));
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /*
    USAGE: updates all the cards and takes information
     */
    @Override
    public void updateBoard(Location location) {

        //clears old components
        for(Component comp : cardList){
            frame.getLayeredPane().remove(comp);
        }
        cardList.clear();

        //in the set loop update the shot counter with the correct dots
        for (BaseSet set: location.getSets().values()){

            // hit scene card
            // init scene info
            if(set instanceof SceneSet){
                SceneCard scene = ((SceneSet) set).getSceneCard();
                URL sceneImg;
                if(!((SceneSet) set).isActive()){
                    sceneImg = getClass().getClassLoader().getResource("img/cardback.png");
                }else{
                    sceneImg = getClass().getClassLoader().getResource("img/"+scene.getImageName());
                }
                JLabel card = new JLabel(new ImageIcon(sceneImg.getPath()));
                card.setLocation(set.getMinX(), set.getMinY());
                card.setSize(205, 115);

                cardList.add(card);
                frame.getLayeredPane().add(card);
                // takes loop
                for (Take take:((SceneSet) set).getTakes()){
                    if(take.isFlipped()) {
                        JLabel takeLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource(shotImagePath)));
                        takeLabel.setLocation(take.getMinX(), take.getMinY());
                        takeLabel.setSize(47, 47);
                        cardList.add(takeLabel);
                        frame.getLayeredPane().add(takeLabel);
                    }
                }
            }
        }
    }

    /*
    USAGE:used to update all the information in the control panel including the player information and the move panel
     */
    @Override
    public void updatePlayer(Location location, Queue<Player> players) {
        //removes the old components
        for(Component comp : playerBoardList){
            frame.getLayeredPane().remove(comp);
        }
        playerBoardList.clear();
        frame.getLayeredPane().remove(controlPanel);


        //Creates new control panel
        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(300, 900));
        // Set height same as the board image. board image dimensions are 1200 x 900

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add padding around edges

        JLabel team = new JLabel("Team Name");
        team.setFont(new Font("TimesRoman", Font.BOLD, 20));
        controlPanel.add(team);
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        controlPanel.add(new JSeparator());
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        JLabel playerInfoLabel = new JLabel("Player List");
        playerInfoLabel.setFont(new Font("TimesRoman", Font.BOLD, 18));
        controlPanel.add(playerInfoLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        //iterates through players and updates information on the control panel
        for(Player player: players){
            int playerCredits = player.getCredits();
            int playerMoney = player.getMoney();
            int playerRank = player.getRank();
            int playerID = player.getPlayerID();
            URL diceImg = getClass().getClassLoader().getResource("img/dice_" +diceToPlayer[playerID-1]+ playerRank+".png");
            BaseSet playerLocation = location.getSets().get(player.getCurrentLocation().getName());
            if(player.getCurrentRole() == null  ){
                if(playerLocation.isPersonOnBlank(playerID)) {
                    BlankArea openBlank;
                    for (BlankArea blank : playerLocation.getBlankAreasList()) {
                        if (blank.getPlayerId() == playerID) {
                            openBlank = blank;
                            JLabel playerDice = new JLabel(new ImageIcon(diceImg.getPath()));
                            playerDice.setLocation(openBlank.getX(), openBlank.getY());
                            playerDice.setSize(40, 40);
                            playerBoardList.add(playerDice);
                            frame.getLayeredPane().add(playerDice);
                            break;
                        }
                    }
                }else{
                    BlankArea openBlank;
                    for (BlankArea blank : playerLocation.getBlankAreasList()) {
                        if (blank.getPlayerId() == 0) {
                            openBlank = blank;
                            JLabel playerDice = new JLabel(new ImageIcon(diceImg.getPath()));
                            playerDice.setLocation(openBlank.getX(), openBlank.getY());
                            playerDice.setSize(40, 40);
                            playerLocation.addPlayerToBlanks(playerID);
                            playerBoardList.add(playerDice);
                            frame.getLayeredPane().add(playerDice);
                            break;
                        }
                    }
                }
            }
            else{
                Role playerRole = player.getCurrentRole();
                JLabel playerDice = new JLabel(new ImageIcon(diceImg.getPath()));
                int xVal ,yVal;
                if(playerRole.getType() == "Extra"){
                    xVal = playerRole.getX();
                    yVal = playerRole.getY();

                }else{
                    xVal = (playerRole.getX() + playerLocation.getMinX());
                    yVal = (playerRole.getY() + playerLocation.getMinY());
                }
                playerDice.setLocation(xVal,yVal);
                playerDice.setSize(40, 40);
                playerBoardList.add(playerDice);
                frame.getLayeredPane().add(playerDice);
                frame.getLayeredPane().setLayer(playerDice,2);
            }

            //adds the new player info
            controlPanel.add(showPlayerInfo(playerID, playerLocation.getName(), playerMoney, playerCredits, "dice_" + diceToPlayer[playerID-1] + playerRank + ".png"));

        }
        //adds the new move panel
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding
        controlPanel.add(new JSeparator());
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding
        controlPanel.add(movePanel);
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding
        controlPanel.add(Box.createRigidArea(new Dimension(0,VERTICAL_PADDING))); // Add padding

        controlPanel.setLocation(1200, 0);
        controlPanel.setSize(300, 800);

        //enables and sets the end turn button
        frame.getLayeredPane().add(endTurnButton);
        frame.getLayeredPane().add(controlPanel);
        frame.getLayeredPane().setLayer(endTurnButton,2);

    }

    /*
    updates player count only once at the start of the game
     */
    @Override
    public void updatePlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    /*
    USAGE: Used to update the text in the move panel or to add the buttons to the move panel
     */
    @Override
    public void updateMovePanel(int type,String stringComment) {
        //inits the view panel with the rehearse and act buttokns
        if(type == 1) {
            movePanel = new JPanel();
            movePanel.setLayout(new BoxLayout(movePanel, BoxLayout.PAGE_AXIS));


            JLabel panelTitle = new JLabel("Move options: ", SwingConstants.CENTER);
            panelTitle.setFont(new Font("TimesRoman", Font.BOLD, 18));
            movePanel.add(panelTitle);

            JTextArea comment = new JTextArea(stringComment);

            comment.setLineWrap(true);
            JButton rehearse = new JButton("Rehearse");
            rehearse.setActionCommand("Rehearse");
            rehearse.setPreferredSize(new Dimension(300,25));
            rehearse.addActionListener(new clickButtonListener());

            JButton act = new JButton("Act");
            act.setActionCommand("Act");
            act.setPreferredSize(new Dimension(300,25));
            act.addActionListener(new clickButtonListener());

            JPanel buttons = new JPanel();
            buttons.setPreferredSize(new Dimension(300,75));
            buttons.add(rehearse);
            buttons.add(act);

            movePanel.add(buttons);
            movePanel.add(comment);

        }
        //inits the move panel without the buttons
        else if(type ==2){
            movePanel = new JPanel();
            movePanel.setLayout(new BoxLayout(movePanel, BoxLayout.PAGE_AXIS));

            JLabel panelTitle = new JLabel("Move options: ", SwingConstants.CENTER);
            panelTitle.setFont(new Font("TimesRoman", Font.BOLD, 18));
            movePanel.add(panelTitle);

            JTextArea comment = new JTextArea(stringComment);
            comment.setLineWrap(true);
            movePanel.add(comment);
        }
    }

    /*
    USAGE: Called when game is over and it removes the old frame and creates a new end game frame
     */
    @Override
    public void updateEndGame(Queue<Player> players){
        //deletes old frame
        frame.setVisible(false);
        //creates new frame
        JPanel endPopup = new JPanel(new GridLayout(9,1));;
        ArrayList<JLabel> finalList= new ArrayList<>();
        Player winner = players.peek();
        //inits player info in a label
        for (Player player: players){
            if (player.getScore() > winner.getScore()){
                winner = player;
            }
            String endText = "\nPlayer ";
            endText += player.getPlayerID();
            endText +=": had a score of ";
            endText += player.getScore();
            JLabel playerLabel = new JLabel(endText, SwingConstants.CENTER);
            playerLabel.setFont(new Font("TimesRoman", Font.BOLD, 18));
            finalList.add(playerLabel);
        }

        //adds winner to the frame
        String winnerText = "Player " + winner.getPlayerID() + " Won!";
        JLabel winnerLabel = new JLabel(winnerText, SwingConstants.CENTER);
        winnerLabel.setFont(new Font("TimesRoman", Font.BOLD, 36));
        endPopup.add(winnerLabel);

        //adds players to the frame
        for (JLabel allScore: finalList){
            endPopup.add(allScore);
        }

        //inits new frame
        JFrame endGame = new JFrame();
        endGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endGame.setLayout(null);
        endGame.setPreferredSize(new Dimension(400, 600));
        endPopup.setLocation(0,0);
        endPopup.setSize(new Dimension(400,500));
        endGame.add(endPopup);
        endGame.pack();
        endGame.setVisible(true);
    }
}
