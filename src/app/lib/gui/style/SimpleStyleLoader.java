package app.lib.gui.style;

import app.lib.io.InputProperties;
import app.lib.io.Resources;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class SimpleStyleLoader implements StyleLoader {
    public static final SimpleStyleLoader instance = new SimpleStyleLoader();

    @Override
    public Style loadStyle(String path) {
        return loadStyle(SimpleStyleLoader.class.getResourceAsStream(path));
    }

    @Override
    public Style loadStyle(InputStream input) {
        InputProperties styleProperties = new InputProperties();
        styleProperties.load(input);
        Map<String, Object> style = new HashMap<>();

        // images
        String imagePath = styleProperties.getString("image");
        if(imagePath != null)
            style.put("image", Resources.loadImage(imagePath, SimpleStyleLoader.class));
        else
            style.put("image", null);

        // colors
        style.put("foreground", getColor(styleProperties.getString("foreground")));
        style.put("background", getColor(styleProperties.getString("background")));

        // size
        String[] sizeText = styleProperties.getString("size").split("x");
        style.put("componentSize", new Dimension(Integer.parseInt(sizeText[0]), Integer.parseInt(sizeText[1])));

        // focusable, alignment
        style.put("focusable", styleProperties.getBoolean("focusable", false));
        style.put("alignment", getAlignment(styleProperties.getString("alignment")));

        // font
        Font font;
        try {
            InputStream fontStream = SimpleStyler.class.getResourceAsStream(styleProperties.getString("font_family"));
            float size = styleProperties.getFloat("font_size", 15f);
            // noinspection all : nullpointerexception handled
            font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(size);
        } catch (IOException | FontFormatException | NullPointerException e) {
            font = Font.getFont(Font.SANS_SERIF);
        }
        style.put("font", font);
        return new MapStyle(style);
    }

    /**
     * Converts alignment from string to integer from {@link SwingConstants}.
     * @param alignment Alignment as String.
     * @return alignment as integer from swing constants
     */
    public static int getAlignment(String alignment) {
        if (alignment.equals("left")) {
            return SwingConstants.LEFT;
        } else if (alignment.equals("right")) {
            return SwingConstants.RIGHT;
        } else {
            return SwingConstants.CENTER;
        }
    }

    /**
     * Converts color from string to AWT {@link Color}.
     * Color format must be this: r,g,b
     * @param text rgb color as string with the above format
     * @return The same color as AWT {@link Color}.
     */
    public static Color getColor(String text) {
        String[] colorStr = text.split(",");
        int[] color = new int[3];
        for (int i = 0; i < colorStr.length; i++)
            color[i] = Integer.parseInt(colorStr[i]);
        return new Color(color[0], color[1], color[2]);
    }

    private SimpleStyleLoader(){}

    private static class MapStyle implements Style {
        private final Map<String, Object> mStyles;

        public MapStyle(Map<String, Object> styles) {
            this.mStyles = styles;
        }

        @Override
        public Object get(String key) {
            return mStyles.get(key);
        }
    }
}
