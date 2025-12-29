package app.lib.gui;

import app.lib.UtilNotInitializedException;
import app.lib.gui.layout.VerticalFlowLayout;
import app.lib.io.Configuration;
import app.lib.io.InputProperties;
import app.lib.io.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

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
            throw new UtilNotInitializedException();
    }

    public static void showException(Exception exception) {
        String messageText = String.format("""
                        The application has a bug (%s).
                        The reason: %s
                        Please report this to the developer (iasonas.tan@gmail.com)
                        """,
                exception.getClass().getName(),
                exception.getMessage()==null?"No details":exception.getMessage()
        );
        Image image = Resources.loadImage("alert.png");
        image = image.getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
        JPanel message = newComponentBuilder(new JPanel(), toJLabels((Object[]) messageText.split("\n")))
                .setLayout(new VerticalFlowLayout(10, 10))
                .build();
        JOptionPane.showMessageDialog(null,  message,"Unexpected Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(image));
    }

    public static JLabel[] toJLabels(Object... objects) {
        JLabel[] labels = new JLabel[objects.length];
        for (int i = 0; i < objects.length; i++) {
            labels[i] = new JLabel(objects[i].toString());
        }
        return labels;
    }

    public static <C extends JComponent> ComponentBuilder<C> newComponentBuilder(C component) {
        return new JComponentBuilder<>(component);
    }

    public static <C extends JComponent> ComponentBuilder<C> newComponentBuilder(C component, Component... childs) {
        for (Component child : childs) {
            component.add(child);
        }
        return new JComponentBuilder<>(component);
    }

    private static class JComponentBuilder<C extends JComponent> extends AbstractComponentBuilder<C> {
        public JComponentBuilder(C comp) {
            super(comp);
        }

        @Override
        public C build() {
            return component;
        }
    }

    private UI() {}
}
