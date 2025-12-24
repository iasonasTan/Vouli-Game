package app.io;

import app.ui.UI;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InputProperties {
    private final Properties mProperties = new Properties();

    public InputProperties(InputStream inputStream) {
        try {
            mProperties.load(inputStream);
        } catch (IOException e) {
            UI.showException(e);
            throw new RuntimeException(e);
        }
    }

    public String getString(String key) {
        return mProperties.getProperty(key);
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    public int getInteger(String key) throws NumberFormatException {
        return Integer.parseInt(getString(key));
    }

    public float getFloat(String key) throws NumberFormatException {
        return Float.parseFloat(getString(key));
    }
}
