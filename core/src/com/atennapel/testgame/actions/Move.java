package com.atennapel.testgame.actions;

import java.util.Optional;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.Tile;
import com.atennapel.testgame.actors.Actor;
import com.atennapel.testgame.Map;
import com.atennapel.testgame.Constants;

public class Move implements Action {
  private final int dx;
  private final int dy;
  private final boolean waitOnBlocked;
  private final boolean canOpenDoors;

  public Move(int dx, int dy, boolean waitOnBlocked, boolean canOpenDoors) {
    this.dx = dx;
    this.dy = dy;
    this.waitOnBlocked = waitOnBlocked;
    this.canOpenDoors = canOpenDoors;
  }

  public Move(int dx, int dy) {
    this(dx, dy, true, false);
  }

  @Override
  public ActionResult perform(TestGame game, Actor actor) {
    if (dx == 0 && dy == 0)
      return ActionResult.alternateAction(new Wait());
    int x = actor.getX() + dx;
    int y = actor.getY() + dy;
    Map map = game.getMap();
    if (canOpenDoors && map.is(x, y, Tile.DOOR_CLOSED))
      return ActionResult.alternateAction(new OpenDoor(x, y));
    if (map.isBlocked(x, y)) {
      if (waitOnBlocked) {
        return ActionResult.alternateAction(new Wait());
      } else {
        actor.bump(dx, dy, Constants.BUMPING_RATIO);
        game.addLog(actor + " bumps in to the wall.");
        return ActionResult.failure();
      }
    }
    Optional<Actor> blockingActor = game.actorAt(x, y);
    if (blockingActor.isEmpty()) {
      actor.move(x, y);
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
