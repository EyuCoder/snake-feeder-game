package com.codexo;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;


public class GamePlay extends JPanel implements ActionListener {

    public enum Direction {UP, DOWN, LEFT, RIGHT}
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 100;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int snakeLength = 3;
    int score;
    int appleX;
    int appleY;
    Direction direction = Direction.RIGHT;
    Boolean running = false;
    Timer timer;
    Random random;

    public GamePlay() {
//        try {
//            flappy = Font.createFont(Font.TRUETYPE_FONT,
//                    Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("FlappyBirdy.ttf")));
//            flappy.deriveFont(Font.BOLD, 40);
//        } catch (FontFormatException | IOException e) {
//            e.printStackTrace();
//        }
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    public void draw(Graphics g) {
        for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        if (timer.isRunning()) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    //g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            gameOver(g);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("SCORE: " + score, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + score)) / 2, g.getFont().getSize());
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case UP:
                y[0] = y[0] - UNIT_SIZE;
                break;
            case DOWN:
                y[0] = y[0] + UNIT_SIZE;
                break;
            case LEFT:
                x[0] = x[0] - UNIT_SIZE;
                break;
            case RIGHT:
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            snakeLength++;
            score++;
            newApple();
        }
    }

    public void checkCollision() {
        //check body collision
        for (int i = snakeLength; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                timer.stop();
            }
        }

        //check top border collision
        if (y[0] < 0) {
            timer.stop();
        }

        //check bottom border collision
        if (y[0] > SCREEN_HEIGHT) {
            timer.stop();
        }

        //check left border collision
        if (x[0] < 0) {
            timer.stop();
        }

        //check right border collision
        if (x[0] > SCREEN_WIDTH) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER!", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER!")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (timer.isRunning()) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != Direction.RIGHT) direction = Direction.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != Direction.LEFT) direction = Direction.RIGHT;
                    break;
                case KeyEvent.VK_UP:
                    if (direction != Direction.DOWN) direction = Direction.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != Direction.UP) direction = Direction.DOWN;
                    break;
                case KeyEvent.VK_ENTER:
                    //
                    startGame();
                    break;
            }
        }
    }
}