package com.atennapel.testgame;

public enum Tiles {
  EMPTY,
  WALL,
  DOOR_CLOSED,
  DOOR_OPEN;

  public int getTile() {
    switch (this) {
      case EMPTY:
        return 17;
      case WALL:
        return 8;
      case DOOR_CLOSED:
        return 9;
      case DOOR_OPEN:
        return 10;
      default:
        return 16;
    }
  }
}
