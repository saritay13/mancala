package com.game.manacala.service;

import com.game.manacala.model.MancalaGame;
import com.game.manacala.model.MancalaPitBoard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MancalGameServiceTest {

    private static final int NUMBER_OF_PITS=6;
    private static final int NUMBER_OF_STONES=6;
    private static final int EXPECTED_BOARD_LENGTH=14;
    private static final int EXPECTED_INTITIAL_LARGE_PIT_STONES=0;

    @Autowired
    MancalaGameService mancalaGameService;
    MancalaGame mancalaGame;

    @Before
    public void setup(){
        mancalaGame = mancalaGameService.setupGame(NUMBER_OF_PITS,NUMBER_OF_STONES);
    }

    @Test
    public void verifyMancalaBoardLengthTest(){
        Assert.assertTrue(EXPECTED_BOARD_LENGTH == mancalaGame.getMancalaPitBoard().getBoardLength());
    }

    @Test
    public void ifPitBoardInitializedTest(){
        MancalaPitBoard mancalaPitBoard = mancalaGame.getMancalaPitBoard();
        int [] mancalaPit = mancalaPitBoard.getPitBoard();

        Assert.assertTrue(mancalaPit.length== EXPECTED_BOARD_LENGTH);
    }
    @Test
    public void ifLargePitsEmptyAtStartTest(){
        MancalaPitBoard mancalaPitBoard = mancalaGame.getMancalaPitBoard();
        int [] mancalaPit = mancalaPitBoard.getPitBoard();
        int leftLargePitStones = mancalaPit[mancalaPitBoard.getLeftPlayerLargePit()];
        int rightLargePitStones = mancalaPit[mancalaPitBoard.getRightPlayerLargePit()];
        Assert.assertTrue(EXPECTED_INTITIAL_LARGE_PIT_STONES == leftLargePitStones);
        Assert.assertTrue(EXPECTED_INTITIAL_LARGE_PIT_STONES == rightLargePitStones);
    }
}
