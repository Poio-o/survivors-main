package survivor.entities;

public abstract class Entity {
    protected int x;
    protected int y;
    protected char symbol;
    protected int vitality;

    public Entity(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.vitality = (int) (Math.random() * 10) + 1;
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

    public int getVitality() {
        return vitality;
    }

    public void move(int newX, int newY) {
        x = newX;
        y = newY;
    }
}
