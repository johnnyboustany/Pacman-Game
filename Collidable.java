package pacman;

/**
 *  The Collidable interface is implemented by
 *  dots, energizers, and ghosts. All these objects
 *  define their collide and getType methods. The collide
 *  method dictates what happens when they collide with pacman
 *  and getType is used to know the type of the collidable
 *  (dot, energizer or ghost).
 *
 */
public interface Collidable {
    void collide();
    String getType();
}
