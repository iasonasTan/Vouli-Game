package app.game.model;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.sound.sampled.Clip;

import app.SoundUtils;
import app.game.Context;
import app.game.model.abstraction.DamageableModel;
import app.game.model.abstraction.Model;
import app.game.model.abstraction.ThrowableModel;
import app.game.util.KeyEventHandler;
import app.game.util.Vector2;
import app.game.util.executor.LazyExecutor;
import app.ui.UI;

public final class Player extends DamageableModel {
	private final PlayerMouseListener mKeyListener = new PlayerMouseListener();
    private final Clip mAttackSound = SoundUtils.loadClip("/game/sound/siopi.wav");

    public Player(Context context) {
        super(context, 10);
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
        return UI.loadImage("/game/sprites/player/player.png");
    }

    @Override
    protected Clip killSound() {
        return SoundUtils.loadOneUseClip("/game/sound/iphone30.wav");
    }

    @Override
    protected Image killSprite() {
        return UI.loadImage("/game/sprites/player/player_killed.png");
    }

    private final class PlayerMouseListener extends LazyExecutor implements MouseListener {
        private final Image mAttackSprite = UI.loadImage("/game/sprites/player/player_attack.png");

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
            SoundUtils.playSFX(mAttackSound);
        }
    }

    private final class PlayerKeyListener extends KeyEventHandler {
        @Override protected void onPress(int kc) {
        	switch(kc) {
                case KeyEvent.VK_W: addVelocity(new Vector2(0, -2)); break;
                case KeyEvent.VK_A: addVelocity(new Vector2(-2, 0)); break;
                case KeyEvent.VK_S: addVelocity(new Vector2(0, +2)); break;
                case KeyEvent.VK_D: addVelocity(new Vector2(+2, 0)); break;
                case KeyEvent.VK_SPACE:
                    context.getModel(Enemy.sEnemyName).ifPresent(m -> mKeyListener.requestExecute(m.copyPosition()));
                    break;
            }
		}

		@Override protected void onRelease(int kc) {
			switch (kc) {
				case KeyEvent.VK_W: addVelocity(new Vector2(0, +2)); break;
	            case KeyEvent.VK_A: addVelocity(new Vector2(+2, 0)); break;
	            case KeyEvent.VK_S: addVelocity(new Vector2(0, -2)); break;
	            case KeyEvent.VK_D: addVelocity(new Vector2(-2, 0)); break;
	        }	
		}
    }

    public static final class Frape extends ThrowableModel {

        public Frape(Context context, Model parent, Vector2 target) {
            super(context, parent, target);
        }

        @Override
        protected Image getSprite() {
            return UI.loadImage("/game/sprites/player/frape.png");
        }
    }
}
