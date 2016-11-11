package com.darkun;

import com.badlogic.gdx.audio.Music;

/**
 * @author Dmitry Kartsev <dek.alpha@mail.ru>
 * @since 10.11.2016.
 */
public class BackgroundMusic {
    private Music song;

    public BackgroundMusic(Music song) {
        this.song = song;
        song.setLooping(true);
    }

    public void play() { song.play(); }

    public void pause() { song.pause(); }
}
