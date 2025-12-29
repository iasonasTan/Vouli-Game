package app.lib.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractScreen extends JPanel implements Screen {
    private static JFrame sFrame;

    public static void dispose() {
        sFrame.dispose();
    }

    private final Image mBackgroundImage;

    public AbstractScreen() {
        mBackgroundImage = background();
    }

    public static JFrame createFrame(JPanel panel, String title, Image icon) {
        UI.check();
        JFrame frame = new JFrame(title);
        frame.setIconImage(icon);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        if(UI.sSystemFullScreen) {
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .setFullScreenWindow(frame);
        } else {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setPreferredSize(size);
            frame.setSize(size);
        }
        frame.setVisible(true);
        return frame;
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
            sFrame = createFrame(this, title(), icon());

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

    public <C extends JComponent> ComponentBuilder<C> addComponentBuilder(C comp, Object param) {
        return new ComponentAdderBuilder<>(comp, param);
    }

    @Deprecated
    public <C extends JComponent> ComponentBuilder<C> addComponentBuilder(C comp, Object param, JComponent... children) {
        return new ComponentAdderBuilder<>(comp, param, children);
    }

    private final class ComponentAdderBuilder<C extends JComponent> extends AbstractComponentBuilder<C> {
        private final Object mParam;
        private final java.util.List<JComponent> mChildren;

        public ComponentAdderBuilder(C c, Object param, JComponent... children) {
            super(c);
            this.mParam = param;
            mChildren = new ArrayList<>(Arrays.asList(children));
        }

        @Override
        public ComponentBuilder<C> addChildren(JComponent... children) {
            mChildren.addAll(List.of(children));
            return this;
        }

        @Override
        public C build() {
            for (JComponent child: mChildren)
                component.add(child);

            if(mParam == null)
                add(component);
            else
                add(component, mParam);
            return component;
        }
    }
}
