package dev.teraprath.gamelib.game;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Map;

public class GameSettings {

    private final int minPlayers;
    private final int maxPlayers;
    private final int runningTime;
    private final int waitingTime;
    private final int shutdownTime;
    private Map<GameState, String> motd;

    public GameSettings(@Nonnegative int minPlayers, @Nonnegative int maxPlayers, @Nonnegative int runningTime, @Nonnegative int waitingTime, @Nonnegative int shutdownTime) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.runningTime = runningTime;
        this.waitingTime = waitingTime;
        this.shutdownTime = shutdownTime;
        for (GameState gameState : GameState.values()) {
            this.motd.put(gameState, gameState.name());
        }
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getRunningTime() {
        return this.runningTime;
    }

    public int getWaitingTime() {
        return this.waitingTime;
    }

    public int getShutdownTime() {
        return this.shutdownTime;
    }

    public void setMotd(@Nonnull GameState gameState, @Nonnull String motd) {
        this.motd.put(gameState, motd);
    }

    public String getMotd(@Nonnull GameState gameState) {
        final String motd = this.motd.get(gameState);
        return motd != null ? motd : gameState.name();
    }

}
