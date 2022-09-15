package com.atennapel.testgame.actors;

import java.util.Optional;

import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;

public abstract class Actor {
  protected int x = 0;
  protected int y = 0;
  protected int energy = 0;
  protected int speed = 100;

  public boolean needsInput() {
    return false;
  }

  public abstract Optional<Action> getAction(TestGame game);

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getEnergy() {
    return energy;
  }

  public void resetEnergy() {
    energy = energy - 100;
  }

  public boolean canTakeTurn() {
    return energy >= 100;
  }

  public boolean gainEnergy() {
    energy += speed;
    return canTakeTurn();
  }
}
