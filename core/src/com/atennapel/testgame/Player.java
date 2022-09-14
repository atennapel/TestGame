package com.atennapel.testgame;

public class Player implements Actor {
  private Input input;

  public int x = 0;
  public int y = 0;
  public boolean disabled = false;
  public float cooldown = 0;

  public Player(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void setNextInput(Input input) {
    this.input = input;
  }

  public Input getAction() {
    Input nextInput = input;
    input = null;
    return nextInput;
  }
}
