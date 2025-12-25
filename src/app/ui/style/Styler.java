package app.ui.style;

import javax.swing.*;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class Styler {
    private final Consumer<JComponent> mStyler;
    private final Style mStyle;

    public Styler(Consumer<JComponent> styler) {
        mStyler = styler;
        mStyle = null;
    }

    public Styler(Style style) {
        mStyler = null;
        mStyle = style;
    }

    public JComponent styleComponent(JComponent component) {
        if(mStyle != null) {
            component.setBackground(mStyle.background());
            component.setForeground(mStyle.foreground());
            component.setPreferredSize(mStyle.size());
            component.setFont(mStyle.font());
            component.setFocusable(mStyle.focusable());
            if(component instanceof JButton)
                ((JButton)component).setHorizontalAlignment(mStyle.alignment());
        }
        if(mStyler!=null) {
            mStyler.accept(component);
        }
        return component;
    }

    @SuppressWarnings("all")
    public JComponent[] styleComponents(JComponent... components) {
        for (JComponent component : components) {
            styleComponent(component);
        }
        return components;
    }
}
