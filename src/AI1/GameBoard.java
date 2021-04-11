package AI1;

import java.util.ArrayList;

public class GameBoard {
    private static int[][] board;
    private static int height, width;
    private static ArrayList<Line> lines;
    private static ArrayList<Integer> x_points;
    private static ArrayList<Integer> y_points;

    public static void setUpBoard(int height, int width){
        board = new int[height][width];
        GameBoard.height = height;
        GameBoard.width = width;
        lines = new ArrayList<>();
    }

    public static void cleanUp(){
        lines.clear();

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                board[i][j] = 0;
            }
        }
    }

    public static void drawObstacle(String obs) {
        String[] obsDesc = obs.split(" ");
        for (int i = 1; i < obsDesc.length; i++) {
            lines.add(new Line(genPoint(obsDesc[i-1]), genPoint(obsDesc[i])));
            drawLine(obsDesc[i - 1], obsDesc[i], null, null);
        }
    }

    public static ArrayList<Line> getLines(){
        return lines;
    }

    public static Point genPoint(String points){
        String[] PXY = points.split(",");
        return new Point(Integer.parseInt(PXY[0]), Integer.parseInt(PXY[1]));
    }

    public static void drawSnake(String[] snakeDesc) {
        x_points = new ArrayList<>();
        y_points = new ArrayList<>();

        for(int i = 4; i < snakeDesc.length; i++){
            lines.add(new Line(genPoint(snakeDesc[i-1]), genPoint(snakeDesc[i])));
            drawLine(snakeDesc[i-1], snakeDesc[i], x_points, y_points);
        }
    }

    public static ArrayList<Integer> getX_points(){
        return x_points;
    }

    public static ArrayList<Integer> getY_points(){
        return y_points;
    }

    public static int[][] generateEmptyBoard(){
        return new int[height][width];
    }

    public static void drawLine(String firstNick, String secondNick, ArrayList<Integer> x_points, ArrayList<Integer> y_points) {
        String[] firstNicks = firstNick.split(",");
        String[] secondNicks = secondNick.split(",");

        int x0 = Integer.parseInt(firstNicks[0]);
        int y0 = Integer.parseInt(firstNicks[1]);
        int x1 = Integer.parseInt(secondNicks[0]);
        int y1 = Integer.parseInt(secondNicks[1]);

        if (x0 == x1){
            int start = Math.min(y0, y1);
            int end = Math.max(y0, y1);

            for (int i = start; i <= end; i++){
                board[i][x0] = 1;
                if (x_points != null){
                    y_points.add(i);
                    x_points.add(x0);
                }
            }
        }
        else if (y0 == y1){
            int start = Math.min(x0, x1);
            int end = Math.max(x0, x1);

            for (int i = start; i <= end; i++){
                board[y0][i] = 1;
                if (x_points != null){
                    y_points.add(y0);
                    x_points.add(i);
                }
            }
        }
    }

    public static Point getPoint(int x, int y){
        return (x >= 0 && x < width && y >= 0 && y < height && board[y][x] == 0) ? new Point(x, y) : null;
    }

    public static void markPosition(int x, int y){
        board[y][x] = 1;
    }

    public static void unMarkPosition(int x, int y){
        board[y][x] = 0;
    }
}
