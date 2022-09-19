package com.atennapel.testgame.actors;

import java.util.Optional;

import static com.atennapel.testgame.Constants.*;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;

public abstract class Actor {
  protected int x = 0;
  protected int y = 0;
  protected int energy = 0;
  protected int speed = 100;

  // for animations
  protected int actualX = 0;
  protected int actualY = 0;
  protected int goalX = 0;
  protected int goalY = 0;
  protected boolean bumping = false;

  protected Actor(int x, int y) {
    this.x = x;
    this.y = y;
    actualX = x * GRID;
    actualY = y * GRID;
    goalX = actualX;
    goalY = actualY;
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

  public int getActualX() {
    return actualX;
  }

  public int getActualY() {
    return actualY;
  }

  public void move(int x, int y) {
    goalX = x * GRID;
    goalY = y * GRID;
  }

  public boolean updateAnimation(float dt) {
    if (goalX == actualX && goalY == actualY) {
      if (bumping) {
        bumping = false;
        goalX = x * GRID;
        goalY = y * GRID;
      } else {
        x = goalX / GRID;
        y = goalY / GRID;
        return false;
      }
    }
    int change = (int) (dt * ANIMATION_SPEED);
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

  public void bump(int dx, int dy, float ratio) {
    if (!bumping) {
      bumping = true;
      goalX = (int) (actualX + dx * GRID * ratio);
      goalY = (int) (actualY + dy * GRID * ratio);
    }
  }
}
