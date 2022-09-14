package com.atennapel.testgame;

public abstract class Input {
  private Input() {
  }

  public static final class Direction extends Input {
    public final int dx;
    public final int dy;

    public Direction(int dx, int dy) {
      if (dx == 0 && dy == 0)
        throw new IllegalArgumentException("either dx or dy has to be non-zero");
      this.dx = dx;
      this.dy = dy;
    }
  }

  public static Direction direction(int dx, int dy) {
    return new Direction(dx, dy);
  }
}
