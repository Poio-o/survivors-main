package survivor;

import survivor.entities.Hunter;
import survivor.entities.Obstacle;
import survivor.entities.Prey;

public class Main {

    private static int randomInt(int min, int max) {
        return min + (int)(Math.random() * (max - min + 1));
    }

    public static void main(String[] args) throws Exception {

        MapGrid map = new MapGrid();

        for (int i = 0; i < GameConfig.OBSTACLES; i++) {
            while (true) {
                int x = randomInt(1, GameConfig.WIDTH - 2);
                int y = randomInt(1, GameConfig.HEIGHT - 2);

                if (!map.isBlocked(x, y)) {
                    map.getObstacles().add(new Obstacle(x, y));
                    break;
                }
            }
        }

        for (int i = 0; i < GameConfig.PREYS; i++) {
            while (true) {
                int x = randomInt(1, GameConfig.WIDTH - 2);
                int y = randomInt(1, GameConfig.HEIGHT - 2);

                if (!map.isBlocked(x, y)) {
                    map.getPreys().add(new Prey(x, y));
                    break;
                }
            }
        }

        for (int i = 0; i < GameConfig.HUNTERS; i++) {
            while (true) {
                int x = randomInt(1, GameConfig.WIDTH - 2);
                int y = randomInt(1, GameConfig.HEIGHT - 2);

                if (!map.isBlocked(x, y)) {
                    map.getHunters().add(new Hunter(x, y));
                    break;
                }
            }
        }

        new GameLoop(map).start();
    }
}
