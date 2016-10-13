package com.darkun;

import com.darkun.environment.Space;
import com.darkun.objects.Asteroid;
import com.darkun.objects.Missile;
import com.darkun.objects.MissileBoom;
import com.darkun.objects.Playership;
import com.darkun.utility.Keys;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.imageio.*;
import java.util.Random;
import javax.imageio.*;
import java.util.*;
import java.io.*;
import java.lang.*;

/*
 * Asteroid Attack scroller game (Refactoring)
 * @author Dmitry Kartsev, based on SpaceInviders by Sergey (biblelamp) - https://github.com/biblelamp
 * @version 0.4.7 18/09/2016
*/

public class AsteroidAttack extends JFrame {
    final String NAME_OF_GAME = "Asteroid Attack! Scroller Game";
    private static final int POINT_SCALE = 2;
    public static final int FIELD_WIDTH = 400*POINT_SCALE;
    public static final int FIELD_HEIGHT = 300*POINT_SCALE;
    private static final int START_LOCATION = 150;
    private static final int FIELD_DX = 7; // determined experimentally
    private static final int FIELD_DY = 26;
    //public static final int GROUND_Y = FIELD_HEIGHT - 20;
    public static final int LEFT = 37; // key codes
    public static final int RIGHT = 39;
    public static final int DOWN = 40;
    public static final int UP = 38;
    public static final int UP_AND_RIGHT = 87;
    public static final int UP_AND_LEFT = 85;
    public static final int DOWN_AND_RIGHT = 79;
    public static final int DOWN_AND_LEFT = 77;
    //public static final int FIRE = 32;
    public static final int GAME_SPEED = 5; // speed of game
    public static final int MISS_LIMIT = 5; // how many asteroids player need to miss to fault

    public static boolean gameOver;
    private static int countScore; // point we got while playing
    private static int missedScore; // missed asteroids count (we need to destoy it, right?)
    Canvas canvasPanel = new Canvas();
    Random random = new Random();
    Keys k = new Keys(); // keyboard utility

    public static Image ast, ship, missile, m_explosion, driverflame, space; // sprites for asteroids, spaceship, missile, explosive, space

    public static Playership playership = new Playership(); // players spaceship
    Space open_space = new Space();

    public static volatile ArrayList<Missile> missiles = new ArrayList<>(); // missiles, launched by player
    public static volatile ArrayList<Asteroid> asteroids = new ArrayList<>(); // missiles, launched by player
    public static volatile ArrayList<MissileBoom> m_explosions = new ArrayList<>(); // missile explosions

    public static void main(String args[]) {
        new AsteroidAttack().go();
    }

