package com.atennapel.testgame.actions;

import java.util.Optional;

public class ActionResult {
  private final boolean success;
  private final Optional<Action> alternateAction;

  private ActionResult(boolean success, Optional<Action> alternateAction) {
    this.success = success;
    this.alternateAction = alternateAction;
  }

  public Optional<Action> getAlternateAction() {
    return alternateAction;
  }

  public boolean succeeded() {
    return success;
  }

  public static ActionResult success() {
    return new ActionResult(true, Optional.<Action>empty());
  }

  public static ActionResult failure() {
    return new ActionResult(false, Optional.<Action>empty());
  }

  public static ActionResult alternateAction(Action action) {
    return new ActionResult(action != null, Optional.ofNullable(action));
  }
}
