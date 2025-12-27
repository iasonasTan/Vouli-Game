package app.menu;

import app.lib.io.Configuration;
import app.Main;
import app.lib.gui.UI;
import app.lib.gui.layout.VerticalFlowLayout;
import app.lib.io.InputProperties;
import app.lib.io.OutputProperties;
import app.lib.io.Resources;
import app.menu.abstraction.AbstractScreen;
import app.lib.gui.style.SimpleStyleLoader;
import app.lib.gui.style.SimpleStyler;

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
        try (InputStream inputStream = Configuration.getConfigInputStream("settings.properties", true)) {
            InputProperties properties = new InputProperties(inputStream);
            mSysFullScrCheckBox.setSelected(properties.getBoolean("sys_full_scr", true));
            mEnableMusicCheckBox.setSelected(properties.getBoolean("enable_music", true));
            mEnableSFXCheckBox.setSelected(properties.getBoolean("enable_sfx", true));
        } catch (IOException e) {
            UI.showException(e);
            throw new RuntimeException(e);
        }
    }

    private void initSwing() {
        InputStream is = Menu.class.getResourceAsStream("/menu/styles/settings_style.style");
        SimpleStyler simpleStyler = new SimpleStyler(SimpleStyleLoader.instance.loadStyle(is));
        simpleStyler.styleComponents(mExitButton, mSysFullScrCheckBox, mEnableMusicCheckBox, mEnableSFXCheckBox, mShowInstructionsButton);

        setLayout(new GridBagLayout());
        Consumer<JPanel> styleConsumer = p -> {
            p.setLayout(new VerticalFlowLayout(10, 10));
            p.setPreferredSize(new Dimension(520, 500));
            p.setBackground(new Color(50, 50, 50));
        };
        JComponent[] components = {
                mSysFullScrCheckBox, mEnableMusicCheckBox, mEnableSFXCheckBox, mShowInstructionsButton, mExitButton,
                new JTextField("Changes to some settings will take \neffect after restarting the application.")
        };
        add(UI.createPanel(null, styleConsumer, components), new GridBagConstraints());
        mExitButton.addActionListener(new OnExitListener());
        mShowInstructionsButton.addActionListener(OnShowInstructionsListener.fromResource("/instructions.txt"));
        JTextField field = (JTextField) components[5];
        field.setEditable(false);
        field.setBackground(Color.RED);
        field.setForeground(Color.WHITE);
    }

    private static final class OnShowInstructionsListener implements ActionListener {
        private final String mInstructions;

        private OnShowInstructionsListener(String instructions) {
            this.mInstructions = instructions;
        }

        public static OnShowInstructionsListener fromResource(String path) {
            // noinspection all : nullpointerexception handled
            try (BufferedReader br = new BufferedReader(new InputStreamReader(Settings.class.getResourceAsStream(path)))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                return new OnShowInstructionsListener(sb.toString());
            } catch (IOException | NullPointerException e) {
                UI.showException(e);
                return new OnShowInstructionsListener("Failed to load instructions.");
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, mInstructions);
        }
    }

    private final class OnExitListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent actionEvent) {
            try (OutputStream outputStream = Configuration.getConfigOutputStream("settings.properties")) {
                OutputProperties properties = new OutputProperties();
                properties.put("sys_full_scr", mSysFullScrCheckBox.isSelected());
                properties.put("enable_music", mEnableMusicCheckBox.isSelected());
                properties.put("enable_sfx", mEnableSFXCheckBox.isSelected());
                properties.store(outputStream);
            } catch (IOException e) {
                UI.showException(e);
                throw new RuntimeException(e);
            }
            Main.initUtils();
            new Menu().setVisible();
        }
    }

    @Override
    protected String title() {
        return "Vouli Game - Settings";
    }

    @Override
    protected Image background() {
        return Resources.loadImage("/background.jpg");
    }

    @Override
    protected Image icon() {
        return Resources.loadImage("/app_icon.png");
    }

    @Override
    public AbstractScreen setVisible() {
        super.setVisible();
        setPreferredSize(getFrame().getSize());
        return this;
    }
}
