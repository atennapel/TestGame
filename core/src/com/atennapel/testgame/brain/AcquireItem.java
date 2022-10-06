package com.atennapel.testgame.brain;

import java.util.Optional;

import com.atennapel.testgame.Pos;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;
import com.atennapel.testgame.actors.Pickaxe;

public class AcquireItem extends Goal {
  private final String item;
  private final int count;

  public AcquireItem(String item, int count) {
    this.item = item;
    this.count = count;
  }

  public AcquireItem(String item) {
    this(item, 1);
  }

  @Override
  public GoalResult perform(TestGame game, Actor actor) {
    if (actor.getInventory().count(item) >= count) {
      return GoalResult.succeeded();
    } else if (item == "gold") {
      return GoalResult.alternateGoal(new GoMine());
    } else if (item == "pickaxe") {
      Optional<Pos> pos = game.findActor(Pickaxe.class);
      if (pos.isEmpty())
        return GoalResult.failed();
      return GoalResult.alternateGoal(new Goto(pos.get()));
    } else {
      return GoalResult.failed();
    }
  }
}
