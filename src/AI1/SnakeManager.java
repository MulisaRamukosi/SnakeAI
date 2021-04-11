package AI1;

import java.util.ArrayList;

public class SnakeManager {

    private static Snake mySnake = null;

    public static void initMyCoordinates(String[] desc, ArrayList<Integer> xPoints, ArrayList<Integer> yPoints){
        if (mySnake == null) mySnake = new Snake(desc, xPoints, yPoints);
        else mySnake.updateCoordinates(desc, xPoints, yPoints);
    }

    public static int generateMove(){
        return mySnake.generateMove();
    }

    public static Point getMyRealTail(){
        return mySnake.getRealTail();
    }

    public static Point getMyHead(){
        return mySnake.getHead();
    }

    public static int getMyLength(){
        return mySnake.getLength();
    }

    public static void ghostMeOut(int n){
        mySnake.ghostOut(n);
    }

    public static void ghostMeIn(){
        mySnake.ghostIn();
    }

    public static Point[] getSnakePolygon(){
        return mySnake.getSnakePolygon();
    }

}
