package com.geekbrains.td;

public class TurretTemplate {
    // # name, image_x, image_y, price, fireRadius, chargeTime, rotationSpeed, childName, bulletName
    private String name;
    private int imageX, imageY;
    private int price;
    private float fireRadius;
    private float chargeTime;
    private float rotationSpeed;
    private String childName;
    private String bulletName;

    public String getName() {
        return name;
    }

    public int getImageX() {
        return imageX;
    }

    public int getImageY() {
        return imageY;
    }

    public int getPrice() {
        return price;
    }

    public float getFireRadius() {
        return fireRadius;
    }

    public float getChargeTime() {
        return chargeTime;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public String getChildName() {
        return childName;
    }

    public String getBulletName() {
        return bulletName;
    }

    public TurretTemplate(String strTemplate) {
        String[] tokens = strTemplate.split(",");
        name = tokens[0].trim();
        imageX = Integer.parseInt(tokens[1].trim());
        imageY = Integer.parseInt(tokens[2].trim());
        price = Integer.parseInt(tokens[3].trim());
        fireRadius = Float.parseFloat(tokens[4].trim());
        chargeTime = Float.parseFloat(tokens[5].trim());
        rotationSpeed = Float.parseFloat(tokens[6].trim());
        childName = tokens[7].trim();
        bulletName = tokens[8].trim();
    }
}
