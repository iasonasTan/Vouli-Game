package app.game.util.executor;

public abstract class TimerExecutor extends Thread {
    // time in millis
    private final long mDelay;
    private boolean mActive = true;

    public TimerExecutor(long delay) {
        mDelay = delay;
    }

    public long getDelay() {
        return mDelay;
    }

    public void close() {
        if(!mActive)
            throw new IllegalStateException("Executor is already closed.");
        mActive = false;
    }

    @Override
    public void run() {
        while(mActive) {
            execute();
            try {
                // noinspection all
                Thread.sleep(mDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected abstract void execute();
}
