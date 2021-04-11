package AI1;

import GAME.Constants;
import GAME.GamePoint;
import GAME.GameApple;

import java.util.ArrayList;
import java.util.Random;

public class SnakeAgent0 {

    public int hx, hy, tx, ty;
    int sNum;
    private int length = 2, direction;
    private final Random random = new Random();

    private final ArrayList<GamePoint> snakeKnicks = new ArrayList<>();
    private boolean alreadyUpdated = false;
    private final MyAgent agent;

    public SnakeAgent0(int[][] board, String initString, int sNum) {
        genSnake(board);
        agent = new MyAgent();
        agent.initGame(initString);
        this.sNum = sNum;
    }

    public boolean getAlreadyUpdated(){
        return alreadyUpdated;
    }

    public void genSnake(int[][] board){
        length = 2;
        direction = random.nextInt(4);
        //0 -> up, 1 -> down, 2 -> left, 3 -> right
        hx = random.nextInt(50);
        hy = random.nextInt(50);

        while (board[hy][hx] != 0){
            hx = random.nextInt(50);
            hy = random.nextInt(50);
        }

        board[hy][hx] = sNum;

        switch (direction){
            case 0:
                if (hy + 1 < 50 && board[hy + 1][hx] == 0){
                    board[hy + 1][hx] = sNum;
                    tx = hx;
                    ty = hy + 1;
                }
                else {
                    board[hy][hx] = 0;
                    genSnake(board);
                }
                break;

            case 1:
                if (hy - 1 >= 0 && board[hy - 1][hx] == 0){
                    board[hy - 1][hx] = sNum;
                    tx = hx;
                    ty = hy - 1;
                }
                else {
                    board[hy][hx] = 0;
                    genSnake(board);
                }
                break;

            case 2:
                if (hx + 1 < 50 && board[hy][hx + 1] == 0){
                    board[hy][hx + 1] = sNum;
                    tx = hx + 1;
                    ty = hy;
                }
                else {
                    board[hy][hx] = 0;
                    genSnake(board);
                }
                break;

            case 3:
                if (hx - 1 >= 0 && board[hy][hx - 1] == 0){
                    board[hy][hx - 1] = sNum;
                    tx = hx - 1;
                    ty = hy;
                }
                else {
                    board[hy][hx] = 0;
                    genSnake(board);
                }
                break;
        }

        snakeKnicks.clear();
        snakeKnicks.add(new GamePoint(hx, hy));
        snakeKnicks.add(new GamePoint(tx, ty));
    }

    public void ateApple(int newX, int newY, int newDirection) {
        hx = newX;
        hy = newY;
        if (direction == newDirection){
            GamePoint start = snakeKnicks.get(0);
            start.setX(newX);
            start.setY(newY);
            snakeKnicks.set(0, start);
        }
        else{
            snakeKnicks.add(0, new GamePoint(newX, newY));
            direction = newDirection;
        }
    }

    public void setNewHead(int newX, int newY, int newDirection){
        hx = newX;
        hy = newY;
        if (direction == newDirection){
            GamePoint start = snakeKnicks.get(0);
            start.setX(newX);
            start.setY(newY);
            snakeKnicks.set(0, start);
        }
        else{
            snakeKnicks.add(0, new GamePoint(newX, newY));
            direction = newDirection;
        }
    }

    public String[] getSnakeDesc() {
        return genSnakeDescription().split(" ");
    }

    public String genSnakeDescription() {
        StringBuilder desc = new StringBuilder("alive " + length + " 1");
        for (GamePoint knick: snakeKnicks) desc.append(" ").append(knick.toString());
        return desc.toString();
    }

