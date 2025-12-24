package app.ui;

import app.IO;
import app.game.util.VerticalFlowLayout;
import app.io.InputProperties;
import app.io.OutputProperties;
import app.ui.abstraction.AbstractScreen;
import app.ui.style.StyleLoader;
import app.ui.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.function.Consumer;

class Settings extends AbstractScreen {
    private final JButton mExitButton = new JButton("Save And Exit"),
            mShowInstructionsButton = new JButton("How to play");
    private final JCheckBox mSysFullScrCheckBox = new JCheckBox("Normal Full Screen (Disable if game isn't working correctly)"),
            mEnableMusicCheckBox = new JCheckBox("Enable Music"),
            mEnableSFXCheckBox = new JCheckBox("Enable Sound Effects");

    public Settings() {
        initSwing();
        loadSettings();
    }

    private void loadSettings() {
        try (InputStream inputStream = IO.getConfigInputStream("settings.properties")) {
            InputProperties properties = new InputProperties(inputStream);
            mSysFullScrCheckBox.setSelected(properties.getBoolean("sys_full_scr"));
            mEnableMusicCheckBox.setSelected(properties.getBoolean("enable_music"));
            mEnableSFXCheckBox.setSelected(properties.getBoolean("enable_sfx"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
            throw new RuntimeException(e);
        }
    }

    private void initSwing() {
        InputStream is = Menu.class.getResourceAsStream("/menu/styles/settings_style.style");
        Styler styler = new Styler(StyleLoader.instance.loadStyle(is));
        styler.styleComponents(mExitButton, mSysFullScrCheckBox, mEnableMusicCheckBox, mEnableSFXCheckBox, mShowInstructionsButton);

        setLayout(new GridBagLayout());
        Consumer<JPanel> styleConsumer = p -> {
            p.setLayout(new VerticalFlowLayout(10, 10));
            p.setPreferredSize(new Dimension(520, 500));
            p.setBackground(new Color(50, 50, 50));
        };
        JComponent[] components = {
                mSysFullScrCheckBox, mEnableMusicCheckBox, mEnableSFXCheckBox, mShowInstructionsButton, mExitButton
        };
        add(UI.createPanel(null, styleConsumer, components), new GridBagConstraints());
        mExitButton.addActionListener(new ExitListener());
        mShowInstructionsButton.addActionListener(OnShowInstructionsListener.fromResource("/instructions.txt"));
    }

    private static final class OnShowInstructionsListener implements ActionListener {
        private final String mInstructions;

        private OnShowInstructionsListener(String instructions) {
            this.mInstructions = instructions;
        }

        public static OnShowInstructionsListener fromResource(String path) {
            // noinspection all : nullpointerexception handled
            try (BufferedReader br = new BufferedReader(new InputStreamReader(Settings.class.getResourceAsStream("/instructions.txt")))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                return new OnShowInstructionsListener(sb.toString());
            } catch (IOException | NullPointerException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return new OnShowInstructionsListener("Failed to load instructions.");
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, mInstructions);
        }
    }

    private final class ExitListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent actionEvent) {
            try (OutputStream outputStream = IO.getConfigOutputStream("settings.properties")) {
                OutputProperties properties = new OutputProperties();
                properties.put("sys_full_scr", mSysFullScrCheckBox.isSelected());
                properties.put("enable_music", mEnableMusicCheckBox.isSelected());
                properties.put("enable_sfx", mEnableSFXCheckBox.isSelected());
                properties.store(outputStream);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                throw new RuntimeException(e);
            }
            new Menu().setVisible();
        }
    }

    @Override
    protected String title() {
        return "Vouli Game - Settings";
    }

    @Override
    protected Image background() {
        return UI.loadImage("/background.jpg");
    }

    @Override
    protected Image icon() {
        return UI.loadImage("/app_icon.png");
    }

    @Override
    public void setVisible() {
        super.setVisible();
        setPreferredSize(getFrame().getSize());
    }
}
