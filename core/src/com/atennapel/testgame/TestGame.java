package com.atennapel.testgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;

import com.atennapel.testgame.actors.*;
import com.atennapel.testgame.actions.*;
import static com.atennapel.testgame.Constants.*;

public class TestGame extends ApplicationAdapter {
  private static enum InputMode {
    NORMAL,
    DOOR
  }

  private SpriteBatch batch;
  private TextureAtlas atlas;
  private TextureRegion playerRegion;
  private TextureRegion monsterRegion;
  private TextureRegion wallRegion;
  private TextureRegion doorClosedRegion;
  private TextureRegion doorOpenRegion;
  private TextureRegion emptyRegion;
  private TextureRegion dotRegion;
  private BitmapFont font;
  private Sound bumpSound;
  private Sound doorOpenSound;
  private Sound doorCloseSound;
  private Sound hitSound;

  private InputMode inputMode = InputMode.NORMAL;

  private Random random;

  private Map map;
  private ShadowCasting shadowCasting;
  private Pathfinding pathfinding;

  private Player player;
  private Monster monster;

  private List<Actor> actors = new ArrayList<>();
  private int currentActor = 0;

  private int frame = 0;
  private int turn = 0;
  private int playerTurn = 0;
  private List<String> logs = new ArrayList<>();
  private boolean updatingAnimations = false;

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  @Override
  public void create() {
    batch = new SpriteBatch();
    atlas = new TextureAtlas("sheet.atlas");
    AtlasRegion region = atlas.findRegion("sheet");
    playerRegion = new TextureRegion(region, 0, 0, 16, 16);
    monsterRegion = new TextureRegion(region, 16, 0, 16, 16);
    wallRegion = new TextureRegion(region, 0, 16, 16, 16);
    doorClosedRegion = new TextureRegion(region, 16, 16, 16, 16);
    doorOpenRegion = new TextureRegion(region, 32, 16, 16, 16);
    emptyRegion = new TextureRegion(region, 0, 32, 16, 16);
    dotRegion = new TextureRegion(region, 16, 32, 16, 16);

    font = new BitmapFont();
    bumpSound = Gdx.audio.newSound(Gdx.files.internal("bump.wav"));
    doorOpenSound = Gdx.audio.newSound(Gdx.files.internal("door_open.wav"));
    doorCloseSound = Gdx.audio.newSound(Gdx.files.internal("door_close.wav"));
    hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));

    random = new Random();

    player = new Player(3, 3);
    monster = new Monster(7, 6);
    actors.add(player);
    actors.add(monster);

    map = new Map();
    shadowCasting = new ShadowCasting(map);
    pathfinding = new Pathfinding(map);

    player.getInventory().add("gold", 10);
  }

  private void handleInput() {
    if (updatingAnimations)
      return;
    if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
      StringBuilder sb = new StringBuilder("You have: ");
      boolean nonEmpty = false;
      for (Entry<String, Integer> e : player.getInventory().entries.entrySet()) {
        if (e.getValue() > 0) {
          nonEmpty = true;
          sb.append(e.getValue() + " " + e.getKey() + ", ");
        }
      }
      if (nonEmpty)
        sb.delete(sb.length() - 2, sb.length());
      else
        sb.append("nothing");
      sb.append(".");
      addLog(sb.toString());
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
      inputMode = InputMode.DOOR;
      addLog("Pick a direction to use a door.");
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5) || Gdx.input.isKeyJustPressed(Input.Keys.L))
      player.setNextAction(new Wait());
    else {
      int dx = 0, dy = 0;
      if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)
          || Gdx.input.isKeyJustPressed(Input.Keys.O)) {
        dy = -1;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)
          || Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
        dy = 1;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4)
          || Gdx.input.isKeyJustPressed(Input.Keys.K)) {
        dx = -1;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_6)
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
      if (dx != 0 || dy != 0) {
        if (inputMode == InputMode.NORMAL)
          player.setNextAction(new Move(dx, dy, false, true));
        else if (inputMode == InputMode.DOOR) {
          inputMode = InputMode.NORMAL;
          player.setNextAction(new UseDoor(player.getX() + dx, player.getY() + dy));
        }
      }
    }
  }

  private void advanceActor() {
    currentActor = (currentActor + 1) % actors.size();
  }

  private void processTurn() {
    if (updatingAnimations)
      return;

    frame++;

    Actor actor = actors.get(currentActor);

    if (actor.needsInput())
      return;

    if (actor.canTakeTurn() || actor.gainEnergy()) {
      Optional<Action> action = actor.getAction(this);
      if (action.isEmpty())
        return;
      ActionResult result = action.get().perform(this, actor);
      while (result.getAlternateAction().isPresent())
        result = result.getAlternateAction().get().perform(this, actor);
      if (result.succeeded()) {
        actor.resetEnergy();
        advanceActor();
        turn++;
        if (actor instanceof Player)
          playerTurn++;
      }
    } else {
      advanceActor();
    }
  }

  private void updateAnimations() {
    updatingAnimations = false;
    for (int i = 0; i < actors.size(); i++) {
      if (actors.get(i).updateAnimation(Gdx.graphics.getDeltaTime()))
        updatingAnimations = true;
    }
  }

  private void refreshVisibility() {
    shadowCasting.refreshVisibility(player.getX(), player.getY());
  }

  private void draw(TextureRegion region, int x, int y) {
    batch.draw(region, x * GRID, (HEIGHT - 1 - y) * GRID, GRID, GRID);
  }

  private void draw(TextureRegion region, int x, int y, int r, int g, int b) {
    batch.setColor(r / 255f, g / 255f, b / 255f, 1);
    draw(region, x, y);
  }

  private void draw(Actor actor) {
    TextureRegion region = null;
    if (actor instanceof Player)
      region = playerRegion;
    else if (actor instanceof Monster)
      region = monsterRegion;
    if (region != null)
      batch.draw(region, actor.getActualX(), HEIGHT * GRID - actor.getActualY() - GRID, GRID, GRID);
  }

  private void draw(Actor actor, int r, int g, int b) {
    batch.setColor(r / 255f, g / 255f, b / 255f, 1);
    draw(actor);
  }

  private void draw() {
    ScreenUtils.clear(96 / 255f, 62 / 255f, 52 / 255f, 1);

    batch.begin();

    // map
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT - 2; y++) {
        if (!map.isExplored(x, y)) {
          draw(emptyRegion, x, y, 66, 32, 22);
          continue;
        }
        boolean visible = map.isVisible(x, y);
        if (!visible)
          draw(emptyRegion, x, y, 86, 52, 42);
        Tiles tile = map.get(x, y);
        switch (tile) {
          case EMPTY:
            if (visible)
              draw(dotRegion, x, y, 126, 92, 82);
            else
              draw(dotRegion, x, y, 96, 62, 52);
            break;
          case WALL:
            if (visible)
              draw(wallRegion, x, y, 156, 178, 112);
            else
              draw(wallRegion, x, y, 76, 42, 32);
            break;
          case DOOR_CLOSED:
            if (visible)
              draw(doorClosedRegion, x, y, 123, 92, 66);
            else
              draw(doorClosedRegion, x, y, 93, 62, 36);
            break;
          case DOOR_OPEN:
            if (visible)
              draw(doorOpenRegion, x, y, 123, 92, 66);
            else
              draw(doorOpenRegion, x, y, 93, 62, 36);
            break;
        }
      }
    }

    // actors
    if (map.isVisible(monster.getX(), monster.getY()))
      draw(monster, 154, 64, 55);
    draw(player, 81, 143, 77);

    // debug text
    font.setColor(Color.WHITE);
    String debugMsg = "" + frame + ", (" + player.getX() + ", " + player.getY() + ")" + "(" + player.getActualX() + ", "
        + player.getActualY() + ")" + ", " + turn + ", " + playerTurn + ", " + player.getEnergy() + ", "
        + monster.getEnergy();
    font.draw(batch, debugMsg, 2 * GRID, (HEIGHT - 2) * GRID);

    // logs
    font.setColor(Color.WHITE);
    float logY = 2 * GRID - 5;
    for (int i = logs.size() - 1; i >= 0; i--) {
      font.draw(batch, logs.get(i), 0, logY);
      logY -= 15;
      if (logY < 10)
        break;
    }

    batch.end();
  }

  @Override
  public void render() {
    handleInput();
    processTurn();
    updateAnimations();
    refreshVisibility();
    draw();
  }

  @Override
  public void dispose() {
    batch.dispose();
    atlas.dispose();
    font.dispose();
  }

  public Map getMap() {
    return map;
  }

  public Pathfinding getPathfinding() {
    return pathfinding;
  }

  public ShadowCasting getShadowCasting() {
    return shadowCasting;
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

  public boolean anyActorAt(int x, int y) {
    return actorAt(x, y).isPresent();
  }

  public void addLog(String msg) {
    LocalDateTime date = LocalDateTime.now();
    logs.add(date.format(formatter) + " - " + msg);
  }

  public void playSound(Sounds sound) {
    switch (sound) {
      case BUMP:
        bumpSound.play();
        break;
      case DOOR_OPEN:
        doorOpenSound.play();
        break;
      case DOOR_CLOSE:
        doorCloseSound.play();
        break;
      case HIT:
        hitSound.play();
        break;
    }
  }
}
