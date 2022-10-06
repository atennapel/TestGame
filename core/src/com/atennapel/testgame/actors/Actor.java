package com.atennapel.testgame.actors;

import java.util.Optional;

import static com.atennapel.testgame.Constants.*;

import com.atennapel.testgame.RGB;
import com.atennapel.testgame.Pos;
import com.atennapel.testgame.Dir;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;

public abstract class Actor {
  protected Pos pos = new Pos(0, 0);
  protected int energy = 0;
  protected int speed = 100;
  protected final Inventory inventory = new Inventory();

  public boolean toBeRemoved = false;

  // for animations
  protected int actualX = 0;
  protected int actualY = 0;
  protected int goalX = 0;
  protected int goalY = 0;
  protected boolean bumping = false;

  protected Actor(Pos pos) {
    this.pos = pos;
    actualX = pos.x * GRID;
    actualY = pos.y * GRID;
    goalX = actualX;
    goalY = actualY;
  }

  public boolean needsInput() {
    return false;
  }

  public abstract Optional<Action> getAction(TestGame game);

  public void succeeded() {
  }

  public void failed() {
  }

  public abstract int getTile();

  public abstract RGB getColor();

  public boolean canOpenDoors() {
    return false;
  }

  public Pos getPos() {
    return pos;
  }

  public Dir dir(Pos other) {
    return pos.dir(other);
  }

  public Dir dir(Actor other) {
    return dir(other.getPos());
  }

  public int getActualX() {
    return actualX;
  }

  public int getActualY() {
    return actualY;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void move(int x, int y) {
    goalX = x * GRID;
    goalY = y * GRID;
    pos.x = x;
    pos.y = y;
  }

  public void move(Pos pos) {
    move(pos.x, pos.y);
  }

  public void move(Dir dir) {
    move(pos.add(dir));
  }

  public boolean updateAnimation(float dt) {
    if (goalX == actualX && goalY == actualY) {
      if (!bumping)
        return false;
      bumping = false;
      goalX = pos.x * GRID;
      goalY = pos.y * GRID;
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

  public void bump(Dir dir, float ratio) {
    if (!bumping) {
      bumping = true;
      goalX = (int) (actualX + dir.dx * GRID * ratio);
      goalY = (int) (actualY + dir.dy * GRID * ratio);
    }
  }
}
