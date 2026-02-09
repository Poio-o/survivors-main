package survivor.entities;

import survivor.MapGrid;

public class Hunter extends Entity {

    public Hunter(int x, int y) {
        super(x, y, 'H');
    }

    public int[] chooseMove(MapGrid map) {
        Prey target = map.getNearestPrey(x, y);
        if (target == null) return null;

        return map.bestMove(this, target.getX(), target.getY(), true);
    }
}
