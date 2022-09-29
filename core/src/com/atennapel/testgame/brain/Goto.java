package com.atennapel.testgame.brain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.atennapel.testgame.Dir;
import com.atennapel.testgame.Pos;
import com.atennapel.testgame.TestGame;
import com.atennapel.testgame.actions.Action;
import com.atennapel.testgame.actions.Move;
import com.atennapel.testgame.actors.Actor;

public class Goto extends Goal {
  private final Pos pos;

  public Goto(Pos pos) {
    this.pos = pos;
  }

  @Override
  public GoalResult perform(TestGame game, Actor actor) {
    Optional<List<Dir>> opath = game.getPathfinding().findPath(actor.getPos(), pos);
    if (opath.isEmpty()) return GoalResult.failed();
    List<Dir> path = opath.get();
    List<Action> actions = new ArrayList<>(path.size());
    for (Dir dir : path) actions.add(new Move(dir));
    return GoalResult.alternateGoal(new Actions(actions));
  }

  @Override
  public String toString() {
    return "Goto(" + pos + ")";
  }
}
