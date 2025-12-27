package app.game.lib;

import app.game.lib.model.Model;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface Context extends Runnable {
    void addModel(String id, Model model);
    <T extends Model> Optional<T> getModel(String id, Class<T> clazz);
    Optional<Model> getModel(String id);

    Optional<Model> getColliderOf(Model model);
    <T extends Model>List<T> getInstancesOf(Class<T> clazz);

    void addKeyListener(KeyListener listener);
    void addMouseListener(MouseListener listener);

    float width();
    float height();
    void exitGame();
    void stopGameThread();
}
