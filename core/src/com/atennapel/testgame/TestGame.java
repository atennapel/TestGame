package com.atennapel.testgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    NORMAL, DOOR
  }

  private static enum ScreenMode {
    GAME, INVENTORY
  }

  private SpriteBatch batch;
  private Sheet sheet;
  private BitmapFont font;
  private Sound bumpSound;
  private Sound doorOpenSound;
  private Sound doorCloseSound;
  private Sound hitSound;

  private InputMode inputMode = InputMode.NORMAL;
  private ScreenMode screenMode = ScreenMode.GAME;
  private int inventoryPointer = 0;

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
    sheet = Sheet.create(SHEET_FILENAME, SHEET_REGION, SHEET_GRID, SHEET_GRID);

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
    player.getInventory().add("sword of fire", 1);
    player.getInventory().add("scroll of ennui", 2);
    player.getInventory().add("water bottle", 3);
    player.getInventory().add("water bottle", 3);
    player.getInventory().add("a", 3);
    player.getInventory().add("b", 3);
    player.getInventory().add("c", 3);
    player.getInventory().add("d", 3);
    player.getInventory().add("e", 3);
    player.getInventory().add("f", 3);
    player.getInventory().add("g", 3);
    player.getInventory().add("h", 3);
    player.getInventory().add("i", 3);
    player.getInventory().add("j", 3);
    player.getInventory().add("k", 3);
    player.getInventory().add("l", 3);
  }

  private void handleInput() {
    if (updatingAnimations)
      return;
    if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
      if (screenMode == ScreenMode.GAME)
        screenMode = ScreenMode.INVENTORY;
      else if (screenMode == ScreenMode.INVENTORY)
        screenMode = ScreenMode.GAME;
      return;
    }
    if (screenMode == ScreenMode.INVENTORY) {
      if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)
          || Gdx.input.isKeyJustPressed(Input.Keys.O)) {
        inventoryPointer--;
        if (inventoryPointer < 0)
          inventoryPointer = player.getInventory().size() - 1;
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2)
          || Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) {
        inventoryPointer++;
        if (inventoryPointer >= player.getInventory().size())
          inventoryPointer = 0;
      }
      return;
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
      inputMode = InputMode.DOOR;
      addLog("Pick a direction to use a door.");
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5) || Gdx.input.isKeyJustPressed(Input.Keys.L))
      player.setNextAction(new Wait());
    else {
      int dx = 0, dy = 0;
      if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8)
          || Gdx.input.isKeyPressed(Input.Keys.O)) {
        dy = -1;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)
          || Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
        dy = 1;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4)
          || Gdx.input.isKeyPressed(Input.Keys.K)) {
        dx = -1;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6)
          || Gdx.input.isKeyPressed(Input.Keys.SEMICOLON)) {
        dx = 1;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_7) || Gdx.input.isKeyPressed(Input.Keys.I)) {
        dx = -1;
        dy = -1;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_9) || Gdx.input.isKeyPressed(Input.Keys.P)) {
        dx = 1;
        dy = -1;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1) || Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
        dx = -1;
        dy = 1;
      }
      if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_3) || Gdx.input.isKeyPressed(Input.Keys.SLASH)) {
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

  private void setColor(int r, int g, int b) {
    batch.setColor(r / 255f, g / 255f, b / 255f, 1);
  }

  private void draw(Tiles tile, int x, int y) {
    batch.draw(sheet.getRegion(tile.getTile()), x * GRID, (HEIGHT - 1 - y) * GRID, GRID, GRID);
  }

  private void clear(int x, int y, int r, int g, int b) {
    setColor(r, g, b);
    batch.draw(sheet.getEmptyRegion(), x * GRID, (HEIGHT - 1 - y) * GRID, GRID, GRID);
  }

  private void draw(Actor actor) {
    RGB color = actor.getColor();
    setColor(color.r, color.g, color.b);
    batch.draw(sheet.getRegion(actor.getTile()), actor.getActualX(), HEIGHT * GRID - actor.getActualY() - GRID, GRID,
        GRID);
  }

  private void draw() {
    ScreenUtils.clear(96 / 255f, 62 / 255f, 52 / 255f, 1);

    batch.begin();

    // map
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        if (y >= HEIGHT - 2) {
          clear(x, y, 0, 0, 0);
          continue;
        }
        if (!map.isExplored(x, y)) {
          clear(x, y, 66, 32, 22);
          continue;
        }
        boolean visible = map.isVisible(x, y);
        if (!visible)
          clear(x, y, 86, 52, 42);
        Tiles tile = map.get(x, y);
        switch (tile) {
          case EMPTY:
            if (visible)
              setColor(126, 92, 82);
            else
              setColor(96, 62, 52);
            break;
          case WALL:
            if (visible)
              setColor(156, 178, 112);
            else
              setColor(76, 42, 32);
            break;
          case DOOR_CLOSED:
            if (visible)
              setColor(123, 92, 66);
            else
              setColor(93, 62, 36);
            break;
          case DOOR_OPEN:
            if (visible)
              setColor(123, 92, 66);
            else
              setColor(93, 62, 36);
            break;
        }
        draw(tile, x, y);
      }
    }

    // actors
    if (map.isVisible(monster.getX(), monster.getY()))
      draw(monster);
    draw(player);

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

    // inventory
    if (screenMode == ScreenMode.INVENTORY) {
      for (int x = 3; x < WIDTH - 3; x++) {
        for (int y = 3; y < HEIGHT - 3; y++) {
          clear(x, y, 0, 0, 0);
        }
      }
      font.setColor(Color.WHITE);
      float invY = (HEIGHT - 4) * GRID;
      boolean tooMuch = false;
      int index = 0;
      for (Entry<String, Integer> e : player.getInventory().entries.entrySet()) {
        String item = e.getKey();
        int count = e.getValue();
        if (index == inventoryPointer)
          font.draw(batch, ">", GRID * 4 - 13, invY);
        font.draw(batch, count + " " + item, GRID * 4, invY);
        invY -= 15;
        index++;
        if (invY < 4 * GRID) {
          tooMuch = true;
          break;
        }
      }
      if (tooMuch)
        font.draw(batch, "- more -", GRID * 4, 4 * GRID);
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
    sheet.dispose();
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
