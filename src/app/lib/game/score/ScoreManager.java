package app.lib.game.score;

import app.lib.io.Configuration;
import app.lib.io.InputProperties;
import app.lib.io.OutputProperties;

import java.io.Closeable;

public final class ScoreManager implements Closeable {
    public static ScoreManager fromSaved() {
        InputProperties inputProperties = new InputProperties();
        Configuration.loadProperties("score.properties", inputProperties);
        return new ScoreManager(inputProperties.getDouble("score", 0d));
    }

    private final double mBestScore;
    private double mScore;

    private ScoreManager(double bestScore) {
        mBestScore = bestScore;
    }

    public void increaseScore() {
        mScore+=1f;
    }

    public void increaseScore(double s) {
        mScore += s;
    }

    public double getScore() {
        return mScore;
    }

    public double getBestScore() {
        return mBestScore;
    }

    @Override
    public String toString() {
        return String.format("Score: %f, BestScore: %f", mScore, mBestScore);
    }

    @Override
    public void close() {
        if(mScore>mBestScore) {
            var op = new OutputProperties().put("score", mScore);
            Configuration.storeProperties("score.properties", op);
        }
    }
}
