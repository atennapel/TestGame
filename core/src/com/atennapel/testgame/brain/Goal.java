package com.atennapel.testgame.brain;

import java.util.Optional;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actors.Actor;

public abstract class Goal {
  private Optional<Goal> originalGoal = Optional.empty();

  public void setOriginalGoal(Goal goal) {
    originalGoal = Optional.ofNullable(goal);
  }

  public Optional<Goal> getOriginalGoal() {
    return originalGoal;
  }

  public abstract GoalResult perform(TestGame game, Actor actor);

  public Optional<Goal> succeeded() { return Optional.empty(); }
}
