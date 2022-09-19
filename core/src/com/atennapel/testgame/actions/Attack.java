package com.atennapel.testgame.actions;

import com.atennapel.testgame.Constants;
import com.atennapel.testgame.Sounds;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;

public class Attack implements Action {
  private final Actor target;

  public Attack(Actor target) {
    this.target = target;
  }

  @Override
  public ActionResult perform(TestGame game, Actor actor) {
    int dx = 0;
    int dy = 0;
    if (target.getX() > actor.getX())
      dx = 1;
    else if (target.getX() < actor.getX())
      dx = -1;
    if (target.getY() > actor.getY())
      dy = 1;
    else if (target.getY() < actor.getY())
      dy = -1;
    actor.bump(dx, dy, Constants.ATTACK_RATIO);
    game.addLog(actor + " attacks " + target);
    game.playSound(Sounds.HIT);
    return ActionResult.success();
  }

  @Override
  public String toString() {
    return "Attack(" + target + ")";
  }
}
