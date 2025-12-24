package app.io;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class OutputProperties {
    private final Properties mProperties = new Properties();

    public OutputProperties() {
    }

    public void put(String key, String value) {
        mProperties.put(key, value);
    }

    public void put(String key, boolean value) {
        mProperties.put(key, String.valueOf(value));
    }

    public void put(String key, int value) {
        mProperties.put(key, String.valueOf(value));
    }

    public void put(String key, float value) {
        mProperties.put(key, String.valueOf(value));
    }

    public void store(OutputStream outputStream) {
        try {
            mProperties.store(outputStream, "Updated Settings");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
