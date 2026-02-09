package survivor;

public class GameLoop {

    private final MapGrid map;

    public GameLoop(MapGrid map) {
        this.map = map;
    }

    public void start() throws InterruptedException {
        while (true) {
            clearConsole();
            map.update();
            map.print();
            Thread.sleep(GameConfig.SPEED);
        }
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
