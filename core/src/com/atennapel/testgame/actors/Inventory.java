package com.atennapel.testgame.actors;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
  public final Map<String, Integer> entries;

  public Inventory() {
    entries = new HashMap<>();
  }

  public void add(String item, int count) {
    entries.put(item, count(item) + count);
  }

  public void add(String item) {
    add(item, 1);
  }

  public void remove(String item, int count) {
    entries.put(item, Math.max(0, count(item) - count));
  }

  public void remove(String item) {
    remove(item, 1);
  }

  public int size() {
    return entries.size();
  }

  public int count(String item) {
    return entries.getOrDefault(item, 0);
  }

  public boolean contains(String item) {
    return count(item) > 0;
  }
}
