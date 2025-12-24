package app.game.model;

import app.SoundUtils;
import app.game.Context;
import app.game.model.abstraction.DamageableModel;
import app.game.model.abstraction.Model;
import app.game.model.abstraction.ThrowableModel;
import app.game.util.Vector2;
import app.game.util.executor.TimerExecutor;
import app.ui.UI;

import javax.sound.sampled.Clip;
import java.awt.*;

public class Enemy extends DamageableModel {
    private final TimerExecutor mAttackTimerExecutor = new EnemyAttacker(2000);
    private final Image mKilledImage = UI.loadImage("/game/sprites/enemy/enemy_killed.png");
    private final Clip mAttackSound = SoundUtils.loadClip("/game/sound/ti_eipate.wav");

    public Enemy(Context context) {
        super(context, 3);
        mAttackTimerExecutor.start();
    }

    @Override
    protected Image getSprite() {
        return UI.loadImage("/game/sprites/enemy/enemy.png");
    }

    @Override
    protected Image killSprite() {
        return mKilledImage;
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        context.getModel("_PLAYER_").ifPresent(model -> {
            Vector2 newVel = new Vector2();
            newVel.x = Math.signum(model.copyPosition().x - copyPosition().x)*0.9f;
            newVel.y = Math.signum(model.copyPosition().y - copyPosition().y)*0.9f;
            setVelocity(newVel);
        });
    }

    @Override
    protected void beforeKilled() {
        super.beforeKilled();
        mAttackTimerExecutor.close();
        mAttackSound.close();
    }

    @Override
    protected Clip killSound() {
        return SoundUtils.loadOneUseClip("/game/sound/tha_ti_skotoso.wav");
    }

    @Override
    protected void onKilled() {
        super.onKilled();
        // spawn another
        System.out.println("Adding new enemy...");
        Enemy dmgMod = new Enemy(context);
        context.addModel("_ENEMY_"+dmgMod.hashCode(), dmgMod);
    }

    public static class Zoe extends ThrowableModel {
        public Zoe(Context context, Model parent, Vector2 target) {
            super(context, parent, target);
        }

        @Override
        protected Image getSprite() {
            return UI.loadImage("/game/sprites/enemy/zoe.png");
        }
    }

    private final class EnemyAttacker extends TimerExecutor {
        private final Image mAttackSprite = UI.loadImage("/game/sprites/enemy/enemy_attack.png");

        public EnemyAttacker(long delay) {
            super(delay);
        }

        @Override
        protected void execute() {
            context.getModel("_PLAYER_").ifPresent(m -> {
                ThrowableModel thrMod = new Zoe(context, Enemy.this, m.copyPosition());
                context.addModel("ZOE_"+thrMod.hashCode(), thrMod);
                useSprite(mAttackSprite, getDelay()/3);
                SoundUtils.playSFX(mAttackSound);
            });
        }
    }
}
