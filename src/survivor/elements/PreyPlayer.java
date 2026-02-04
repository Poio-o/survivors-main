package survivor.elements;

import survivor.strategies.PreyStrategy;

public class PreyPlayer extends Player implements Prey{

    public PreyPlayer(int x, int y, int accel){
        super(new PreyStrategy(null), x, y, accel);
    }

    @Override
    public void setHunter(Element hunter) {
        ((PreyStrategy)this.movementStrategy).setHunter(hunter);
    }

    @Override
    public String toString() {
        return "P";
    }
}
 