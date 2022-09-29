package com.atennapel.testgame.actors;

import java.util.Optional;

import com.atennapel.testgame.RGB;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.Pos;
import com.atennapel.testgame.actions.Action;
import com.atennapel.testgame.brain.Brain;

public class Monster extends Actor {
  private final Brain brain = new Brain(this);

  public Monster(Pos pos) {
    super(pos);
    this.speed = 50;
  }

  @Override
  public Optional<Action> getAction(TestGame game) {
    return Optional.of(brain.getAction(game));
    /*
     * Optional<Dir> dir = game.getPathfinding().findDir(pos,
     * game.getPlayer().getPos());
     * if (dir.isEmpty()) {
     * return Optional.<Action>of(new Move(new Dir(game.getRandom().nextInt(3) - 1,
     * game.getRandom().nextInt(3) - 1)));
     * } else {
     * return Optional.<Action>of(new Move(dir.get()));
     * }
     */
  }

  @Override
  public void succeeded() { brain.succeeded(); }
  @Override
  public void failed() { brain.failed(); }

  @Override
  public boolean canOpenDoors() {
    return true;
  }

  @Override
  public int getTile() {
    return 1;
  }

  @Override
  public RGB getColor() {
    return new RGB(154, 64, 55);
  }

  @Override
  public String toString() {
    return "Monster";
  }
}
