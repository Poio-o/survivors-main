package survivor.elements;

import survivor.strategies.MovementStrategy;

public abstract class Player extends Element{
    protected Player(MovementStrategy movementStrategy, int x, int y, int accel){
        super(movementStrategy, x, y, accel);
    }

    
}
  