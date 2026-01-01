package app.lib.gui.style;

import app.lib.gui.ComponentBuilder;
import app.lib.gui.UI;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ComponentFactory {
    private final Styler mSimpleStyler;

    public ComponentFactory(Styler mSimpleStyler) {
        this.mSimpleStyler = mSimpleStyler;
    }

    public <T extends JComponent> T newComponent(Class<T> clazz, String text) {
        try {
            Constructor<T> strConstructor = clazz.getConstructor(String.class);
            T component = strConstructor.newInstance(text);
            return mSimpleStyler.styleComponent(component);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            UI.showException(e);
            throw new RuntimeException(e);
        }
    }

    public <T extends JComponent> T newComponent(Class<T> clazz) {
        try {
            Constructor<T> strConstructor = clazz.getConstructor();
            T component = strConstructor.newInstance();
            return mSimpleStyler.styleComponent(component);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            UI.showException(e);
            throw new RuntimeException(e);
        }
    }

    public <T extends JComponent> ComponentBuilder<T> newComponentBuilder(Class<T> clazz) {
        try {
            Constructor<T> strConstructor = clazz.getConstructor();
            T component = strConstructor.newInstance();
            T t = mSimpleStyler.styleComponent(component);
            return UI.newComponentBuilder(t);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            UI.showException(e);
            throw new RuntimeException(e);
        }
    }
}
