package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class King {
    private Vector2 position;
    private TextureRegion[] texture;
    private TextureRegion textureHp;
    private TextureRegion textureBackHp;
    private float animationTimer, timePerFrame;
    private int hp, hpMax;

    public boolean isAlive() {
        return hp >= 0;
    }

    public int getCellX() {
        return (int) (position.x / 80);
    }

    public int getCellY() {
        return (int) (position.y / 80);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    public King() {
        this.position = new Vector2(0 * 80 + 40, 4 * 80 + 40);
        this.texture = new TextureRegion(Assets.getInstance().getAtlas().findRegion("animatedKing")).split(80, 80)[0];
        this.textureHp = Assets.getInstance().getAtlas().findRegion("monsterHp");
        this.textureBackHp = Assets.getInstance().getAtlas().findRegion("monsterBackHP");
        this.timePerFrame = 0.1f;
        this.hpMax = 200;
        this.hp = this.hpMax;
    }

    public void update(float dt) {
        animationTimer += dt;
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        int index = (int) (animationTimer / timePerFrame) % texture.length;
        batch.draw(texture[index], position.x - 40, position.y - 40);
        batch.setColor(1, 1, 1, 0.8f);
        batch.draw(textureBackHp, position.x - 30, position.y + 40 - 16 + 4 * (float) Math.sin(animationTimer * 5));
        batch.draw(textureHp, position.x - 30 + 2, position.y + 40 - 14 + 4 * (float) Math.sin(animationTimer * 5), 56 * ((float) hp / hpMax), 12);
        font.draw(batch, "" + hp, position.x - 30, position.y + 42 + 4 * (float) Math.sin(animationTimer * 5), 60, 1, false);
        batch.setColor(1, 1, 1, 1);
    }
}
