package app.menu;

import app.game.Game;
import app.lib.gui.UI;
import app.lib.gui.layout.VerticalFlowLayout;
import app.lib.gui.style.ComponentFactory;
import app.lib.gui.style.SimpleStyleLoader;
import app.lib.gui.style.SimpleStyler;
import app.lib.gui.style.Style;
import app.lib.io.Configuration;
import app.lib.io.Resources;
import app.menu.abstraction.AbstractScreen;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public final class GameOverScreen extends AbstractScreen {
    private final JButton mExitButton, mReplayButton;
    private final JLabel mTitleLabel;

    {
        Style style = SimpleStyleLoader.instance.loadStyle("/menu/styles/menu_style.style");
        ComponentFactory factory = new ComponentFactory(new SimpleStyler(style));
        mExitButton = factory.newComponent(JButton.class, "Menu");
        mReplayButton = factory.newComponent(JButton.class, "Play Again");
        mTitleLabel = factory.newComponent(JLabel.class, "Game Over!");
    }

    public GameOverScreen() {
        initSwing();
    }

    @SuppressWarnings("all") // : intellij tells lies
    private void initSwing() {
        setLayout(new GridBagLayout());
        Consumer<JPanel> configurer = p -> {
            p.setLayout(new VerticalFlowLayout(10, 10));
            p.setPreferredSize(new Dimension(300, 300));
            p.setBackground(new Color(50, 50, 50));
        };
        add(
                UI.createPanel(null, configurer, mTitleLabel, mExitButton, mReplayButton),
                new GridBagConstraints()
        );
        mExitButton.addActionListener(ae -> new Menu().setVisible());
        mReplayButton.addActionListener(ae -> {
            Game game = new Game();
            game.setVisible();
            game.start();
        });
    }

    @Override
    protected String title() {
        return "Vouli Game - Game Over";
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
