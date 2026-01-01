package app.menu;

import app.game.Game;
import app.lib.gui.layout.VerticalFlowLayout;
import app.lib.gui.style.ComponentFactory;
import app.lib.gui.style.SimpleStyleLoader;
import app.lib.gui.style.SimpleStyler;
import app.lib.gui.style.Style;
import app.lib.io.Resources;
import app.lib.gui.AbstractScreen;

import javax.swing.*;
import java.awt.*;

public final class GameOverScreen extends AbstractScreen {
    private final JButton mExitButton, mReplayButton;
    private final JLabel mTitleLabel;

    {
        Style style = SimpleStyleLoader.instance.loadStyle("/res/menu/styles/menu_style.style");
        ComponentFactory factory = new ComponentFactory(new SimpleStyler(style));
        mExitButton = factory.newComponent(JButton.class, "Menu");
        mReplayButton = factory.newComponent(JButton.class, "Play Again");
        mTitleLabel = factory.newComponent(JLabel.class, "Game Over!");
    }

    public GameOverScreen() {
        initSwing();
    }

    private void initSwing() {
        setLayout(new GridBagLayout());
        addComponentBuilder(new JPanel(), new GridBagConstraints())
                .addChildren(mTitleLabel, mExitButton, mReplayButton)
                .setLayout(new VerticalFlowLayout(10, 10))
                .setSize(new Dimension(300, 300))
                .setBackground(new Color(50, 50, 50))
                .build();

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
        return Resources.loadImage("/res/background.jpg");
    }

    @Override
    protected Image icon() {
        return Resources.loadImage("/res/app_icon.png");
    }
}
