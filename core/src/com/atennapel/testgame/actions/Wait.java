package com.atennapel.testgame.actions;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;

public class Wait implements Action {
  @Override
  public ActionResult perform(TestGame game, Actor actor) {
    return ActionResult.success();
  }

  @Override
  public String toString() {
    return "Wait";
  }
}
