package app.ui.style;

import javax.swing.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ComponentFactory {
    private final Styler mStyler;

    public ComponentFactory(Styler mStyler) {
        this.mStyler = mStyler;
    }

    public JComponent newComponent(Class<? extends JComponent> clazz, String text) {
        try {
            Constructor<? extends JComponent> strConstructor = clazz.getConstructor(String.class);
            JComponent component = strConstructor.newInstance(text);
            return mStyler.styleComponent(component);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
