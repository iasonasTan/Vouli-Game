package app.game;

import java.awt.Color;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import app.game.model.abstraction.Model;
import app.game.model.Enemy;
import app.game.model.Player;
import app.game.model.abstraction.AbstractModel;
import app.game.util.Vector2;
import app.ui.Menu;
import app.ui.UI;
import app.ui.abstraction.AbstractScreen;

// TODO add score
// TODO add save score

public class Game extends AbstractScreen implements Context {
    private Map<String, Model> mModels = Collections.synchronizedMap(new HashMap<>());
    private final Image mBackground = UI.loadImage("/background.jpg");
    private boolean mRunning = false;
    private Thread mThread;

    public Game() {
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
        addKeyListener(new Listener());
    	
        addModel("_PLAYER_", new Player(this));
        addModel("_ENEMY_", new Enemy(this).setPosition(new Vector2(500, 500)));
    }

    @Override
    protected String title() {
        return "Vouli Game - Main";
    }

    @Override
    protected Image background() {
        return UI.loadImage("/background.jpg");
    }

    @Override
    protected Image icon() {
        return UI.loadImage("/app_icon.png");
    }

    @Override
    public Optional<Model> getColliderOf(Model m) {
        for (Model model : mModels.values()) {
            if(m.hasCollisionWith(model))
                return Optional.of(model);
        }
        return Optional.empty();
    }

    @Override
    public <T extends Model> List<T> getInstancesOf(Class<T> clazz) {
        List<T> out = new ArrayList<>();
        for (Model value : mModels.values()) {
            if(clazz.isAssignableFrom(value.getClass()))
                out.add(clazz.cast(value));
        }
        return out;
    }

    @Override
    public void exit() {
        stop();
        new Menu().setVisible();
    }

    private final class Listener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    exit();
                    break;
                case KeyEvent.VK_F1:
                    AbstractModel.sDrawDebug = !AbstractModel.sDrawDebug;
                    break;
            }
        }

        @Override public void keyReleased(KeyEvent keyEvent) {}
    }

    @Override
    public void stop() {
        mRunning = false;
        mModels.remove("_PLAYER_");
        for (Model model : mModels.values()) {
            model.kill();
        }
    }

    public void start() {
        mRunning = true;
        if(mThread == null)
            mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public float width() {
        return getWidth();
    }

    @Override
    public float height() {
        return getHeight();
    }

    @Override
    public void addModel(String id, Model model) {
        System.out.println("Added new model... "+model);
        mModels.put(id, model);
    }

    @Override
    public <T extends Model> Optional<T> getModel(String id, Class<T> clazz) throws ClassCastException {
        Optional<Model> nodeOpt = getModel(id);
        Model model = nodeOpt.orElse(null);
        // noinspection all : responsibility of user
        return Optional.ofNullable(clazz.cast(model));
    }

    @Override
    public Optional<Model> getModel(String id) {
        Model model = mModels.get(id);
        return Optional.ofNullable(model);
    }

    @Override
    public void run() {
        long startTime, endTime, deltaTime;
        final double TARGET_FPS = 1000f/60f;

        while(mRunning) {
            startTime = System.nanoTime();
            updateGame(1);
            render();
            endTime = System.nanoTime();
            deltaTime = endTime - startTime;

            long sleep = (long)(TARGET_FPS-deltaTime/1_000_000f);
            try {
                // noinspection all
                Thread.sleep(sleep > 0 ? sleep : 0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void updateGame(final double delta) {
        Map<String, Model> copy = new HashMap<>(mModels);
        copy.entrySet().removeIf(e -> !e.getValue().isAlive());
        copy.values().forEach(model -> model.update(delta));
        mModels = copy;
    }

    private void render() {
        repaint();
        paintImmediately(getVisibleRect());
    }

    @Override
    protected void paintComponent(final java.awt.Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(mBackground, 0, 0, (int)width(), (int)height(), null);

        List<Model> models = new ArrayList<>(mModels.values());
        // noinspection all : avoid concurrent modification
        for (int i = 0; i < models.size(); i++) {
            models.get(i).render(graphics);
        }

        graphics.setColor(Color.BLUE);
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        graphics.fillOval(mousePos.x-20, mousePos.y-20, 40, 40);
        graphics.setColor(Color.WHITE);

        graphics.dispose();
    }
}
