package survivor.entities;

import survivor.GameConfig;
import survivor.MapGrid;

public class Prey extends Entity {

    public Prey(int x, int y) {
        super(x, y, 'P');
    }

    public int[] chooseMove(MapGrid map) {
        Hunter target = map.getNearestHunter(x, y);
        if (target == null)
            return null;

        int dist = map.distance(x, y, target.getX(), target.getY());
        if (dist > GameConfig.PREY_PERCEPTION)
            return null;

        return map.bestMove(this, target.getX(), target.getY(), false);
    }
}
