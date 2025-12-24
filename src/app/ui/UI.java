package app.ui;

import app.IO;
import app.io.InputProperties;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.function.Consumer;

public final class UI {
    public static boolean sSystemFullScreen = false;

    public static void init() {
        try(InputStream inputStream= IO.getConfigInputStream("settings.properties")) {
            InputProperties properties = new InputProperties(inputStream);
            sSystemFullScreen = properties.getBoolean("sys_full_scr");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private UI() {
    }

    public static JPanel createPanel(JPanel parent, Consumer<JPanel> configurer, Component... comps) {
        JPanel panel = new JPanel();
        if(configurer!=null)
            configurer.accept(panel);
        for (Component comp : comps) {
            panel.add(comp);
        }
        if (parent != null)
            parent.add(panel);
        return panel;
    }

    public static JFrame createFrame(JPanel panel, String title, Image icon) {
        JFrame frame = new JFrame(title);
        frame.setIconImage(icon);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        if(sSystemFullScreen) {
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .setFullScreenWindow(frame);
        } else {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setPreferredSize(size);
            frame.setSize(size);
        }
        frame.setVisible(true);
        return frame;
    }

    public static BufferedImage loadImage(String path) {
        try {
            URL rsrcUrl = UI.class.getResource(path);
            if (rsrcUrl == null) {
                System.err.println("Failed to get image " + path);
                return null;
            }
            return ImageIO.read(rsrcUrl);
        } catch (IOException e) {
            System.err.println("Failed to get image " + path);
            return null;
        }
    }
}
