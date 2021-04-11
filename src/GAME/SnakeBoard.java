package GAME;

import AI1.SnakeAgent0;

public class SnakeBoard {

    private final int[][] board = new int[50][50];
    private final GameApple apple = new GameApple();

    private final String obs0 = "30,21 29,21 28,21 27,21 26,21";
    private final String obs1 = "16,32 16,33 16,34 16,35 16,36";
    private final String obs2 = "47,26 46,26 45,26 44,26 43,26";

    private final SnakeAgent0 snakeAi;

    public SnakeBoard(){
        String initString = "4 50 50 1";
        snakeAi = new SnakeAgent0(board, initString, Constants.SNAKE_0);
    }

    public void updateBoard(){
        addObs();
        updateSnakeData();
        clearBoard();
        addObs();
        apple.checkForPossibleUpdate();



        snakeAi.setAlreadyUpdate(false);
        drawSnake(snakeAi.getSnakeDesc(), snakeAi.getSnakeNum());

        int ax = apple.getX();
        int ay = apple.getY();


        while (board[ay][ax] != 0){
            apple.generateNewPoints();
            ax = apple.getX();
            ay = apple.getY();
        }

        board[ay][ax] = Constants.APPLE;
    }

    private void updateSnakeData(){
        snakeAi.setAppleCoord(apple.toString());
        snakeAi.addObstacle(obs0, obs1, obs2);
        snakeAi.addMe(snakeAi.genSnakeDescription());
        snakeAi.move(board, apple, true);
    }

    public void addObs(){
        drawObstacle(obs0);
        drawObstacle(obs1);
        drawObstacle(obs2);
    }

    public int[][] getBoard(){
        return board;
    }

    public void clearBoard(){
        for (int i = 0; i < 50; i++){
            for (int j = 0; j < 50; j++){
                board[i][j] = 0;
            }
        }
    }

    public void drawSnake(String[] snakeDesc, int snakeNum) {
        for(int i = 4; i < snakeDesc.length; i++){
            drawLine(snakeDesc[i-1], snakeDesc[i], snakeNum);
        }
    }

    public void drawObstacle(String obs) {
        String[] obsDesc = obs.split(" ");
        for (int i = 1; i < obsDesc.length; i++) {
            drawLine(obsDesc[i - 1], obsDesc[i], Constants.OBSTACLE);
        }
    }

    public void drawLine(String firstNick, String secondNick, int c) {
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
                board[i][x0] = c;
            }
        }
        else if (y0 == y1){
            int start = Math.min(x0, x1);
            int end = Math.max(x0, x1);

            for (int i = start; i <= end; i++){
                board[y0][i] = c;
            }
        }
    }

}
