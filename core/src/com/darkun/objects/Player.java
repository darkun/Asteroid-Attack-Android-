package com.darkun.objects;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 10.11.16
 */
public class Player {
    private int health = 100;
    private boolean gameOver = false;

    public void getDamage(int damage) {
        health -= damage;
        gameOver = health <= 0;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getHealth() {
        return health;
    }
}
