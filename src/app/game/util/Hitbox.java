package app.game.util;

@SuppressWarnings("unused")
public class Hitbox {
    public final Vector2 pos;
    public final Size size;

    public Hitbox(Vector2 pos, Size size) {
        this.pos = pos;
        this.size = size;
    }

    public Hitbox() {
        pos = new Vector2();
        size = new Size();
    }

    public Hitbox(Hitbox hitbox) {
        pos = new Vector2(hitbox.pos);
        size = new Size(hitbox.size);
    }

    @Override
    public String toString() {
        return "Hitbox{"+pos+", "+size+"}";
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o == null) return false;
        if(o instanceof Hitbox hb) {
            return hb.pos.equals(pos)&&hb.size.equals(size);
        } else {
            return false;
        }
    }

    public final boolean intersects(Hitbox hb) {
        // this
        double tx = pos.x, ty = pos.y;
        double tw = size.width, th = size.height;
        // other
        double ox = hb.pos.x, oy = hb.pos.y;
        double ow = hb.size.width, oh = hb.size.height;

        return tx < ox+ow && tx+tw > ox &&
                ty < oy+oh && ty+th > oy;
    }

}
