package app;

import app.lib.io.Resources;
import app.menu.Menu;
import app.lib.gui.UI;
import app.lib.io.Configuration;
import app.lib.media.Sound;

import javax.sound.sampled.Clip;

public class Main {
    public static void main(String[] args) {
        Configuration.init("vouli_game");
        Resources.init();

        initUtils();

        new Menu().setVisible();
    }

    public static void initUtils() {
        UI.load();
        Sound.load();

        Clip music = Resources.loadClip("/game/background_music.wav");
        Sound.playMusic(music);
    }
}
