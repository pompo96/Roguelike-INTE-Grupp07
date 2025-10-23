package map.mapGeneration.strategies;

import map.mapGeneration.GenerationStrategy;
import map.tileFactory.Tile;
import map.tileFactory.TileFactory;

public class RandomStrategy implements GenerationStrategy {
    @Override
    public Tile[][] generate(TileFactory factory, int height, int width) {
        return new Tile[height][width];
    }
}
