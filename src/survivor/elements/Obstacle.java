package survivor.elements;

import survivor.strategies.StandStrategy;

public class Obstacle extends Element {

    public Obstacle(int x, int y) {
        super(new StandStrategy(), x, y, 0);        
    }
    

    @Override
    public String toString() {
        return "*";
    }
    
}
 