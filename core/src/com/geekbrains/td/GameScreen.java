package com.geekbrains.td;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.LinkedList;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Vector2 mousePosition;
    private Map map;
    private TextureRegion selectedCellTexture;
    private ParticleEmitter particleEmitter;
    private MonsterEmitter monsterEmitter;
    private BulletEmitter bulletEmitter;
    private TurretEmitter turretEmitter;
    private InfoEmitter infoEmitter;
    private BitmapFont font18;
    private BitmapFont font24;
    private BitmapFont font72;
    private int selectedCellX, selectedCellY;
    private Player player;
    private Antiking antiking;
    private King king;
    private int level;
    private boolean isGameOver;

    enum EventSource {
        EV_MONSTER_EMITTER,
        EV_BULLET_EMITTER,
        EV_TURRET_BUTTON
    }

    private Stage stage;
    private Group groupTurretAction;
    private Group groupTurretSelection;


    public Map getMap() {
        return map;
    }

    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }

    public MonsterEmitter getMonsterEmitter() {
        return monsterEmitter;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public TurretEmitter getTurretEmitter() {
        return turretEmitter;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public InfoEmitter getInfoEmitter() {
        return infoEmitter;
    }

    public Antiking getAntiking() {
        return antiking;
    }

    public King getKing() {
        return king;
    }

    @Override
    public void show() {
        this.player = new Player();
        this.mousePosition = new Vector2(0, 0);
        this.particleEmitter = new ParticleEmitter();
        this.font18 = Assets.getInstance().getAssetManager().get("fonts/zorque18.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/zorque24.ttf");
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/zorque72.ttf");
        this.king = new King();
        this.antiking = new Antiking(this);
        this.bulletEmitter = new BulletEmitter(this);
        this.map = new Map("level01.map");
        this.monsterEmitter = new MonsterEmitter(this);
        this.turretEmitter = new TurretEmitter(this);
        this.infoEmitter = new InfoEmitter(this);
        this.selectedCellTexture = Assets.getInstance().getAtlas().findRegion("cursor");
        this.createGUI();
        level = 1;
        isGameOver = false;
        levelStart();
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);

        InputProcessor myProc = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                mousePosition.set(screenX, screenY);
                ScreenManager.getInstance().getViewport().unproject(mousePosition);
                if (selectedCellX == (int) (mousePosition.x / 80) && selectedCellY == (int) (mousePosition.y / 80)) {
                    map.setWall((int) (mousePosition.x / 80), (int) (mousePosition.y / 80));
                }
                selectedCellX = (int) (mousePosition.x / 80);
                selectedCellY = (int) (mousePosition.y / 80);
                return true;
            }
        };

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage, myProc);
        Gdx.input.setInputProcessor(inputMultiplexer);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        groupTurretAction = new Group();
        groupTurretAction.setPosition(250, 600);

        Button btnSetTurret = new TextButton("Set", skin, "simpleSkin");
        Button btnUpgradeTurret = new TextButton("Upg", skin, "simpleSkin");
        Button btnDestroyTurret = new TextButton("Dst", skin, "simpleSkin");
        btnSetTurret.setPosition(10, 10);
        btnUpgradeTurret.setPosition(110, 10);
        btnDestroyTurret.setPosition(210, 10);
        groupTurretAction.addActor(btnSetTurret);
        groupTurretAction.addActor(btnUpgradeTurret);
        groupTurretAction.addActor(btnDestroyTurret);

        groupTurretSelection = new Group();
        groupTurretSelection.setVisible(false);
        groupTurretSelection.setPosition(250, 500);
        Button btnSetTurret1 = new TextButton("T1", skin, "simpleSkin");
        Button btnSetTurret2 = new TextButton("T2", skin, "simpleSkin");
        btnSetTurret1.setPosition(10, 10);
        btnSetTurret2.setPosition(110, 10);
        groupTurretSelection.addActor(btnSetTurret1);
        groupTurretSelection.addActor(btnSetTurret2);

        btnSetTurret1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.buildTurret(player, "Red-Turret-I", selectedCellX, selectedCellY);
            }
        });

        btnSetTurret2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.buildTurret(player, "Blue-Turret-I", selectedCellX, selectedCellY);
            }
        });

        btnDestroyTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.removeTurret(player, selectedCellX, selectedCellY);
                levelEvent(EventSource.EV_TURRET_BUTTON);
            }
        });

        btnUpgradeTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                turretEmitter.upgradeTurret(player, selectedCellX, selectedCellY);
                levelEvent(EventSource.EV_TURRET_BUTTON);
            }
        });

        stage.addActor(groupTurretSelection);
        stage.addActor(groupTurretAction);

