package GAME;

public class GamePoint {

    private int x, y;

    public GamePoint(int hx, int hy) {
        x = hx;
        y = hy;
    }

    @Override
    public String toString() {
        return x+","+y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof GamePoint) && ((GamePoint) obj).getX() == x && ((GamePoint) obj).getY() == y;
    }
}
