package com.atennapel.testgame.brain;

import com.atennapel.testgame.actions.Action;

public class GoalResult {
  public final boolean failed;
  public final boolean hasAction;
  public final Action action;
  public final Goal goal;

  private GoalResult(boolean failed, boolean hasAction, Action action, Goal goal) {
    this.failed = failed;
    this.hasAction = hasAction;
    this.action = action;
    this.goal = goal;
  }

  public static GoalResult alternateGoal(Goal goal) {
    return new GoalResult(false, false, null, goal);
  }

  public static GoalResult action(Action action) {
    return new GoalResult(false, true, action, null);
  }

  public static GoalResult failed() {
    return new GoalResult(true, false, null, null);
  }

  @Override
  public String toString() {
    if (failed) return "GoalResult(failed)";
    if (hasAction) return "GoalResult(" + action + ")";
    return "GoalResult(" + goal + ")";
  }
}
