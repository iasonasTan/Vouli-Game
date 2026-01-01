package app.lib.game;

/**
 * <b>Not</b> working in <i>hash collections</i>
 */
public final class Size {
    public float width, height;

    public Size() {
        this(0f, 0f);
    }

    public Size(float width, float height) {
        this.width  = width;
        this.height = height;
    }

    public Size(Size data) {
        width  = data.width;
        height = data.height;
    }

    @Override
    public String toString() {
        return String.format("Size{%f,%f}", width, height);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o == null) return false;
        if(o instanceof Size size) {
            return size.width == width && size.height == height;
        } else {
            return false;
        }
    }

    public int getWidth() {
        return (int)width;
    }

    public int getHeight() {
        return (int)height;
    }

    public void set(Size data) {
        width  = data.width;
        height = data.height;
    }
}
