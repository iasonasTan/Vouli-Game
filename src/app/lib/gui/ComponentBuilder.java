package app.lib.gui;

import app.lib.gui.style.Styler;

import javax.swing.*;
import java.awt.*;

public interface ComponentBuilder<C> {
    ComponentBuilder<C> setBackground(Color color);
    ComponentBuilder<C> setSize(Dimension size);
    ComponentBuilder<C> setLayout(LayoutManager layout);
    ComponentBuilder<C> setFont(Font font);
    ComponentBuilder<C> style(Styler styler);
    ComponentBuilder<C> setForeground(Color color);
    ComponentBuilder<C> setEditable(boolean editable);
    ComponentBuilder<C> setText(String text);
    ComponentBuilder<C> addChildren(JComponent... children);

    C build();
}
