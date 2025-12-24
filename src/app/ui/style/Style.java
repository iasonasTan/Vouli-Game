package app.ui.style;

import java.awt.*;

public final class Style {
    public final Font font;
    public final Color background;
    public final Color foreground;
    public final Dimension size;
    public final boolean focusable;
    public final int alignment;

    public Style(Font font, Color background, Color foreground, Dimension size, boolean focusable, int alignment) {
        this.font = font;
        this.background = background;
        this.foreground = foreground;
        this.size = size;
        this.focusable = focusable;
        this.alignment = alignment;
    }
}
