package com.darkun.entity;


import lombok.Getter;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 10.11.16
 */
public class Player {
    @Getter
    private int health = 100;
    @Getter
    private boolean gameOver = false;

    public void getDamage(int damage) {
        health -= damage;
        gameOver = health <= 0;
    }
}
