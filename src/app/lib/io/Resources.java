package app.lib.io;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

public final class Resources {
    private static BufferedImage mCannotLoadImage;

    public static void init() {
        try {
            InputStream inputStream = Resources.class.getResourceAsStream("cannotloadimage.png");
            if(inputStream!=null)
                mCannotLoadImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            // ignore
        }
    }

    public static BufferedImage loadImage(String path) {
        return loadImage(path, Resources.class);
    }

    public static BufferedImage loadImage(final String path, Class<?> clazz) {
        final Supplier<BufferedImage> errorHandler = () -> {
            System.out.println("Cannot load image "+path);
            return mCannotLoadImage;
        };
        try {
            URL rsrcUrl = clazz.getResource(path);
            if (rsrcUrl == null) {
                System.err.println("Failed to load image '"+path+"'");
                return errorHandler.get();
            }
            BufferedImage image = ImageIO.read(rsrcUrl);
            return image == null ? errorHandler.get() : image;
        } catch (IOException e) {
            return errorHandler.get();
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