package com.atennapel.testgame;

import static com.atennapel.testgame.Constants.*;

public class Map {
  private boolean[][] obstacles;

  public Map() {
    obstacles = new boolean[WIDTH][HEIGHT];
    for (int i = 0; i < WIDTH; i++) {
      obstacles[i][0] = true;
      obstacles[i][HEIGHT - 1] = true;
      if (i > 0 && i < HEIGHT - 1) {
        obstacles[0][i] = true;
        obstacles[WIDTH - 1][i] = true;
      }
    }
  }

  public boolean isBlocked(int x, int y) {
    return obstacles[x][y];
  }
}
