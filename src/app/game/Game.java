package app.game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import app.game.lib.Context;
import app.game.model.Konstantopoulou;
import app.game.lib.model.Model;
import app.game.model.Player;
import app.game.lib.model.AbstractModel;
import app.lib.io.Resources;
import app.lib.game.Vector2;
import app.menu.GameOverScreen;
import app.lib.gui.AbstractScreen;

public class Game extends AbstractScreen implements Context {
    private final Map<String, Model> mModels = new HashMap<>(), mModelsBuff = new HashMap<>();
    private final Image mBackground = Resources.loadImage("/res/background.jpg");
    private boolean mRunning = false;
    private Thread mThread;

    public Game() {
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
        addKeyListener(new Listener());
    	
        addModel("_PLAYER_", new Player(this));
        addModel("_KONSTANTOPOULOU_", new Konstantopoulou(this).setPosition(new Vector2(500, 500)));
    }

    @Override
    public Vector2 randomVector(Model model) {
        int x = (int)(Math.random()*(width()-model.copySize().width));
        int y = (int)(Math.random()*(height()-model.copySize().height));
        return new Vector2(x, y);
    }

    private final class Listener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    exitGame();
                    break;
                case KeyEvent.VK_F1:
                    AbstractModel.sDrawDebug = !AbstractModel.sDrawDebug;
                    break;
            }
        }

        @Override public void keyReleased(KeyEvent keyEvent) {}
    }

    @Override
    protected String title() {
        return "Vouli Game - Main";
    }

    @Override
    protected Image background() {
        return Resources.loadImage("/res/background.jpg");
    }

    @Override
    protected Image icon() {
        return Resources.loadImage("/res/app_icon.png");
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
    public void exitGame() {
        stopGameThread();
        Iterator<Model> modelIter = mModels.values().iterator();
        while(modelIter.hasNext()) {
            modelIter.next().kill();
            modelIter.remove();
        }
        new GameOverScreen().setVisible();
    }

    @Override
    public void stopGameThread() {
        mRunning = false;
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
        System.out.println("Added model "+model);
        mModelsBuff.put(id, model);
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
            updateGame();
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

     // TODO Remove warnings, post on github, post on website.

    private void updateGame() {
        mModels.putAll(mModelsBuff);
        mModelsBuff.clear();
        mModels.values().forEach(m -> m.update(1));
        mModels.values().removeIf(m -> !m.isAlive() && !(m instanceof Player));
        getModel("_PLAYER_").ifPresent(m -> {
            if(!m.isAlive())
                exitGame();
        });
    }

    private void configureGraphics(Graphics g) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/res/menu/fonts/ibm_plex_serif_bold.ttf");
            if(inputStream==null) return;
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            g.setFont(font.deriveFont(25f));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void render() {
        repaint();
        paintImmediately(getVisibleRect());
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        configureGraphics(graphics);

        graphics.drawImage(mBackground, 0, 0, (int)width(), (int)height(), null);

        List<Model> models = new ArrayList<>(mModels.values());
        // noinspection all : avoid concurrent modification
        for (int i = 0; i < models.size(); i++) {
            models.get(i).render(graphics);
        }

        graphics.setColor(Color.BLUE);
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        final int SIZE = 15;
        graphics.drawLine(mousePos.x-SIZE, mousePos.y, mousePos.x+SIZE, mousePos.y); // horizontal
        graphics.drawLine(mousePos.x, mousePos.y-SIZE, mousePos.x, mousePos.y+SIZE); // vertical

        graphics.dispose();
    }
}
