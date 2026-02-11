package survivor;

public class GameLoop {

    private final MapGrid map;

    public GameLoop(MapGrid map) {
        this.map = map;
    }

    private void clearScreen() {
        System.out.print("\033[2J");
        System.out.print("\033[H");
        System.out.flush();
    }

    public void start() throws InterruptedException {
        while (true) {
            System.out.print("\033[H");
            map.update();
            map.print();
            if (map.checkVictory()) {
                clearScreen();
                map.checkVictory();
                break;
            }
            Thread.sleep(GameConfig.SPEED);
        }
    }

}
