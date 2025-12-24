package app.game.util.executor;

public abstract class LazyExecutor {
    // time in millis
    private long mBreakTime;
    private long mLastExecutionTime;

    public LazyExecutor(long breakTime) {
        mBreakTime = breakTime;
    }

    public final void requestExecute(Object... params) {
        if(mBreakTime+mLastExecutionTime < System.currentTimeMillis()) {
            mLastExecutionTime = System.currentTimeMillis();
            execute(params);
        }
    }

    protected abstract void execute(Object... params);

    public long getBreakTime() {
        return mBreakTime;
    }
}
