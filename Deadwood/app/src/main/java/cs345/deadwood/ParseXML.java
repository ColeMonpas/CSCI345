package cs345.deadwood;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.net.URL;

/*
USAGE: This classes soul purpose is to read the xml files, and parse them, and puts all of the sets, and cards into two lists.
function that gets information to create the scenes sets and roles,
this information is then sent to location with model facilitating that transition
 */
public class ParseXML {
    private ArrayList<SceneCard> allSceneCards = new ArrayList<SceneCard>();
    private ArrayList<BaseSet> setsList = new ArrayList<BaseSet>();

    /*
    USAGE: Helper function used by parseFunctions, it will try to parse the file, or fail.
     */
    private Document getDocFromFile(String filename) throws ParserConfigurationException {
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;
            try {
                doc = db.parse(filename);
            } catch (Exception ex) {
                System.out.println("XML parse failure");
                ex.printStackTrace();
            }
            return doc;
        }
    }

    /*
    USAGE: tries to open the cards.xml file for parsing, if it does successfully it will then
    call initializeAllScenes, to parse the scenes data from the file.
    */
    public void ParseForTheScenes(){

        Document doc = null;
        try {
            URL resource = ParseXML.class.getClassLoader().getResource("cards.xml");
            doc = getDocFromFile(resource.getPath().replace("%20", " "));
            initializeAllScenes(doc);
        } catch (NullPointerException e) {
            System.out.println("Error = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error = " + e);
            return;
        }
    }

    /*
    USAGE: tries to open the board.xml file for parsing, if it does successfully it will then
    call readBoardData, to parse the board xml data.
    */
    public void ParseForTheSets(){
        Document doc = null;
        try {
            // tries to find the board.xml file, if it finds it, it will parse the data
            URL resource = ParseXML.class.getClassLoader().getResource("board.xml");
            doc =getDocFromFile(resource.getPath().replace("%20", " "));
            readBoardData(doc);
        } catch (NullPointerException e) {
            System.out.println("Error = " + e);
            return;
        } catch (Exception e) {
            System.out.println("Error = " + e);
            return;
        }
    }

    /*
    USAGE: This function will parse through the cards.xml and initialize all scenes with their roles
    It will assign these scenes to a set with the extra's also attached.
     */
    public void initializeAllScenes(Document d) {
        Element root = d.getDocumentElement();
        NodeList cards = root.getElementsByTagName("card");

        for (int i = 0; i < cards.getLength(); i++) {
            // read data from each card:
            Node card = cards.item(i);
            // each card variables:
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            String imgPath = card.getAttributes().getNamedItem("img").getNodeValue();
            int budget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());
            String sceneCardText = "ERROR";
            ArrayList<Role> roleList = new ArrayList<Role>();
            NodeList children = card.getChildNodes();

            for (int j = 0; j < children.getLength(); j++) {
                Node sub = children.item(j);
                if ("sceneCard".equals(sub.getNodeName())) {
                    // grabbing the scene card information
                    sceneCardText = sub.getTextContent();
                }else if ("part".equals(sub.getNodeName())) {
                    // grabbing the roles info
                    String roleName = sub.getAttributes().getNamedItem("name").getNodeValue();
                    int level = Integer.parseInt(sub.getAttributes().getNamedItem("level").getNodeValue());
                    int xRole = Integer.parseInt(sub.getChildNodes().item(1).getAttributes().getNamedItem("x").getNodeValue());
                    int yRole = Integer.parseInt(sub.getChildNodes().item(1).getAttributes().getNamedItem("y").getNodeValue());
                    String roleText = sub.getChildNodes().item(3).getTextContent();
                    // add roles to role List
                    roleList.add(new Role(roleName, level, "Main", roleText, xRole, yRole));
                }
            }
            // init the card here
            allSceneCards.add(new SceneCard(cardName,budget,imgPath,sceneCardText,roleList));
        }
    }

    /*
    USAGE: pulls the sets, the extras attached to each set and initializes the neighbors in location
     */
    private void readBoardData(Document d) {
        Element root = d.getDocumentElement();
        NodeList sets = root.getElementsByTagName("set");

        for (int i = 0; i < sets.getLength(); i++) {
            //read data from the nodes
            Node set = sets.item(i);
            String setName = set.getAttributes().getNamedItem("name").getNodeValue();
            //read data from children nodes
            NodeList children = set.getChildNodes();
            //neighbors list for each set
            ArrayList<String> neighborList = new ArrayList<String>();
            // takes list for each set
            ArrayList<Take> takeList = new ArrayList<Take>();
            // extras list for each set
            ArrayList<Role> extras = new ArrayList<Role>();
            // blanks list for each set
            ArrayList<BlankArea> blankAreaList = new ArrayList<BlankArea>();

            //the sets position variables:
            int setXLow = 0;
            int setWidth = 0;
            int setXHigh = 0;
            int setYLow = 0;
            int setHeight = 0;
            int setYHigh = 0;

            for (int j = 0; j < children.getLength(); j++) {
                Node sub = children.item(j);
                if ("parts".equals(sub.getNodeName())) {
                    NodeList parts = sub.getChildNodes();
                    for(int k = 0; k < parts.getLength(); k++){
                        Node part = parts.item(k);
                        if ("part".equals(part.getNodeName())) {
                            String roleName = part.getAttributes().getNamedItem("name").getNodeValue();
                            int rank = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());
                            // grabbing the two x coordinates
                            int xLow = Integer.parseInt(part.getChildNodes().item(1).getAttributes().getNamedItem("x").getNodeValue());
                            // grabbing the two y coordinates
                            int yLow = Integer.parseInt(part.getChildNodes().item(1).getAttributes().getNamedItem("y").getNodeValue());
                            String text = part.getChildNodes().item(3).getTextContent();
                            Role role = new Role(roleName,setName, rank, "Extra", text, xLow, yLow);
                            extras.add(role);
                        }
                    }
                }else if("neighbors".equals(sub.getNodeName())){
                    // adding in the neighbors
                    NodeList neighbors = sub.getChildNodes();
                    for(int k = 0; k < neighbors.getLength(); k++){
                        Node neighbor = neighbors.item(k);
                        if("neighbor".equals(neighbor.getNodeName())){
                            //System.out.println("Test " + neighbor.getAttributes().getNamedItem("name").getNodeValue());
                            neighborList.add(neighbor.getAttributes().getNamedItem("name").getNodeValue());
                        }
                    }
                }else if("area".equals(sub.getNodeName())){
                    // grabbing the x coordinates of the set
                    setXLow = Integer.parseInt(sub.getAttributes().getNamedItem("x").getNodeValue());
                    setWidth = Integer.parseInt(sub.getAttributes().getNamedItem("w").getNodeValue());
                    setXHigh = setXLow + setWidth;

                    // grabbing the y coordinates of the set
                    setYLow = Integer.parseInt(sub.getAttributes().getNamedItem("y").getNodeValue());
                    setHeight = Integer.parseInt(sub.getAttributes().getNamedItem("h").getNodeValue());
                    setYHigh = setYLow + setHeight;
                }else if("takes".equals(sub.getNodeName())){
                    NodeList takes = sub.getChildNodes();
                    for(int k = 0; k < takes.getLength(); k++){
                        Node take = takes.item(k);
                        if("take".equals(take.getNodeName())){
                            // grabbing Takes information
                            int takeNumber = Integer.parseInt(take.getAttributes().getNamedItem("number").getNodeValue());
                            // grabbing the x coordinates of the takes
                            int xLow = Integer.parseInt(take.getChildNodes().item(0).getAttributes().getNamedItem("x").getNodeValue());
                            int width = Integer.parseInt(take.getChildNodes().item(0).getAttributes().getNamedItem("w").getNodeValue());
                            int xHigh = xLow + width;
                            // grabbing the y coordinates of the takes
                            int yLow = Integer.parseInt(take.getChildNodes().item(0).getAttributes().getNamedItem("y").getNodeValue());
                            int height = Integer.parseInt(take.getChildNodes().item(0).getAttributes().getNamedItem("h").getNodeValue());
                            int yHigh = yLow + height;
                            // adding the takes into the takes list
                            Take takeObject = new Take(takeNumber, xHigh, yHigh, xLow, yLow);
                            takeList.add(takeObject);
                        }
                    }
                } else if("blanks".equals(sub.getNodeName())){
                    NodeList blanks = sub.getChildNodes();
                    for(int k = 0; k < blanks.getLength(); k++){
                        Node blank = blanks.item(k);
                        if("blank".equals(blank.getNodeName())){
                            int blankNumber = Integer.parseInt(blank.getAttributes().getNamedItem("number").getNodeValue());

                            // grabbing the blank coordinates -- height and width is 40 so we don't need to grab these.
                            int xBlank = Integer.parseInt(blank.getChildNodes().item(0).getAttributes().getNamedItem("x").getNodeValue());
                            int yBlank = Integer.parseInt(blank.getChildNodes().item(0).getAttributes().getNamedItem("y").getNodeValue());

                            //System.out.println("Set " + setName + " " + blankNumber + " " + xBlank + " " + yBlank);
                            BlankArea blankSpot = new BlankArea(xBlank, yBlank, blankNumber);
                            blankAreaList.add(blankSpot);
                        }
                    }
                }
            }
            // adding the scenes with their neighbors, blank areas, and variables into the sets list
            SceneSet setObject = new SceneSet(setName, setXHigh, setYHigh, setXLow, setYLow, extras, neighborList, takeList, blankAreaList );
            setsList.add(setObject);
        }
        parseOfficeOrTrailer(root.getElementsByTagName("trailer"));
        parseOfficeOrTrailer(root.getElementsByTagName("office"));
    }

    private void parseOfficeOrTrailer(NodeList nodeName){
        /*
        USAGE: This will parse the office/trailer since they are special cases of the sets
         */
        for (int i = 0; i < nodeName.getLength(); i++) {

            //read data from the nodes
            Node set = nodeName.item(i);
            String setName = set.getNodeName();
            //read data from children nodes
            NodeList children = set.getChildNodes();
            //neighbors list
            ArrayList<String> neighborList = new ArrayList<String>();
            // blanks list
            ArrayList<BlankArea> blankAreaList = new ArrayList<BlankArea>();
            // upgrades list for the office
            ArrayList<Upgrade> upgradeList = new ArrayList<Upgrade>();

            //the office/trailer position variables:
            int setXLow = 0;
            int setWidth = 0;
            int setXHigh = 0;
            int setYLow = 0;
            int setHeight = 0;
            int setYHigh = 0;

            for (int j = 0; j < children.getLength(); j++) {
                Node sub = children.item(j);
                if ("neighbors".equals(sub.getNodeName())) {
                    // adding in the neighbors
                    NodeList neighbors = sub.getChildNodes();
                    for (int k = 0; k < neighbors.getLength(); k++) {
                        Node neighbor = neighbors.item(k);
                        if ("neighbor".equals(neighbor.getNodeName())) {
                            neighborList.add(neighbor.getAttributes().getNamedItem("name").getNodeValue());
                        }
                    }
                } else if ("area".equals(sub.getNodeName())) {
                    // grabbing the x coordinates of the set
                    setXLow = Integer.parseInt(sub.getAttributes().getNamedItem("x").getNodeValue());
                    setWidth = Integer.parseInt(sub.getAttributes().getNamedItem("w").getNodeValue());
                    setXHigh = setXLow + setWidth;

                    // grabbing the y coordinates of the set
                    setYLow = Integer.parseInt(sub.getAttributes().getNamedItem("y").getNodeValue());
                    setHeight = Integer.parseInt(sub.getAttributes().getNamedItem("h").getNodeValue());
                    setYHigh = setYLow + setHeight;
                }else if("blanks".equals(sub.getNodeName())){
                    NodeList blanks = sub.getChildNodes();
                    for(int k = 0; k < blanks.getLength(); k++){
                        Node blank = blanks.item(k);
                        if("blank".equals(blank.getNodeName())){
                            int blankNumber = Integer.parseInt(blank.getAttributes().getNamedItem("number").getNodeValue());
                            // grabbing the blank coordinates -- height and width is 40 so we don't need to grab these.
                            int xBlank = Integer.parseInt(blank.getChildNodes().item(0).getAttributes().getNamedItem("x").getNodeValue());
                            int yBlank = Integer.parseInt(blank.getChildNodes().item(0).getAttributes().getNamedItem("y").getNodeValue());
                            //System.out.println("Set " + setName + " " + blankNumber + " " + xBlank + " " + yBlank);
                            BlankArea blankSpot = new BlankArea(xBlank, yBlank, blankNumber);
                            blankAreaList.add(blankSpot);
                        }
                    }
                }else if("upgrades".equals(sub.getNodeName())){
                    NodeList upgrades = sub.getChildNodes();
                    for(int k = 0; k < upgrades.getLength(); k++){
                        Node upgrade = upgrades.item(k);
                        if("upgrade".equals(upgrade.getNodeName())){
                            int upgradeRank = Integer.parseInt(upgrade.getAttributes().getNamedItem("level").getNodeValue());
                            String upgradeCurrency = upgrade.getAttributes().getNamedItem("currency").getNodeValue();
                            int upgradeCost = Integer.parseInt(upgrade.getAttributes().getNamedItem("amt").getNodeValue());

                            // getting the upgrade coordinates
                            int xUpgrade = Integer.parseInt(upgrade.getChildNodes().item(1).getAttributes().getNamedItem("x").getNodeValue());
                            int yUpgrade = Integer.parseInt(upgrade.getChildNodes().item(1).getAttributes().getNamedItem("y").getNodeValue());
                            // adding in the upgrades to the list
                            Upgrade upgradeObject = new Upgrade(xUpgrade, yUpgrade, upgradeCost, upgradeCurrency, upgradeRank);
                            upgradeList.add(upgradeObject);
                        }
                    }
                }
            }
            // initialize the office/trailer here
            if(setName.equals("trailer")){
                // init the trailer here
                setsList.add(new Trailer(setName, neighborList, blankAreaList, setXHigh, setYHigh, setXLow, setYLow));
            }else if(setName.equals("office")){
                // init the office here
                setsList.add(new CastingOffice(setName, neighborList, blankAreaList, setXHigh, setYHigh, setXLow, setYLow, upgradeList));
            }
        }
    }

    // getters for the cards list and the sets list
    public ArrayList<SceneCard> getAllSceneCards() {
        return allSceneCards;
    }
    public ArrayList<BaseSet> getSetsList() {
        return setsList;
    }
}
