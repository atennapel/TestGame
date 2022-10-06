package com.atennapel.testgame.brain;

import java.util.Optional;

import com.atennapel.testgame.Pos;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;
import com.atennapel.testgame.actors.Mine;

public class GoMine extends Goal {
  @Override
  public GoalResult perform(TestGame game, Actor actor) {
    if (!actor.getInventory().contains("pickaxe"))
      return GoalResult.alternateGoal(new AcquireItem("pickaxe"));
    Optional<Pos> minePos = game.findActor(Mine.class);
    if (minePos.isEmpty()) {
      return GoalResult.failed();
    } else {
      return GoalResult.alternateGoal(new Goto(minePos.get()));
    }
  }
}
