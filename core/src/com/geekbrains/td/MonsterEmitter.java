package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MonsterEmitter extends ObjectPool<Monster> {
    private GameScreen gameScreen;

    public MonsterEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    protected Monster newObject() {
        return new Monster(gameScreen);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup(int cellX, int cellY) {
        Monster monster = getActiveElement();
        monster.activate(cellX * 80 + 40, cellY * 80 + 40);
    }
}
