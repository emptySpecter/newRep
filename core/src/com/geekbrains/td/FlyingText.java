package com.geekbrains.td;

import com.badlogic.gdx.math.Vector2;

public class FlyingText implements Poolable {
    private String text;
    private boolean active;
    private Vector2 position;
    private Vector2 velocity;
    private float time;
    private float maxTime;

    @Override
    public boolean isActive() {
        return active;
    }

    public String getText() {
        return text;
    }

    public Vector2 getPosition() {
        return position;
    }

    public FlyingText() {
        this.text = "";
        this.active = false;
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(20.0f, 40.0f);
        this.time = 0.0f;
        this.maxTime = 1.5f;
    }

    public void setup(float x, float y, String text) {
        this.position.set(x, y);
        this.active = true;
        this.text = text;
        this.time = 0.0f;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= maxTime) {
            active = false;
        }
    }
}
