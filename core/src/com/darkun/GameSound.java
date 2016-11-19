package com.darkun;

import com.badlogic.gdx.audio.Sound;

/**
 * @author Dmitry Kartsev <dek.alpha@mail.ru>
 * @since 19.11.2016
 */
public class GameSound {
    private Sound sound;

    public GameSound(Sound sound) {
        this.sound = sound;
    }

    public void play() { sound.play(); }

    public void pause() { sound.pause(); }

    public void stop() { sound.stop(); }
}
