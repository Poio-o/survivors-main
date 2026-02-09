package survivor.entities;

public abstract class Entity {
    protected int x;
    protected int y;
    protected char symbol;

    public Entity(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getSymbol() {
        return symbol;
    }

    public void move(int nx, int ny) {
        x = nx;
        y = ny;
    }
}
