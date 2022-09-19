package com.atennapel.testgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

import static com.atennapel.testgame.Constants.*;

// Adaption from https://www.redblobgames.com/pathfinding/a-star/implementation.html#algorithm
public class Pathfinding {
  private final Map map;

  private class Loc implements Comparable<Loc> {
    private final int x;
    private final int y;
    private final int priority;

    private Loc(int x, int y, int priority) {
      this.x = x;
      this.y = y;
      this.priority = priority;
    }

    @Override
    public int compareTo(Loc other) {
      return priority - other.priority;
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof Loc))
        return false;
      Loc o = (Loc) other;
      return x == o.x && y == o.y;
    }

    @Override
    public int hashCode() {
      return x * 10000 + y;
    }

    @Override
    public String toString() {
      return "Loc(" + x + ", " + y + ")";
    }

    private Iterable<Loc> neighbours() {
      List<Loc> ns = new ArrayList<>();
      for (int nx = x - 1; nx <= x + 1; nx++) {
        for (int ny = y - 1; ny <= y + 1; ny++) {
          if (nx < 0 || nx >= WIDTH || ny < 0 || ny >= HEIGHT || (nx == x && ny == y) || map.isBlocked(nx, ny))
            continue;
          ns.add(new Loc(nx, ny, 0));
        }
      }
      return ns;
    }
  }

  public Pathfinding(Map map) {
    this.map = map;
  }

  private int heuristic(int x, int y, int gx, int gy) {
    int ox = Math.abs(gx - x);
    int oy = Math.abs(gy - y);
    int diagonal = Math.min(ox, oy);
    int straight = Math.max(ox, oy) - diagonal;
    return straight * 10 + diagonal * 11;
  }

  private int cost(int x, int y) {
    if (map.get(x, y) == Tiles.DOOR_OPEN)
      return 11;
    return 10;
  }

  public Optional<Pos> findPath(int x, int y, int gx, int gy) {
    HashMap<Loc, Loc> cameFrom = new HashMap<>();
    HashMap<Loc, Integer> costSoFar = new HashMap<>();
    PriorityQueue<Loc> frontier = new PriorityQueue<>();

    Loc goal = new Loc(gx, gy, 0);
    Loc start = new Loc(x, y, 0);
    frontier.add(start);
    cameFrom.put(start, start);
    costSoFar.put(start, 0);

    while (frontier.size() > 0) {
      Loc current = frontier.poll();
      if (current.equals(goal))
        break;
      for (Loc next : current.neighbours()) {
        int newCost = costSoFar.get(current) + cost(next.x, next.y);
        if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
          costSoFar.put(next, newCost);
          int priority = newCost + heuristic(next.x, next.y, gx, gy);
          frontier.add(new Loc(next.x, next.y, priority));
          cameFrom.put(next, current);
        }
      }
    }

    if (cameFrom.get(goal) == null)
      return Optional.empty();
    Loc current = goal;
    while (!current.equals(start)) {
      Loc next = cameFrom.get(current);
      if (next.equals(start))
        return Optional.of(new Pos(current.x, current.y));
      current = next;
    }
    return Optional.empty();
  }
}
