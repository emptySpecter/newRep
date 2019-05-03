package com.geekbrains.td;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class TurretEmitter extends ObjectPool<Turret> {
    private GameScreen gameScreen;
    private TextureRegion[][] allTextures;
    private HashMap<String, TurretTemplate> templates;

    public TurretEmitter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.allTextures = new TextureRegion(Assets.getInstance().getAtlas().findRegion("turrets")).split(80, 80);
        this.templates = new HashMap<String, TurretTemplate>();
        this.loadTemplates();
    }

    @Override
    protected Turret newObject() {
        return new Turret(gameScreen, allTextures);
    }

    public boolean setup(String turretTemplateName, int cellX, int cellY) {
        if (!canIDeployItHere(cellX, cellY)) {
            return false;
        }
        Turret turret = getActiveElement();
        turret.setup(templates.get(turretTemplateName), cellX, cellY);
        return true;
    }

    public void buildTurret(Player player, String turretTemplateName, int cellX, int cellY) {
        TurretTemplate turretTemplate = templates.get(turretTemplateName);
        if (player.isMoneyEnough(turretTemplate.getPrice())) {
            if (setup(turretTemplate.getName(), cellX, cellY)) {
                player.changeCoins(-turretTemplate.getPrice());
                gameScreen.getInfoEmitter().setup(cellX * 80 + 40, cellY * 80 + 40, "-" + turretTemplate.getPrice());
            }
        }
    }

    public void upgradeTurret(Player player, int cellX, int cellY) {
        Turret turretForUpgrade = findTurretInCell(cellX, cellY);
        if (turretForUpgrade == null) {
            return;
        }
        TurretTemplate currentTurretTemplate = templates.get(turretForUpgrade.getTurretTemplateName());
        if (currentTurretTemplate.getChildName().equals("-")) {
            gameScreen.getInfoEmitter().setup(cellX * 80 + 40, cellY * 80 + 40, "[ERROR] Top turret");
            return;
        }
        TurretTemplate nextLevelTurretTemplate = templates.get(currentTurretTemplate.getChildName());
        if (player.isMoneyEnough(nextLevelTurretTemplate.getPrice())) {
            turretForUpgrade.setup(nextLevelTurretTemplate, cellX, cellY);
            player.changeCoins(-nextLevelTurretTemplate.getPrice());
            gameScreen.getInfoEmitter().setup(cellX * 80 + 40, cellY * 80 + 40, "-" + nextLevelTurretTemplate.getPrice());
        }
    }

    public void removeTurret(Player player, int cellX, int cellY) {
        Turret turretForDelete = findTurretInCell(cellX, cellY);
        if (turretForDelete == null) {
            return;
        }
        turretForDelete.deactivate();
        TurretTemplate turretTemplate = templates.get(turretForDelete.getTurretTemplateName());
        player.changeCoins(turretTemplate.getPrice() / 2);
        gameScreen.getInfoEmitter().setup(cellX * 80 + 40, cellY * 80 + 40, "+" + (turretTemplate.getPrice() / 2));
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

    public boolean canIDeployItHere(int cellX, int cellY) {
        if (!gameScreen.getMap().isCellEmpty(cellX, cellY)) {
            return false;
        }
        for (int i = 0; i < activeList.size(); i++) {
            Turret turret = activeList.get(i);
            if (turret.isActive() && turret.getCellX() == cellX && turret.getCellY() == cellY) {
                return false;
            }
        }
        return true;
    }

    public Turret findTurretInCell(int cellX, int cellY) {
        for (int i = 0; i < activeList.size(); i++) {
            Turret turret = activeList.get(i);
            if (turret.isActive() && turret.getCellX() == cellX && turret.getCellY() == cellY) {
                return turret;
            }
        }
        return null;
    }

    public void loadTemplates() {
        BufferedReader reader = null;
        try {
            reader = Gdx.files.internal("armory.dat").reader(8192);
            String str;
            boolean reading = false;
            while ((str = reader.readLine()) != null) {
                if (str.equals("# turrets-start")) {
                    reading = true;
                    continue;
                }
                if (reading && str.equals("# turrets-end")) {
                    break;
                }
                if (reading) {
                    TurretTemplate turretTemplate = new TurretTemplate(str);
                    templates.put(turretTemplate.getName(), turretTemplate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
