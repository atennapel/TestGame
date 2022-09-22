package com.atennapel.testgame.actors;

import java.util.Optional;

import com.atennapel.testgame.Pos;
import com.atennapel.testgame.RGB;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;
import com.atennapel.testgame.actions.Move;
import com.atennapel.testgame.actions.Wait;

public class Monster extends Actor {
  public Monster(int x, int y) {
    super(x, y);
    this.speed = 50;
  }

  @Override
  public Optional<Action> getAction(TestGame game) {
    int px = game.getPlayer().getX();
    int py = game.getPlayer().getY();
    Optional<Pos> path = game.getPathfinding().findPath(x, y, px, py);
    if (path.isEmpty()) {
      int dx = game.getRandom().nextInt(3) - 1;
      int dy = game.getRandom().nextInt(3) - 1;
      if (dx == 0 && dy == 0)
        return Optional.<Action>of(new Wait());
      else
        return Optional.<Action>of(new Move(dx, dy));
    } else {
      int nx = path.get().x;
      int ny = path.get().y;
      int dx = 0;
      int dy = 0;
      if (nx > x)
        dx = 1;
      else if (nx < x)
        dx = -1;
      if (ny > y)
        dy = 1;
      else if (ny < y)
        dy = -1;
      return Optional.<Action>of(new Move(dx, dy));
    }
  }

  @Override
  public int getTile() {
    return 1;
  }

  @Override
  public RGB getColor() {
    return new RGB(154, 64, 55);
  }

  @Override
  public String toString() {
    return "Monster";
  }
}
