package com.atennapel.testgame.actors;

import java.util.Optional;

import com.atennapel.testgame.Pos;
import com.atennapel.testgame.RGB;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;
import com.atennapel.testgame.actions.Wait;

public class Mine extends Actor {
  public Mine(Pos pos) {
    super(pos);
    inventory.add("gold", 100);
  }

  @Override
  public Optional<Action> getAction(TestGame game) {
    return Optional.of(new Wait());
  }

  @Override
  public int getTile() {
    return 2;
  }

  @Override
  public RGB getColor() {
    return new RGB(126, 128, 145);
  }

  @Override
  public String toString() {
    return "Mine";
  }
}
