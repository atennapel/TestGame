package com.atennapel.testgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class TestGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private TextureRegion player;
	private TextureRegion wall;

	private int x = 32;
	private int y = 32;
	private boolean disabled = false;
	private float cooldown = 0;

	private boolean[][] obstacles;

	@Override
	public void create() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("testgame.atlas");
		AtlasRegion region = atlas.findRegion("sheet");
		player = new TextureRegion(region, 0, 0, 16, 16);
		wall = new TextureRegion(region, 16, 0, 16, 16);

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

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();

		if (!disabled) {
			int dx = 0;
			int dy = 0;
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				dy = 32;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				dy = -32;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				dx = -32;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				dx = 32;
			}
			if (dx != 0 || dy != 0) {
				int nextX = x + dx;
				int nextY = y + dy;
				if (!obstacles[nextX / 32][nextY / 32]) {
					disabled = true;
					cooldown = 0;
					x = nextX;
					y = nextY;
				}
			}
		} else {
			cooldown += dt;
			if (cooldown > 0.1) {
				disabled = false;
			}
		}

		ScreenUtils.clear(96 / 255f, 62 / 255f, 52 / 255f, 1);
		batch.begin();
		batch.setColor(156 / 255f, 178 / 255f, 112 / 255f, 1);
		batch.draw(player, x, y, 32, 32);
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
	public void dispose() {
		batch.dispose();
		atlas.dispose();
	}
}
