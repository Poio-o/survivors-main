package survivor.elements;

import survivor.strategies.HunterStrategy;

public class EnemyPlayer extends Player implements Enemy{
    public EnemyPlayer(int x, int y, int accel){
        super(new HunterStrategy(null), x, y, accel);
    }

    @Override
    public void setPrey(Element prey) {
        ((HunterStrategy)this.movementStrategy).setPrey(prey);
    }

    @Override
    public String toString() {
        return "H";
    } 
}
