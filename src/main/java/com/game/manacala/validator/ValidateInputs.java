package com.game.manacala.validator;

import com.game.manacala.enums.Player;
import com.game.manacala.model.MancalaGame;
import com.game.manacala.model.MancalaPitBoard;

import java.util.logging.Logger;

import static com.game.manacala.constants.ValidationMessages.*;

public class ValidateInputs {
    Logger logger = Logger.getLogger(ValidateInputs.class.getName());

    private boolean validatePickedPitNumber(MancalaGame mancalaGame){
        MancalaPitBoard manacalaMancalaPitBoard = mancalaGame.getMancalaPitBoard();
        Player playerTurn = mancalaGame.getCurrentPlayer();
        int choosenPit = mancalaGame.getChoosenPit();

        if(playerTurn==null){
            logger.info(INVALID_PLAYER);
            mancalaGame.setTurnMessage(INVALID_PLAYER);
            return false;
        }
        if(choosenPit== manacalaMancalaPitBoard.getLeftPlayerLargePit()
                || choosenPit == manacalaMancalaPitBoard.getRightPlayerLargePit()){
            logger.info(CANT_CHOOSE_LARGE_PITS);
            mancalaGame.setTurnMessage(CANT_CHOOSE_LARGE_PITS);
            return false;
        }

        if(choosenPit<0 || choosenPit> manacalaMancalaPitBoard.getBoardLength()){
            logger.info(INVALID_PIT_NUMBER);
            mancalaGame.setTurnMessage(INVALID_PIT_NUMBER);
            return false;
        }

        if((playerTurn==Player.PLAYER1 && choosenPit> manacalaMancalaPitBoard.getBoardLength()/2)
            ||(playerTurn==Player.PLAYER2 && (choosenPit<= manacalaMancalaPitBoard.getBoardLength()/2)
            || choosenPit>= manacalaMancalaPitBoard.getBoardLength())){
            logger.info(CHOOSE_FROM_PLAYER_PLAYING_SIDE);
            mancalaGame.setTurnMessage(CHOOSE_FROM_PLAYER_PLAYING_SIDE);
            return false;
        }

        if(manacalaMancalaPitBoard.getPitBoard()[choosenPit]==0){
            logger.info(CANT_CHOOSE_EMPTY_PIT);
            mancalaGame.setTurnMessage(CANT_CHOOSE_EMPTY_PIT);
            return false;
        }
        return true;

    }

    public boolean validateRequest(MancalaGame mancalaGame){

        if(mancalaGame==null){
            logger.info(NO_GAME_FOUND);
            mancalaGame.setTurnMessage(NO_GAME_FOUND);
            return false;
        }
        return validatePickedPitNumber(mancalaGame);
    }
}
