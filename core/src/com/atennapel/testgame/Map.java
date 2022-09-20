package com.atennapel.testgame;

import static com.atennapel.testgame.Constants.*;
import static com.atennapel.testgame.Tiles.*;

public class Map {
  private Tiles[][] map;
  private boolean[][] visible;
  private boolean[][] explored;

  public Map() {
    map = new Tiles[WIDTH][HEIGHT];
    visible = new boolean[WIDTH][HEIGHT];
    explored = new boolean[WIDTH][HEIGHT];

    // floor
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        map[x][y] = EMPTY;
        visible[x][y] = false;
        explored[x][y] = false;
      }
    }

    // place walls
    for (int i = 0; i < WIDTH; i++) {
      map[i][0] = WALL;
      map[i][HEIGHT - 3] = WALL;
      if (i > 0 && i < HEIGHT - 3) {
        map[0][i] = WALL;
        map[WIDTH - 1][i] = WALL;
      }
    }
    for (int i = 6; i < WIDTH - 6; i++) {
      map[i][5] = WALL;
      map[i][HEIGHT - 6] = WALL;
      if (i > 5 && i < HEIGHT - 6) {
        map[6][i] = WALL;
        map[WIDTH - 7][i] = WALL;
      }
    }

    map[6][7] = DOOR_CLOSED;
  }

  public boolean isBlocked(int x, int y) {
    Tiles t = map[x][y];
    return t == WALL || t == DOOR_CLOSED;
  }

  public Tiles get(int x, int y) {
    return map[x][y];
  }

  public void set(int x, int y, Tiles t) {
    map[x][y] = t;
  }

  public boolean is(int x, int y, Tiles t) {
    return map[x][y] == t;
  }

  public boolean isVisible(int x, int y) {
    return visible[x][y];
  }

  public boolean isExplored(int x, int y) {
    return explored[x][y];
  }

  public void setVisible(int x, int y, boolean isVisible) {
    visible[x][y] = isVisible;
    if (isVisible)
      explored[x][y] = true;
  }

  public void setExplored(int x, int y, boolean isExplored) {
    explored[x][y] = isExplored;
  }
}
