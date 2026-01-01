package app.game.lib.model;

import app.game.lib.Context;
import app.lib.game.Size;
import app.lib.game.Vector2;

import java.util.Optional;

public abstract class ThrowableModel extends AbstractModel {
    private final Model mParent;

    public ThrowableModel(Context context, Model parent, Vector2 target) {
        super(context);
        mParent = parent;
        setPosition(parent.copyPosition());
        calculateVelocity(target, parent.copyVelocity());
        setSize(new Size(50, 50));
    }
    
    private void calculateVelocity(Vector2 target, Vector2 velocityObj) {
        double diffX = target.x - copyPosition().x;
        double diffY = target.y - copyPosition().y;
        float length = (float)Math.sqrt(diffX * diffX + diffY * diffY);

        Vector2 vel = new Vector2(diffX / length, diffY / length);
        double parentVelocity = Math.abs(velocityObj.x) + Math.abs(velocityObj.y);
        double velocity = Math.max(10, parentVelocity);
        vel.x *= velocity;
        vel.y *= velocity;
        addVelocity(vel);
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        Optional<Model> colliderOptional = context.getColliderOf(this);
        colliderOptional.ifPresent(m -> {
            if(!m.equals(mParent)&&!m.equals(this)) {
                if(m instanceof DamageableModel dModel) {
                    dModel.damage(this);
                } else {
                    m.kill();
                }
                kill();
            }
        });
    }

    @Override
    protected void onOutOfScreen() {
        super.onOutOfScreen();
        kill();
    }
}
