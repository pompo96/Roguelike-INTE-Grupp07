package map;

import map.tileFactory.Tile;
import player.Player;

import java.util.ArrayList;
import java.util.List;

public class    DungeonMap {
    private Tile[][] map;
    private Tile entrance;
    private Tile exit;
    private final int height;
    private final int width;

    public DungeonMap(int Height, int Width) {
        this.map = new Tile[Height][Width];
        this.height = Height;
        this.width = Width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile[][] getTileGrid() {
        return map;
    }

    public Tile getEntrance() {
        return entrance;
    }

    public void setEntrance(Tile entrance) {
        this.entrance = entrance;
    }

    public Tile getExit() {
        return exit;
    }

    public void setExit(Tile exit) {
        this.exit = exit;
    }

    public void drawMap() {
        StringBuilder sb = new StringBuilder();
        for(Tile[] row : map){
            for(Tile tile : row){
                sb.append(tile.toString());
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public void spawnAtEntrance(Player player) {
        Tile tile = getFirstWalkableNeighbour(entrance);
        tile.setTileContainer(player);
    }
    public void spawnAtExit(Player player) {
        Tile tile = getFirstWalkableNeighbour(exit);
        tile.setTileContainer(player);
    }

    private Tile getFirstWalkableNeighbour(Tile tile){
        WalkablePathFinder pathFinder = new WalkablePathFinder(this, 'x', 'x');
        int[] neighbourCoords = pathFinder.getWalkableTile(tile);
        return map[neighbourCoords[0]][neighbourCoords[1]];
    }

}
