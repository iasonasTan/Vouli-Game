package app.menu;

import app.lib.gui.AbstractScreen;
import app.lib.gui.UI;
import app.lib.gui.layout.VerticalFlowLayout;
import app.lib.gui.style.*;
import app.lib.io.Resources;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HowToPlay extends AbstractScreen {
    private final JButton mBackButton;
    private final JTextArea mTextArea;

    {
        Style style = SimpleStyleLoader.instance.loadStyle("/res/menu/styles/settings_style.style");
        Styler styler = new SimpleStyler(style);
        ComponentFactory factory = new ComponentFactory(styler);
        mBackButton = factory.newComponent(JButton.class, "OK");
        mTextArea = UI.newComponentBuilder(new JTextArea())
                .style(styler)
                .setSize(new Dimension(500, 200))
                .setEditable(false)
                .build();
    }

    public HowToPlay() {
        initSwing();
    }

    private void initSwing() {
        setLayout(new GridBagLayout());

        mBackButton.addActionListener(ae -> new Menu().setVisible());
        try (BufferedReader br = new BufferedReader(new InputStreamReader(HowToPlay.class.getResourceAsStream("/res/instructions.txt")))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            mTextArea.setText(sb.toString());
        } catch (IOException | NullPointerException e) {
            UI.showException(e);
            throw new RuntimeException();
        }

        addComponentBuilder(new JPanel(), new GridBagConstraints())
                .addChildren(mBackButton, mTextArea)
                .setLayout(new VerticalFlowLayout(10, 10))
                .setBackground(new Color(50, 50, 50))
                .setSize(new Dimension(520, 300))
                .build();
    }

    @Override
    protected String title() {
        return "Vouli Game - How To Play";
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
