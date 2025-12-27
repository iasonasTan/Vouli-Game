package app.menu;

import app.lib.gui.UI;
import app.lib.io.Resources;
import app.lib.media.Sound;
import app.game.Game;
import app.lib.gui.layout.VerticalFlowLayout;
import app.menu.abstraction.AbstractScreen;
import app.lib.gui.style.ComponentFactory;
import app.lib.gui.style.SimpleStyleLoader;
import app.lib.gui.style.SimpleStyler;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.function.Consumer;

public class Menu extends AbstractScreen {
    // swing components
    private final JButton mStartGameButton = new JButton("Start Game"),
            mExitButton = new JButton("Exit"),
            mSettingsButton = new JButton("Settings");

    public Menu() {
        initSwing();
        UI.load();
        Sound.load();
    }

    private void initSwing() {
        setLayout(new GridBagLayout());
        addListeners();

        InputStream is = Menu.class.getResourceAsStream("/menu/styles/menu_style.style");
        SimpleStyler simpleStyler = new SimpleStyler(SimpleStyleLoader.instance.loadStyle(is));
        simpleStyler.styleComponents(mStartGameButton, mExitButton, mSettingsButton);
        ComponentFactory factory = new ComponentFactory(simpleStyler);

        Consumer<JPanel> styleConsumer = p -> {
            p.setLayout(new VerticalFlowLayout(10,10));
            p.setPreferredSize(new Dimension(300, 500));
            p.setBackground(new Color(50, 50, 50));
        };
        JComponent[] components = {
                factory.newComponent(JLabel.class, "Vouli Game"),
                mStartGameButton,
                mSettingsButton,
                mExitButton,
                factory.newComponent(JLabel.class, "Version 2.4"),
                factory.newComponent(JLabel.class, "Made by JasonTan in 6 hours.")
        };
        components[0].setFont(components[0].getFont().deriveFont(25f));
        add(UI.createPanel(null, styleConsumer, components), new GridBagConstraints());
    }

    private void addListeners() {
        mStartGameButton.addActionListener(ae -> {
            Game game = new Game();
            game.setVisible();
            game.start();
        });
        mExitButton.addActionListener(ae -> {
            dispose();
            System.exit(0);
        });
        mSettingsButton.addActionListener(ae -> new Settings().setVisible());
    }

    @Override
    public AbstractScreen setVisible() {
        super.setVisible();
        setPreferredSize(getFrame().getSize());
        return this;
    }

    @Override
    protected String title() {
        return "Vouli Game - Menu";
    }

    @Override
    protected Image background() {
        return Resources.loadImage("/background.jpg");
    }

    @Override
    protected Image icon() {
        return Resources.loadImage("/app_icon.png");
    }
}
