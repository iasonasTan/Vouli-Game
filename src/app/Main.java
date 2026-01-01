package app;

import app.lib.gui.UI;
import app.lib.io.Configuration;
import app.lib.io.Resources;
import app.lib.media.Sound;
import app.menu.Menu;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;

public class Main {
    public static final double APPLICATION_VERSION = 1.1;

    public static void main(String[] args) {
        Configuration.init("vouli_game");
        Resources.init();

        UI.init();
        Sound.load();

        Clip music = Resources.loadClip("/res/game/background_music.wav");
        Sound.playMusic(music);

        UpdateChecker updateChecker = new UpdateChecker();
        updateChecker.checkVersionAsynchronously();

        new Menu().setVisible();
    }

    private static final class UpdateChecker {
        public void checkVersionAsynchronously() {
            new Thread(this::checkVersion).start();
        }

        public void checkVersion() {
            try {
                String latestVersion = requestVersionFromServer();
                String appVersion = String.valueOf(APPLICATION_VERSION);
                if(needsUpdate(appVersion, latestVersion))
                    showUpdateDialog();
            } catch (IOException e) {
                // causes: no internet, DNS problem or server is closed
                // ignore, no version check
                System.out.println(e);
            }
        }

        private boolean needsUpdate(String appV, String latV) {
            double latestVersion = Double.parseDouble(latV);
            double appVersion = Double.parseDouble(appV);
            System.out.println("Latest version: " + latestVersion + ", App version: " + appVersion);
            return appVersion < latestVersion;
        }

        private String requestVersionFromServer() throws IOException {
            BufferedWriter writer = null;
            BufferedReader reader = null;
            Socket socket = null;
            try {
                System.out.println("Checking for updates...");
                socket = new Socket("iasonas.duckdns.org", 1422);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.write("get_version_code?vouli_game");
                writer.append('\n');
                writer.flush();
                return reader.readLine();
            } finally {
                if(reader != null && !socket.isClosed()) {
                    reader.close();
                }
                if(writer!= null && !socket.isClosed()) {
                    writer.write("disconnect\n");
                    writer.flush();
                    writer.close();
                }
                if(socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        }

        private void showUpdateDialog() {
            System.out.println("Asking user to update...");

            int userReply = JOptionPane.showConfirmDialog(null, "New version available, would you like to update?", "Update available.", JOptionPane.YES_NO_OPTION);
            if(userReply==JOptionPane.YES_OPTION) {
                String messageTxt = """
                                    Download update from this website:
                                    http://iasonas.duckdns.org/vouli-game/index.html
                                    To copy the link you can highlight it and press Ctrl+C.
                                    """;
                JTextArea message = UI.newComponentBuilder(new JTextArea())
                        .setText(messageTxt)
                        .setEditable(false)
                        .build();
                JOptionPane.showMessageDialog(null, message);
            }
        }
    }
}
