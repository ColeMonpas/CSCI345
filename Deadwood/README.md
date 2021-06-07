Cole Monpas, Tom Clewes

## Game Setup:
+ Once you click run, it will prompt the user to enter the amount of players who wish to play, just enter a valid number and the game should start up.
+ If you want to move to a blank spot make sure you click on the top of the card, as you might accidentally move onto a role. 

## Design Patterns:
+ Observer Pattern
    * Model, ModelObserver -> BoardView
+ Model View Controller Pattern
    * DeadwoodModel, Controller, BoardView
+ Abstract Factory Pattern
    * BaseSet
        - SceneSet
        - Trailer
        - CastingOffice
    
## UML Change Log:
+ New classes: 
    * BaseSet
    * BlankArea
    * CastingOffice
    * Location
    * ParseXML
    * SceneCard
    * SceneSet
    * Take
    * Trailer
    * Upgrade
+ Deleted Classes:
    * Button