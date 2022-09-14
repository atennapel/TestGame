package com.atennapel.testgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
	
	@Override
	public void create () {
		batch = new SpriteBatch();
    atlas = new TextureAtlas("testgame.atlas");
    AtlasRegion region = atlas.findRegion("sheet");
    player = new TextureRegion(region, 0, 0, 16, 16);
    wall = new TextureRegion(region, 16, 0, 16, 16);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
    batch.setColor(Color.RED);
		batch.draw(player, 0, 0);
		batch.draw(wall, 16, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		atlas.dispose();
	}
}
