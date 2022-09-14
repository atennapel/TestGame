package com.atennapel.testgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class TestGame extends ApplicationAdapter {
  private SpriteBatch batch;
  private TextureAtlas atlas;
  private TextureRegion playerRegion;
  private TextureRegion wall;

  private Player player;

  private boolean[][] obstacles;

  @Override
  public void create() {
    batch = new SpriteBatch();
    atlas = new TextureAtlas("testgame.atlas");
    AtlasRegion region = atlas.findRegion("sheet");
    playerRegion = new TextureRegion(region, 0, 0, 16, 16);
    wall = new TextureRegion(region, 16, 0, 16, 16);

    player = new Player(32, 32);

    obstacles = new boolean[20][15];
    for (int i = 0; i < 20; i++) {
      obstacles[i][0] = true;
      obstacles[i][14] = true;
      if (i > 0 && i < 14) {
        obstacles[0][i] = true;
        obstacles[19][i] = true;
      }
    }
  }

  private void handleInput() {
    int dx = 0, dy = 0;
    if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
      dy = -1;
    }
    if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
      dy = 1;
    }
    if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
      dx = -1;
    }
    if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
      dx = 1;
    }
    if (dx != 0 || dy != 0)
      player.setNextInput(Input.direction(dx, dy));
  }

  private void draw() {
    ScreenUtils.clear(96 / 255f, 62 / 255f, 52 / 255f, 1);
    batch.begin();
    batch.setColor(156 / 255f, 178 / 255f, 112 / 255f, 1);
    batch.draw(playerRegion, player.x, player.y, 32, 32);
    for (int i = 0; i < 20; i++) {
      batch.draw(wall, i * 32, 0, 32, 32);
      batch.draw(wall, i * 32, 14 * 32, 32, 32);
      if (i > 0 && i < 14) {
        batch.draw(wall, 0, i * 32, 32, 32);
        batch.draw(wall, 19 * 32, i * 32, 32, 32);
      }
    }
    batch.end();
  }

  @Override
  public void render() {
    float dt = Gdx.graphics.getDeltaTime();

    handleInput();

    Input input = player.getAction();
    if (input != null) {
      if (input instanceof Input.Direction) {
        if (!player.disabled) {
          int nextX = player.x + ((Input.Direction) input).dx * 32;
          int nextY = player.y + ((Input.Direction) input).dy * -32;
          if (!obstacles[nextX / 32][nextY / 32]) {
            player.disabled = true;
            player.cooldown = 0;
            player.x = nextX;
            player.y = nextY;
          }
        } else {
          player.cooldown += dt;
          if (player.cooldown > 0.1) {
            player.disabled = false;
          }
        }
      }
    }

    draw();
  }

  @Override
  public void dispose() {
    batch.dispose();
    atlas.dispose();
  }
}
