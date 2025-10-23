package map;

import map.tileFactory.Tile;

import java.util.ArrayList;
import java.util.List;

public class WalkablePathFinder {
    int startX, startY, endX, endY;
    Tile[][] map;
    char start, end;

    public WalkablePathFinder(Tile[][] map, char start, char end) {
            this.map = map;
            this.start = start;
            this.end = end;
            findSymbolCoordinate(start, true);
            findSymbolCoordinate(end, false);
    }

    public boolean pathExists() {
        boolean[][] visited = new boolean[map.length][map[0].length];
        return dfs(startX, startY, endX, endY, visited);
    }

    private boolean dfs(int x, int y, int targetX, int targetY, boolean[][] visited) {
        if (x == targetX && y == targetY) return true;
        visited[y][x] = true;

        for (int[] n : walkableNeighbours(x, y)) {
            int nx = n[0], ny = n[1];
            if (!visited[ny][nx] && dfs(nx, ny, targetX, targetY, visited)) {
                return true;
            }
        }
        return false;
    }

    //note - missing 4 directions
    private List<int[]> walkableNeighbours(int x, int y) {
        List<int[]> neighbours = new ArrayList<>();
        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        for (int[] d : dirs) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (ny >= 0 && ny < map.length && nx >= 0 && nx < map[0].length) {
                if (map[ny][nx].isWalkable()) {
                    neighbours.add(new int[]{nx, ny});
                }
            }
        }
        return neighbours;
    }

    private void findSymbolCoordinate(char symbol, boolean isStart) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x].getTileSymbol() == symbol) {
                    if (isStart) {
                        startY = y;
                        startX = x;
                    }else{
                        endY = y;
                        endX = x;
                    }
                }
            }
        }
    }

}
