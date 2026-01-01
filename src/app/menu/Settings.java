package app.menu;

import app.lib.io.Configuration;
import app.lib.gui.UI;
import app.lib.gui.layout.VerticalFlowLayout;
import app.lib.io.InputProperties;
import app.lib.io.OutputProperties;
import app.lib.io.Resources;
import app.lib.gui.AbstractScreen;
import app.lib.gui.style.SimpleStyleLoader;
import app.lib.gui.style.SimpleStyler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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
        InputProperties properties = new InputProperties();
        Configuration.loadProperties("settings.properties", properties);
        mSysFullScrCheckBox.setSelected(properties.getBoolean("sys_full_scr", true));
        mEnableMusicCheckBox.setSelected(properties.getBoolean("enable_music", true));
        mEnableSFXCheckBox.setSelected(properties.getBoolean("enable_sfx", true));
    }

    private void initSwing() {
        InputStream is = Menu.class.getResourceAsStream("/res/menu/styles/settings_style.style");
        SimpleStyler styler = new SimpleStyler(SimpleStyleLoader.instance.loadStyle(is));

        setLayout(new GridBagLayout());

        JTextField warningField = UI.newComponentBuilder(new JTextField("Changes will take effect after restarting the application."))
                .setEditable(false)
                .setBackground(Color.RED)
                .setForeground(Color.WHITE)
                .build();

        JComponent[] components = {mSysFullScrCheckBox, mEnableMusicCheckBox, mEnableSFXCheckBox, mShowInstructionsButton, mExitButton};

        styler.styleComponents(components);

        addComponentBuilder(new JPanel(), new GridBagConstraints())
                .setSize(new Dimension(520, 500))
                .setLayout(new VerticalFlowLayout(10, 10))
                .setBackground(new Color(50, 50, 50))
                .addChildren(components)
                .addChildren(warningField)
                .build();

        mExitButton.addActionListener(new OnExitListener());
        mShowInstructionsButton.addActionListener(ae -> new HowToPlay().setVisible());
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
            //Main.initUtils();
            new Menu().setVisible();
        }
    }

    @Override
    protected String title() {
        return "Vouli Game - Settings";
    }

    @Override
    protected Image background() {
        return Resources.loadImage("/res/background.jpg");
    }

    @Override
    protected Image icon() {
        return Resources.loadImage("/res/app_icon.png");
    }
}
