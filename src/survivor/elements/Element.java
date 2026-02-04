package survivor.elements;

import survivor.position.Position;
import survivor.strategies.Movement;
import survivor.strategies.MovementStrategy;

public abstract class Element implements Movement{
    Position position;
    protected int accel;
    protected MovementStrategy movementStrategy;


    protected Element(MovementStrategy movementStrategy, int x, int y, int accel){
        this.movementStrategy = movementStrategy;
        this.position = new Position(x, y);
        this.accel = accel;
    }

    public int getX() {
        return position.getX();
    }
    
    public int getY() {
        return position.getY(); 
    }   


    public Position getPosition() {
        return position;
    }

    @Override
    public void move() {
        this.position = this.movementStrategy.move(this.position);
    }
    
}
