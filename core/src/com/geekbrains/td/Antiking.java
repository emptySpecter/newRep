package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;

public class Antiking extends Monster {
    private float animationTimer, timePerFrame;
    private BitmapFont font;
    private int destCellX;
    private int destCellY;
    private float speed;

    public Circle getHitArea() {
        return hitArea;
    }

    public boolean isActive() {
        return active;
    }

    public void activate(float x, float y) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(-150.0f, 0.0f);
        this.hpMax = 500;
        this.hp = this.hpMax;
        this.getNextPoint();
        this.active = true;
        this.hitArea.set(position.x, position.y, 32.0f);

    }

    public Vector2 getPosition() {
        return position;
    }

    public Antiking(GameScreen gameScreen) {
        super(gameScreen);
        this.position = new Vector2(10 * 80 + 40, 4 * 80 + 40);
        this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("antiking"));
        this.timePerFrame = 0.1f;
        this.hpMax = 500;
        this.hp = this.hpMax;
        this.font = gameScreen.getFont24();
        this.hitArea = new Circle(0, 0, 0);
        this.speed = 150.0f;
        this.destCellX = 15;
        this.destCellY = 0;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 40, position.y - 40);
        batch.setColor(1, 1, 1, 0.8f);
        batch.draw(textureBackHp, position.x - 30, position.y + 40 - 4 + 4 * (float) Math.sin(animationTimer * 5));
        batch.draw(textureHp, position.x - 30 + 2, position.y + 40 - 2 + 4 * (float) Math.sin(animationTimer * 5), 56 * ((float) hp / hpMax), 12);
        font.draw(batch, "" + hp, position.x - 30, position.y + 42 + 10 + 4 * (float) Math.sin(animationTimer * 5), 60, 1, false);
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float dt) {
        velocity.set(destination).sub(position).nor().scl(speed);
        position.mulAdd(velocity, dt);
        if (position.dst(destination) < 2.0f) {
            getNextPoint();
        }

        animationTimer += dt;
        this.hitArea.set(position.x, position.y, 32.0f);
    }


    public void getNextPoint() {
        float destX = destCellX * 80 + 40;
        float destY = destCellY * 80 + 40;
        if ((position.x - destX) * (position.x - destX) + (position.y - destY) * (position.y - destY) < 8.0f) {
            CheckDestPosition();
        }
        gameScreen.getMap().buildRoute(destCellX, destCellY, (int) (position.x / 80), (int) (position.y / 80), destination);
        destination.scl(80, 80).add(40, 40);
    }

    private void CheckDestPosition() {
        int code = helperMap[destCellX][destCellY];
        if (code == 2) {
            speed = 0.0f;
        } else {
            speed = 150.0f;
            if (!findCell(2)) {
                for (int i = 0; i < 16; i++) {
                    destCellX = MathUtils.random(3, 15);
                    destCellY = MathUtils.random(0, 8);
                    int newCode = helperMap[destCellX][destCellY];
                    if (code == 1 && newCode == 0) break;
                    if (code == 0 && newCode == 1) break;
                }
            }
        }
    }

    private boolean findCell(int code) {
        int width = helperMap.length;
        int height = helperMap[0].length;
        for (int i = width - 1; i >= 0; i--) {
            for (int j = height - 1; j >= 0; j--) {
                if (helperMap[i][j] == code) {
                    destCellX = i;
                    destCellY = j;
                    return true;
                }
            }
        }
        return false;
    }


    int[][] helperMap;

    public void updateHelperMap() {
        helperMap = gameScreen.getMap().getMapOfWalls();
        int width = helperMap.length;
        int height = helperMap[0].length;

        Rectangle rectangle = new Rectangle();
        for (Turret t : gameScreen.getTurretEmitter().getActiveList()) {
            helperMap[t.getCellX()][t.getCellY()] = 5;
        }
        for (Turret t : gameScreen.getTurretEmitter().getActiveList()) {
            float fireRadius2 = t.getFireRadius() * t.getFireRadius();
            int x = t.getCellX() * 80 + 40;
            int y = t.getCellY() * 80 + 40;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int x1 = i * 80 + 40;
                    int y1 = j * 80 + 40;
                    double r2 = (x - x1) * (x - x1) + (y - y1) * (y - y1);
                    if (r2 < fireRadius2 && helperMap[i][j] != 9) {
                        if (helperMap[i][j] != 5) helperMap[i][j] = 1;
                    }
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (helperMap[i][j] == 1) {
                    int x1 = i * 80 + 40;
                    int y1 = j * 80 + 40;
                    for (int k = 0; k < width; k++) {
                        for (int l = 0; l < height; l++) {
                            if (helperMap[k][l] == 9) {
                                rectangle.set(k * 80 + 1, l * 80 + 1, 80 - 2, 80 - 2);
                                boolean isSafe = true;
                                for (Turret t : gameScreen.getTurretEmitter().getActiveList()) {
                                    int x = t.getCellX() * 80 + 40;
                                    int y = t.getCellY() * 80 + 40;
                                    isSafe &= Intersector.intersectSegmentRectangle(x, y, x1, y1, rectangle);
                                }
                                if (isSafe) {
                                    helperMap[i][j] = 2;
                                }

                            }
                        }
                    }
                }
            }
        }
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                System.out.print(helperMap[j][i] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}


