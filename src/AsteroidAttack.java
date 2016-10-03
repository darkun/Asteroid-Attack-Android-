/*
 * Asteroid Attack scroller game (Java lvl 1 parctice)
 * @author Dmitry Kartsev, based on SpaceInviders of Sergey (biblelamp) - https://github.com/biblelamp
 * @version 0.6.3 21/09/2016
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.Random;
import javax.imageio.*;
import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.lang.*;

public class AsteroidAttack extends JFrame {

    final String NAME_OF_GAME = "Asteroid Attack! Scroller Game";
    final int POINT_SCALE = 2;
    final int FIELD_WIDTH = 400*POINT_SCALE;
    final int FIELD_HEIGHT = 300*POINT_SCALE;
    final int START_LOCATION = 150;
    final int FIELD_DX = 7; // determined experimentally
    final int FIELD_DY = 26;
    final int LEFT = 37; // key codes
    final int RIGHT = 39;
    final int DOWN = 40;
    final int UP = 38;
    final int UP_AND_RIGHT = 87;
    final int UP_AND_LEFT = 85;
    final int DOWN_AND_RIGHT = 79;
    final int DOWN_AND_LEFT = 77;
    final int GAME_SPEED = 5; // speed of game
    public static boolean gameOver;
    public int countScore; // point we got while playing
    Image asteroid, ship, missile, m_explosion, driverflame, space; // sprites for asteroids, spaceship, missile, explosive, space
    Canvas canvasPanel = new Canvas();
    Random random = new Random();
    PlayerShip playership = new PlayerShip(); // players spaceship
    Space open_space = new Space();
    volatile ArrayList<Missile> missiles = new ArrayList<>(); // missiles, launched by player
    volatile ArrayList<Asteroid> asteroids = new ArrayList<>(); // missiles, launched by player
    volatile ArrayList<MissileBoom> m_explosions = new ArrayList<>(); // missile explosions
    Keys k = new Keys(); // keyboard utility
    private static final ArrayList<Integer> keyChain = new ArrayList<>();;

    public static void main(String args[]) {
        new AsteroidAttack().go();
    }

    AsteroidAttack() {
        setTitle(NAME_OF_GAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, FIELD_WIDTH + FIELD_DX, FIELD_HEIGHT + FIELD_DY);
        setResizable(false);
        canvasPanel.setBackground(Color.black);
        getContentPane().add(BorderLayout.CENTER, canvasPanel);

        // let's load sprites for ship & asteroid
        try {
            ship = ImageIO.read(new File("img/spaceship.png"));
            asteroid = ImageIO.read(new File("img/asteroid.png"));
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

        asteroids.add(new Asteroid(random.nextInt(FIELD_WIDTH), -50, 0, 15)); // let's start
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
                    if (asteroid.isEnable()) asteroid.fly();
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

                playership.fly();
                clearObjects();
            }
        }
    }

    void clearObjects() { // let's delete empty objects from array lists
        for(int i = 0; i < missiles.size(); i++) { // for missiles
            if(!missiles.get(i).isEnable()) missiles.remove(i);
        }
        // clear dead asteroids
        for(int i = 0; i < asteroids.size(); i++) { // for missiles
            if(!asteroids.get(i).isEnable()) {
                asteroids.remove(i);
                // and creating new
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

    class PlayerShip { // players ship
        final int RADIUS = 25; // radius of playership to check damages
        final int DX = 2;
        final int DY = 2;
        final int WIDTH = 72;
        final int FLAME_CELL = 5; // width of flame sprite cell
        final int FLAME_FRAMES = 19;
        final int ANIM_SPEED = 25; // speed of flame animation
        final long DELAY = 1500; // delay for next lunch
        int x, y, direction;
        long lastLunch, lastShot; // here we will store last lunch time
        int health, flameTime, flamePhase; // if it is less then 1 - game over
        boolean wing = true; // from what wing of ship do we start missile this time

        public PlayerShip() {
            x = 400;
            y = FIELD_HEIGHT - HEIGHT - 100;
            health = 100;
            this.flamePhase = 0;
            this.flameTime = 0;
            lastLunch = System.currentTimeMillis() - 1500; // this is needed to fire from fist seconds of game
            lastShot = System.currentTimeMillis() - 1500; // this is needed to fire from fist seconds of game
        }

        void move() { // spaceship can move
            if (direction == LEFT && x > 40) x -= DX;
            if (direction == RIGHT && x < FIELD_WIDTH - WIDTH) x += DX;
            if (direction == DOWN && y < FIELD_HEIGHT - HEIGHT - RADIUS*2) y += DY;
            if (direction == UP && y > FIELD_HEIGHT/2 - HEIGHT - 16) y -= DY;
            if (direction == UP_AND_RIGHT && y > FIELD_HEIGHT/2 - HEIGHT - 16 && x < FIELD_WIDTH - WIDTH ) { y -= DY; x += DX; }
            if (direction == UP_AND_LEFT && y > FIELD_HEIGHT/2 - HEIGHT - 16 && x > 40) { y -= DY; x -= DX; }
            if (direction == DOWN_AND_RIGHT && y < FIELD_HEIGHT - HEIGHT - RADIUS*2) { y += DY; x += DX; }
            if (direction == DOWN_AND_LEFT && y < FIELD_HEIGHT - HEIGHT - RADIUS*2) { y += DY; x -= DX; }
        }

        void setDirection(int direction) { this.direction = direction; }

        void shotMissile() {
            /*playSound(new File("sounds/shoot.wav"));
            ray.start(x, y);*/
            System.out.println(System.currentTimeMillis() - lastLunch);
            if((System.currentTimeMillis() - lastLunch) > DELAY) {
                if(wing) {
                    missiles.add(new Missile(x, y));
                    wing = false;
                }
                else {
                    missiles.add(new Missile(x - 39, y));
                    wing = true;
                }
                lastLunch = System.currentTimeMillis();
            }
        }

        void fly() { // drawing ship animation
            if(this.flameTime >= ANIM_SPEED) {
                if(this.flamePhase < FLAME_FRAMES) this.flamePhase++;
                else this.flamePhase = 0;

                this.flameTime = 0;
            }
            else this.flameTime++;
        }

        int getX() { return x; }
        int getY() { return y; }
        int getRadius() { return RADIUS; }
        int getWidth() { return WIDTH; }
        int getHealth() { return health; }
        void getDamage(int damage) {
            m_explosions.add(new MissileBoom(x, y));
            health -= damage;
            if(health <= 0) gameOver = true;
        }

        void paint(Graphics g) {

            g.drawImage(ship, x-RADIUS, y-RADIUS, null);
            // drawing flame of drivers
            g.drawImage(driverflame, x-RADIUS+38, y-RADIUS+67, x-RADIUS+38+FLAME_CELL, y-RADIUS+67+12,  this.flamePhase*FLAME_CELL, 0, this.flamePhase*FLAME_CELL+FLAME_CELL, FLAME_CELL, null);
        }
    }

    class Missile { // players gun
        final int WIDTH = 2; // for accuracy calc
        final int HEIGHT = 30; // for accuracy calc
        final int DY = 30;
        final int SPEED = 16; // speed of missile
        final int DAMAGE = 35; // what damage it do to asteroid / enemy
        int bonus = 15; // score bonus for destroying asteroid
        int x, y, flyTime, lastLunch;
        boolean exists;

        Missile(int x, int y) {
            if (!exists) {
                this.exists = true;
                this.x = x + (playership.getWidth() - WIDTH) / 2;
                this.y = y - HEIGHT;
                this.flyTime = 0;
            }
        }

        void start(int x, int y) {
            if (!exists) {
                this.exists = true;
                this.x = x + (playership.getWidth() - WIDTH) / 2;
                this.y = y - HEIGHT;
                this.flyTime = 0;
            }
        }

        void fly() {
            if (exists) {
                if(flyTime > SPEED) // checking, if our missile not too hurry )
                {
                    y -= DY;
                    this.exists = (y + DY) > 0;
                    this.flyTime = 0;
                }
                else this.flyTime++;

                if(checkTarget(this.x, this.y)) {
                    this.exists = false; // our missile is exploded
                    m_explosions.add(new MissileBoom(x, y));
                    countScore += bonus;
                }
            }
        }

        boolean checkTarget(int x, int y) {
            for (Asteroid asteroid : asteroids) {
                // here we check, if our missile got to radius of asteroid
                if((asteroid.isEnable()) && (Math.sqrt(Math.pow((double)asteroid.getX()-(double)x, 2) + Math.pow((double)asteroid.getY()-(double)y, 2)) <= asteroid.getRadius())) {
                    asteroid.getDamage(DAMAGE);
                    return true;
                }
            }
            return false;
        }

        void disable() { exists = false; }

        boolean isEnable() { return exists; }

        int getX() { return x; }
        int getY() { return y; }
        int getLastLunch() { return lastLunch; }

        void paint(Graphics g) {
            g.drawImage(missile, x-7, y, null);
        }
    }

    class Asteroid { // asteroid, that attacks player

        final int SPRITE_CELL = 72; // size of sprite cell
        final int RADIUS = 35; // for interaction calc
        final int DY = 30;
        final int DAMAGE = 11; // what damage it do on crash
        final int ANIM_FRAMES = 17; // how many frames do we have for animation

        int speed = 7; // speed of asteroid flying
        int anim_speed = 12; // speed of animation
        int x, y, flyTime, animTime, animPhase; // current position of asteroid, what course it is flying and phase of animation
        double traectory; // coeff for X dislocation
        boolean exists; // is it is?
        int live; // current level of durability (how much damage it needed for destroying)

        Asteroid (int x, int y, double direction, int speed)
        {
            this.x = x; // starting position
            this.y = y;
            this.traectory = direction;
            this.flyTime = 0;
            this.animTime = 0;
            this.speed = speed;
            this.anim_speed = speed / 2;
            this.live = 20;
            this.exists = true;
        }

        void fly() {
            if (exists) {
                if(this.flyTime > this.speed) {// checking, if our asteroid not too hurry )
                    y += DY;
                    x += this.traectory;
                    this.exists = (y + DY) < FIELD_HEIGHT + RADIUS;
                    this.flyTime = 0;
                    System.out.println(exists);
                    System.out.println(x + "," + y);
                }
                else this.flyTime++;

                if(this.animTime >= this.anim_speed) {
                    if(this.animPhase < ANIM_FRAMES) this.animPhase++;
                    else this.animPhase = 0;
                    animTime = 0;
                }
                else this.animTime++;

                checkCrash(x, y);
            }
        }

        void disable() { exists = false; }

        boolean isEnable() { return this.exists; }

        int getX() { return x; }
        int getY() { return y; }
        void getDamage(int damage) {
            this.live -= damage;
            if(this.live <=0 ) this.exists = false; // if asteroid is destroed, let's kill it
        }
        void doDamage() {
            this.exists = false; // if asteroid is destroed, let's kill it
            playership.getDamage(DAMAGE);
        }
        boolean checkCrash(int x, int y) { // check, if asteroid touches playership (are radiuses of asteroid and spaceship in touch)
            if((this.isEnable()) && (Math.sqrt(Math.pow((double)playership.getX() - (double)this.x, 2) + Math.pow((double)playership.getY() - (double)this.y, 2)) <= this.getRadius() + playership.getRadius())) {
                this.doDamage();
            }
            return false;
        }
        int getRadius() { return RADIUS; }

        void paint(Graphics g) {
            g.drawImage(asteroid, x-RADIUS, y-RADIUS, x-RADIUS+SPRITE_CELL, y-RADIUS+SPRITE_CELL, this.animPhase*SPRITE_CELL, 0, this.animPhase*SPRITE_CELL+SPRITE_CELL, SPRITE_CELL, null);
        }
    }

    class MissileBoom { // explosion after missile hit

        final int SPRITE_CELL = 72; // size of sprite cell
        final int ANIM_SPEED = 1; // speed of animation
        final int ANIM_FRAMES = 71; // how many frames do we have for animation

        int x, y, animTime, animPhase; // position of explosion and phase of animation
        boolean exists; // is it is?

        MissileBoom (int x, int y) {
            this.x = x; // starting position
            this.y = y;
            this.animTime = 0;
            this.exists = true;
        }

        void explode() {
            if (exists) {
                if(this.animTime >= ANIM_SPEED) {
                    if(this.animPhase < ANIM_FRAMES) this.animPhase++;
                    else this.exists = false; // end of explosion
                    animTime = 0;
                }
                else this.animTime++;
            }
        }

        void disable() { exists = false; }

        boolean isEnable() { return this.exists; }

        int getX() { return x; }
        int getY() { return y; }

        void paint(Graphics g) {
            g.drawImage(m_explosion, x-(SPRITE_CELL/2), y-(SPRITE_CELL/2), x-(SPRITE_CELL/2)+SPRITE_CELL, y-(SPRITE_CELL/2)+SPRITE_CELL, this.animPhase*SPRITE_CELL, 0, this.animPhase*SPRITE_CELL+SPRITE_CELL, SPRITE_CELL, null);
        }
    }

    class Space { // wave of asteroid attack
        final int STEP = 1; // step for space wallpaper scroll
        final int SPEED = 35; // speed of animation
        final int ANIM_FRAMES = 821;

        int animPhase, animTime;

        Space () {
            this.animPhase = ANIM_FRAMES; // starting frame
            this.animTime = 0;
        }

        void slide() {
            if(this.animTime >= SPEED) {
                if(this.animPhase > 0) this.animPhase-=STEP;
                else this.animPhase = ANIM_FRAMES;
                animTime = 0;
            }
            else this.animTime++;
        }

        void paint(Graphics g) {
           g.drawImage(space, 0, 0, FIELD_WIDTH, FIELD_HEIGHT, 0, this.animPhase * STEP, FIELD_WIDTH, FIELD_HEIGHT + this.animPhase * STEP, null);
        }
    }

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

