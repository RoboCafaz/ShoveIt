package com.robocafaz.shove.common.objects;

public abstract class GameObject {
  protected final String name;

  public GameObject(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public abstract void update(long currentTime);
}
