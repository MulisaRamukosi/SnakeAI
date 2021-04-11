package AI1;

import java.util.ArrayList;
import java.util.Random;

public class Snake {

    private String[] desc;
    private Point head;
    private Point tail;
    private int length;
    private ArrayList<Integer> xPoints, yPoints;
    private final ArrayList<Integer> newHx = new ArrayList<>();
    private final ArrayList<Integer> newHy = new ArrayList<>();
    private int ghostTrack = 0;

    public Snake(String[] desc, ArrayList<Integer> xPoints, ArrayList<Integer> yPoints){
        this.desc = desc;
        this.head = generatePoint(desc[3]);
        this.tail = generatePoint(desc[desc.length - 1]);
        this.length = Integer.parseInt(desc[1]);
        this.xPoints = xPoints;
        this.yPoints = yPoints;
    }

    private void reset(){
        newHx.clear();
        newHy.clear();
        ghostTrack = 0;
    }

    public synchronized void ghostOutHead(){
        while (!newHx.isEmpty()){
            int x = newHx.remove(0);
            int y = newHy.remove(0);
            GameBoard.markPosition(x, y);
        }
    }

    public synchronized void ghostOut(int n){
        if (n <= 1) return;

        ghostTrack = n;
        int i = 1;
        int l = xPoints.size();
        while (n > 0){
            if (l - i < 0) break;
            int currX = xPoints.get(l - i);
            int currY = yPoints.get(l - i);

            GameBoard.unMarkPosition(currX, currY);

            if(l - i - 1 >= 0) {
                tail.setX(xPoints.get(l - i - 1));
                tail.setY(yPoints.get(l - i - 1));
            }

            i++;
            n--;
        }
    }

    public synchronized void ghostIn(){
        ghostOutHead();
        int i = 1;
        int l = xPoints.size();
        while (ghostTrack >= 0){
            if (l - i < 0) break;
            int currX = xPoints.get(l - i);
            int currY = yPoints.get(l - i);

            GameBoard.markPosition(currX, currY);
            i++;
            ghostTrack--;
        }
        ghostTrack = 0;
        this.head = generatePoint(desc[3]);
        this.tail = generatePoint(desc[desc.length - 1]);
    }

    public Point[] getSnakePolygon(){
        Point[] snake = new Point[desc.length - 3];

        for (int i = 4; i < desc.length; i++){
            snake[i - 4] = generatePoint(desc[i - 1]);
            snake[i - 3] = generatePoint(desc[i]);
        }
        return snake;
    }

    public void updateCoordinates(String[] desc, ArrayList<Integer> xPoints, ArrayList<Integer> yPoints){
        Point newHead = generatePoint(desc[3]);
        if (Algorithms.distance(this.head, newHead) != 1){
            MyAgent.log("I died");
        }

        this.desc = desc;
        this.head = generatePoint(desc[3]);
        this.tail = generatePoint(desc[desc.length - 1]);
        this.length = Integer.parseInt(desc[1]);
        this.xPoints = xPoints;
        this.yPoints = yPoints;
    }

    public int getLength() {
        return length;
    }

    public Point getHead() {
        return head;
    }

    public Point getRealTail(){
        return tail;
    }

    public Point getTail() {
        int farDistance = Integer.MIN_VALUE;
        Point safeTail = null;

        for (Point p : tail.getChildren()){
            if (p == null) continue;
            int distance = Algorithms.trueDistanceEstimate(SnakeManager.getMyHead(), p);
            if (distance > farDistance){
                safeTail = p;
            }
        }

        if (safeTail == null) return tail;
        return safeTail;
    }

    private Point generatePoint(String point){
        String[] xy = point.split(",");
        int x = Integer.parseInt(xy[0]);
        int y = Integer.parseInt(xy[1]);
        return new Point(x, y);
    }

    public int maxStall(){
        Point tailPoint = getTail();

        if (tailPoint.equals(tail)) GameBoard.unMarkPosition(tailPoint.getX(), tailPoint.getY());
        ArrayList<Point> maxStallPath = Algorithms.maxStall(head, tailPoint);
        if (tailPoint.equals(tail)) GameBoard.markPosition(tailPoint.getX(), tailPoint.getY());

        if (!maxStallPath.isEmpty()){
            MyAgent.log("GO WITH MAX STALL, Max Stall found");
            return generateIntMove(maxStallPath.remove(maxStallPath.size() - 1));
        }

        MyAgent.log("MAX FAILED, GO STRAIGHT, IT WAS THE END");
        return 5;
    }

    public int generateMove() {
        reset();
        return pathToApple();
    }

    private int pathToApple() {
        ArrayList<Point> pathToApple = Algorithms.generatePathToGoal(head, Apple.getAppleCoordinates());

        if (pathToApple.isEmpty()) return maxStall();

        for (Point p : pathToApple) GameBoard.markPosition(p.getX(), p.getY());

        Point tailPoint = getTail();
        ghostOut(pathToApple.size());

        if (tailPoint.equals(tail)) GameBoard.unMarkPosition(tailPoint.getX(), tailPoint.getY());
        ArrayList<Point> pathToTail = Algorithms.generatePathToGoal(Apple.getAppleCoordinates(), tailPoint);
        if (tailPoint.equals(tail)) GameBoard.markPosition(tailPoint.getX(), tailPoint.getY());


        for (Point p : pathToApple) GameBoard.unMarkPosition(p.getX(), p.getY());

        if (pathToTail.size() < 2) {
            //remark snake to clear up ghosted out paths
            GameBoard.drawSnake(desc);
            return maxStall();
        }

        return generateIntMove(pathToApple.remove(pathToApple.size() - 1));
    }


    private int generateIntMove(Point point) {
        if (head.getX() == point.getX()) return point.getY() > head.getY() ? 1 : 0;
        else if (head.getY() == head.getY()) return point.getX() > head.getX() ? 3 : 2;
        return 5;
    }
}
