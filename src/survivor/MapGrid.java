package survivor;

import java.util.ArrayList;
import survivor.entities.Entity;
import survivor.entities.Hunter;
import survivor.entities.Obstacle;
import survivor.entities.Prey;

public class MapGrid {

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String BROWN = "\033[38;5;130m";

    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
    private final ArrayList<Prey> preys = new ArrayList<>();
    private final ArrayList<Hunter> hunters = new ArrayList<>();

    private static final int[][] MOVES = {
            { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 },
            { -1, -1 }, { 1, -1 }, { -1, 1 }, { 1, 1 }
    };

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public ArrayList<Prey> getPreys() {
        return preys;
    }

    public ArrayList<Hunter> getHunters() {
        return hunters;
    }

    public int distance(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        return dx > dy ? dx : dy;
    }

    public boolean isBlocked(int x, int y) {
        if (x <= 0 || y <= 0 ||
                x >= GameConfig.WIDTH - 1 ||
                y >= GameConfig.HEIGHT - 1)
            return true;

        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if (obstacle.getX() == x && obstacle.getY() == y)
                return true;
        }

        for (int i = 0; i < hunters.size(); i++) {
            Hunter hunter = hunters.get(i);
            if (hunter.getX() == x && hunter.getY() == y)
                return true;
        }

        for (int i = 0; i < preys.size(); i++) {
            Prey prey = preys.get(i);
            if (prey.getX() == x && prey.getY() == y)
                return true;
        }

        return false;
    }

    public int[] bestMove(Entity mover, int tx, int ty, boolean front) {
        int bestX = mover.getX();
        int bestY = mover.getY();
        int bestScore = front ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        boolean found = false;

        for (int[] MOVES1 : MOVES) {
            int newX = mover.getX() + MOVES1[0];
            int newY = mover.getY() + MOVES1[1];
            if (isBlocked(newX, newY))
                continue;
            int score = distance(newX, newY, tx, ty);

            if ((front && score < bestScore) || (!front && score > bestScore)) {
                bestScore = score;
                bestX = newX;
                bestY = newY;
                found = true;
            } else if (score == bestScore) {
                if (Math.random() < 0.5) {
                    bestX = newX;
                    bestY = newY;
                    found = true;
                }
            }
        }

        if (!found)
            return null;
        return new int[] { bestX, bestY };
    }

    public Prey getNearestPrey(int x, int y) {
        Prey best = null;
        int bestDist = Integer.MAX_VALUE;

        for (int i = 0; i < preys.size(); i++) {
            Prey prey = preys.get(i);
            int actDist = distance(x, y, prey.getX(), prey.getY());
            if (actDist < bestDist) {
                bestDist = actDist;
                best = prey;
            }
        }
        return best;
    }

    public Hunter getNearestHunter(int x, int y) {
        Hunter best = null;
        int bestDist = Integer.MAX_VALUE;

        for (int i = 0; i < hunters.size(); i++) {
            Hunter hunter = hunters.get(i);
            int actDist = distance(x, y, hunter.getX(), hunter.getY());
            if (actDist < bestDist) {
                bestDist = actDist;
                best = hunter;
            }
        }
        return best;
    }

    public void update() {

        for (int i = 0; i < hunters.size(); i++) {
            Hunter hunter = hunters.get(i);

            for (int j = preys.size() - 1; j >= 0; j--) {
                Prey prey = preys.get(j);

                int dx = Math.abs(hunter.getX() - prey.getX());
                int dy = Math.abs(hunter.getY() - prey.getY());

                if ((dx == 1 && dy == 0) ||
                        (dx == 0 && dy == 1)) {

                    int hunterVit = hunter.getVitality();
                    int preyVit = prey.getVitality();
                    double chanceHunter = (double) hunterVit /
                            (hunterVit + preyVit);

                    double roll = Math.random();

                    if (roll < chanceHunter) {
                        preys.remove(j);
                    } else {
                        hunters.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < hunters.size(); i++) {
            Hunter hunter = hunters.get(i);
            int[] move = hunter.chooseMove(this);
            if (move != null)
                hunter.move(move[0], move[1]);
        }

        for (int i = 0; i < preys.size(); i++) {
            Prey prey = preys.get(i);
            int[] move = prey.chooseMove(this);
            if (move != null)
                prey.move(move[0], move[1]);
        }

    }

    public void print() {
        System.out.println(
                "Total: " + (preys.size() + hunters.size()) + GREEN +
                        " | Preys: " + preys.size() + RED +
                        " | Hunters: " + hunters.size() + RESET+"  ");

        String[][] grid = new String[GameConfig.HEIGHT][GameConfig.WIDTH];

        for (int y = 0; y < GameConfig.HEIGHT; y++) {
            for (int x = 0; x < GameConfig.WIDTH; x++) {
                grid[y][x] = " ";
            }
        }

        for (int x = 0; x < GameConfig.WIDTH; x++) {
            grid[0][x] = "*";
            grid[GameConfig.HEIGHT - 1][x] = "*";
        }
        for (int y = 0; y < GameConfig.HEIGHT; y++) {
            grid[y][0] = "*";
            grid[y][GameConfig.WIDTH - 1] = "*";
        }

        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            grid[obstacle.getY()][obstacle.getX()] = BROWN + obstacle.getSymbol() + RESET;
        }

        for (int i = 0; i < preys.size(); i++) {
            Prey prey = preys.get(i);
            grid[prey.getY()][prey.getX()] = GREEN + prey.getSymbol() + RESET;
        }

        for (int i = 0; i < hunters.size(); i++) {
            Hunter hunter = hunters.get(i);
            grid[hunter.getY()][hunter.getX()] = RED + hunter.getSymbol() + RESET;
        }

        for (int y = 0; y < GameConfig.HEIGHT; y++) {
            for (int x = 0; x < GameConfig.WIDTH; x++) {
                System.out.print(grid[y][x] + " ");
            }
            System.out.println();
        }
    }

    private void showVictory(String message, String color) {
        System.out.print("\033[H");
        System.out.println("\n\n");
        System.out.println(color);
        System.out.println("#################################");
        System.out.println("########   " + message + "   ########");
        System.out.println("#################################");
        System.out.println(RESET);
    }

    public boolean checkVictory() {

        if (hunters.isEmpty() && !preys.isEmpty()) {
            showVictory(" PREYS WIN ", GREEN);
            return true;
        }

        if (preys.isEmpty() && !hunters.isEmpty()) {
            showVictory("HUNTERS WIN", RED);
            return true;
        }

        return false;
    }

}