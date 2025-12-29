package app.menu;

import app.game.Game;
import app.lib.gui.AbstractScreen;
import app.lib.gui.UI;
import app.lib.gui.layout.VerticalFlowLayout;
import app.lib.gui.style.ComponentFactory;
import app.lib.gui.style.SimpleStyleLoader;
import app.lib.gui.style.SimpleStyler;
import app.lib.gui.style.Style;
import app.lib.io.Resources;
import app.lib.media.Sound;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

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
        Style style = SimpleStyleLoader.instance.loadStyle(is);
        SimpleStyler styler = new SimpleStyler(style);
        ComponentFactory factory = new ComponentFactory(styler);

        JLabel titleLabel = UI.newComponentBuilder(new JLabel())
                .setText("Vouli Game")
                .setFont(style.getFont("font").deriveFont(25f))
                .build();

        JComponent[] components = {
                titleLabel,
                mStartGameButton,
                mSettingsButton,
                mExitButton,
                factory.newComponent(JLabel.class, "Version 2.4"),
                factory.newComponent(JLabel.class, "Made by JasonTan in 6 hours.")
        };

        styler.styleComponents(components);

        addComponentBuilder(new JPanel(), new GridBagConstraints())
                .addChildren(components)
                .setLayout(new VerticalFlowLayout(10, 10))
                .setSize(new Dimension(300, 500))
                .setBackground(new Color(50, 50, 50))
                .build();
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
