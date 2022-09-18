package com.atennapel.testgame.actions;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;
import com.atennapel.testgame.Tile;
import static com.atennapel.testgame.Tile.*;
import com.atennapel.testgame.Map;

public class UseDoor implements Action {
  private final int x;
  private final int y;

  public UseDoor(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  @Override
  public ActionResult perform(TestGame game, Actor actor) {
    Map map = game.getMap();
    Tile tile = map.get(x, y);
    if (tile == DOOR_CLOSED) {
      map.set(x, y, DOOR_OPEN);
      return ActionResult.success();
    } else if (tile == DOOR_OPEN && !game.anyActorAt(x, y)) {
      map.set(x, y, DOOR_CLOSED);
      return ActionResult.success();
    } else return ActionResult.failure();
  }

  @Override
  public String toString() {
    return "UseDoor(" + x + ", " + y + ")";
  }
}
