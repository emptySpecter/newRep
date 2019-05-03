package com.geekbrains.td;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TowerDefenseGame extends Game {
    private SpriteBatch batch;

    // Домашнее задание:
    // 1. + Сделать меню, и переход Игра > Меню > Игра. В меню: Новая игра и Выход
    // 2. Добавить короля, который бегает и монстры иду к нему
    // * Переход на второй уровень
    // ** Система волн (первые 30 секунд бегут слабые монстры (выбегает каждые 2 сек), потом в течение минуты посильнее (каждые 1.5 секунды), и далее-далее)
    // *** Вынести настройки пушек в отдельный файл (Или обычный тхт, или делать автомаршаллинг через Jackson например)

    // На занятии:
    // Анимация
    // *** Погодные эффекты, Радиальное меню
    // Перенести на Андроид

    @Override
    public void create() {
        batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
