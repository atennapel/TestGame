package com.atennapel.testgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.atennapel.testgame.actors.*;
import com.atennapel.testgame.actions.*;
import static com.atennapel.testgame.Constants.*;

public class TestGame extends ApplicationAdapter {
  private SpriteBatch batch;
  private TextureAtlas atlas;
  private TextureRegion playerRegion;
  private TextureRegion monsterRegion;
  private TextureRegion wallRegion;
  private BitmapFont font;

  private Random random;

  private Map map;

  private Player player;
  private Monster monster;

  private List<Actor> actors;
  private int currentActor = 0;

  private int frame = 0;
  private int turn = 0;
  private int playerTurn = 0;
  private float cooldown = 0;

  @Override
  public void create() {
    batch = new SpriteBatch();
    atlas = new TextureAtlas("testgame.atlas");
    AtlasRegion region = atlas.findRegion("sheet");
    playerRegion = new TextureRegion(region, 0, 0, 16, 16);
    monsterRegion = new TextureRegion(region, 0, 16, 16, 16);
    wallRegion = new TextureRegion(region, 16, 0, 16, 16);
    font = new BitmapFont();

    random = new Random();

    player = new Player(3, 3);
    monster = new Monster(6, 6);

    actors = new ArrayList<>();
    actors.add(player);
    actors.add(monster);

    map = new Map();
  }

  private void handleInput() {
    if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5) || Gdx.input.isKeyJustPressed(Input.Keys.L))
      player.setNextAction(new Wait());
    int dx = 0, dy = 0;
    if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)
        || Gdx.input.isKeyJustPressed(Input.Keys.O)) {
      dy = -1;
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)
        || Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
      dy = 1;
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4)
        || Gdx.input.isKeyJustPressed(Input.Keys.K)) {
      dx = -1;
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_6)
        || Gdx.input.isKeyJustPressed(Input.Keys.SEMICOLON)) {
      dx = 1;
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7) || Gdx.input.isKeyJustPressed(Input.Keys.I)) {
      dx = -1;
      dy = -1;
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9) || Gdx.input.isKeyJustPressed(Input.Keys.P)) {
      dx = 1;
      dy = -1;
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1) || Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) {
      dx = -1;
      dy = 1;
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3) || Gdx.input.isKeyJustPressed(Input.Keys.SLASH)) {
      dx = 1;
      dy = 1;
    }
    if (dx != 0 || dy != 0)
      player.setNextAction(new Move(dx, dy));
  }

  private void advanceActor() {
    currentActor = (currentActor + 1) % actors.size();
  }

  private void processTurn() {
    cooldown += Gdx.graphics.getDeltaTime();
    if (cooldown < MAX_COOLDOWN)
      return;
    cooldown -= MAX_COOLDOWN;

    frame++;

    Actor actor = actors.get(currentActor);

    if (actor.needsInput()) return;

    if (actor.canTakeTurn() || actor.gainEnergy()) {
      Optional<Action> action = actor.getAction(this);
      if (action.isEmpty()) return;
      ActionResult result = action.get().perform(this, actor);
      while (result.getAlternateAction().isPresent())
        result = result.getAlternateAction().get().perform(this, actor);
      if (result.succeeded()) {
        actor.resetEnergy();
        advanceActor();
        turn++;
        if (actor instanceof Player) playerTurn++;
      }
    } else {
      advanceActor();
    }
  }

  private void draw() {
    ScreenUtils.clear(96 / 255f, 62 / 255f, 52 / 255f, 1);
    batch.begin();
    batch.setColor(156 / 255f, 178 / 255f, 112 / 255f, 1);
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        if (map.isBlocked(x, y))
          batch.draw(wallRegion, x * GRID, (HEIGHT - 1 - y) * GRID, GRID, GRID);
      }
    }
    batch.draw(monsterRegion, monster.getX() * GRID, (HEIGHT - 1 - monster.getY()) * GRID, GRID, GRID);
    batch.draw(playerRegion, player.getX() * GRID, (HEIGHT - 1 - player.getY()) * GRID, GRID, GRID);
    batch.setColor(1, 1, 1, 1);
    String debugMsg = "" + frame + ", " + turn + ", " + playerTurn + ", " + player.getEnergy() + ", "
        + monster.getEnergy();
    font.draw(batch, debugMsg, 2 * GRID, (HEIGHT - 2) * GRID);
    batch.end();
  }

  @Override
  public void render() {
    handleInput();
    processTurn();
    draw();
  }

  @Override
  public void dispose() {
    batch.dispose();
    atlas.dispose();
  }

  public Map getMap() {
    return map;
  }

  public Random getRandom() {
    return random;
  }

  public Player getPlayer() {
    return player;
  }

  public Optional<Actor> actorAt(int x, int y) {
    for (int i = 0; i < actors.size(); i++) {
      Actor actor = actors.get(i);
      if (actor.getX() == x && actor.getY() == y)
        return Optional.of(actor);
    }
    return Optional.empty();
  }
}
