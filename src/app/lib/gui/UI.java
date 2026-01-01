package app.lib.gui;

import app.lib.UtilNotInitializedException;
import app.lib.io.Configuration;
import app.lib.io.InputProperties;
import app.lib.io.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class UI {
    private static boolean sInitialized = false;
    public static boolean sSystemFullScreen = false;

    public static void init() {
        InputProperties properties = new InputProperties();
        Configuration.loadProperties("settings.properties", properties);
        sSystemFullScreen = properties.getBoolean("sys_full_scr", true);
        sInitialized = true;
    }

    public static void check() {
        if(!sInitialized)
            throw new UtilNotInitializedException();
    }

    public static void showException(Exception exception) {
        String messageText = String.format("""
                        The application has a bug "%s: %s".
                        
                        Stacktrace:
                        %s
                        Please send this to the developer (iasonas.tan@gmail.com)
                        """,
                exception.getClass().getName(),
                exception.getMessage()==null?"No message details.":exception.getLocalizedMessage(),
                stacktraceToString(exception.getStackTrace())
        );
        Image image = Resources.loadImage("alert.png", UI.class);
        image = image.getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
        JTextArea textArea = UI.newComponentBuilder(new JTextArea())
                .setEditable(false)
                .setText(messageText)
                .build();
        JPanel panel = UI.newComponentBuilder(new JPanel())
                .addChildren(textArea)
                .setLayout(new FlowLayout())
                .build();
        JOptionPane.showMessageDialog(null, panel, "Unexpected Error", JOptionPane.ERROR_MESSAGE, new ImageIcon(image));
    }

    private static String stacktraceToString(StackTraceElement[] stackTrace) {
        StringBuilder messageBuilder = new StringBuilder();
        for (StackTraceElement stackTraceElement: stackTrace) {
            messageBuilder
                    .append(stackTraceElement.toString())
                    .append('\n');
        }
        return messageBuilder.toString();
    }

    public static <C extends JComponent> ComponentBuilder<C> newComponentBuilder(C component) {
        return new JComponentBuilder<>(component);
    }

    /**
     * @deprecated Use {@link ComponentBuilder#addChildren(JComponent...)} for adding children.
     */
    @Deprecated
    public static <C extends JComponent> ComponentBuilder<C> newComponentBuilder(C component, Component... children) {
        for (Component child : children) {
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
