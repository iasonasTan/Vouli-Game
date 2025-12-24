package app.game.util;

import java.awt.*;

/**
 * <b>Not</b> working in <i>hash collections</i>
 */
public final class Vector2 {
    public float x, y;

    public Vector2() {
        this(0f, 0f);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 data) {
        x = data.x;
        y = data.y;
    }

    public Vector2(Point data) {
        x = data.x;
        y = data.y;
    }

    @Override
    public String toString() {
        return String.format("Vector2{%f,%f}", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o == null) return false;
        if(o instanceof Vector2) {
            Vector2 vec = (Vector2)o;
            return vec.x == x && vec.y == y;
        } else {
            return false;
        }
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public void set(Vector2 data) {
        x = data.x;
        y = data.y;
    }
    
    public void add(Vector2 data) {
    	x += data.x;
    	y += data.y;
    }
}
