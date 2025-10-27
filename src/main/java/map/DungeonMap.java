package map;

import equipment.Item;
import gameObject.GameObject;
import map.pathfinding.Directions;
import map.pathfinding.WalkablePathFinder;
import map.tiles.Tile;
import player.Player;

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

    public void drawMap() { //test -
        StringBuilder sb = new StringBuilder();
        for(Tile[] row : map){
            for(Tile tile : row){
                sb.append(tile.toString());
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public void spawnAtLocation(int y, int x, GameObject gameObject){ //test +
        map[y][x].setTileContainer(gameObject);
        gameObject.setY(y);
        gameObject.setX(x);
    }

    public void spawnPlayerAtEntrance(Player player) { //test -
        Tile tile = getFirstWalkableNeighbour(entrance);
        tile.setTileContainer(player);
        player.setY(tile.getY());
        player.setX(tile.getX());
    }
    public void spawnPlayerAtExit(Player player) { //test -
        Tile tile = getFirstWalkableNeighbour(exit);
        tile.setTileContainer(player);
        player.setY(tile.getY());
        player.setX(tile.getX());
    }

    private Tile getFirstWalkableNeighbour(Tile tile){
        WalkablePathFinder pathFinder = new WalkablePathFinder(this, 'x', 'x');
        int[] neighbourCoords = pathFinder.getWalkableTile(tile);
        return map[neighbourCoords[0]][neighbourCoords[1]];
    }

    public boolean movePlayer(Directions dir, Player player) {
        if (player == null) return false;

        int oldY = player.getY();
        int oldX = player.getX();

        int newY = oldY + dir.getRow();
        int newX = oldX + dir.getColumn();

        if (newY < 0 || newY >= getHeight() || newX < 0 || newX >= getWidth()) {
            return false;
        }

        Tile targetTile = map[newY][newX];

        if (!targetTile.isWalkable()) {
            return false;
        }

        if(targetTile.getTileContainer() instanceof Item){
            player.equipItem((Item) targetTile.getTileContainer());
        }

        Tile oldTile = map[oldY][oldX];
        oldTile.clearTileContainer();
        targetTile.setTileContainer(player);
        player.setY(newY);
        player.setX(newX);

        return true;
    }


}
