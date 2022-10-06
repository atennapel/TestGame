package com.atennapel.testgame.actions;

import com.atennapel.testgame.Constants;
import com.atennapel.testgame.Sounds;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;
import com.atennapel.testgame.actors.Mine;
import com.atennapel.testgame.actors.Pickaxe;

public class Attack implements Action {
  private final Actor target;

  public Attack(Actor target) {
    this.target = target;
  }

  @Override
  public ActionResult perform(TestGame game, Actor actor) {
    actor.bump(actor.dir(target), Constants.ATTACK_RATIO);
    game.playSound(Sounds.HIT);
    if (target instanceof Mine) {
      if (!actor.getInventory().contains("pickaxe")) {
        game.addLog(actor + " tries to mine from " + target + " but they do not have a pickaxe");
      } else if (target.getInventory().contains("gold")) {
        target.getInventory().remove("gold");
        actor.getInventory().add("gold");
        game.addLog(actor + " mines from " + target + " and gains 1 gold");
      } else {
        game.addLog(actor + " tries to mine from " + target + " but it is empty");
      }
    } else if (target instanceof Pickaxe) {
      target.toBeRemoved = true;
      actor.getInventory().add("pickaxe");
      game.addLog(actor + " picks up the " + target);
    } else {
      game.addLog(actor + " attacks " + target);
    }
    return ActionResult.success();
  }

  @Override
  public String toString() {
    return "Attack(" + target + ")";
  }
}
