package com.atennapel.testgame.actions;

import java.util.Optional;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.Tiles;
import com.atennapel.testgame.actors.Actor;
import com.atennapel.testgame.Map;
import com.atennapel.testgame.Sounds;
import com.atennapel.testgame.Constants;
import com.atennapel.testgame.Dir;
import com.atennapel.testgame.Pos;

public class Move implements Action {
  private final Dir dir;

  public Move(Dir dir) {
    this.dir = dir;
  }

  @Override
  public ActionResult perform(TestGame game, Actor actor) {
    if (dir.noChange())
      return ActionResult.alternateAction(new Wait());
    Pos pos = actor.getPos().add(dir);
    Map map = game.getMap();
    if (actor.canOpenDoors() && map.is(pos, Tiles.DOOR_CLOSED))
      return ActionResult.alternateAction(new OpenDoor(pos));
    if (map.isBlocked(pos)) {
      if (actor.waitOnBlocked()) {
        return ActionResult.alternateAction(new Wait());
      } else {
        actor.bump(dir, Constants.BUMPING_RATIO);
        game.addLog(actor + " bumps in to the wall");
        game.playSound(Sounds.BUMP);
        return ActionResult.failure();
      }
    }
    Optional<Actor> blockingActor = game.actorAt(pos);
    if (blockingActor.isEmpty()) {
      actor.move(dir);
      return ActionResult.success();
    } else {
      return ActionResult.alternateAction(new Attack(blockingActor.get()));
    }
  }

  @Override
  public String toString() {
    return "Move(" + dir + ")";
  }
}
