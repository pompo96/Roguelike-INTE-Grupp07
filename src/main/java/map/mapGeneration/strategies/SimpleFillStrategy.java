package map.mapGeneration.strategies;


import map.mapGeneration.GenerationStrategy;
import map.tileFactory.Tile;
import map.tileFactory.TileFactory;

public class SimpleFillStrategy implements GenerationStrategy {

    @Override
    public Tile[][] generate(TileFactory factory, int width, int height) {
        return new Tile[width][height];
    }
}
