package app;

import app.io.InputProperties;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class SoundUtils {
    public static boolean sEnableSFX, sEnableMusic;

    public static void init() {
        try (InputStream inputStream = IO.getConfigInputStream("settings.properties")) {
            InputProperties inputProperties = new InputProperties(inputStream);
            sEnableSFX = inputProperties.getBoolean("enable_sfx");
            sEnableMusic = inputProperties.getBoolean("enable_music");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void playSFX(Clip clip) {
        if (clip == null || !sEnableSFX)
            return;
        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public static void playMusic(Clip clip) {
        if(clip == null || !sEnableMusic) {
            return;
        }

        if(clip.isRunning())
            clip.stop();

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    public static Clip loadClip(String path) {
        try {
            URL rsrcUrl = SoundUtils.class.getResource(path);
            if (rsrcUrl == null) {
                System.err.println("Failed to load sound " + path);
                return null;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(rsrcUrl);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.err.println("Failed to load sound " + path + " (Exception thrown)");
            return null;
        }
    }

    public static Clip loadOneUseClip(String path) {
        Clip clip = loadClip(path);
        if (clip == null) return null;
        clip.addLineListener(l -> {
            if (l.getType() == LineEvent.Type.STOP && clip.isOpen()) {
                clip.close();
            }
        });
        return clip;
    }

    private SoundUtils() {}
}
