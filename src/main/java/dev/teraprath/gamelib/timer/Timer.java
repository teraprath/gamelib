package dev.teraprath.gamelib.timer;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public abstract class Timer {

    protected final JavaPlugin plugin;
    protected final BukkitScheduler scheduler;
    protected BukkitTask task;
    protected final int length;
    private int time;

    protected Timer(@Nonnull JavaPlugin plugin, @Nonnegative int length) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        this.length = length;
        this.time = this.length;
    }

    public void start() {
        this.scheduler.runTaskTimer(plugin, task -> {

            if (this.task != task || task == null) {
                this.task = task;
            }

            if (time == 0) {
                end();
                stop();
            }

            this.time--;
            run();

        }, 20L, 20L);
    }

    public void resume() {
        start();
    }

    public void pause() {
        this.task.cancel();
    }

    public void stop() {
        this.time = length;
        this.task.cancel();
    }

    public void setTime(@Nonnegative int seconds) {
        this.time = seconds;
    }

    public int getTime() {
        return this.time;
    }

    public boolean isRunning() {
        return  (scheduler.isCurrentlyRunning(task.getTaskId()));
    }

    public abstract void run();
    public abstract void end();

}
