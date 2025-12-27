package app.lib.gui.style;

import java.io.InputStream;

public interface StyleLoader {
    Style loadStyle(InputStream inputStream);
    Style loadStyle(String path);
}
