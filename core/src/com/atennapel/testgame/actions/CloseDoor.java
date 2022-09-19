package com.atennapel.testgame.actions;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;
import com.atennapel.testgame.Tiles;
import static com.atennapel.testgame.Tiles.*;
import com.atennapel.testgame.Map;
import com.atennapel.testgame.Sounds;

public class CloseDoor implements Action {
  private final int x;
  private final int y;

  public CloseDoor(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public ActionResult perform(TestGame game, Actor actor) {
    Map map = game.getMap();
    Tiles tile = map.get(x, y);
    if (tile == DOOR_OPEN && !game.anyActorAt(x, y)) {
      map.set(x, y, DOOR_CLOSED);
      game.addLog(actor + " closes a door.");
      game.playSound(Sounds.DOOR_CLOSE);
      return ActionResult.success();
    } else
      return ActionResult.failure();
  }

  @Override
  public String toString() {
    return "CloseDoor(" + x + ", " + y + ")";
  }
}
