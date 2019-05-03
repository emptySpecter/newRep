package com.geekbrains.td;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.LinkedList;

public class MonsterEmitter extends ObjectPool<Monster> {
    private GameScreen gameScreen;
    private float monsterTimer;
    private LinkedList<MonsterWaveType> waveList;
    private MonsterWaveType currentWave;
    private int emittedMonsters;
    private Monster singleMonster;
    private boolean isSingleMonsterStatus;
    private boolean isSingleFirstTime;

    public MonsterEmitter(GameScreen gameScreen) {
        isSingleFirstTime = true;
        isSingleMonsterStatus = false;
        this.gameScreen = gameScreen;
        waveList = new LinkedList<MonsterWaveType>();
        resetGeneration();
    }

    public boolean getSingleMonsterStatus() {
        return isSingleMonsterStatus;
    }

    public void setSingleMonster(Monster singleMonster) {
        this.singleMonster = singleMonster;
    }

    public LinkedList<MonsterWaveType> getWaveList() {
        return waveList;
    }

    private void resetGeneration() {
        monsterTimer = 0;
        emittedMonsters = 0;

        currentWave = null;


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
        generateMonsters(dt);
    }

    public void setup(int cellX, int cellY) {
        Monster monster = getActiveElement();
        monster.activate(cellX * 80 + 40, cellY * 80 + 40);
    }

    private void generateMonsters(float dt) {
        if (monsterTimer < 0.001f) {
            if (!waveList.isEmpty()) {
                currentWave = waveList.pop();
            } else {
                currentWave = null;
                gameScreen.levelFinish();
            }
        }
        if (currentWave != null) {
            monsterTimer += dt;
            if (currentWave == MonsterWaveType.SINGLE){
                isSingleMonsterStatus = true;
                if (isSingleFirstTime) {
                    gameScreen.levelEvent(GameScreen.EventSource.EV_MONSTER_EMITTER);
                }
            }
            if (!isSingleMonsterStatus) {
                int mustBeEmitted = (int) (monsterTimer / currentWave.rate);
                int deltaMonsters = mustBeEmitted - emittedMonsters;
                if (deltaMonsters > 0) {
                    for (int i = 0; i < deltaMonsters; i++) {
                        setup(15, MathUtils.random(0, 8));
                        emittedMonsters++;
                    }
                }
            } else {
                if (singleMonster != null){
                    if(!activeList.contains(singleMonster)){
                       if (isSingleFirstTime){
                            activeList.add(singleMonster);
                            singleMonster.activate(15*80, 5*80);
                            isSingleFirstTime = false;
                       }

                        if (!singleMonster.isAlive()){
                            singleMonster.deactivate();
                            activeList.remove(singleMonster);
                            freeList.remove(singleMonster);

                        }
                    }
                }
            }
            if (monsterTimer > currentWave.duration) {
                if (isSingleMonsterStatus) {
                    activeList.remove(singleMonster);
                    freeList.remove(singleMonster);
                    isSingleMonsterStatus = false;
                    isSingleFirstTime = true;
                    gameScreen.levelEvent(GameScreen.EventSource.EV_MONSTER_EMITTER);
                }
                monsterTimer = 0;
                emittedMonsters = 0;
            }
        }

    }


    public void reset() {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).deactivate();
        }
        checkPool();
    }
}
