package com.game.manacala.controller;

import com.game.manacala.model.MancalaGame;
import com.game.manacala.model.MancalaPitBoard;
import com.game.manacala.service.MancalaGameService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MancalaControllerTest {
    @Autowired
    MancalaController mancalaController;

    @Autowired
    MancalaGameService mancalaGameService;

    final int NUMBER_OF_PITS=6;
    final int NUMBER_OF_STONES=6;
    final int EXPECTED_BOARD_LENGTH=14;
    final String VALID_GAME_ID = "abc";
    final String INVALID_GAME_ID= "xyz";
    final String MANCALA_GAME_STORE_FIELD = "mancalaGamesStore";

    @Test
    public void validGetBoardRequestTest(){
        ResponseEntity<MancalaGame> mancalaGameResp = mancalaController.getMancalaBoard(0,0);
        MancalaGame mancalaGame = mancalaGameResp.getBody();
        Assert.assertTrue(mancalaGameResp.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void invalidGetBoardRequestTest(){
        ResponseEntity<MancalaGame> mancalaGameResp = mancalaController.getMancalaBoard(NUMBER_OF_PITS,NUMBER_OF_STONES);
        MancalaGame mancalaGame = mancalaGameResp.getBody();
        MancalaPitBoard mancalaPitBoard = mancalaGame.getMancalaPitBoard();
        Assert.assertTrue(EXPECTED_BOARD_LENGTH == mancalaPitBoard.getPitBoard().length);
    }

    @Test
    public void invalidGameIdTest(){
        HashMap<String, MancalaGame> mancalaGamesStore = new HashMap<>();
        mancalaGamesStore.put(VALID_GAME_ID, new MancalaGame(""));
        ReflectionTestUtils.setField(mancalaGameService, MANCALA_GAME_STORE_FIELD, mancalaGamesStore);
        ResponseEntity<MancalaGame> mancalaGameResp = mancalaController.playManacala(INVALID_GAME_ID, 1);
        Assert.assertTrue(mancalaGameResp.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void validGameIdTest(){
        HashMap<String, MancalaGame> mancalaGamesStore = new HashMap<>();
        mancalaGamesStore.put(VALID_GAME_ID, new MancalaGame(""));
        ReflectionTestUtils.setField(mancalaGameService, MANCALA_GAME_STORE_FIELD, mancalaGamesStore);
        ResponseEntity<MancalaGame> mancalaGameResp = mancalaController.playManacala(VALID_GAME_ID, 1);
        Assert.assertTrue(mancalaGameResp.getStatusCode() == HttpStatus.OK);
    }

}
