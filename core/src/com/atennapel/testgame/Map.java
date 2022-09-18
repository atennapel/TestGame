package com.atennapel.testgame;

import static com.atennapel.testgame.Constants.*;
import static com.atennapel.testgame.Tile.*;

public class Map {
  private Tile[][] map;

  public Map() {
    map = new Tile[WIDTH][HEIGHT];

    // floor
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        map[x][y] = EMPTY;
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
    Tile t = map[x][y];
    return t == WALL || t == DOOR_CLOSED;
  }

  public Tile get(int x, int y) {
    return map[x][y];
  }

  public void set(int x, int y, Tile t) {
    map[x][y] = t;
  }

  public boolean is(int x, int y, Tile t) {
    return map[x][y] == t;
  }
}
