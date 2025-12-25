package app.game.model.abstraction;

import app.game.util.Hitbox;
import app.game.util.Size;
import app.game.util.Vector2;

import java.awt.*;

@SuppressWarnings("unused")
public interface Model {
    Model setPosition(Vector2 position);
    Model setSize(Size size);
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