    public void move(int[][] board, GameApple apple, boolean alreadyUpdated) {
        this.alreadyUpdated = alreadyUpdated;
        int newDirection = getMove(); //for now

        if (newDirection == 5){
            newDirection = direction;
        }

        if (direction == 0 && (newDirection == 1 || newDirection == 0)){
            newDirection = 0;
        }
        else if (direction == 1 && (newDirection == 1 || newDirection == 0)){
            newDirection = 1;
        }
        else if (direction == 2 && (newDirection == 2 || newDirection == 3)){
            newDirection = 2;
        }
        else if (direction == 3 && (newDirection == 2 || newDirection == 3)){
            newDirection = 3;
        }

        //0 -> up, 1 -> down, 2 -> left, 3 -> right
        switch (newDirection){
            case 0:

                if (hy - 1 >= 0){
                    if (board[hy - 1][hx] == Constants.OBSTACLE){
                        genSnake(board);
                    }
                    else if (board[hy - 1][hx] != 0 && board[hy - 1][hx] != Constants.APPLE) {
                        if (isTailOfUnupatedSnake(hx, hy - 1)){
                            GamePoint tail = snakeKnicks.get(snakeKnicks.size() - 1);
                            GamePoint secondLastPoint = snakeKnicks.get(snakeKnicks.size() - 2);
                            updateTail(tail, secondLastPoint);
                            if (snakeKnicks.size() == 1){
                                hy -= 1;
                                snakeKnicks.add(new GamePoint(hx, hy));
                            }
                            else{
                                hy -= 1;
                                setNewHead(hx, hy, newDirection);
                            }
                        }
                        else genSnake(board);
                    }
                    else if (board[hy - 1][hx] == Constants.APPLE){
                        length++;
                        apple.setSoonToChange(true);
                        ateApple(hx, hy - 1, newDirection);
                    }
                    else if (board[hy - 1][hx] == 0){

                        GamePoint tail = snakeKnicks.get(snakeKnicks.size() - 1);
                        GamePoint secondLastPoint = snakeKnicks.get(snakeKnicks.size() - 2);
                        updateTail(tail, secondLastPoint);
                        if (snakeKnicks.size() == 1){
                            hy -= 1;
                            snakeKnicks.add(new GamePoint(hx, hy));
                        }
                        else{
                            hy -= 1;
                            setNewHead(hx, hy, newDirection);
                        }
                    }
                }
                else genSnake(board);

                break;

            case 1:


                if (hy + 1 < 50){
                    if (board[hy + 1][hx] == Constants.OBSTACLE){
                        genSnake(board);
                    }
                    else if (board[hy + 1][hx] != 0 && board[hy + 1][hx] != Constants.APPLE) {
                        if (isTailOfUnupatedSnake(hx, hy + 1)){
                            GamePoint tail = snakeKnicks.get(snakeKnicks.size() - 1);
                            GamePoint secondLastPoint = snakeKnicks.get(snakeKnicks.size() - 2);
                            updateTail(tail, secondLastPoint);
                            if (snakeKnicks.size() == 1){
                                hy += 1;
                                snakeKnicks.add(new GamePoint(hx, hy));
                            }
                            else{
                                hy += 1;
                                setNewHead(hx, hy, newDirection);
                            }
                        }
                        else genSnake(board);
                    }
                    else if (board[hy + 1][hx] == Constants.APPLE){
                        length++;
                        apple.setSoonToChange(true);
                        ateApple(hx, hy + 1, newDirection);
                    }
                    else if (board[hy + 1][hx] == 0){
                        GamePoint tail = snakeKnicks.get(snakeKnicks.size() - 1);
                        GamePoint secondLastPoint = snakeKnicks.get(snakeKnicks.size() - 2);
                        updateTail(tail, secondLastPoint);
                        if (snakeKnicks.size() == 1){
                            hy += 1;
                            snakeKnicks.add(new GamePoint(hx, hy));
                        }
                        else{
                            hy += 1;
                            setNewHead(hx, hy, newDirection);
                        }
                    }
                }
                else genSnake(board);

                break;

            case 2:

                if (hx - 1 >= 0){
                    if (board[hy][hx - 1] == Constants.OBSTACLE){
                        genSnake(board);
                    }
                    else if (board[hy][hx - 1] != 0 && board[hy][hx - 1] != Constants.APPLE) {
                        if (isTailOfUnupatedSnake(hx - 1, hy)){
                            GamePoint tail = snakeKnicks.get(snakeKnicks.size() - 1);
                            GamePoint secondLastPoint = snakeKnicks.get(snakeKnicks.size() - 2);
                            updateTail(tail, secondLastPoint);
                            if (snakeKnicks.size() == 1){
                                hx -= 1;
                                snakeKnicks.add(new GamePoint(hx, hy));
                            }
                            else{
                                hx -= 1;
                                setNewHead(hx, hy, newDirection);
                            }
                        }
                        else genSnake(board);
                    }
                    else if (board[hy][hx - 1] == Constants.APPLE){
                        length++;
                        apple.setSoonToChange(true);
                        ateApple(hx - 1, hy, newDirection);
                    }
                    else if (board[hy][hx - 1] == 0){
                        GamePoint tail = snakeKnicks.get(snakeKnicks.size() - 1);
                        GamePoint secondLastPoint = snakeKnicks.get(snakeKnicks.size() - 2);
                        updateTail(tail, secondLastPoint);
                        if (snakeKnicks.size() == 1){
                            hx -= 1;
                            snakeKnicks.add(new GamePoint(hx, hy));
                        }
                        else{
                            hx -= 1;
                            setNewHead(hx, hy, newDirection);
                        }
                    }
                }
                else genSnake(board);
                break;

            case 3:
                if (hx + 1 < 50){
                    if (board[hy][hx + 1] == Constants.OBSTACLE){
                        genSnake(board);
                    }
                    else if (board[hy][hx + 1] != 0 && board[hy][hx + 1] != Constants.APPLE) {
                        if (isTailOfUnupatedSnake(hx + 1, hy)){
                            GamePoint tail = snakeKnicks.get(snakeKnicks.size() - 1);
                            GamePoint secondLastPoint = snakeKnicks.get(snakeKnicks.size() - 2);
                            updateTail(tail, secondLastPoint);
                            if (snakeKnicks.size() == 1){
                                hx += 1;
                                snakeKnicks.add(new GamePoint(hx, hy));
                            }
                            else{
                                hx += 1;
                                setNewHead(hx, hy, newDirection);
                            }
                        }
                        else genSnake(board);
                    }
                    else if (board[hy][hx + 1] == Constants.APPLE){
                        length++;
                        apple.setSoonToChange(true);
                        ateApple(hx + 1, hy, newDirection);
                    }
                    else if (board[hy][hx + 1] == 0){
                        GamePoint tail = snakeKnicks.get(snakeKnicks.size() - 1);
                        GamePoint secondLastPoint = snakeKnicks.get(snakeKnicks.size() - 2);
                        updateTail(tail, secondLastPoint);
                        if (snakeKnicks.size() == 1){
                            hx += 1;
                            snakeKnicks.add(new GamePoint(hx, hy));
                        }
                        else{
                            hx += 1;
                            setNewHead(hx, hy, newDirection);
                        }
                    }
                }
                else genSnake(board);
                break;
        }
    }

