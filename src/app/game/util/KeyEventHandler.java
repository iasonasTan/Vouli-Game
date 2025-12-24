package app.game.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public abstract class KeyEventHandler implements KeyListener {
	private final Map<Integer, Boolean> mKeySates = new HashMap<>();
	
	@Override
	public final void keyPressed(KeyEvent e) {
		int kc = e.getKeyCode();
		if(!mKeySates.containsKey(kc)) {
			mKeySates.put(kc, true);
			onPress(kc);
			return;
		}
		boolean pressed = mKeySates.get(kc);
		if(!pressed) {
			mKeySates.put(kc, true);
			onPress(kc);
		}
	}

	@Override
	public final void keyReleased(KeyEvent e) {
		int kc = e.getKeyCode();
		if(!mKeySates.containsKey(kc)) {
			mKeySates.put(kc, false);
			onRelease(kc);
			return;
		}
		boolean pressed = mKeySates.get(kc);
		if(pressed) {
			mKeySates.put(kc, false);
			onRelease(kc);
		}
	}
	
	protected abstract void onPress(int kc);
	protected abstract void onRelease(int kc);
	
	@Override public final void keyTyped(KeyEvent e) {}
}
