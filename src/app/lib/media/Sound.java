package app.lib.media;

import app.lib.UtilNotInitializedException;
import app.lib.io.Configuration;
import app.lib.io.InputProperties;

import javax.sound.sampled.Clip;

public final class Sound {
    private static boolean sInitialized = false;
    private static boolean sEnableSFX, sEnableMusic;

    public static void load() {
        InputProperties inputProperties = new InputProperties();
        Configuration.loadProperties("settings.properties", inputProperties);
        sEnableSFX = inputProperties.getBoolean("enable_sfx", true);
        sEnableMusic = inputProperties.getBoolean("enable_music", true);
        sInitialized = true;
    }

    private static void check() {
        if(!sInitialized)
            throw new UtilNotInitializedException();
    }

    public static void playSFX(Clip clip) {
        check();
        if (clip == null || !sEnableSFX)
            return;
        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public static void playMusic(Clip clip) {
        check();
        if(clip == null || !sEnableMusic) {
            return;
        }

        if(clip.isRunning())
            clip.stop();

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    private Sound() {}
}
