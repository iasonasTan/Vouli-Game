package app.game.model;

import app.game.lib.model.DamageableModel;
import app.game.lib.model.ThrowableModel;
import app.lib.io.Resources;
import app.lib.media.Sound;
import app.game.lib.Context;
import app.lib.gui.Vector2;
import app.lib.LazyExecutor;

import javax.sound.sampled.Clip;
import java.awt.*;

public abstract class AbstractEnemy extends DamageableModel {
    private static final double SPEED = 0.5;
    private final LazyExecutor mAttackExecutor = new EnemyAttacker(2000);
    private final Clip mAttackSound;
    //private final Class<? extends ThrowableModel> mThrowableModelType;

    public AbstractEnemy(Context context) {
        super(context, 3);
        mAttackSound = attackSound();
        //mThrowableModelType = throwableModelType();
    }

    protected abstract ThrowableModel createThrowableModel(Vector2 target);
    //protected abstract Class<? extends ThrowableModel> throwableModelType();
    protected abstract Clip attackSound();

    @Override
    public void update(double delta) {
        super.update(delta);
        context.getModel("_PLAYER_").ifPresent(player -> {
            Vector2 newVel = new Vector2();
            newVel.x = Math.signum(player.copyPosition().x - copyPosition().x)*SPEED;
            newVel.y = Math.signum(player.copyPosition().y - copyPosition().y)*SPEED;
            setVelocity(newVel);
            if(hasCollisionWith(player)) {
                DamageableModel damageable = (DamageableModel) player;
                damageable.damage(this);
            }
        });
        mAttackExecutor.requestExecute();
    }

    @Override
    protected void beforeKilled() {
        super.beforeKilled();
        mAttackSound.close();
    }

    private final class EnemyAttacker extends LazyExecutor {
        private final Image mAttackSprite = Resources.loadImage("/game/sprites/enemy/enemy_attack.png");

        public EnemyAttacker(long delay) {
            super(delay);
        }

        @Override
        protected void execute(Object... params) {
            context.getModel("_PLAYER_").ifPresent(player -> {
                //Constructor<? extends ThrowableModel> constructor = mThrowableModelType.getConstructor(Context.class, Model.class, Vector2.class);
                //ThrowableModel thrMod = constructor.newInstance(context, AbstractEnemy.this, player.copyPosition());
                ThrowableModel thrMod = createThrowableModel(player.copyPosition());
                context.addModel("thrMod"+thrMod.getClass().getName()+thrMod.hashCode(), thrMod);
                useSprite(mAttackSprite, getBreakTime()/3);
                Sound.playSFX(mAttackSound);
            });
        }
    }
}
