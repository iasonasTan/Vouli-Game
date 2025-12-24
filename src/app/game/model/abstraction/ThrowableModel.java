package app.game.model.abstraction;

import java.util.Optional;

import app.game.Context;
import app.game.util.Size;
import app.game.util.Vector2;

public abstract class ThrowableModel extends AbstractModel {
    private final Model mParent;

    public ThrowableModel(Context context, Model parent, Vector2 target) {
        super(context);
        mParent = parent;
        setPosition(parent.copyPosition());
        calculateVelocity(target);
        setSize(new Size(50, 50));
    }
    
    private void calculateVelocity(Vector2 target) {
        float diffX = target.x - copyPosition().x;
        float diffY = target.y - copyPosition().y;
        float length = (float)Math.sqrt(diffX * diffX + diffY * diffY);

        Vector2 vel = new Vector2(diffX / length, diffY / length);
        float VELOCITY = 8;
        vel.x *= VELOCITY;
        vel.y *= VELOCITY;
        addVelocity(vel);
    }


    @Override
    public void update(double delta) {
        super.update(delta);
        Optional<Model> colliderOptional = context.getColliderOf(this);
        colliderOptional.ifPresent(m -> {
            if(!m.equals(mParent)&&!m.equals(this)) {
                if(m instanceof DamageableModel) {
                    DamageableModel dModel = (DamageableModel)m;
                    dModel.damage(this);
                    kill();
                } else {
                    m.kill();
                    kill();
                }
            }
        });
    }

    @Override
    protected void onOutOfScreen() {
        super.onOutOfScreen();
        kill();
    }
}
