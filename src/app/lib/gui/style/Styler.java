package app.lib.gui.style;

import javax.swing.*;

public interface Styler {
    <T extends JComponent> T[] styleComponents(T... components);
    <T extends JComponent> T styleComponent(T component);
}