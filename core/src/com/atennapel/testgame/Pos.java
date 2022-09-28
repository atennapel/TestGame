package com.atennapel.testgame;

public class Pos {
  public int x;
  public int y;

  public Pos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Dir dir(Pos other) {
    int dx = 0;
    int dy = 0;
    if (this.x < other.x)
      dx = 1;
    else if (this.x > other.x)
      dx = -1;
    if (this.y < other.y)
      dy = 1;
    else if (this.y > other.y)
      dy = -1;
    return new Dir(dx, dy);
  }

  public Pos add(Dir dir, boolean mutate) {
    if (mutate) {
      x += dir.dx;
      y += dir.dy;
      return this;
    } else {
      return new Pos(x + dir.dx, y + dir.dy);
    }
  }

  public Pos add(Dir dir) {
    return add(dir, false);
  }

  @Override
  public String toString() {
    return "Pos(" + x + ", " + y + ")";
  }
}
