package app.lib.gui;

import app.lib.NotInitializedException;
import app.lib.io.Configuration;
import app.lib.io.InputProperties;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Consumer;

public final class UI {
    private static boolean sInitialized = false;
    public static boolean sSystemFullScreen = false;

    public static void load() {
        try (InputStream inputStream = Configuration.getConfigInputStream("settings.properties", true)) {
            InputProperties properties = new InputProperties(inputStream);
            sSystemFullScreen = properties.getBoolean("sys_full_scr", true);
            sInitialized = true;
        } catch (IOException e) {
            UI.showException(e);
            throw new RuntimeException(e);
        }
    }

    public static void check() {
        if(!sInitialized)
            throw new NotInitializedException();
    }

    public static void showException(Exception exception) {
        JOptionPane.showMessageDialog(null, exception.getMessage()+" \n, "+ Arrays.asList(exception.getStackTrace()));
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
        check();
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

    private UI() {}
}
