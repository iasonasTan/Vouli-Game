package app.lib.gui;

import app.lib.gui.style.Styler;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public abstract class AbstractComponentBuilder<C extends JComponent> implements ComponentBuilder<C> {
    protected final C component;

    public AbstractComponentBuilder(C component) {
        this.component = component;
    }

    @Override
    public ComponentBuilder<C> setBackground(Color color) {
        component.setBackground(color);
        return this;
    }

    @Override
    public ComponentBuilder<C> setSize(Dimension size) {
        component.setPreferredSize(size);
        return this;
    }

    @Override
    public ComponentBuilder<C> setLayout(LayoutManager layout) {
        component.setLayout(layout);
        return this;
    }

    @Override
    public ComponentBuilder<C> setFont(Font font) {
        component.setFont(font);
        return this;
    }

    @Override
    public ComponentBuilder<C> style(Styler styler) {
        styler.styleComponent(component);
        return this;
    }

    @Override
    public ComponentBuilder<C> setForeground(Color color) {
        component.setForeground(color);
        return this;
    }

    @Override
    public ComponentBuilder<C> setEditable(boolean editable) {
        if(component instanceof JTextField field)
            field.setEditable(editable);
        if(component instanceof JTextArea area)
            area.setEditable(editable);
        return this;
    }

    @Override
    public ComponentBuilder<C> addChildren(JComponent... children) {
        for (JComponent child: children) {
            component.add(child);
        }
        return this;
    }

    @Override
    public ComponentBuilder<C> setText(String text) {
        if(component instanceof JTextComponent comp)
            comp.setText(text);
        if(component instanceof JLabel label)
            label.setText(text);
        return this;
    }
}
