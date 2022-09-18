package com.atennapel.testgame.actors;

import java.util.Optional;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;

public class Player extends Actor {
  private Optional<Action> action = Optional.empty();

  public Player(int x, int y) {
    super(x, y);
    this.speed = 100;
  }

  public void setNextAction(Action action) {
    System.out.println("setNextAction: " + action);
    this.action = Optional.ofNullable(action);
  }

  @Override
  public boolean needsInput() {
    return action.isEmpty();
  }

  @Override
  public Optional<Action> getAction(TestGame game) {
    Optional<Action> nextAction = action;
    action = Optional.empty();
    return nextAction;
  }

  @Override
  public String toString() {
    return "Player";
  }
}
