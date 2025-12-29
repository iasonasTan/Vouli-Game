package app.game.lib.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import app.game.lib.Context;
import app.lib.gui.Hitbox;
import app.lib.gui.Size;
import app.lib.gui.Vector2;

public abstract class AbstractModel implements Model {
    protected Context context;
    public static boolean sDrawDebug = false;
    private boolean mAlive = true;

    private final Hitbox mHitbox = new Hitbox();
    private final Vector2 mVelocity = new Vector2();

    private final Image mSprite;
    private Image mTempSprite;
    private long mTempSpriteEndTime;

    public AbstractModel(Context context) {
        mHitbox.pos.set(new Vector2(10, 10));
        mHitbox.size.set(new Size(120, 120));
        if(context == null)
            throw new NullPointerException("Context cannot be null");
        this.context = context;
        mSprite = getSprite();
    }

    protected abstract Image getSprite();

    protected void onOutOfScreen() {
    }

    protected void onKilled() {
    }

    protected void useSprite(Image sprite, long time) {
        if(mTempSprite!=null)
            return;
        mTempSprite = sprite;
        mTempSpriteEndTime = time+System.currentTimeMillis();
    }

    @Override
    public void move(Vector2 vec) {
        mHitbox.pos.add(vec);
        Vector2 p = copyPosition();
        Size s = copySize();
        if(p.x <= 0 || p.x+s.width >= context.width() || p.y <= 0 || p.y+s.height >= context.height()) {
            mHitbox.pos.add(vec.scale(new Vector2(-1)));
            onOutOfScreen();
        }
    }

    @Override
    public void addVelocity(Vector2 acceleration) {
        mVelocity.add(acceleration);
    }

    @Override
    public Vector2 copyVelocity() {
        return new Vector2(mVelocity);
    }

    @Override
    public void update(double delta) {
        move(copyVelocity().scale(new Vector2(delta)));
        Vector2 p = copyPosition();
        if(p.x <= 0 || p.x >= context.width() || p.y <= 0 || p.y >= context.height()) {
            move(copyVelocity().scale(new Vector2(-delta)));
        }

        if(mTempSpriteEndTime<System.currentTimeMillis()) {
            mTempSprite = null;
        }
    }

    @Override
    public void render(Graphics g) {
        Vector2 pos = copyPosition();
        Size size = copySize();
        final int OUTLINE = 20;
        g.setColor(Color.WHITE);
        g.fillOval(pos.getX()-OUTLINE/2, pos.getY()-OUTLINE/2, size.getWidth()+OUTLINE, size.getHeight()+OUTLINE);
        Image spriteToRender = mTempSprite != null ? mTempSprite : mSprite;
        g.drawImage(spriteToRender, pos.getX(), pos.getY(), size.getWidth(), size.getHeight(), null);

        if(sDrawDebug) {
            g.setColor(Color.GREEN);
            g.drawRect(pos.getX(), pos.getY(), size.getWidth(), size.getHeight());
        }
    }

    @Override
    public Model setPosition(Vector2 position) {
        mHitbox.pos.set(position);
        return this;
    }

    @Override
    public Model setSize(Size size) {
        mHitbox.size.set(size);
        return this;
    }

    @Override
    public Vector2 copyPosition() {
        return new Vector2(mHitbox.pos);
    }

    @Override
    public Size copySize() {
        return new Size(mHitbox.size);
    }

    @Override
    public Hitbox copyHitbox() {
        return new Hitbox(mHitbox);
    }

    @Override
    public boolean hasCollisionWith(Model model) {
        return copyHitbox().intersects(model.copyHitbox());
    }

    @Override
    public boolean isAlive() {
        return mAlive;
    }

    @Override
    public void kill() {
        mAlive = false;
        onKilled();
    }

    @Override
    public String toString() {
        return String.format("%s{\n\tPosition: %s\n\tSize: %s\n\tVelocity: %s\n}\n", getClass().getName(), copyPosition(), copySize(), copyVelocity());
    }

    public void setVelocity(Vector2 vel) {
    	mVelocity.set(vel);
    }
}
