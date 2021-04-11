package GAME;

import java.util.Random;

public class GameApple {
    int x, y;
    private boolean change = false;
    private final Random random = new Random();

    public GameApple(){
        x = random.nextInt(50);
        y = random.nextInt(50);

        System.out.println("apple x: " + x);
        System.out.println("apple y: " + y);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void generateNewPoints() {
        x = random.nextInt(50);
        y = random.nextInt(50);
    }


    public void genToNewPoint(){
        generateNewPoints();
    }

    public void setSoonToChange(boolean b) {
        change = b;
    }

    public void checkForPossibleUpdate() {
        if (change){
           genToNewPoint();
           change = false;
        }
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}
