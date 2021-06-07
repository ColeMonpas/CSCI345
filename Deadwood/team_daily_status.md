# Team Daily Status

## Day 0
Have compliactions that will inhibit my working time on friday so decided to use today (5/22) to make changes.
As is copy and pasted from my commit message from today: 

Biggest changes:
 added a location class that will handle all of the times whe want to get a info from the board
 Added alot of implementation to the model class
 Added different vars to the Roles, Scenes, Players class
 Added changes to the controller class to account for x and y value inputs
 Added some changes to the view

 Biggest Hurdles:
 Still need to work on how we are going to update the view board
 How we are going to utilize a button class, might be omitted
 And How are we going to parse through the xml file
 And how we are going to use and treat x and y inputs in location

Good progress was made , did an estimated 4 -5 hours of work. I expect to see about 10-15 more hours of work or less till we are completed

## Day 1
We did peer programming today

 We started implementing parseXML to get the extras roles from the cards.XML file

 We made a plan of how we are going to store and extract information from the xml files
 
We also implemented a small amount of the model functions as well



## Day 2
We both did some brief peer programming early on to establish who is going to do what coding. 
And along with that we also set a plan of attack for the rest of the data extraction by creating a set
Class that will hold all of the information related to : Scenes, Roles, Takes, Coordinates etc..

Cole: Implemented the board.xml parsing for the parseXML file and added the takes class. All of these classes 
listed above where linked together in the Scene class. Also edited the Role class so that each role no longer needed to remember which set it was in. 

Tom: Added in More functionality to the deadwood model, added in some input parsing in the controller
Added comments to the code and fixed issues related to previously untouched with changes. added in the Casting office class
which will store the casting office information once that is parsed from the xml file
And added in Todo://s on all of the necessary places.

Solid progress was made today, more might be made tonight pending studying for finals. 

Todo's within the next next, add in the cards.xml parsing and link the 10 scenes to the board,
and work on getting the view to displlay the cards and user information with the given update info buttons

futre issues to be worked through, display casting office stuff.


## Day 3
Cole: Completed parsing with peer programming for the cards
xml and solo programming for board and portions of card

Tom: Added more functionality into the Deadwood model, also added in inheritance into the set information 
with guidance from shri

Fixed bugs and added in more functionality into the model

## Day 4
From Day 4 on almost all work was peer programming over discord and the live share option on intellij

Worked on the view and made changes to allow it to display players, and flip cards. Connected the view to the model
Not working properly, but a good start to base for tomorrow

## Day 5

added in the takes counter, works great, players can now rehearse and act, 
still needs more work on future rehearse and act responses. 
fixed issue with extra dice being made when players move

## Day 6

Players can now rehearse, act, end scene functionality works, moving to an open space works, moving from open space to a role on the same set works.
added in day ending functionality, it currently doesn't work, also fixed minor issues with working as an extra and scenes finishing.


## Day 7

Fixed all the bugs in the game from previous day and added in end scene and move panel notifications

Next to do would be add in end game, upgrade rank, more move notifications and bug check

## Day 8

Final version is completed added in upgrade office and end game popup, added in comments and "smoothed" out the code better.

last steps are to update uml, update milestone3, and do a final run through of code

