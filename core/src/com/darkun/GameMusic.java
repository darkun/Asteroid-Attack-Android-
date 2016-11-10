package com.darkun;

import com.badlogic.gdx.audio.Music;

/**
 * @author Dmitry Kartsev <dek.alpha@mail.ru>
 * @since 10.11.2016.
 */
public class GameMusic {
    private Music song;

    public GameMusic(Music song) {
        this.song = song;
        song.setLooping(true);
    }

    public void playMusic() { song.play(); }

    public void pauseMusic() { song.pause(); }
}
