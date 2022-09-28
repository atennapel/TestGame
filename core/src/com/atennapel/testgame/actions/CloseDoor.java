package com.atennapel.testgame.actions;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;
import com.atennapel.testgame.Tiles;
import static com.atennapel.testgame.Tiles.*;
import com.atennapel.testgame.Map;
import com.atennapel.testgame.Sounds;
import com.atennapel.testgame.Pos;

public class CloseDoor implements Action {
  private final Pos pos;

  public CloseDoor(Pos pos) {
    this.pos = pos;
  }

  @Override
  public ActionResult perform(TestGame game, Actor actor) {
    Map map = game.getMap();
    Tiles tile = map.get(pos);
    if (tile == DOOR_OPEN && !game.anyActorAt(pos)) {
      map.set(pos, DOOR_CLOSED);
      game.addLog(actor + " closes a door.");
      game.playSound(Sounds.DOOR_CLOSE);
      return ActionResult.success();
    } else
      return ActionResult.failure();
  }

  @Override
  public String toString() {
    return "CloseDoor(" + pos + ")";
  }
}
