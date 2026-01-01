package app.game.lib.model;

import app.lib.game.Vector2;
import app.lib.media.Sound;
import app.game.lib.Context;

import javax.sound.sampled.Clip;
import java.awt.*;

public abstract class DamageableModel extends AbstractModel {
    private int mHp;
    private long mKillTime = -1;

    private final Image mOnKilledSprite;
    private final Clip mOnKilledClip;

    public DamageableModel(Context context, int hp) {
        super(context);
        mHp = hp;
        mOnKilledSprite = killSprite();
        mOnKilledClip = killSound();
    }

    protected abstract Clip killSound();
    protected abstract Image killSprite();

    protected void beforeKilled() {
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(Color.RED);
        int s = 10;
        int gap = 5;
        for (int i = 0; i <mHp; i++) {
            g.fillOval(copyPosition().getX()+s*i+gap, copyPosition().getY(), s, s);
        }
        g.setColor(Color.WHITE);
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if(System.currentTimeMillis() > mKillTime && mKillTime != -1) {
            kill();
        }
    }

    public void damage(Model attacker) {
        mHp--;
        if(mHp<=0 && mKillTime == -1) {
        	final long KILL_AFTER = 2000L;
            mKillTime = System.currentTimeMillis()+KILL_AFTER;
            useSprite(mOnKilledSprite, KILL_AFTER);
            Sound.playSFX(mOnKilledClip);
            beforeKilled();
        }

        int knockbackX = copyPosition().getX() - attacker.copyPosition().getX();
        int knockbackY = copyPosition().getY() - attacker.copyPosition().getY();
        int knockbackDiff = (int) (copySize().getWidth()*1.5);
        if(knockbackX >= knockbackY) {
            move(new Vector2(Integer.signum(knockbackX)*knockbackDiff, 0));
        }
        if (knockbackY >= knockbackX){
            move(new Vector2(0, Integer.signum(knockbackX)*knockbackDiff));
        }
    }
}
