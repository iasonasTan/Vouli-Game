package app.game.model.abstraction;

import app.SoundUtils;
import app.game.Context;

import javax.sound.sampled.Clip;
import java.awt.*;

public abstract class DamageableModel extends AbstractModel {
    private int mHp;
    private long mKillTime = -1;

    public DamageableModel(Context context, int hp) {
        super(context);
        mHp = hp;
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
            useSprite(killSprite(), KILL_AFTER);
            SoundUtils.playSFX(killSound());
            beforeKilled();
        }
        int knockbackX = copyPosition().getX() - attacker.copyPosition().getX();
        int knockbackY = copyPosition().getY() - attacker.copyPosition().getY();
        int knockbackDiff = copySize().getWidth();
        if(knockbackX >= knockbackY) {
            copyPosition().x += Integer.signum(knockbackX)*knockbackDiff;
        }
        if (knockbackY >= knockbackX){
            copyPosition().y += Integer.signum(knockbackY)*knockbackDiff;
        }
    }
}
