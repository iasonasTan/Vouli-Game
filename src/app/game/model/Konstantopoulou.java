package app.game.model;

import app.game.lib.Context;
import app.game.lib.model.Model;
import app.game.lib.model.ThrowableModel;
import app.lib.io.Resources;
import app.lib.gui.Vector2;

import javax.sound.sampled.Clip;
import java.awt.*;

public class Konstantopoulou extends AbstractEnemy {
    public Konstantopoulou(Context context) {
        super(context);
    }

    @Override
    protected ThrowableModel createThrowableModel(Vector2 target) {
        return new Zoe(context, this, target);
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }

    @Override
    protected void beforeKilled() {
        super.beforeKilled();
    }

    @Override
    protected Clip attackSound() {
        return Resources.loadClip("/game/sound/ti_eipate.wav");
    }

    @Override
    protected Clip killSound() {
        return Resources.loadClip("/game/sound/tha_ti_skotoso.wav");
    }

    @Override
    protected Image killSprite() {
        return Resources.loadImage("/game/sprites/enemy/enemy_killed.png");
    }

    @Override
    protected Image getSprite() {
        return Resources.loadImage("/game/sprites/enemy/enemy.png");
    }

    public static final class Zoe extends ThrowableModel {
        public Zoe(Context context, Model parent, Vector2 target) {
            super(context, parent, target);
        }

        @Override
        protected Image getSprite() {
            return Resources.loadImage("/game/sprites/enemy/zoe.png");
        }
    }
}
