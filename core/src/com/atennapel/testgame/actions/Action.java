package com.atennapel.testgame.actions;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;

public interface Action {
  public abstract ActionResult perform(TestGame game, Actor actor);
}
