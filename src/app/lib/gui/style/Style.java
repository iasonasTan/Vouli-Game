package app.lib.gui.style;

import java.awt.*;

public interface Style {
    Object get(String key);

    default Color getColor(String key) {
        return (Color)get(key);
    }

    default Dimension getDimension(String key) {
        return (Dimension)get(key);
    }

    default boolean getBoolean(String key) {
        return (Boolean)get(key);
    }

    default Font getFont(String key) {
        return (Font)get(key);
    }

    default int getInteger(String key) {
        return (Integer)get(key);
    }

    default Image getImage(String key) {
        return (Image)get(key);
    }
}
