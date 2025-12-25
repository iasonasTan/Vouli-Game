package app;

import app.ui.UI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Configuration {
    private static String sConfigDirectory;

    public static void init() {
        Path systemConfigDir;
        String osName = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        if(osName.contains("win")) {
            systemConfigDir = Paths.get(System.getenv("APPDATA"));
        } else if (osName.contains("mac")) {
            systemConfigDir = Paths.get(userHome, "Library", "Application Support");
        } else { // Unix, Linux
            systemConfigDir = Paths.get(userHome, ".config");
        }
        Path configDir = Paths.get(systemConfigDir.toAbsolutePath().toString(), "vouli_game");
        if(!Files.isDirectory(configDir) || !Files.exists(configDir)) {
            try {
                Files.createDirectory(configDir);
            } catch (IOException e) {
                UI.showException(e);
                throw new RuntimeException(e);
            }
        }
        sConfigDirectory = configDir.toAbsolutePath().toString();
    }

    public static OutputStream getConfigOutputStream(String fileName) {
        try {
            return Files.newOutputStream(getConfigPath(fileName));
        } catch (IOException e) {
            UI.showException(e);
            throw new RuntimeException(e);
        }
    }

    public static InputStream getConfigInputStream(String fileName) {
        try {
            return Files.newInputStream(getConfigPath(fileName));
        } catch (IOException e) {
            UI.showException(e);
            throw new RuntimeException(e);
        }
    }

    public static Path getConfigPath(String fileName) {
        Path out = Paths.get(sConfigDirectory, fileName);
        if (!Files.isRegularFile(out) || !Files.exists(out)) {
            try {
                Files.createFile(out);
            } catch (IOException e) {
                UI.showException(e);
                throw new RuntimeException(e);
            }
        }
        return out;
    }

    private Configuration(){}
}
