# Pacman-Game
README

Overview:
    The App class instantiates the PaneOrganizer, which in turn
    instantiates the BorderPane. It also instantiates a vanilla Pane (boardPane)
    and an HBox, and  respectively sets them to the top and bottom of the BorderPane. The button
    added to the HBox is associated with a QuitHandler, allowing the user to exit from the game.
    The Sidebar class is instantiated in the PaneOrganizer and is used to display the score and lives
    labels. The top-level logic class Game is also instantiated and boardPane is passed in.
    This allows the Game class to graphically add various nodes to the boardPane.

    In the Game constructor, it instantiates a 2D Array of MazeSquares that represents the board.
    Also, it sets up the board with the pacman, 4 ghosts, dots and energizers. Three of the ghosts
    start out in the GhostPen and are gradually released both from the queue in the GhostPen class and also
    graphically.

    Game has a TimeHandler private class which controls the switching between chase and scatter mode,
    moves the pacman and ghosts, checks for collisions and updates the score and lives labels. It also
    is in charge of setting the mode to frightened mode when pacman eats an energizer. Moreover, it checks
     whether the game is over (in that case, it stops the timeline and displays a Game is Over label).

    Game also has a KeyHandler private class which allows the user to choose the direction that pacman
    moves in through using the LEFT, RIGHT, UP and DOWN keys. It only updates the direction instance variable
    of pacman/each ghost if the move is valid. The TimeHandler passes the direction instance variable
    into the move method.

    Chase and scatter mode are achieved through a Breadth first search algorithm that checks all neighbors of the ghost
    and adds valid neighbours to a queue and updates a 2D array of Direction. While the queue isn't empty, a BoardCoordinate
    from the front is removed and gets its direction marked in the 2D Directions array. It uses the distance formula
    to calculate the distance between the current and target squares, and it stores the closest square. In the end,
    the direction of the closest square is returned and is passed into the move method.

    Frightened mode is achieved through checking the neighbors of the ghost and adding valid directions to an arraylist.
    A random number that is less than the size of the arraylist is generated and is used to pick a valid direction. This
    direction then gets passed into the move method.

Design Choices:
    Polymorphism is taken full advantage of as each MazeSquare contains an arraylist of type Collidable
    and the Game class loops through the arraylist of each square that pacman is currently located on.
    The Collidable interface is implemented by dots, energizers and ghosts, which all define their collide()
    and getType() methods. All these 3 classes are associated with Game and are passed the instance of game in
    their constructor. Hence, they can call helper methods from Game that allow them to increment the score among
    other things. The ghost collide method increments the score and adds the ghost to the pen when the game is in frightened
    mode. It calls the killPacman() method which either resets or ends the game depending on the number of remaining lives
    pacman has.

    The getType() method that is defined by all three objects that implement Collidable returns a string
    that characterizes the object. It has 2 purposes:
     1- It allows Game to check whether a given MazeSquare's arraylist contains a dot or energizer by checking
     whether the object's getType() method returns "dot" or "energizer." This is used in a helper method in Game
     that checks every MazeSquare on the board for dots or energizers, in order to know when the board no longer has
     any dots/energizers (and, hence, when the game should end).
         Side Note:
         Although it might be seen as inefficient to use for loops to check for if the game is over, it's neat and compact
         (unlike the approach of using counters to determine when all dots/energizers have been removed). So, there are
         tradeoffs for both design choices and it depends on the programmer's priorities
         (minimizing the number of operations executed vs. cleanliness and compactness of code).

     2- It allows the GhostPen to set each ghost to a certain position inside the pen according to their type.
     It checks whether getType() returns "blinky," "inky," clyde," or "pinky."

    The GhostPen uses a counter to know when to release a ghost graphically and from the queue. It increments the
    counter whenever the queue isn't empty. In frightened mode, the collide method from the Ghost class adds its
    ghost to the pen. Pinky, Inky and Clyde are added back to the pen whenever the game is reset (when pacman loses a life).

    When pacman is out of lives, the killPacman() method that is called from the collide method of the Ghost class brings
    pacman and all ghosts back to their starting positions (using endGame()) and displays the game is over label.
    However, the endGame() method is still called in the TimeHandler handle method for all cases of the game being over
    (including for when pacman is out of lives). Although this seems redundant, it makes sure that there is no delay
    and that pacman and the ghosts are all in their places and don't move at all when the game ends.
    Nevertheless, the game is over label is only displayed for the case of no more
    dots/energizers as the label is already being displayed for the case of pacman losing lives in the killPacman() method.

Additional Feature: Two seconds before frightened mode is over, the ghosts blink  (turn white). This is achieved in the
setMode() method of the Game class and is done by checking whether the frightened counter is 2 seconds away from
frightened mode ending and acting accordingly (changing the color to white).
