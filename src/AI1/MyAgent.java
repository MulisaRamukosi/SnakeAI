package AI1;

import java.util.ArrayList;


public class MyAgent {

    public void initGame(String initString){
        String[] temp = initString.split(" ");
        int width = Integer.parseInt(temp[1]);
        int height = Integer.parseInt(temp[2]);
        GameBoard.setUpBoard(height, width);
    }

    public void setAppleCoord(String line){
        GameBoard.cleanUp();
        Apple.setAppleCoordinates(line);
    }

    public void addObstacle(String obs){
        GameBoard.drawObstacle(obs);
    }


    public void addMe(String snakeLine){
        String[] snakeDesc = snakeLine.split(" ");
        GameBoard.drawSnake(snakeDesc);
        ArrayList<Integer> xPoints = GameBoard.getX_points();
        ArrayList<Integer> yPoints = GameBoard.getY_points();
        SnakeManager.initMyCoordinates(snakeDesc, xPoints, yPoints);
    }

    public int getMove(){
        Timer.start();
        int move = SnakeManager.generateMove();
        Timer.end();

        if (Timer.getDuration() > 50L) log("/////////////////////////////////////////////RUNTIME: " + Timer.getDuration());

        return move;
    }

    public static void log(String message){
        System.out.println("Agent: " + message);
    }
}