//        upperPanel = new UpperPanel(playerInfo, stage, 0, 720 - 60);

        btnSetTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                groupTurretSelection.setVisible(!groupTurretSelection.isVisible());
                levelEvent(EventSource.EV_TURRET_BUTTON);
            }
        });

        TextButton.TextButtonStyle textButtonStyleMenu = new TextButton.TextButtonStyle();
        textButtonStyleMenu.up = skin.getDrawable("simpleButton");
        textButtonStyleMenu.font = font24;
        skin.add("menuSkin", textButtonStyleMenu);
        Button btnMenu = new TextButton("Menu", skin, "menuSkin");
        stage.addActor(btnMenu);
        btnMenu.setPosition(900, 610);
        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        skin.dispose();
    }

    public BitmapFont getFont24() {
        return font24;
    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        batch.begin();
        map.render(batch);

        batch.setColor(1, 1, 0, 0.5f);
        batch.draw(selectedCellTexture, selectedCellX * 80, selectedCellY * 80);
        batch.setColor(1, 1, 1, 1);
        king.render(batch, font24);
        monsterEmitter.render(batch);
        turretEmitter.render(batch);
        bulletEmitter.render(batch);
        particleEmitter.render(batch);
        player.renderInfo(batch, font18);
        infoEmitter.render(batch, font24);
        drawLevel();
        if (isGameOver) drawGameOver();
        batch.end();

        stage.draw();
    }

    public void update(float dt) {
        map.update(dt);
        king.update(dt);
        monsterEmitter.update(dt);
        turretEmitter.update(dt);
        particleEmitter.update(dt);
        bulletEmitter.update(dt);
        checkCollisions();
        checkMonstersInCastle();
        infoEmitter.update(dt);

        monsterEmitter.checkPool();
        particleEmitter.checkPool();
        bulletEmitter.checkPool();
        turretEmitter.checkPool();
        infoEmitter.checkPool();

        stage.act(dt);
    }

    public void checkMonstersInCastle() {
        for (int i = 0; i < monsterEmitter.getActiveList().size(); i++) {
            Monster m = monsterEmitter.getActiveList().get(i);
            if ((int) (m.getPosition().x / 80) == king.getCellX() && (int) (m.getPosition().y / 80) == king.getCellY()) {
                king.takeDamage(5);
                infoEmitter.setup(king.getPosition().x, king.getPosition().y, "DMG 5");
                m.deactivate();
            }
        }
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            Bullet b = bulletEmitter.getActiveList().get(i);
            if (b.getPosition().x < 0 || b.getPosition().x > 1280 || b.getPosition().y < 0 || b.getPosition().y > 720) {
                b.deactivate();
                continue;
            }
            if (!map.isCellEmpty((int) (b.getPosition().x / 80), (int) (b.getPosition().y / 80))) {
                b.deactivate();
                continue;
            }
            for (int j = 0; j < monsterEmitter.getActiveList().size(); j++) {
                Monster m = monsterEmitter.getActiveList().get(j);
                if (m.getHitArea().contains(b.getPosition())) {
                    b.deactivate();
                    if (m.takeDamage(b.getPower())) {
                        player.changeCoins(5);
                        particleEmitter.getEffectBuilder().buildMonsterSplash(m.getPosition().x, m.getPosition().y);
                    }
                }
            }
        }
    }

    public void levelStart() {
        monsterEmitter.reset();
        turretEmitter.reset();
        LinkedList<MonsterWaveType> waveList = monsterEmitter.getWaveList();
        switch (level) {
            case 1:
                waveList.push(MonsterWaveType.STRONG);
                waveList.push(MonsterWaveType.WEEK);
                MonsterWaveType pause = MonsterWaveType.PAUSE;
                pause.setDuration(5);
                waveList.push(pause);

                break;
            case 2:
                monsterEmitter.setSingleMonster(antiking);
                waveList.push(MonsterWaveType.WEEK);
                waveList.push(MonsterWaveType.SINGLE);
                pause = MonsterWaveType.PAUSE;
                pause.setDuration(5);
                waveList.push(pause);
                waveList.push(MonsterWaveType.STRONG);
                break;
        }
    }


    private int bulletCounter = 0;

    public void levelEvent(EventSource event) {
        switch (event) {
            case EV_BULLET_EMITTER:
                bulletCounter++;
                if (bulletCounter == 8) {
                    player.changeCoins(-15);
                    bulletCounter = 0;
                }
                break;
            case EV_MONSTER_EMITTER:
                if (monsterEmitter.getSingleMonsterStatus()) {
                    groupTurretSelection.setVisible(false);
                    bulletEmitter.setHasCost(true);
                    antiking.updateHelperMap();
                } else {
                    groupTurretSelection.setVisible(true);
                    bulletEmitter.setHasCost(false);
                }
                break;
            case EV_TURRET_BUTTON:
                if (monsterEmitter.getSingleMonsterStatus()) {
                    groupTurretSelection.setVisible(false);
                    antiking.updateHelperMap();
                }
                break;

        }
    }

    public void levelFinish() {
        if (level == 2) {
            isGameOver = true;
        } else {
            level++;
            levelStart();
        }
    }

    public void drawLevel() {
        font18.draw(batch, "Level: " + level, 20, 700);
    }

    public void drawGameOver() {
        font72.draw(batch, "Game Over", 500, 300);
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
