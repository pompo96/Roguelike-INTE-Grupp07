package spelare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import race.Race;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Spelare attribut
 * currentLife
 * maxLife (default += raceModifier)
 * //100 liv, base movement, inga koordinater, inget equipped gear, inget yrke, behöver och ras
 * Race (Race objekt)
 * Story progress map (questname -> completed(boolean))
 * Movement speed (deault += raceModifier)
 * Koordinater ??
 * Equipped gear map(weapon -> itemObj, Armour -> itemObj, Boots -> itemObj)
 * Questlog list(questObjects)
 */

public class PlayerTests {
    Player defaultPlayer;

    @BeforeEach
    public void initializePlayer(){

        Race mockRace = mock(Race.class);
        when(mockRace.getLifeModifier()).thenReturn(20); // t.ex. +20 HP
        when(mockRace.getMovementModifier()).thenReturn(2); // t.ex. +2 speed

        defaultPlayer = new Player();
    }

    @Test
    public void defaultPlayer_hasDefaultHp(){
        assertEquals(100, defaultPlayer.getMaxLife());
    }

    @Test
    public void updateMaxLife_updatesMaxLife() {
        defaultPlayer.updateMaxLife(10);
        assertEquals(110, defaultPlayer.getMaxLife());
    }

    @Test
    public void defaultPlayer_hasDefaultCurrentLife(){
        assertEquals(100, defaultPlayer.getCurrentLife());
    }
    
    @Test
    public void updateCurrentLife_updatesCurrentLife(){
        defaultPlayer.updateCurrentLife(-10);
        assertEquals(90, defaultPlayer.getCurrentLife());
    }
    
    @Test
    public void updateCurrentLife_limitedByMaxLife(){
        defaultPlayer.updateCurrentLife(10);
        assertEquals(defaultPlayer.getMaxLife(), defaultPlayer.getCurrentLife());
    }




}
