package survivor.strategies;

import survivor.elements.Element;
import survivor.position.Position;
import static survivor.position.Position.distanceFrom;

public class PreyStrategy implements MovementStrategy {

    private Element hunter;

    public PreyStrategy(Element hunter) {
        this.hunter = hunter;
    }

    public void setHunter(Element hunter) {
        this.hunter = hunter;
    }

    @Override
    public Position move(Position current) {
        if (distanceFrom(hunter.getPosition(), current) > 5)
            return current;

        int dx = (current.getX() >= 1) ? ((current.getX() > hunter.getX()) ? 1 : -1) : 0;
        int dy = (current.getY() >= 1) ? ((current.getY() > hunter.getY()) ? 1 : -1) : 0;

        return new Position(
                current.getX() + dx,
                current.getY() + dy);
    }
}
