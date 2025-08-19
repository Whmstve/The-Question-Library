package net.whmstve.thequestionlib.general.camera.common;

public class Timeline {
    private double length;
    private double ticks = 0.0D;
    private boolean enabled = false;

    public Timeline() {
    }

    public void tick(double speed) {
        if (this.enabled) {
            if (this.ticks <= this.length) {
                this.ticks += speed;
            } else {
                this.stop();
            }
        }

    }

    public void start(double length) {
        this.length = length;
        this.start();
    }

    public void start() {
        this.enabled = true;
        this.ticks = 0.0D;
    }

    public void stop() {
        this.enabled = false;
        this.ticks = 0.0D;
    }

    public double getLength() {
        return this.length;
    }

    public double getTicks() {
        return this.ticks;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}