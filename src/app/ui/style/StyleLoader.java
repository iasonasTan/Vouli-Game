package app.ui.style;

import app.io.InputProperties;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public final class StyleLoader {
    public static StyleLoader instance = new StyleLoader();

    public Style loadStyle(InputStream input) {
        InputProperties styleProperties = new InputProperties(input);

        // colors
        Color foreground = getColor(styleProperties.getString("foreground"));
        Color background = getColor(styleProperties.getString("background"));

        // size
        String[] sizeText = styleProperties.getString("size").split("x");
        Dimension dimension = new Dimension(Integer.parseInt(sizeText[0]), Integer.parseInt(sizeText[1]));

        // focusable, alignment
        boolean focusable = styleProperties.getBoolean("focusable");
        int alignment = getAlignment(styleProperties.getString("alignment"));

        // font
        Font font;
        try {
            InputStream fontStream = Styler.class.getResourceAsStream(styleProperties.getString("font_family"));
            float size = styleProperties.getFloat("font_size");
            // noinspection all : nullpointerexception handled
            font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(size);
        } catch (IOException | FontFormatException | NullPointerException e) {
            font = Font.getFont(Font.SANS_SERIF);
        }
        return new Style(font, background, foreground, dimension, focusable, alignment);
    }

    private int getAlignment(String alignment) {
        if (alignment.equals("left")) {
            return SwingConstants.LEFT;
        } else if (alignment.equals("right")) {
            return SwingConstants.RIGHT;
        } else {
            return SwingConstants.CENTER;
        }
    }

    private Color getColor(String text) {
        String[] colorStr = text.split(",");
        int[] color = new int[3];
        for (int i = 0; i < colorStr.length; i++)
            color[i] = Integer.parseInt(colorStr[i]);
        return new Color(color[0], color[1], color[2]);
    }

    private StyleLoader(){}
}
