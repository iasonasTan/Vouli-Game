package app.lib.io;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public final class Resources {
    private static BufferedImage mCannotLoadImage;

    @SuppressWarnings("all")
    public static void init() {
        try {
            mCannotLoadImage = ImageIO.read(Resources.class.getResourceAsStream("cannotloadimage.png"));
        } catch (IOException e) {
            // ignore
        }
    }

    public static BufferedImage loadImage(String path) {
        try {
            URL rsrcUrl = Resources.class.getResource(path);
            if (rsrcUrl == null) {
                System.err.println("Failed to get image " + path);
                return mCannotLoadImage;
            }
            BufferedImage image = ImageIO.read(rsrcUrl);
            return image == null ? mCannotLoadImage : image;
        } catch (IOException e) {
            return mCannotLoadImage;
        }
    }

    public static Clip loadClip(String path) {
        try {
            URL rsrcUrl = Resources.class.getResource(path);
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
            throw new CannotLoadClipException();
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

    private Resources() {}
}