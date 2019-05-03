package com.geekbrains.td;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class Map {
    private final int MAP_WIDTH = 16;
    private final int MAP_HEIGHT = 9;

    private final int ELEMENT_GRASS = 0;
    private final int ELEMENT_ROAD = 1;
    private final int ELEMENT_WALL = 2;

    private int[][] data;
    private TextureRegion textureRegionGrass;
    private TextureRegion textureRegionRoad;

    private int[][] routeHelperArray;

    public Map(String mapName) {
        data = new int[MAP_WIDTH][MAP_HEIGHT];
        routeHelperArray = new int[MAP_WIDTH][MAP_HEIGHT];

        textureRegionGrass = Assets.getInstance().getAtlas().findRegion("grass");
        textureRegionRoad = Assets.getInstance().getAtlas().findRegion("road");
        loadMapFromFile(mapName);
    }

    public boolean isCellEmpty(int cellX, int cellY) {
        int value = data[cellX][cellY];
        if (value == ELEMENT_GRASS || value == ELEMENT_ROAD) {
            return true;
        }
        return false;
    }

    public void setWall(int cx, int cy) {
        data[cx][cy] = ELEMENT_WALL;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < MAP_WIDTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                if (data[i][j] == ELEMENT_GRASS) {
                    batch.draw(textureRegionGrass, i * 80, j * 80);
                }
                if (data[i][j] == ELEMENT_ROAD) {
                    batch.draw(textureRegionRoad, i * 80, j * 80);
                }
                if (data[i][j] == ELEMENT_WALL) {
                    batch.setColor(0, 0, 0, 1);
                    batch.draw(textureRegionRoad, i * 80, j * 80);
                    batch.setColor(1, 1, 1, 1);
                }
            }
        }
    }

    public void update(float dt) {
    }

    public void loadMapFromFile(String mapName) {
        this.data = new int[MAP_WIDTH][MAP_HEIGHT];
        BufferedReader reader = null;
        try {
            reader = Gdx.files.internal("maps/" + mapName).reader(8192);
            for (int i = 0; i < 9; i++) {
                String str = reader.readLine();
                for (int j = 0; j < 16; j++) {
                    char symb = str.charAt(j);
                    if (symb == '1') {
                        data[j][8 - i] = ELEMENT_ROAD;
                    }
                    if (symb == '2') {
                        data[j][8 - i] = ELEMENT_WALL;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildRoute(int srcX, int srcY, int dstX, int dstY, Vector2 destination) {
        for (int i = 0; i < MAP_WIDTH; i++) {
            for (int j = 0; j < MAP_HEIGHT; j++) {
                routeHelperArray[i][j] = 0;
                if (!isCellEmpty(i, j)) {
                    routeHelperArray[i][j] = -1;
                }
            }
        }

        routeHelperArray[srcX][srcY] = 1;

        int lastPoint = -1;

        for (int i = 1; i < 100; i++) { // todo почему 100?
            for (int x = 0; x < MAP_WIDTH; x++) {
                for (int y = 0; y < MAP_HEIGHT; y++) {
                    if (routeHelperArray[x][y] == i) {
                        fillAroundPoint(routeHelperArray, x, y, i + 1);
                    }
                }
            }
            if (routeHelperArray[dstX][dstY] > 0) {
                lastPoint = routeHelperArray[dstX][dstY];
                break;
            }
        }

        for (int i = dstX - 1; i <= dstX + 1; i++) {
            for (int j = dstY - 1; j <= dstY + 1; j++) {
                if (i >= 0 && i < MAP_WIDTH && j >= 0 && j < MAP_HEIGHT && routeHelperArray[i][j] == lastPoint - 1) {
                    destination.set(i, j);
                    return;
                }
            }
        }
    }

    public void fillAroundPoint(int[][] arr, int x, int y, int number) {
        if (x - 1 > -1 && arr[x - 1][y] == 0) {
            arr[x - 1][y] = number;
        }
        if (x + 1 < MAP_WIDTH && arr[x + 1][y] == 0) {
            arr[x + 1][y] = number;
        }
        if (y + 1 < MAP_HEIGHT && arr[x][y + 1] == 0) {
            arr[x][y + 1] = number;
        }
        if (y - 1 > -1 && arr[x][y - 1] == 0) {
            arr[x][y - 1] = number;
        }
    }
}
