package com.atennapel.testgame.actors;

import java.util.Optional;

import static com.atennapel.testgame.Constants.*;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;

public abstract class Actor {
  protected int x = 0;
  protected int y = 0;
  protected int actualX = 0;
  protected int actualY = 0;
  protected int energy = 0;
  protected int speed = 100;

  protected Actor(int x, int y) {
    this.x = x;
    this.y = y;
    this.actualX = x * GRID;
    this.actualY = y * GRID;
  }

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

  public int getActualX() {
    return actualX;
  }

  public int getActualY() {
    return actualY;
  }

  public void setActualX(int x) {
    this.actualX = x;
  }

  public void setActualY(int y) {
    this.actualY = y;
  }

  public boolean updateAnimation(float dt) {
    int goalX = x * GRID;
    int goalY = y * GRID;
    if (goalX == actualX && goalY == actualY)
      return false;
    int change = (int) Math.floor(dt * ANIMATION_SPEED);
    if (actualX < goalX) {
      actualX += change;
      if (actualX > goalX)
        actualX = goalX;
    } else if (actualX > goalX) {
      actualX -= change;
      if (actualX < goalX)
        actualX = goalX;
    }
    if (actualY < goalY) {
      actualY += change;
      if (actualY > goalY)
        actualY = goalY;
    } else if (actualY > goalY) {
      actualY -= change;
      if (actualY < goalY)
        actualY = goalY;
    }
    return true;
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
