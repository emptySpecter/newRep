package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private int coins;

    public int getCoins() {
        return coins;
    }

    public boolean isMoneyEnough(int amount) {
        return coins >= amount;
    }

    public void changeCoins(int amount) {
        this.coins += amount;
    }

    public Player() {
        this.coins = 500;
    }

    public void renderInfo(SpriteBatch batch, BitmapFont font) {
        font.draw(batch, "Coins: " + coins, 20, 680);
    }
}
