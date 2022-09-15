package com.atennapel.testgame.actions;

import java.util.Optional;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;

public class Move implements Action {
  private final int dx;
  private final int dy;

  public Move(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }

  @Override
  public ActionResult perform(TestGame game, Actor actor) {
    if (dx == 0 && dy == 0)
      return ActionResult.alternateAction(new Wait());
    int x = actor.getX() + dx;
    int y = actor.getY() + dy;
    if (game.getMap().isBlocked(x, y))
      return ActionResult.failure();
    Optional<Actor> blockingActor = game.actorAt(x, y);
    if (blockingActor.isEmpty()) {
      actor.setX(x);
      actor.setY(y);
      return ActionResult.success();
    } else {
      return ActionResult.alternateAction(new Wait());
    }
  }

  @Override
  public String toString() {
    return "Move(" + dx + ", " + dy + ")";
  }
}
