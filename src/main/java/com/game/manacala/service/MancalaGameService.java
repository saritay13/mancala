package com.game.manacala.service;


import com.game.manacala.constants.ManacalaConstants;
import com.game.manacala.enums.Player;
import com.game.manacala.model.MancalaGame;
import com.game.manacala.model.MancalaPitBoard;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@Service
@Getter
public class MancalaGameService {

    private MancalaPitBoard mancalaPitBoard;
    private MancalaGame mancalaGame;
    private final HashMap<String, MancalaGame> mancalaGamesStore = new HashMap<>();


    /**
     *
     * This will Initialize the MancalaGame object and store the game instance based on generated unique gameid
     * in mancalaGameStore.
     */
    public MancalaGame setupGame(int numberOfPits, int numberOfStones){

        int boardLength = (numberOfPits + ManacalaConstants.LARGE_PIT_PER_PLAYER) * ManacalaConstants.NUMBER_OF_PLAYERS;
        int pitBoard[] = new int[boardLength];
        int leftPlayerLargePit =0;
        int rightPlayerLargePit = (boardLength)/ ManacalaConstants.NUMBER_OF_PLAYERS;

        Arrays.fill(pitBoard,numberOfStones);
        pitBoard[leftPlayerLargePit]=0;
        pitBoard[rightPlayerLargePit]=0;

        mancalaPitBoard = new MancalaPitBoard(numberOfPits,numberOfStones,boardLength,pitBoard,leftPlayerLargePit,rightPlayerLargePit);
        String gameId = UUID.randomUUID().toString();
        mancalaGame = new MancalaGame(mancalaPitBoard, gameId);
        mancalaGame.setCurrentPlayer(Player.PLAYER1);
        mancalaGamesStore.put(gameId, mancalaGame);
        return mancalaGame;
    }

}
