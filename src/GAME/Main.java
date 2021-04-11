package GAME;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private JPanel mPanel;

    Main() {
        // Frame initializion
        initFrame();

        SnakeBoard snakeBoard = new SnakeBoard();


        Timer timer = new Timer(20, null);
        timer.addActionListener(e -> {
            snakeBoard.updateBoard();
            mPanel.removeAll();
            createCells(snakeBoard.getBoard());
            mPanel.revalidate();
            mPanel.repaint();
        });
        timer.start();
    }

    private void initFrame(){
        JFrame frame = new JFrame("Snake");
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        mPanel = new JPanel();
        mPanel.setLayout(new GridLayout(50, 50));

        frame.add(mPanel);
    }

    public static void main(String[] a) {
        new Main();
    }

    public void createCells(int[][] board) {
        JLabel[][] cells = new JLabel[50][50];

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                cells[i][j] = new JLabel("");
                cells[i][j].setOpaque(true);

                if (board[i][j] == Constants.OBSTACLE){
                    cells[i][j].setBackground(Color.lightGray);
                }
                else if (board[i][j] == Constants.APPLE){
                    cells[i][j].setBackground(Color.green);
                }
                else if (board[i][j] == Constants.SNAKE_0){
                    cells[i][j].setBackground(Color.blue);
                }
                else{
                    cells[i][j].setBackground(Color.black);
                }

                mPanel.add(cells[i][j]);
            }
        } // End for

    }
}
