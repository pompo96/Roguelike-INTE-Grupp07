package map.mapGeneration;

import map.tileFactory.Tile;
import map.tileFactory.TileFactory;

public class MapGenerator  {
    private final TileFactory tileFactory;
    private final int height, width;

    public MapGenerator(TileFactory tileFactory, int height, int width) {
        this.tileFactory = tileFactory;
        this.height = height;
        this.width = width;
    }

    public Tile[][] generate(GenerationStrategy strategy) {
        return strategy.generate(tileFactory, height, width);
    }
}
