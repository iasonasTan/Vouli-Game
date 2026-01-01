package app.game.model;

import app.game.lib.model.DamageableModel;
import app.game.lib.model.Model;
import app.game.lib.model.ThrowableModel;
import app.lib.media.Sound;
import app.game.lib.Context;
import app.lib.game.Vector2;
import app.lib.LazyExecutor;

import javax.sound.sampled.Clip;
import java.awt.*;

public abstract class AbstractEnemy extends DamageableModel {
    private static double sSpeed = 0.3;

    public static void increaseSpeed() {
        sSpeed += 0.1;
    }

    private Image mOnAttackSprite;
    private Clip mOnAttackClip;
    private final LazyExecutor mAttackExecutor;

    public AbstractEnemy(Context context) {
        super(context, 3);
        mAttackExecutor = new EnemyAttacker(2000);
        loadResources();
    }

    private void loadResources() {
        new Thread(() -> {
            mOnAttackSprite = attackSprite();
            mOnAttackClip = attackSound();
        }).start();
    }

    protected abstract Image attackSprite();
    protected abstract ThrowableModel createThrowableModel(Vector2 target);
    protected abstract Clip attackSound();

    @Override
    public void update(double delta) {
        super.update(delta);
        context.getModel("_PLAYER_").ifPresent(this::followModel);
        mAttackExecutor.requestExecute();
    }

    protected void followModel(Model player) {
        Vector2 newVel = new Vector2();
        final int REQUIRED_DIFF = 15;
        double diffX = player.copyPosition().x - copyPosition().x;
        double diffY = player.copyPosition().y - copyPosition().y;
        if(Math.abs(diffX) > REQUIRED_DIFF)
            newVel.x = Math.signum(diffX)*sSpeed;
        if(Math.abs(diffY) > REQUIRED_DIFF)
            newVel.y = Math.signum(diffY)*sSpeed;
        setVelocity(newVel);
        if(hasCollisionWith(player)) {
            DamageableModel damageable = (DamageableModel)player;
            damageable.damage(this);
        }
    }

    @Override
    protected void onKilled() {
        super.onKilled();
        new Thread(() -> {
            AbstractEnemy enemy;
            if(Math.random()*100 > 50) {
                enemy = new Semertzidou(context);
            } else {
                enemy = new Konstantopoulou(context);
            }
            context.addModel("ENEMY_"+enemy.hashCode(), enemy.setPosition(context.randomVector(enemy)));
            context.getModel("_PLAYER_", Player.class).ifPresent(player -> player.getScoreMan().increaseScore());
            increaseSpeed();
        }).start();
    }

    @Override
    protected void beforeKilled() {
        super.beforeKilled();
        mOnAttackClip.close();
    }

    private final class EnemyAttacker extends LazyExecutor {
        public EnemyAttacker(long delay) {
            super(delay);
        }

        @Override
        protected void execute(Object... params) {
            context.getModel("_PLAYER_").ifPresent(player -> {
                ThrowableModel thrMod = createThrowableModel(player.copyPosition());
                context.addModel("thrMod"+thrMod.getClass().getName()+thrMod.hashCode(), thrMod);
                useSprite(mOnAttackSprite, getBreakTime()/3);
                Sound.playSFX(mOnAttackClip);
            });
        }
    }
}
