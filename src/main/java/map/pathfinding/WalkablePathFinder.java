package map.pathfinding;

import map.DungeonMap;
import map.tiles.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WalkablePathFinder {
    private final DungeonMap map;
    private final Coordinate start;
    private final Coordinate end;


    public WalkablePathFinder(DungeonMap map, char startSymbol, char endSymbol) {
        this.map = Objects.requireNonNull(map);
        this.start = findSymbolCoordinate(startSymbol);
        this.end = findSymbolCoordinate(endSymbol);
    }

    public boolean pathExists() {
        boolean[][] visited = new boolean[map.getHeight()][map.getWidth()];
        return depthFirstSearch(start, end, visited);
    }

    public int[] getWalkableTile(Tile tile) {
        List<Coordinate> cordList = walkableNeighbors(new Coordinate(tile.getY(), tile.getX()));
        int[] neighbourCoords = new int[2];
        neighbourCoords[0] = cordList.getFirst().column();
        neighbourCoords[1] = cordList.getFirst().row();

        return neighbourCoords;
    }

    private boolean depthFirstSearch(Coordinate current, Coordinate target, boolean[][] visited) {
        if (current.equals(target)) {
            return true;
        }

        visited[current.column()][current.row()] = true;

        for (Coordinate neighbor : walkableNeighbors(current)) {
            if (!visited[neighbor.column()][neighbor.row()] && depthFirstSearch(neighbor, target, visited)) {
                return true;
            }
        }
        return false;
    }

    private List<Coordinate> walkableNeighbors(Coordinate position) {
        List<Coordinate> neighbors = new ArrayList<>();
        for (Directions directions : Directions.values()) {
            Coordinate next = position.move(directions);
            if (map.getTileGrid()[next.column()][next.row()].isWalkable()) {
                neighbors.add(next);
            }
        }
        return neighbors;
    }



    private Coordinate findSymbolCoordinate(char symbol) {
        for (int column = 0; column < map.getHeight(); column++) {
            for (int row = 0; row < map.getWidth(); row++) {
                if (map.getTileGrid()[column][row].getObjectSymbol() == symbol) {
                    return new Coordinate(column, row);
                }
            }
        }
        throw new IllegalArgumentException("Symbol '" + symbol + "' not found in map.");
    }

    private record Coordinate(int column, int row) {

        public Coordinate move(Directions directions) {
            return new Coordinate(column + directions.getColumn(), row + directions.getRow());
        }
    }
}
