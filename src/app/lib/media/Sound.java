package app.lib.media;

import app.lib.NotInitializedException;
import app.lib.io.InputProperties;
import app.lib.io.Configuration;
import app.lib.gui.UI;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public final class Sound {
    private static boolean sInitialized = false;
    private static boolean sEnableSFX, sEnableMusic;

    public static void load() {
        try (InputStream inputStream = Configuration.getConfigInputStream("settings.properties", true)) {
            InputProperties inputProperties = new InputProperties(inputStream);
            sEnableSFX = inputProperties.getBoolean("enable_sfx", true);
            sEnableMusic = inputProperties.getBoolean("enable_music", true);
            sInitialized = true;
        } catch (IOException e) {
            UI.showException(e);
            throw new RuntimeException(e);
        }
    }

    private static void check() {
        if(!sInitialized)
            throw new NotInitializedException();
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
