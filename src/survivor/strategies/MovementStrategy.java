package survivor.strategies;

import survivor.position.Position;

public interface MovementStrategy {
    Position move(Position currentPosition);
}
