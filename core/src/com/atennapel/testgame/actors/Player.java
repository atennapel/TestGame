package com.atennapel.testgame.actors;

import java.util.Optional;

import com.atennapel.testgame.RGB;
import com.atennapel.testgame.Pos;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;

public class Player extends Actor {
  private Optional<Action> action = Optional.empty();

  public Player(Pos pos) {
    super(pos);
    this.speed = 100;
  }

  public void setNextAction(Action action) {
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
  public int getTile() {
    return 0;
  }

  @Override
  public RGB getColor() {
    return new RGB(81, 143, 77);
  }

  @Override
  public boolean canOpenDoors() {
    return true;
  }

  @Override
  public String toString() {
    return "Player";
  }
}
