package pacman;

public enum Direction {

    NULL, UP, DOWN, LEFT, RIGHT;

    public Direction opposite(){
        switch(this){
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return null;
        }
    }

}
