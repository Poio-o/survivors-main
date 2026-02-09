package survivor;

import java.util.ArrayList;
import java.util.List;
import survivor.entities.Entity;
import survivor.entities.Hunter;
import survivor.entities.Obstacle;
import survivor.entities.Prey;

public class MapGrid {

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String BROWN = "\033[38;5;130m";

    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Prey> preys = new ArrayList<>();
    private List<Hunter> hunters = new ArrayList<>();

    private static final int[][] MOVES = {
            { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 },
            { -1, -1 }, { 1, -1 }, { -1, 1 }, { 1, 1 }
    };

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Prey> getPreys() {
        return preys;
    }

    public List<Hunter> getHunters() {
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
            Obstacle o = obstacles.get(i);
            if (o.getX() == x && o.getY() == y)
                return true;
        }

        for (int i = 0; i < hunters.size(); i++) {
            Hunter h = hunters.get(i);
            if (h.getX() == x && h.getY() == y)
                return true;
        }

        for (int i = 0; i < preys.size(); i++) {
            Prey p = preys.get(i);
            if (p.getX() == x && p.getY() == y)
                return true;
        }

        return false;
    }

    public int[] bestMove(Entity mover, int tx, int ty, boolean toward) {
        int bestX = mover.getX();
        int bestY = mover.getY();
        int bestScore = toward ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        boolean found = false;

        for (int[] MOVES1 : MOVES) {
            int nx = mover.getX() + MOVES1[0];
            int ny = mover.getY() + MOVES1[1];
            if (isBlocked(nx, ny))
                continue;
            int score = distance(nx, ny, tx, ty);
            if ((toward && score < bestScore) ||
                    (!toward && score > bestScore)) {
                bestScore = score;
                bestX = nx;
                bestY = ny;
                found = true;
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
            Prey p = preys.get(i);
            int d = distance(x, y, p.getX(), p.getY());
            if (d < bestDist) {
                bestDist = d;
                best = p;
            }
        }
        return best;
    }

    public Hunter getNearestHunter(int x, int y) {
        Hunter best = null;
        int bestDist = Integer.MAX_VALUE;

        for (int i = 0; i < hunters.size(); i++) {
            Hunter h = hunters.get(i);
            int d = distance(x, y, h.getX(), h.getY());
            if (d < bestDist) {
                bestDist = d;
                best = h;
            }
        }
        return best;
    }

    public void update() {

        // Hunters kill adjacent preys
        for (int i = 0; i < hunters.size(); i++) {
            Hunter h = hunters.get(i);
            for (int j = preys.size() - 1; j >= 0; j--) {
                Prey p = preys.get(j);
                if (distance(h.getX(), h.getY(), p.getX(), p.getY()) == 1) {
                    preys.remove(j);
                }
            }
        }

        // Hunters move
        for (int i = 0; i < hunters.size(); i++) {
            Hunter h = hunters.get(i);
            int[] move = h.chooseMove(this);
            if (move != null)
                h.move(move[0], move[1]);
        }

        // Preys move
        for (int i = 0; i < preys.size(); i++) {
            Prey p = preys.get(i);
            int[] move = p.chooseMove(this);
            if (move != null)
                p.move(move[0], move[1]);
        }
    }

    public void print() {
        System.out.println(
                "Total: " + (preys.size() + hunters.size()) + GREEN +
                        " | Preys: " + preys.size() + RED +
                        " | Hunters: " + hunters.size()+ RESET);

        String[][] grid = new String[GameConfig.HEIGHT][GameConfig.WIDTH];

        for (int y = 0; y < GameConfig.HEIGHT; y++) {
            for (int x = 0; x < GameConfig.WIDTH; x++) {
                grid[y][x] = " ";
            }
        }

        // Borders
        for (int x = 0; x < GameConfig.WIDTH; x++) {
            grid[0][x] = "*";
            grid[GameConfig.HEIGHT - 1][x] = "*";
        }
        for (int y = 0; y < GameConfig.HEIGHT; y++) {
            grid[y][0] = "*";
            grid[y][GameConfig.WIDTH - 1] = "*";
        }

        // Obstacles (brown)
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle o = obstacles.get(i);
            grid[o.getY()][o.getX()] = BROWN + o.getSymbol() + RESET;
        }

        // Preys (green)
        for (int i = 0; i < preys.size(); i++) {
            Prey p = preys.get(i);
            grid[p.getY()][p.getX()] = GREEN + p.getSymbol() + RESET;
        }

        // Hunters (red)
        for (int i = 0; i < hunters.size(); i++) {
            Hunter h = hunters.get(i);
            grid[h.getY()][h.getX()] = RED + h.getSymbol() + RESET;
        }

        for (int y = 0; y < GameConfig.HEIGHT; y++) {
            for (int x = 0; x < GameConfig.WIDTH; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }
}