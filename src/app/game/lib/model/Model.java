package app.game.lib.model;

import app.lib.game.Hitbox;
import app.lib.game.Size;
import app.lib.game.Vector2;

import java.awt.*;

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
