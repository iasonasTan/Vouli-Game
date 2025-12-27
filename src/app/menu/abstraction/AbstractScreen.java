package app.menu.abstraction;

import app.lib.gui.UI;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractScreen extends JPanel implements Screen {
    private static JFrame sFrame;

    public static void dispose() {
        sFrame.dispose();
    }

    private final Image mBackgroundImage;

    public AbstractScreen() {
        mBackgroundImage = background();
    }

    protected abstract String title();
    protected abstract Image background();
    protected abstract Image icon();

    public JFrame getFrame() {
        return sFrame;
    }

    @Override
    public AbstractScreen setVisible() {
        if(sFrame ==null)
            sFrame = UI.createFrame(this, title(), icon());

        setPreferredSize(sFrame.getSize());
        System.out.println(sFrame.getSize());
        sFrame.setContentPane(this);
        sFrame.setTitle(title());
        sFrame.setIconImage(icon());

        sFrame.revalidate();
        sFrame.repaint();

        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
        return this;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if(mBackgroundImage!=null) {
            Dimension pSize = getPreferredSize();
            graphics.drawImage(mBackgroundImage, 0, 0, pSize.width, pSize.height, null);
        }
    }
}
