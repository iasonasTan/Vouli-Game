package app.ui.style;

import java.awt.*;

public record Style(Font font, Color background, Color foreground, Dimension size, boolean focusable, int alignment) {
}
