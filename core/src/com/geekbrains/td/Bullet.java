package com.geekbrains.td;

import com.badlogic.gdx.math.Vector2;

public class Bullet implements Poolable {
    private Monster target;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;

    private int power;
    private float speed;
    private boolean autoaim;

    public Monster getTarget() {
        return target;
    }

    public int getPower() {
        return power;
    }

    public boolean isAutoaim() {
        return autoaim;
    }

    public float getSpeed() {
        return speed;
    }

    public void setAutoaim(boolean autoaim) {
        this.autoaim = autoaim;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public Bullet() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    public void setup(BulletTemplate bulletTemplate, Monster target, float x, float y, float angleRad) {
        this.speed = bulletTemplate.getSpeed();
        this.power = bulletTemplate.getPower();
        this.autoaim = bulletTemplate.isAutoaim();
        this.target = target;
        this.position.set(x, y);
        this.velocity.set(speed * (float)Math.cos(angleRad), speed * (float)Math.sin(angleRad));
        this.active = true;
    }
}
