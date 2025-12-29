package app.game.lib.model;

import app.lib.gui.Hitbox;
import app.lib.gui.Size;
import app.lib.gui.Vector2;

import java.awt.*;

@SuppressWarnings("unused")
public interface Model {
    Model setPosition(Vector2 position);
    Model setSize(Size size);

    void move(Vector2 vec);
    void addVelocity(Vector2 acceleration);

    Vector2 copyPosition();
    Size copySize();
    Vector2 copyVelocity();
    Hitbox copyHitbox();

    void update(double delta);
    void render(Graphics g);

    boolean hasCollisionWith(Model model);

    boolean isAlive();
    void kill();
}
