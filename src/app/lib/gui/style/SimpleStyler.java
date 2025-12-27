package app.lib.gui.style;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class SimpleStyler {
    private final Consumer<JComponent> mStyler;
    private final Style mStyle;

    public SimpleStyler(Consumer<JComponent> styler) {
        mStyler = styler;
        mStyle = null;
    }

    public SimpleStyler(Style style) {
        mStyler = null;
        mStyle = style;
    }

    @SuppressWarnings("all")
    public JComponent[] styleComponents(JComponent... components) {
        for (JComponent component : components) {
            styleComponent(component.getClass(), component);
        }
        return components;
    }

    public <T extends JComponent> T styleComponent(Class<T> type, JComponent component) {
        if(mStyle != null) {
            component.setBackground(mStyle.getColor("background"));
            component.setForeground(mStyle.getColor("foreground"));
            component.setPreferredSize(mStyle.getDimension("componentSize"));
            component.setFont(mStyle.getFont("font"));
            component.setFocusable(mStyle.getBoolean("focusable"));
            if(component instanceof JButton)
                ((JButton)component).setHorizontalAlignment(mStyle.getInteger("alignment"));
            if(component instanceof JLabel) {
                Image image = mStyle.getImage("image");
                if(image!=null)
                    ((JLabel) component).setIcon(new ImageIcon(image));
            }
        }
        if(mStyler!=null) {
            mStyler.accept(component);
        }
        return type.cast(component);
    }
}
