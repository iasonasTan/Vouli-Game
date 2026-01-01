package app.lib.gui.style;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public final class SimpleStyler implements Styler {
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

    @SafeVarargs
    @Override
    public final <T extends JComponent> T[] styleComponents(T... components) {
        for (T component : components) {
            styleComponent(component);
        }
        return components;
    }

    @Override
    public <T extends JComponent> T styleComponent(T component) {
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
        return component;
    }
}
