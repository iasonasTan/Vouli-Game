package app.game.model;

import app.game.lib.Context;
import app.game.lib.model.Model;
import app.game.lib.model.ThrowableModel;
import app.lib.gui.Vector2;
import app.lib.io.Resources;

import javax.sound.sampled.Clip;
import java.awt.*;

public class Semertzidou extends AbstractEnemy {
    public Semertzidou(Context context) {
        super(context);
    }

    @Override
    protected String attackSprite() {
        return "/game/semertzidou/model_attack.png";
    }

    @Override
    protected ThrowableModel createThrowableModel(Vector2 target) {
        return new Ferrari(context, this, target);
    }

    private static final class Ferrari extends ThrowableModel {
        public Ferrari(Context context, Model parent, Vector2 target) {
            super(context, parent, target);
        }

        @Override
        protected Image getSprite() {
            return Resources.loadImage("/game/semertzidou/throwable.png");
        }
    }

    @Override
    protected Image getSprite() {
        return Resources.loadImage("/game/semertzidou/model.png");
    }

    @Override
    protected Image killSprite() {
        return Resources.loadImage("/game/semertzidou/dead_model.png");
    }

    @Override
    protected Clip attackSound() {
        return Resources.loadClip("/game/semertzidou/mono_ena_mixanaki.wav");
    }

    @Override
    protected Clip killSound() {
        return Resources.loadOneUseClip("/game/semertzidou/den_gnorizw.wav");
    }
}
