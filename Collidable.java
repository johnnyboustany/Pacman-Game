package pacman;

/**
 *  The Collidable interface is implemented by
 *  dots, energizers, and ghosts. All these objects
 *  define their collide and getType methods. The collide
 *  method dictates what happens when they collide with pacman
 *  and getType is used to know the type of the collidable
 *  (dot, energizer or type of ghost).
 *
 */
public interface Collidable {
    void collide(); // each object that implements this interface defines their own collide method

    // this method is used to determine if an object that implements this interface is a dot or energizer or if it is
    // a specific type of ghost (blinky, pinky, inky or clyde).
    String getType();
}