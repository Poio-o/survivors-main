package survivor.strategies;

import survivor.elements.Element;
import survivor.position.Position;

public class HunterStrategy implements MovementStrategy {

    private Element prey;

    public HunterStrategy(Element prey) {
        this.prey = prey;
    }

    public void setPrey(Element prey) {
        this.prey = prey;
    }

    @Override
    public Position move(Position current) {
        int dx = (current.getX() >= 1) ? ((current.getX() > prey.getX()) ? -1 : 1) : 0;
        int dy = (current.getY() >= 1) ? ((current.getY() > prey.getY()) ? -1 : 1) : 0;

        return new Position(
                current.getX() + dx,
                current.getY() + dy);
    }
}
