package app;

import app.ui.Menu;
import app.ui.UI;

import javax.sound.sampled.Clip;

public class Main {
    public static void main(String[] args) {
        IO.init();

        UI.init();
        SoundUtils.init();

        Clip music = SoundUtils.loadClip("/game/sound/background_music.wav");
        SoundUtils.playMusic(music);

        new Menu().setVisible();
    }
}
