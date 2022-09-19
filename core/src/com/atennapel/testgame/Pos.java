package com.atennapel.testgame;

public class Pos {
  public final int x;
  public final int y;

  public Pos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Pos(" + x + ", " + y + ")";
  }
}
