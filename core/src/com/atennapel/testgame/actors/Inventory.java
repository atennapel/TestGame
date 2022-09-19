package com.atennapel.testgame.actors;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
  public final Map<String, Integer> entries;

  public Inventory() {
    entries = new HashMap<>();
  }

  public void add(String item, int count) {
    int currentCount = entries.getOrDefault(item, 0);
    entries.put(item, currentCount + count);
  }

  public void add(String item) {
    add(item, 1);
  }

  public int count(String item) {
    return entries.getOrDefault(item, 0);
  }

  public boolean contains(String item) {
    return count(item) > 0;
  }
}
