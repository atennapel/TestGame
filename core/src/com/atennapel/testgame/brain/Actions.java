package com.atennapel.testgame.brain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;
import com.atennapel.testgame.actors.Actor;

public class Actions extends Goal {
  private final List<Action> actions;

  public Actions(List<Action> actions) {
    this.actions = actions;
  }
  
  @Override
  public GoalResult perform(TestGame game, Actor actor) {
    return GoalResult.action(actions.get(0));
  }
  
  @Override
  public Optional<Goal> succeeded() {
    if (actions.size() <= 1) return Optional.empty();
    List<Action> newActions = actions.subList(1, actions.size());
    Actions newGoal = new Actions(newActions);
    if (getOriginalGoal().isPresent())
      newGoal.setOriginalGoal(getOriginalGoal().get());
    return Optional.<Goal>of(newGoal);
  }

  @Override
  public String toString() {
    return "Actions(" + Arrays.toString(actions.toArray()) + ")";
  }
}
