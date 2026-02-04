package survivor.strategies;

import survivor.position.Position;

public class StandStrategy implements MovementStrategy {

    @Override
    public Position move(Position currentPosition) {
        return currentPosition;
    }
}
