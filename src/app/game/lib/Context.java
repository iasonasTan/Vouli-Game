package app.game.lib;

import app.game.lib.model.Model;
import app.lib.game.Vector2;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Optional;

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
    Vector2 randomVector(Model model);

    void exitGame();
    void stopGameThread();
}
