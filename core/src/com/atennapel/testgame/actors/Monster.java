package com.atennapel.testgame.actors;

import java.util.Optional;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;
import com.atennapel.testgame.actions.Move;

public class Monster extends Actor {
  public Monster(int x, int y) {
    this.x = x;
    this.y = y;
    this.speed = 50;
  }

  @Override
  public Optional<Action> getAction(TestGame game) {
    int px = game.getPlayer().getX();
    int py = game.getPlayer().getY();

    int dx = 0;
    int dy = 0;

    if (px > x)
      dx = 1;
    else if (px < x)
      dx = -1;
    if (py > y)
      dy = 1;
    else if (py < y)
      dy = -1;

    return Optional.<Action>of(new Move(dx, dy));
  }

  @Override
  public String toString() {
    return "Monster";
  }
}
