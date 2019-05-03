package com.geekbrains.td;

public class BulletTemplate {
    // # name, power, speed, autoaim
    private String name;
    private int power;
    private float speed;
    private boolean autoaim;

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isAutoaim() {
        return autoaim;
    }

    public BulletTemplate(String strTemplate) {
        String[] tokens = strTemplate.split(",");
        name = tokens[0].trim();
        power = Integer.parseInt(tokens[1].trim());
        speed = Float.parseFloat(tokens[2].trim());
        autoaim = Boolean.parseBoolean(tokens[3].trim());
    }
}