    private boolean isTailOfUnupatedSnake(int newX, int newY) {
        return this.tx == newX && this.ty == newY && !this.getAlreadyUpdated();
    }

    private void updateTail(GamePoint tail, GamePoint secondLastPoint) {
        if (secondLastPoint.getX() == tail.getX()){
            if (tail.getY() > secondLastPoint.getY()){
                tail.setY(tail.getY() - 1);
            }
            else if (tail.getY() < secondLastPoint.getY()){
                tail.setY(tail.getY() + 1);
            }
        }
        else if (secondLastPoint.getY() == secondLastPoint.getY()){
            if (tail.getX() > secondLastPoint.getX()){
                tail.setX(tail.getX() - 1);
            }
            else if (tail.getX() < secondLastPoint.getX()){
                tail.setX(tail.getX() + 1);
            }
        }

        tx = tail.getX();
        ty = tail.getY();

        if (tail.equals(secondLastPoint)){
            snakeKnicks.remove(snakeKnicks.size() - 1);
        }
        else{
            snakeKnicks.set(snakeKnicks.size() - 1, tail);
        }
    }

    private int getMove() {
        return agent.getMove();
    }

    public void setAlreadyUpdate(boolean alreadyUpdated) {
        this.alreadyUpdated = alreadyUpdated;
    }

    public void setAppleCoord(String toString) {
        agent.setAppleCoord(toString);
    }

    public void addObstacle(String... obs) {
        for (String ob : obs) agent.addObstacle(ob);
    }

    public void addMe(String snakeDesc) {
        agent.addMe(snakeDesc);
    }

    public int getSnakeNum() {
        return sNum;
    }
}
