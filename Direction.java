package pacman;

public enum Direction {

    INITIAL, UP, DOWN, LEFT, RIGHT;

    public int getRowOffset(){
       switch(this){
            case UP: return -Constants.MOVE_OFFSET;
            case DOWN: return Constants.MOVE_OFFSET;
            case LEFT: return 0;
            case RIGHT: return 0;
            default: return 0;
        }
    }

    public int getColOffset(){
        switch(this){
            case UP: return 0;
            case DOWN: return 0;
            case LEFT: return -Constants.MOVE_OFFSET;
            case RIGHT: return Constants.MOVE_OFFSET;
            default: return 0;
        }
    }

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