    public AsteroidAttack() {
        setTitle(NAME_OF_GAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, FIELD_WIDTH + FIELD_DX, FIELD_HEIGHT + FIELD_DY);
        setResizable(false);
        canvasPanel.setBackground(Color.black);
        getContentPane().add(BorderLayout.CENTER, canvasPanel);

        // let's load sprites for ship & asteroid
        try {
            ship = ImageIO.read(new File("img/spaceship.png"));
            ast = ImageIO.read(new File("img/asteroid.png"));
            missile = ImageIO.read(new File("img/missile.png"));
            m_explosion = ImageIO.read(new File("img/m_explosion.png"));
            driverflame = ImageIO.read(new File("img/driveflame.png"));
            space = ImageIO.read(new File("img/space.jpg"));
        } catch(IOException e) { e.printStackTrace(); }

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                k.press(e.getKeyCode());
            }
            public void keyReleased(KeyEvent e) {
                k.reset(e.getKeyCode());
                playership.setDirection(0);
            }
        });
        setVisible(true);

        asteroids.add(new Asteroid(random.nextInt(FIELD_WIDTH), -50, 0, 15));// let's start
    }

    public static void setGameOver(boolean gameOver) {
        AsteroidAttack.gameOver = gameOver;
    }

    public static int getMissedScore() {
        return missedScore;
    }

    public static void setMissedScore(int missedScore) {
        AsteroidAttack.missedScore = missedScore;
        if(getMissedScore() >= MISS_LIMIT) gameOver = true;
    }

    void go() { // main loop of game
        while (true) {
            try {
                Thread.sleep(GAME_SPEED);
            } catch (Exception e) { e.printStackTrace(); }
            canvasPanel.repaint();
            if(!gameOver) playership.move();
            open_space.slide();
            if(!gameOver) {
                for (Missile missile : missiles) {
                    if (missile.isEnable()) missile.fly();
                }
                for (Asteroid asteroid : asteroids) {
                    if (asteroid.isEnable()) { asteroid.fly(); }
                }
                for (MissileBoom missile_boom : m_explosions) {
                    if (missile_boom.isEnable()) missile_boom.explode();
                }
                if (k.isPressed(KeyEvent.VK_UP)) playership.setDirection(UP);
                if (k.isPressed(KeyEvent.VK_DOWN)) playership.setDirection(DOWN);
                if (k.isPressed(KeyEvent.VK_LEFT)) playership.setDirection(LEFT);
                if (k.isPressed(KeyEvent.VK_RIGHT)) playership.setDirection(RIGHT);
                if (k.isPressed(KeyEvent.VK_UP) && (k.isPressed(KeyEvent.VK_RIGHT))) playership.setDirection(UP_AND_RIGHT);
                if (k.isPressed(KeyEvent.VK_UP) && (k.isPressed(KeyEvent.VK_LEFT))) playership.setDirection(UP_AND_LEFT);
                if (k.isPressed(KeyEvent.VK_DOWN) && (k.isPressed(KeyEvent.VK_RIGHT))) playership.setDirection(DOWN_AND_RIGHT);
                if (k.isPressed(KeyEvent.VK_DOWN) && (k.isPressed(KeyEvent.VK_LEFT))) playership.setDirection(DOWN_AND_LEFT);
                if (k.isPressed(KeyEvent.VK_SPACE) || (k.isPressed(KeyEvent.VK_CONTROL))) playership.shotMissile();

                playership.fly(); // animation of spaceship engine
                clearObjects();
            }
        }
    }

    void clearObjects() { // let's delete empty objects from array lists
        for(int i = 0; i < missiles.size(); i++) { // for missiles
            if(!missiles.get(i).isEnable()) missiles.remove(i);
        }
        // clear dead asteroids
        for(int i = 0; i < asteroids.size(); i++) { // for asteroids
            if(!asteroids.get(i).isEnable()) {
                asteroids.remove(i); // removing old
                // and adding new
                asteroids.add(new Asteroid(random.nextInt(FIELD_WIDTH), -50, rnd(-3.7, 3.7), (int)rnd(14,38)));
            }
        }
        // if explosion is already gone, than we do not need it
        for(int i = 0; i < m_explosions.size(); i++) { // for missiles
            if(!m_explosions.get(i).isEnable()) m_explosions.remove(i);
        }
    }

    // some utility for generate positive and negative double values
    double rnd(double min, double max) {
        return min + (random.nextDouble() * (max - min));
    }

    public static void setGameScore(int value) { countScore = value; }

    public static int getGameScore() { return countScore;  }

    class Canvas extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            open_space.paint(g);
            paintTextAndLine(g);
            paintNumber(g, countScore, 110, 20);
            //paintNumber(g, playership.getHealth(), 390, 20);
            paintHealthBar(g, playership.getHealth(), 390, 20);
            if (!gameOver) {
                playership.paint(g);
                for (Missile missile : missiles) {
                    if (missile.isEnable()) missile.paint(g);
                }
                for (Asteroid asteroid : asteroids) {
                    if (asteroid.isEnable()) asteroid.paint(g);
                }
                for (MissileBoom missile_boom : m_explosions) {
                    if (missile_boom.isEnable()) missile_boom.paint(g);
                }
            }
        }
    }

    void paintTextAndLine(Graphics g) { // paint score, lives and green line
        final int[][] SCORE = {
                {1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,0,1,0,0,0,0,0,0,1,0,0,0,0,1,0,1,0,0,0,0,0,0,1,1,1,1,1,1},
                {0,0,0,0,0,1,0,1,0,0,0,0,0,0,1,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,0,0,0,0,0,0,1,1,1,1,1,1}
        };
        final int[][] LIVES = {
                {1,0,0,0,0,0,0,1,0,1,0,0,0,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,1,0,1,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
                {1,0,0,0,0,0,0,1,0,0,1,0,1,0,0,1,1,1,1,1,1,0,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,1,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,0,1,0,0,0,1,0,0,0,1,1,1,1,1,1,0,1,1,1,1,1,1}
        };
        final int[][] GAME_OVER = {
                {1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,0,1,0,0,0,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1},
                {1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,1,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
                {1,0,0,0,0,1,0,1,1,1,1,1,1,0,1,0,0,1,0,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,0,0,1,0,1,0,0,1,1,1,1,1,1,0,1,0,0,0,0,0},
                {1,0,0,0,0,1,0,1,0,0,0,0,1,0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,1,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
                {1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,0,0,1,0,0,1,0,1,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,0,0,0,1,0,0,0,1,1,1,1,1,1,0,1,0,0,0,0,0},
                {0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
        };
        g.setColor(Color.white);
        for (int y = 0; y < SCORE.length; y++) {
            for (int x = 0; x < SCORE[y].length; x++)
                if (SCORE[y][x] == 1) g.fillRect(x*POINT_SCALE + 30, y*POINT_SCALE + 20, POINT_SCALE, POINT_SCALE);
            for (int i = 0; i < LIVES[y].length; i++)
                if (LIVES[y][i] == 1) g.fillRect(i*POINT_SCALE + 320, y*POINT_SCALE + 20, POINT_SCALE, POINT_SCALE);
        }
        if (gameOver)
            for (int y = 0; y < GAME_OVER.length; y++)
                for (int x = 0; x < GAME_OVER[y].length; x++)
                    if (GAME_OVER[y][x] == 1) g.fillRect(x*POINT_SCALE + 350, y*POINT_SCALE + 270, POINT_SCALE, POINT_SCALE);
    }

    void paintNumber(Graphics g, int number, int x, int y) { // paint numbers (countScore, countLives)
        final int[][][] NUMBERS = {
                {{1,1,1,1,1,1}, {1,0,0,0,0,1}, {1,0,0,0,0,1}, {1,0,0,0,0,1}, {1,1,1,1,1,1}}, // 0
                {{0,0,0,0,0,1}, {0,0,0,0,0,1}, {0,0,0,0,0,1}, {0,0,0,0,0,1}, {0,0,0,0,0,1}}, // 1
                {{1,1,1,1,1,1}, {0,0,0,0,0,1}, {1,1,1,1,1,1}, {1,0,0,0,0,0}, {1,1,1,1,1,1}}, // 2
                {{1,1,1,1,1,1}, {0,0,0,0,0,1}, {1,1,1,1,1,1}, {0,0,0,0,0,1}, {1,1,1,1,1,1}}, // 3
                {{1,0,0,0,0,1}, {1,0,0,0,0,1}, {1,1,1,1,1,1}, {0,0,0,0,0,1}, {0,0,0,0,0,1}}, // 4
                {{1,1,1,1,1,1}, {1,0,0,0,0,0}, {1,1,1,1,1,1}, {0,0,0,0,0,1}, {1,1,1,1,1,1}}, // 5
                {{1,1,1,1,1,1}, {1,0,0,0,0,0}, {1,1,1,1,1,1}, {1,0,0,0,0,1}, {1,1,1,1,1,1}}, // 6
                {{1,1,1,1,1,1}, {0,0,0,0,0,1}, {0,0,0,0,0,1}, {0,0,0,0,0,1}, {0,0,0,0,0,1}}, // 7
                {{1,1,1,1,1,1}, {1,0,0,0,0,1}, {1,1,1,1,1,1}, {1,0,0,0,0,1}, {1,1,1,1,1,1}}, // 8
                {{1,1,1,1,1,1}, {1,0,0,0,0,1}, {1,1,1,1,1,1}, {0,0,0,0,0,1}, {1,1,1,1,1,1}}  // 9
        };
        String numStr = Integer.toString(number);
        g.setColor(Color.green);
        for (int p = 0; p < numStr.length(); p++) {
            int n = (int) numStr.charAt(p) - 48;
            for (int i = 0; i < 5; i++)
                for (int j = 0; j < 6; j++)
                    if (NUMBERS[n][i][j] == 1) g.fillRect(x + j*POINT_SCALE + p*14, y + i*POINT_SCALE, POINT_SCALE, POINT_SCALE);
        }
    }

    void paintHealthBar(Graphics g, int number, int x, int y) { // paint numbers (countScore, countLives)
        final int WHILE_BAR = 10;
        final int M_BAR_WIDTH = 4; // width of small bar in px
        final int SPACE = 1; // space between bars

        boolean part = true;

        for(int i = 0; i < WHILE_BAR*2; i++)
        {
            double n = (double)number / WHILE_BAR;
            int l_full = (int)(n*2);
            double l_fract = n - l_full;
            if(l_full - i > 0) {
                g.setColor(Color.yellow);
                g.fillRect(x + (i * M_BAR_WIDTH), y, M_BAR_WIDTH, 10);
                g.setColor(Color.green);
                g.fillRect(x+(i*M_BAR_WIDTH)+1, y+1, M_BAR_WIDTH-2, 8);
            }
            else if((l_full - i <= 0) && (l_fract != 0) && part == true) // if we have hot full
            {
                g.setColor(Color.yellow);
                g.fillRect(x + (i * M_BAR_WIDTH), y, M_BAR_WIDTH, 10);
                g.setColor(Color.black);
                g.fillRect(x+(i*M_BAR_WIDTH)+1, y+1, M_BAR_WIDTH-2, 4);
                g.setColor(Color.green);
                g.fillRect(x+(i*M_BAR_WIDTH)+1, y+5, M_BAR_WIDTH-2, 4);
                part = false;
            }
            else {
                g.setColor(Color.yellow);
                g.fillRect(x + (i*M_BAR_WIDTH), y, M_BAR_WIDTH, 10);
                g.setColor(Color.black);
                g.fillRect(x+(i*M_BAR_WIDTH)+1, y+1, M_BAR_WIDTH-2, 8);
            }
            x += SPACE; // we need it to divide our small bars from ech other
        }
    }
}
