package com.atennapel.testgame.brain;

import java.util.Optional;

import com.atennapel.testgame.Pos;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;
import com.atennapel.testgame.actions.Wait;
import com.atennapel.testgame.actors.Actor;

public class Brain {
  private final Actor actor;
  private Optional<Goal> ogoal = Optional.empty();

  public Brain(Actor actor) {
    this.actor = actor;
  }

  public Action getAction(TestGame game) {
    if (ogoal.isEmpty())
      getGoal(game);
    GoalResult result = ogoal.get().perform(game, actor);
    while (result.failed) {
      getGoal(game);
      result = ogoal.get().perform(game, actor);
    }
    while (!result.hasAction) {
      Goal originalGoal = ogoal.get();
      if (result.goal == null)
        return new Wait();
      result.goal.setOriginalGoal(originalGoal);
      ogoal = Optional.of(result.goal);
      result = ogoal.get().perform(game, actor);
    }
    return result.action;
  }

  public void succeeded() {
    if (ogoal.isPresent())
      ogoal = ogoal.get().succeeded();
  }

  public void failed() {
    if (ogoal.isPresent()) {
      Goal goal = ogoal.get();
      Optional<Goal> originalGoal = goal.getOriginalGoal();
      if (originalGoal.isPresent())
        ogoal = originalGoal;
      else
        ogoal = Optional.empty();
    }
  }

  private void getGoal(TestGame game) {
    if (actor.getInventory().count("gold") < 10) {
      this.ogoal = Optional.of(new AcquireItem("gold", 10));
    } else {
      int x = game.getRandom().nextInt(17) + 1;
      int y = game.getRandom().nextInt(10) + 1;
      this.ogoal = Optional.<Goal>of(new Goto(new Pos(x, y)));
    }
  }

  @Override
  public String toString() {
    if (ogoal.isEmpty())
      return "Brain()";
    else
      return "Brain(" + ogoal.get() + ")";
  }
}
