package map.mapGeneration;
import map.tileFactory.Tile;
import map.tileFactory.TileFactory;

public interface GenerationStrategy {
    /**
     * Generate a map using the provided factory and dimensions.
     *
     * @param factory the TileFactory used to build tiles
     * @param width   map width (columns)
     * @param height  map height (rows)
     * @return a height x width Tile[][] map
     */
    Tile[][] generate(TileFactory factory, int height, int width);
}