package app.game.model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.sound.sampled.Clip;

import app.lib.io.Resources;
import app.lib.media.Sound;
import app.game.lib.Context;
import app.game.lib.model.DamageableModel;
import app.game.lib.model.Model;
import app.game.lib.model.ThrowableModel;
import app.lib.gui.event.KeyEventHandler;
import app.lib.gui.Vector2;
import app.lib.LazyExecutor;

public final class Player extends DamageableModel {
    private static int sScore = 0;

    public static void increaseScore() {
        sScore++;
    }

    public static int getScore() {
        return sScore;
    }

	private final PlayerMouseListener mKeyListener = new PlayerMouseListener();
    private final Clip mAttackSound = Resources.loadClip("/game/ksilouris/siopi.wav");

    public Player(Context context) {
        super(context, 4);
        context.addKeyListener(new PlayerKeyListener());
        context.addMouseListener(mKeyListener);
    }

    @Override
    protected void onKilled() {
        super.onKilled();
        mAttackSound.close();
    }

    @Override
    protected Image getSprite() {
        return Resources.loadImage("/game/ksilouris/model.png");
    }

    @Override
    protected Clip killSound() {
        return Resources.loadOneUseClip("/game/ksilouris/iphone30.wav");
    }

    @Override
    protected Image killSprite() {
        return Resources.loadImage("/game/ksilouris/dead_model.png");
    }

    private final class PlayerMouseListener extends LazyExecutor implements MouseListener {
        private final Image mAttackSprite = Resources.loadImage("/game/ksilouris/model_attack.png");

        public PlayerMouseListener() { super(500L); }

        @Override 
        public void mousePressed(MouseEvent mouseEvent) {
            requestExecute(new Vector2(mouseEvent.getPoint()));
        }

        @Override public void mouseClicked  (MouseEvent mouseEvent) {}
        @Override public void mouseReleased (MouseEvent mouseEvent) {}
        @Override public void mouseEntered  (MouseEvent mouseEvent) {}
        @Override public void mouseExited   (MouseEvent mouseEvent) {}

        @Override
        protected void execute(Object... params) {
            Vector2 target = (Vector2)params[0];
            ThrowableModel frape = new Frape(context, Player.this, target);
            context.addModel("FRAPE_"+frape.hashCode(), frape);
            useSprite(mAttackSprite, getBreakTime());
            Sound.playSFX(mAttackSound);
        }
    }

    private final class PlayerKeyListener extends KeyEventHandler {
        private static final double SPEED = 5;

        @Override protected void onPress(int kc) {
        	switch(kc) {
                case KeyEvent.VK_W: addVelocity(new Vector2(0, -SPEED)); break;
                case KeyEvent.VK_A: addVelocity(new Vector2(-SPEED, 0)); break;
                case KeyEvent.VK_S: addVelocity(new Vector2(0, +SPEED)); break;
                case KeyEvent.VK_D: addVelocity(new Vector2(+SPEED, 0)); break;
                case KeyEvent.VK_SPACE:
                    List<AbstractEnemy> models = context.getInstancesOf(AbstractEnemy.class);
                    if(!models.isEmpty())
                        mKeyListener.requestExecute(models.get(0).copyPosition());
                    break;
            }
		}

		@Override protected void onRelease(int kc) {
			switch (kc) {
				case KeyEvent.VK_W: addVelocity(new Vector2(0, +SPEED)); break;
	            case KeyEvent.VK_A: addVelocity(new Vector2(+SPEED, 0)); break;
	            case KeyEvent.VK_S: addVelocity(new Vector2(0, -SPEED)); break;
	            case KeyEvent.VK_D: addVelocity(new Vector2(-SPEED, 0)); break;
	        }	
		}
    }

    public static final class Frape extends ThrowableModel {
        public Frape(Context context, Model parent, Vector2 target) {
            super(context, parent, target);
        }

        @Override
        protected Image getSprite() {
            return Resources.loadImage("/game/ksilouris/throwable.png");
        }
    }
}
