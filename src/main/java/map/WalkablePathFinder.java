package map;

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

    private boolean depthFirstSearch(Coordinate current, Coordinate target, boolean[][] visited) {
        if (current.equals(target)) {
            return true;
        }

        visited[current.y()][current.x()] = true;

        for (Coordinate neighbor : walkableNeighbors(current)) {
            if (!visited[neighbor.y()][neighbor.x()] && depthFirstSearch(neighbor, target, visited)) {
                return true;
            }
        }
        return false;
    }

    private List<Coordinate> walkableNeighbors(Coordinate position) {
        List<Coordinate> neighbors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Coordinate next = position.move(direction);
            if (isWithinBounds(next) && map.getTileGrid()[next.y()][next.x()].isWalkable()) {
                neighbors.add(next);
            }
        }
        return neighbors;
    }

    private boolean isWithinBounds(Coordinate coordinate) {
        return coordinate.y() >= 0 && coordinate.y() < map.getHeight() &&
                coordinate.x() >= 0 && coordinate.x() < map.getWidth();
    }

    private Coordinate findSymbolCoordinate(char symbol) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.getTileGrid()[y][x].getObjectSymbol() == symbol) {
                    return new Coordinate(x, y);
                }
            }
        }
        throw new IllegalArgumentException("Symbol '" + symbol + "' not found in map.");
    }

    private record Coordinate(int x, int y) {

        public Coordinate move(Direction direction) {
            return new Coordinate(x + direction.dx(), y + direction.dy());
        }
    }

    private enum Direction {
        RIGHT(1, 0),
        LEFT(-1, 0),
        DOWN(0, 1),
        DOWNLEFT(-1, 1),
        DOWNRIGHT(1,1),
        UP(0, -1),
        UPLEFT(-1,-1),
        UPRIGHT(1, -1);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int dx() {
            return dx;
        }

        public int dy() {
            return dy;
        }
    }
}
