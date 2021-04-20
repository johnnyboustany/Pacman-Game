package pacman;

public enum Mode {

    SCATTER, CHASE, FRIGHTENED;

    public Mode opposite(){
        switch(this){
            case CHASE: return SCATTER;
            case SCATTER: return CHASE;
            default: return FRIGHTENED;
        }
    }
}
