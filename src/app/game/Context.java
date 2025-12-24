package app.game;

import app.game.model.abstraction.Model;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.Optional;

public interface Context extends Runnable {
    void addModel(String id, Model model);
    <T extends Model> Optional<T> getModel(String id, Class<T> clazz);
    Optional<Model> getModel(String id);
    void exit();
    void addKeyListener(KeyListener listener);
    void addMouseListener(MouseListener listener);
    float width();
    float height();
    Optional<Model> getColliderOf(Model model);
}
