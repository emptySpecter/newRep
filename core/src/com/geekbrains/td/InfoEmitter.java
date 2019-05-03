package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InfoEmitter extends ObjectPool<FlyingText> {
    private GameScreen gameScreen;

    @Override
    protected FlyingText newObject() {
        return new FlyingText();
    }

    public InfoEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setup(float x, float y, String  text) {
        FlyingText flyingText = getActiveElement();
        flyingText.setup(x, y, text);
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        batch.setColor(0, 1, 0, 1);
        for (int i = 0; i < activeList.size(); i++) {
            FlyingText flyingText = activeList.get(i);
            font.draw(batch, flyingText.getText(), flyingText.getPosition().x, flyingText.getPosition().y);
        }
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            FlyingText flyingText = activeList.get(i);
            flyingText.update(dt);
        }
    }
}
