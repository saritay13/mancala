package com.game.manacala.service;

import com.game.manacala.constants.ValidationMessages;
import com.game.manacala.enums.Player;
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
public class ManacalaPlayServiceTest {

    @Autowired
    ManacalaPlayService manacalaPlayService;

    MancalaGame mancalaRequest = new MancalaGame("");

    @Before
    public void initializeBoard(){
        int[] pitBoard= new int[]{0,3,3,0,3,3};
        MancalaPitBoard mancalaPitBoard =  new MancalaPitBoard(2,3,6,pitBoard,0,3);
        manacalaPlayService.setMancalaMancalaPitBoard(mancalaPitBoard);
        mancalaRequest.setMancalaPitBoard(mancalaPitBoard);
        mancalaRequest.setCurrentPlayer(Player.PLAYER1);
    }

    @Test
    public void validateNullPlayerTest(){
        mancalaRequest.setCurrentPlayer(null);
        MancalaGame mancalaGame = manacalaPlayService.playAround(mancalaRequest);
        Assert.assertTrue(mancalaGame.getTurnMessage() == ValidationMessages.INVALID_PLAYER);
    }

    @Test
    public void cantChooseLargePitTest(){
        mancalaRequest.setCurrentPlayer(Player.PLAYER1);
        mancalaRequest.setChoosenPit(0);
        MancalaGame mancalaGame = manacalaPlayService.playAround(mancalaRequest);
        Assert.assertTrue(mancalaGame.getTurnMessage() == ValidationMessages.CANT_CHOOSE_LARGE_PITS);
    }

    @Test
    public void cantChooseAnotherPlayerPitTest(){
        mancalaRequest.setCurrentPlayer(Player.PLAYER1);
        mancalaRequest.setChoosenPit(4);

        MancalaGame mancalaGame = manacalaPlayService.playAround(mancalaRequest);
        Assert.assertTrue(mancalaGame.getTurnMessage()== ValidationMessages.CHOOSE_FROM_PLAYER_PLAYING_SIDE);
    }

   @Test
   public void gameOverCheckTest(){
       int[] pitBoard= new int[]{0,3,3,0,0,0};
       MancalaPitBoard mancalaPitBoard =  new MancalaPitBoard(2,3,6,pitBoard,0,3);
       mancalaRequest.setMancalaPitBoard(mancalaPitBoard);
       mancalaRequest.setChoosenPit(1);
       mancalaRequest.setCurrentPlayer(Player.PLAYER1);
       manacalaPlayService.setMancalaMancalaPitBoard(mancalaPitBoard);
       MancalaGame mancalaGame = manacalaPlayService.playAround(mancalaRequest);

       Assert.assertTrue(mancalaGame.isGameOver());
   }

    @Test
    public void choosingEmptyPitTest(){
        int[] pitBoard= new int[]{0,3,0,0,2,2};
        MancalaPitBoard mancalaPitBoard =  new MancalaPitBoard(2,3,6,pitBoard,0,3);
        mancalaRequest.setMancalaPitBoard(mancalaPitBoard);
        mancalaRequest.setChoosenPit(2);
        mancalaRequest.setCurrentPlayer(Player.PLAYER1);
        manacalaPlayService.setMancalaMancalaPitBoard(mancalaPitBoard);
        MancalaGame mancalaGame = manacalaPlayService.playAround(mancalaRequest);
        Assert.assertTrue(mancalaGame.getTurnMessage()== ValidationMessages.CANT_CHOOSE_EMPTY_PIT);
    }

    @Test
    public void sowCorrectnessTest(){
        int[] pitBoard= new int[]{0,3,3,0,3,3};
        MancalaPitBoard mancalaPitBoard =  new MancalaPitBoard(2,3,6,pitBoard,0,3);
        mancalaRequest.setMancalaPitBoard(mancalaPitBoard);
        mancalaRequest.setChoosenPit(2);
        manacalaPlayService.setMancalaMancalaPitBoard(mancalaPitBoard);
        MancalaGame mancalaGame = manacalaPlayService.playAround(mancalaRequest);

        MancalaPitBoard updateMancalaPitBoard = mancalaGame.getMancalaPitBoard();
        int[] updatePitBoard = updateMancalaPitBoard.getPitBoard();

        /*Check to not sow in opponent big pit*/
        Assert.assertTrue(updatePitBoard[updateMancalaPitBoard.getRightPlayerLargePit()]==0);

        /*check if the chosen pit is empty*/
        Assert.assertTrue(updatePitBoard[2]==0);

        /* Since last stone will land in player1 large pit so next player turn should be payer1*/
        Assert.assertTrue(mancalaGame.getCurrentPlayer()==Player.PLAYER1);

    }

    @Test
    public void captureStones(){
        int[] pitBoard= new int[]{0,1,0,0,3,3};
        MancalaPitBoard mancalaPitBoard =  new MancalaPitBoard(2,3,6,pitBoard,0,3);
        mancalaRequest.setMancalaPitBoard(mancalaPitBoard);
        mancalaRequest.setChoosenPit(1);
        manacalaPlayService.setMancalaMancalaPitBoard(mancalaPitBoard);
        MancalaGame mancalaGame = manacalaPlayService.playAround(mancalaRequest);

        MancalaPitBoard updateMancalaPitBoard = mancalaGame.getMancalaPitBoard();
        int[] updatePitBoard = updateMancalaPitBoard.getPitBoard();

        /*check if Captured stones are updated correctly in players large pit*/
        Assert.assertTrue(updatePitBoard[updateMancalaPitBoard.getLeftPlayerLargePit()]==4);

        /*check opposite side pit from which stones captured should be empty*/
        Assert.assertTrue(updatePitBoard[4]==0);
    }

}
