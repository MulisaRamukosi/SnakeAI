package AI1;

public class Point implements Cloneable{

    private int x, y;
    private int g = 0, h = 0, posInLink = 0;
    private Point parent = null;
    private Point[] children = new Point[4];

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPosInLink() {
        return posInLink;
    }

    public void setPosInLink(int posInLink) {
        this.posInLink = posInLink;
    }

    //cost to get there
    public void setG(int g) {
        this.g = g;
    }

    //cost to goal
    public void setH(int h) {
        this.h = h;
    }

    public int getH() {
        return h;
    }

    public int getG() {
        return g;
    }

    public int getF(){
        return h + g;
    }

    public void setParent(Point parent) {
        this.parent = parent;
    }

    public Point getParent() {
        return parent;
    }

    public Point[] getChildren() {
        children[0] = GameBoard.getPoint(x + 1, y);
        children[1] = GameBoard.getPoint(x - 1, y);
        children[2] = GameBoard.getPoint(x, y + 1);
        children[3] = GameBoard.getPoint(x, y - 1);
        return children;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Point) && (((Point) obj).getX() == x && ((Point) obj).getY() == y);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
