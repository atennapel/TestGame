package com.atennapel.testgame;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.atennapel.testgame.Constants.SHEET_EMPTY;

public class Sheet {
  private String filename;
  private String regionname;
  private int gridWidth;
  private int gridHeight;
  private TextureAtlas atlas;
  private TextureRegion[] regions;

  public Sheet(String filename, String regionname, int gridWidth, int gridHeight) {
    this.filename = filename;
    this.regionname = regionname;
    this.gridWidth = gridWidth;
    this.gridHeight = gridHeight;
  }

  public void create() {
    atlas = new TextureAtlas(filename);
    AtlasRegion region = atlas.findRegion(regionname);

    int w = region.getRegionWidth() / gridWidth;
    int h = region.getRegionHeight() / gridHeight;
    int length = w * h;

    regions = new TextureRegion[length];
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        regions[x + y * w] = new TextureRegion(region, x * gridWidth, y * gridHeight, gridWidth, gridHeight);
      }
    }
  }

  public static Sheet create(String filename, String regionname, int gridWidth, int gridHeight) {
    Sheet sheet = new Sheet(filename, regionname, gridWidth, gridHeight);
    sheet.create();
    return sheet;
  }

  public TextureRegion getRegion(int ix) {
    return regions[ix];
  }

  public TextureRegion getEmptyRegion() {
    return getRegion(SHEET_EMPTY);
  }

  public void dispose() {
    atlas.dispose();
  }
}
