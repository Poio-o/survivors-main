package survivor;


import static java.lang.Thread.sleep;
import java.util.ArrayList;
import survivor.board.Board;
import survivor.elements.Element;
import survivor.elements.EnemyPlayer;
import survivor.elements.Obstacle;
import survivor.elements.PreyPlayer;
import survivor.position.Position;

public class SurvivorGame {

    ArrayList<Element> elements;
    Board board;

    /**
     * Check if the position is occupied by another element
     * 
     * @param x
     * @param y
     * @return true if it is occupied, false otherwise
     */
    private boolean checkPosition(int x, int y) {
        boolean occupied = false;
        for (Element element : elements) {
            if (element.getX() == x && element.getY() == y) {
                occupied = true;
                break;
            }
        }
        return occupied;
    }

    private void obstaclesGenerator() {
        int area = board.getWidth() * board.getHeight();
        int obstacles = (int) (Math.random() * (area * 0.01) + 1);
        for (int i = 0; i < obstacles; i++) {
            int x, y;
            do {
                x = (int) (Math.random() * board.getWidth() + 1);
                y = (int) (Math.random() * board.getHeight() + 1);
            } while (checkPosition(x, y));
            elements.add(new Obstacle(x, y));
        }
    }

    private void huntersGenerator() {
        int area = board.getWidth() * board.getHeight();
        int hunters = (int) (Math.random() * (area * 0.005) + 1);
        for (int i = 0; i < hunters; i++) {
            int x, y;
            do {
                x = (int) (Math.random() * board.getWidth() + 1);
                y = (int) (Math.random() * board.getHeight() + 1);
            } while (checkPosition(x, y));
            elements.add(new EnemyPlayer(x, y, 1));
        }
    }

    private void preysGenerator() {
        int area = board.getWidth() * board.getHeight();
        int preys = (int) (Math.random() * (area * 0.005) + 1);
        for (int i = 0; i < preys; i++) {
            int x, y;
            do {
                x = (int) (Math.random() * board.getWidth() + 1);
                y = (int) (Math.random() * board.getHeight() + 1);
            } while (checkPosition(x, y));
            elements.add(new PreyPlayer(x, y, 1));
        }
    }

    private void playersGenerator() {
        huntersGenerator();
        preysGenerator();
    }

    private Element findElementByDistanceOfType(String targetClassName, Element from) {
        Element found = null;
        double distance = Double.MAX_VALUE;
        for (Element element : elements) {
            double currentDistance = Position.distanceFrom(from.getPosition(), element.getPosition());
            if (element != from && element.getClass().getName().equals(targetClassName) && distance > currentDistance) {
                found = element;
                distance = currentDistance;
            }
        }

        return found;
    }

    public SurvivorGame(int width, int height) {
        board = new Board(width, height);
        elements = new ArrayList<>();
    }

    public void launch() throws InterruptedException {
        obstaclesGenerator();
        playersGenerator();
        do {
            board.clear();
            for (Element element : elements) {
                switch (element.getClass().getName()) {
                    case "survivor.elements.PreyPlayer" -> ((PreyPlayer) element)
                            .setHunter(findElementByDistanceOfType(EnemyPlayer.class.getName(), element));
                    case "survivor.elements.EnemyPlayer" -> ((EnemyPlayer) element)
                            .setPrey(findElementByDistanceOfType(PreyPlayer.class.getName(), element));
                    default -> {
                    }
                }
            }
            for (Element element : elements) {
                element.move();
            }
            for (Element element : elements) {
                board.draw(element);
            }
            sleep(500);
        } while (true);
    }
}
