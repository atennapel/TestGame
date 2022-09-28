package com.atennapel.testgame;

import java.util.ArrayList;
import java.util.List;

import static com.atennapel.testgame.Constants.*;

public class ShadowCasting {
  private static class Shadow {
    private double start;
    private double end;

    private Shadow(double start, double end) {
      this.start = start;
      this.end = end;
    }

    private static Shadow projectTile(int row, int col) {
      double dcol = col;
      double drow = row;
      double topLeft = dcol / (drow + 2);
      double bottomRight = (dcol + 1) / (drow + 1);
      return new Shadow(topLeft, bottomRight);
    }

    private boolean contains(Shadow other) {
      return start <= other.start && end >= other.end;
    }

    @Override
    public String toString() {
      return "[" + start + ", " + end + "]";
    }
  }

  private static class ShadowLine {
    private final List<Shadow> shadows;

    private ShadowLine() {
      shadows = new ArrayList<>();
    }

    private boolean isInShadow(Shadow projection) {
      for (Shadow s : shadows) {
        if (s.contains(projection))
          return true;
      }
      return false;
    }

    private boolean isFullShadow() {
      return shadows.size() == 1 && shadows.get(0).start == 0 && shadows.get(0).end == 1;
    }

    private void add(Shadow shadow) {
      int index = 0;
      for (; index < shadows.size(); index++) {
        if (shadows.get(index).start >= shadow.start)
          break;
      }
      Shadow overlappingPrevious = null;
      if (index > 0 && shadows.get(index - 1).end > shadow.start)
        overlappingPrevious = shadows.get(index - 1);
      Shadow overlappingNext = null;
      if (index < shadows.size() && shadows.get(index).start < shadow.end)
        overlappingNext = shadows.get(index);
      if (overlappingNext != null) {
        if (overlappingPrevious != null) {
          overlappingPrevious.end = overlappingNext.end;
          shadows.remove(index);
        } else {
          overlappingNext.start = shadow.start;
        }
      } else {
        if (overlappingPrevious != null) {
          overlappingPrevious.end = shadow.end;
        } else {
          shadows.add(index, shadow);
        }
      }
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("[");
      for (Shadow s : shadows) {
        sb.append(s);
        sb.append(", ");
      }
      if (shadows.size() > 0)
        sb.delete(sb.length() - 2, sb.length());
      sb.append("]");
      return sb.toString();
    }
  }

  private final Map map;

  public ShadowCasting(Map map) {
    this.map = map;
  }

  public void refreshVisibility(Pos pos) {
    int x = pos.x;
    int y = pos.y;
    for (int octant = 0; octant < 8; octant++)
      refreshOctant(x, y, octant);
    map.setVisible(x, y, true);
  }

  private void refreshOctant(int x, int y, int octant) {
    ShadowLine line = new ShadowLine();
    boolean fullShadow = false;
    for (int row = 1;; row++) {
      Pos posOctantTop = transformOctant(row, 0, octant);
      int posXTop = x + posOctantTop.x;
      int posYTop = y + posOctantTop.y;
      if (posXTop < 0 || posXTop >= WIDTH || posYTop < 0 || posYTop >= HEIGHT)
        break;
      for (int col = 0; col <= row; col++) {
        Pos posOctant = transformOctant(row, col, octant);
        int posX = x + posOctant.x;
        int posY = y + posOctant.y;
        if (posX < 0 || posX >= WIDTH || posY < 0 || posY >= HEIGHT)
          break;
        if (fullShadow) {
          map.setVisible(posX, posY, false);
        } else {
          Shadow projection = Shadow.projectTile(row, col);
          boolean visible = !line.isInShadow(projection);
          map.setVisible(posX, posY, visible);
          if (visible && map.isBlocked(posX, posY)) {
            line.add(projection);
            fullShadow = line.isFullShadow();
          }
        }
      }
    }
  }

  private static Pos transformOctant(int row, int col, int octant) {
    switch (octant) {
      case 0:
        return new Pos(col, -row);
      case 1:
        return new Pos(row, -col);
      case 2:
        return new Pos(row, col);
      case 3:
        return new Pos(col, row);
      case 4:
        return new Pos(-col, row);
      case 5:
        return new Pos(-row, col);
      case 6:
        return new Pos(-row, -col);
      case 7:
        return new Pos(-col, -row);
      default:
        return null;
    }
  }
}
