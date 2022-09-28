package com.atennapel.testgame;

public class Dir {
  public final int dx;
  public final int dy;

  public Dir(int dx, int dy) {
    if (dx < -1 || dx > 1 || dy < -1 || dy > 1)
      throw new IllegalArgumentException(
          "Dir components should be -1, 0 or 1, but got: (" + dx + ", " + dy + ")");
    this.dx = dx;
    this.dy = dy;
  }

  public boolean noChange() {
    return dx == 0 && dy == 0;
  }

  @Override
  public String toString() {
    return "Dir(" + dx + ", " + dy + ")";
  }
}
